package com.example.android.asymmetricfingerprintdialog;

import com.example.android.asymmetricfingerprintdialog.server.StoreBackend;
import com.example.android.asymmetricfingerprintdialog.server.Transaction;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.inject.Inject;

/**
 * fingerprint APIを利用した指紋認証を行うDialogFragmentです。
 * 指紋認証が実行できない場合はパスワードを要求します。
 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener, FingerprintUiHelper.Callback {

    private Button mCancelButton;
    private Button mSecondDialogButton;
    private View mFingerprintContent;
    private View mBackupContent;
    private EditText mPassword;
    private CheckBox mUseFingerprintFutureCheckBox;
    private TextView mPasswordDescriptionTextView;
    private TextView mNewFingerprintEnrolledTextView;

    private Stage mStage = Stage.FINGERPRINT;

    private FingerprintManagerCompat.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private MainActivity mActivity;

    @Inject FingerprintUiHelper.FingerprintUiHelperBuilder mFingerprintUiHelperBuilder;
    @Inject InputMethodManager mInputMethodManager;
    @Inject SharedPreferences mSharedPreferences;
    @Inject StoreBackend mStoreBackend;

    @Inject
    public FingerprintAuthenticationDialogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // orientation changesなどでActivityが再度生成された場合にもFragmentを
        // 作りなおさない。
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        // ここでサーバーサイドにダミーのユーザーを登録しています。
        // アプリ・サービスの要件に合わせてユーザー管理は個別に考えましょう。
        enroll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.sign_in));
        View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mSecondDialogButton = (Button) v.findViewById(R.id.second_dialog_button);
        mSecondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStage == Stage.FINGERPRINT) {
                    goToBackup();
                } else {
                    verifyPassword();
                }
            }
        });
        mFingerprintContent = v.findViewById(R.id.fingerprint_container);
        mBackupContent = v.findViewById(R.id.backup_container);
        mPassword = (EditText) v.findViewById(R.id.password);
        mPassword.setOnEditorActionListener(this);
        mPasswordDescriptionTextView = (TextView) v.findViewById(R.id.password_description);
        mUseFingerprintFutureCheckBox = (CheckBox)
                v.findViewById(R.id.use_fingerprint_in_future_check);
        mNewFingerprintEnrolledTextView = (TextView)
                v.findViewById(R.id.new_fingerprint_enrolled_description);
        mFingerprintUiHelper = mFingerprintUiHelperBuilder.build(
                (ImageView) v.findViewById(R.id.fingerprint_icon),
                (TextView) v.findViewById(R.id.fingerprint_status), this);
        updateStage();

        // 指紋認証が利用できない場合、パスワード認証に切り替えます
        if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
            goToBackup();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStage == Stage.FINGERPRINT) {
            mFingerprintUiHelper.startListening(mCryptoObject);
        }
    }

    public void setStage(Stage stage) {
        mStage = stage;
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    /**
     * 指紋認証時に渡される暗号化オブジェクトをセットします。
     */
    public void setCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    /**
     * パスワード認証スクリーンに切り替えます。
     */
    private void goToBackup() {
        mStage = Stage.PASSWORD;
        updateStage();
        mPassword.requestFocus();

        // キーボードを表示します。
        mPassword.postDelayed(mShowKeyboardRunnable, 500);

        // 指紋認証の待受を止めます。
        mFingerprintUiHelper.stopListening();
    }

    /**
     * 擬似のバックエンドに公開鍵とユーザーアカウントを登録します。
     */
    private void enroll() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            PublicKey publicKey = keyStore.getCertificate(MainActivity.KEY_NAME).getPublicKey();

            /**
             * 以下はサンプルコードならではの処理です。
             * バックエンドが擬似的に同一アプリ内で動いているので、公開鍵をバックエンドに渡しても
             * Android KeyStoreの管理内にあるので、バックエンド側の自由なタイミングで利用しようとすると、
             * ユーザー認証を求められてしまいます。
             * そのため、以下で、公開鍵のインスタンスを別インスタンス化しています。
             * 通常サーバなどで公開鍵を受け取る場合は、通信により別インスタンス化されます。
             */
            KeyFactory factory = KeyFactory.getInstance(publicKey.getAlgorithm());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
            PublicKey verificationKey = factory.generatePublic(spec);

            // バックエンドにアカウント情報と公開鍵を渡します。
            // アカウント情報の管理方法は実際のサービスではしっかりと考えるべきです。
            mStoreBackend.enroll("user", "password", verificationKey);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException |
                IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    /**
     * パスワードが正しいかどうかをチェックし、Activityにその結果を知らせてダイアログを閉じます。
     */
    private void verifyPassword() {
        Transaction transaction = new Transaction("user", 1, new SecureRandom().nextLong());
        if (!mStoreBackend.verify(transaction, mPassword.getText().toString())) {
            return;
        }
        if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                    mUseFingerprintFutureCheckBox.isChecked());
            editor.apply();

            if (mUseFingerprintFutureCheckBox.isChecked()) {
                // Re-create the key so that fingerprints including new ones are validated.
                mActivity.createKeyPair();
                mStage = Stage.FINGERPRINT;
            }
        }
        mPassword.setText("");
        mActivity.onPurchased(null);
        dismiss();
    }

    private final Runnable mShowKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            mInputMethodManager.showSoftInput(mPassword, 0);
        }
    };

    private void updateStage() {
        switch (mStage) {
            case FINGERPRINT:
                mCancelButton.setText(R.string.cancel);
                mSecondDialogButton.setText(R.string.use_password);
                mFingerprintContent.setVisibility(View.VISIBLE);
                mBackupContent.setVisibility(View.GONE);
                break;
            case NEW_FINGERPRINT_ENROLLED:
                // Intentional fall through
            case PASSWORD:
                mCancelButton.setText(R.string.cancel);
                mSecondDialogButton.setText(R.string.ok);
                mFingerprintContent.setVisibility(View.GONE);
                mBackupContent.setVisibility(View.VISIBLE);
                if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
                    mPasswordDescriptionTextView.setVisibility(View.GONE);
                    mNewFingerprintEnrolledTextView.setVisibility(View.VISIBLE);
                    mUseFingerprintFutureCheckBox.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword();
            return true;
        }
        return false;
    }

    @Override
    public void onAuthenticated() {
        // Callback from FingerprintUiHelper.
        // Activityに認証が成功したことを知らせます。
        mPassword.setText("");
        Signature signature = mCryptoObject.getSignature();
        // nonceは秘密鍵で署名されるトランザクション情報の中にも入れることで、
        // サーバーサイドでReplay攻撃を防ぐために検証することができます。
        Transaction transaction = new Transaction("user", 1, new SecureRandom().nextLong());
        try {
            signature.update(transaction.toByteArray());
            byte[] sigBytes = signature.sign();
            if (mStoreBackend.verify(transaction, sigBytes)) {
                mActivity.onPurchased(sigBytes);
                dismiss();
            } else {
                mActivity.onPurchaseFailed();
                dismiss();
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError() {
        goToBackup();
    }

    /**
     * ユーザーが認証しようとしている認証方法の定義です。
     */
    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }
}

package com.example.android.asymmetricfingerprintdialog;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;

import javax.inject.Inject;

/**
 * メイン画面です。購入画面を表示します。
 */
public class MainActivity extends Activity {

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    /** Alias for our key in the Android Key Store */
    public static final String KEY_NAME = "my_key";

    @Inject KeyguardManager mKeyguardManager;
    @Inject FingerprintManagerCompat mFingerprintManager;
    @Inject FingerprintAuthenticationDialogFragment mFragment;
    @Inject KeyStore mKeyStore;
    @Inject KeyPairGenerator mKeyPairGenerator;
    @Inject Signature mSignature;
    @Inject SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((InjectedApplication) getApplication()).inject(this);
        setContentView(R.layout.activity_main);

        Button purchaseButton = (Button) findViewById(R.id.purchase_button);

        // なんらかの理由でFingerprintManagerが取得できなかった場合
        if(mFingerprintManager == null) {
            Toast.makeText(this, "Fingerprint Authentication is unavailable", Toast.LENGTH_LONG).show();
            purchaseButton.setEnabled(false);
            return;
        }

        // ロック画面にセキュリティロックが設定されているかどうかをチェックします。
        if (!mKeyguardManager.isKeyguardSecure()) {
            Toast.makeText(this,
                    "Secure lock screen hasn't set up.\n"
                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
                    Toast.LENGTH_LONG).show();
            purchaseButton.setEnabled(false);
            return;
        }

        // 指紋が登録済みかどうかをチェックします。
        // APILevel 23未満の場合、必ずfalseになります。

        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            purchaseButton.setEnabled(false);
            // This happens when no fingerprints are registered.
            Toast.makeText(this,
                    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // 非対称鍵のペアを生成します。
        createKeyPair();

        // 購入ボタンを有効化します。
        purchaseButton.setEnabled(true);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.confirmation_message).setVisibility(View.GONE);
                findViewById(R.id.encrypted_message).setVisibility(View.GONE);

                // 署名クラスが初期化できたら認証ダイアログを出します。
                if (initSignature()) {
                    mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mSignature));
                    boolean useFingerprintPreference = mSharedPreferences.getBoolean(getString(R.string.use_fingerprint_to_authenticate_key), true);
                    if (useFingerprintPreference) {
                        mFragment.setStage(FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                    } else {
                        mFragment.setStage(FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                    }
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // 鍵のペアを生成後、ロック画面が無効になった場合、もしくは鍵のペアを生成後、新たに指紋が登録された場合
                    mFragment.setStage(FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        });
    }

    /**
     * 生成した非対象鍵のペアから秘密鍵による署名クラスをインスタンス化します。
     *
     * @return {@code true} 初期化が成功した場合, {@code false} 鍵のペアを生成後、ロック画面が無効になった場合、
     * もしくは鍵のペアを生成後、新たに指紋が登録された場合
     */
    private boolean initSignature() {
        try {
            mKeyStore.load(null);
            PrivateKey key = (PrivateKey) mKeyStore.getKey(KEY_NAME, null);
            mSignature.initSign(key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void onPurchased(byte[] signature) {
        showConfirmation(signature);
    }

    public void onPurchaseFailed() {
        Toast.makeText(this, R.string.purchase_fail, Toast.LENGTH_SHORT).show();
    }

    // 署名された情報を表示します。
    private void showConfirmation(byte[] encrypted) {
        findViewById(R.id.confirmation_message).setVisibility(View.VISIBLE);
        if (encrypted != null) {
            TextView v = (TextView) findViewById(R.id.encrypted_message);
            v.setVisibility(View.VISIBLE);
            v.setText(Base64.encodeToString(encrypted, 0 /* flags */));
        }
    }

    /**
     * Android Keystoreに非対称鍵のペアを生成します。
     * 秘密鍵は指紋認証に必ず利用されます。公開鍵の利用に制限はありません。
     */
    public void createKeyPair() {
        try {
            mKeyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(KEY_NAME,
                            KeyProperties.PURPOSE_SIGN)
                            .setDigests(KeyProperties.DIGEST_SHA256)
                            .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                            // 利用時に毎回認証を要求します。
                            .setUserAuthenticationRequired(true)
                            .build());
            mKeyPairGenerator.generateKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

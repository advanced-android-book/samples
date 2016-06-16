package com.example.android.asymmetricfingerprintdialog;

import com.example.android.asymmetricfingerprintdialog.server.StoreBackend;
import com.example.android.asymmetricfingerprintdialog.server.StoreBackendImpl;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.inputmethod.InputMethodManager;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for Fingerprint APIs.
 * DIライブラリ「Dagger」によるクラスモジュールクラスです。説明は割愛します。
 */
@Module(
        library = true,
        injects = {MainActivity.class}
)
public class FingerprintModule {

    private final Context mContext;

    public FingerprintModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context providesContext() {
        return mContext;
    }

    @Provides
    public FingerprintManagerCompat providesFingerprintManager(Context context) {
        return FingerprintManagerCompat.from(context);
    }

    @Provides
    public KeyguardManager providesKeyguardManager(Context context) {
        return (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Provides
    public KeyStore providesKeystore() {
        try {
            return KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
    }

    @Provides
    public KeyPairGenerator providesKeyPairGenerator() {
        try {
            // Googleのサンプルの"EC"アルゴリズムはtargetSdk=23以下では利用できないため、
            // 23未満では便宜的に"RSA"を指定します。
            String algorithm = Build.VERSION.SDK_INT >= 23 ? "EC" : "RSA";
            return KeyPairGenerator.getInstance(algorithm, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyPairGenerator", e);
        }
    }

    @Provides
    public Signature providesSignature(KeyStore keyStore) {
        try {
            return Signature.getInstance("SHA256withECDSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get an instance of Signature", e);
        }
    }

    @Provides
    public InputMethodManager providesInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    public SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public StoreBackend providesStoreBackend() {
        return new StoreBackendImpl();
    }
}

package com.example.android.asymmetricfingerprintdialog.server;

import java.security.PublicKey;

/**
 * バックエンドのインtフェース定義です。
 */
public interface StoreBackend {

    /**
     * クライアントから送られてきた購入トランザクションが、ユーザーIDに紐づく秘密鍵で署名されているか検証します。
     *
     * @param transaction          the contents of the purchase transaction, its contents are
     *                             signed
     *                             by the
     *                             private key in the client side.
     * @param transactionSignature the signature of the transaction's contents.
     * @return true if the signedSignature was verified, false otherwise. If this method returns
     * true, the server can consider the transaction is successful.
     */
    boolean verify(Transaction transaction, byte[] transactionSignature);

    /**
     * クライアントから送られてきた購入トランザクションをパスワードによって検証します。
     *
     * @param transaction the contents of the purchase transaction, its contents are signed by the
     *                    private key in the client side.
     * @param password    the password for the user associated with the {@code transaction}.
     * @return true if the password is verified.
     */
    boolean verify(Transaction transaction, String password);

    /**
     * ユーザーに紐づく公開鍵を登録します。
     *
     * @param userId    the unique ID of the user within the app including server side
     *                  implementation
     * @param password  the password for the user for the server side
     * @param publicKey the public key object to verify the signature from the user
     * @return true if the enrollment was successful, false otherwise
     */
    boolean enroll(String userId, String password, PublicKey publicKey);
}

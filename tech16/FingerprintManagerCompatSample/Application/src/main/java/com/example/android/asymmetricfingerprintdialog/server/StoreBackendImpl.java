package com.example.android.asymmetricfingerprintdialog.server;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 擬似のバックエンド実装
 */
public class StoreBackendImpl implements StoreBackend {

    private final Map<String, PublicKey> mPublicKeys = new HashMap<>();
    private final Set<Transaction> mReceivedTransactions = new HashSet<>();

    /**
     * 購入トランザクションの検証メソッド
     */
    @Override
    public boolean verify(Transaction transaction, byte[] transactionSignature) {
        try {
            if (mReceivedTransactions.contains(transaction)) {
                // Replay攻撃されていないか、同一のnonceを含むか検証する
                return false;
            }
            mReceivedTransactions.add(transaction);
            PublicKey publicKey = mPublicKeys.get(transaction.getUserId());
            Signature verificationFunction = Signature.getInstance("SHA256withECDSA");
            verificationFunction.initVerify(publicKey);
            verificationFunction.update(transaction.toByteArray());
            if (verificationFunction.verify(transactionSignature)) {
                // ユーザーに紐付いた公開鍵で購入トランザクションを検証する
                return true;
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // 検証に問題が起きた場合、実際のケースではクライアントユーザーにメッセージを送る必要がある
        }
        return false;
    }

    @Override
    public boolean verify(Transaction transaction, String password) {
        // サンプルなので、パスワードによる検証は必ず通るようにしてあります。
        return true;
    }

    @Override
    public boolean enroll(String userId, String password, PublicKey publicKey) {
        if (publicKey != null) {
            mPublicKeys.put(userId, publicKey);
        }
        // 本サンプルでは純粋にクライアント側からアカウントのIDとパスワードが提供されていますが、
        // ユーザー管理方法は、アプリ・サービスに適切な形で検討すべきです。
        return true;
    }
}

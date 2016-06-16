package com.example.android.asymmetricfingerprintdialog.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 購入トランザクションを表現するエンティティクラスです。
 */
public class Transaction {

    /** 購入アイテムのユニークなID */
    private final Long mItemId;

    /** トランザクションを生成したユーザのユニークなID */
    private final String mUserId;

    /**
     * 同じnonceがReplay攻撃によって再利用させないために、秘密鍵によって署名され、サーバーで検証されるnonce値
     */
    private final Long mClientNonce;

    public Transaction(String userId, long itemId, long clientNonce) {
        mItemId = itemId;
        mUserId = userId;
        mClientNonce = clientNonce;
    }

    public String getUserId() {
        return mUserId;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeLong(mItemId);
            dataOutputStream.writeUTF(mUserId);
            dataOutputStream.writeLong(mClientNonce);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException ignore) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignore) {
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction that = (Transaction) o;
        return Objects.equals(mItemId, that.mItemId) && Objects.equals(mUserId, that.mUserId) &&
                Objects.equals(mClientNonce, that.mClientNonce);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mItemId, mUserId, mClientNonce);
    }
}

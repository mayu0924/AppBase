package com.appheader.base.common.data.cipher;


import com.appheader.base.common.assit.Base64;

/**
 * @author MaTianyu
 * @date 14-7-31
 */
public class Base64Cipher extends Cipher {
    private Cipher cipher;

    public Base64Cipher() {
    }

    public Base64Cipher(Cipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public byte[] decrypt(byte[] res) {
        if(cipher != null) res = cipher.decrypt(res);
        return Base64.decode(res, Base64.DEFAULT);
    }

    @Override
    public byte[] encrypt(byte[] res) {
        if(cipher != null) res = cipher.encrypt(res);
        return Base64.encode(res, Base64.DEFAULT);
    }
}

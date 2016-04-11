package com.appheader.base.common.assit;

import android.annotation.SuppressLint;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class DES {
	
    private static byte[] iv= {1,2,3,4,5,6,7,8};
    
    private static String key = "zmxnalsk";
    
    public static String encrypt(String str){
    	try {
			return encryptDES(str, key);
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
    }
    
    public static String decrypt(String str){
    	try {
			return decryptDES(str, key);
		} catch (Exception e) {
			return str;
		}
    }

	@SuppressLint("TrulyRandom")
	private static String encryptDES(String encryptString, String encryptKey) throws Exception { 
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");   
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);   
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());   
        
        return Base64Coder.encode(encryptedData);   
    }   
	private static String decryptDES(String decryptString, String decryptKey) throws Exception {   
        byte[] byteMi = Base64Coder.decode(decryptString);   
        IvParameterSpec zeroIv = new IvParameterSpec(iv);   
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);    
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");   
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);   
        byte decryptedData[] = cipher.doFinal(byteMi);   
        
        return new String(decryptedData);   
    }  
	
	public static void main(String[] args) {
		String encryptStr = encrypt("测试测试");
		System.out.println("加密后：" + encryptStr);
		
		String decryptStr = decrypt(encryptStr);
		System.out.println("解密后：" + decryptStr);
	}
}

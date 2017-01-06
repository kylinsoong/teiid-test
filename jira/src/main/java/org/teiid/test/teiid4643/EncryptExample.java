package org.teiid.test.teiid4643;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptExample {
    
    static byte iv[] = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
        byte[] keyBytes = "redhat".getBytes();
        byte[] dataBytes = "this is encrpt/decrpt test".getBytes();

        IvParameterSpec ivspec = new IvParameterSpec(iv);
        byte[] keyval = padkey(keyBytes);
        SecretKey keyValue = new SecretKeySpec(keyval,"AES"); //$NON-NLS-1$
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //$NON-NLS-1$
        cipher.init(Cipher.ENCRYPT_MODE, keyValue, ivspec);
        byte[] encryptedByte = cipher.doFinal(dataBytes);
        
        cipher.init(Cipher.DECRYPT_MODE, keyValue, ivspec);
        byte[] decryptedByte = cipher.doFinal(encryptedByte);
        
        System.out.println(new String(decryptedByte));
    }

    private static byte[] padkey(byte[] bytes) {
        
        byte[] padding = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        
        if(bytes == null || bytes.length == 0) {
            return padding;
        }
        
        int len = bytes.length;
        if(len == 16) {
            return bytes;
        } else {
            for (int i=0; i < 16 && i < len ; i++ ) {
                padding[i] = bytes[i];
            }
            return padding;
        }
    }

}

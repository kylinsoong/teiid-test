package org.teiid.test.teiid4643;

import java.io.UnsupportedEncodingException;
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

import org.teiid.api.exception.query.FunctionExecutionException;
import org.teiid.core.types.BinaryType;
import org.teiid.query.function.TeiidFunction;
import org.teiid.query.function.metadata.FunctionCategoryConstants;

public class TEIID4643 {

    public static void main(String[] args) throws FunctionExecutionException, UnsupportedEncodingException {
        System.out.println(new String(aes_encrypt("key", "kylinsoong").getBytes()));
        System.out.println(new String(aes_decrypt("key", "jO0cgJuvbfPyHBL43BJ2gA==").getBytes()));
    }
    
    // Initializing vector
    private static byte iv[] = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
    
    @TeiidFunction(category=FunctionCategoryConstants.SECURITY, nullOnNull=true)
    public static BinaryType aes_encrypt(String key, String data) throws FunctionExecutionException, UnsupportedEncodingException {
        return aes_encrypt(new BinaryType(key.getBytes("UTF-8")), new BinaryType(data.getBytes("UTF-8"))); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @TeiidFunction(category=FunctionCategoryConstants.SECURITY, nullOnNull=true)
    public static BinaryType aes_encrypt(BinaryType keyBytes, BinaryType dataBytes) throws FunctionExecutionException, UnsupportedEncodingException {
        
        try {
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            byte[] keyval = padkey(keyBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //$NON-NLS-1$
            SecretKey keyValue = new SecretKeySpec(keyval,"AES"); //$NON-NLS-1$
            cipher.init(Cipher.ENCRYPT_MODE, keyValue, ivspec);
            byte[] encryptedByte = cipher.doFinal(dataBytes.getBytes());
            return new BinaryType(org.teiid.core.util.Base64.encodeBytes(encryptedByte).getBytes("UTF-8")); //$NON-NLS-1$
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new FunctionExecutionException(e);
        }
        
    }

    @TeiidFunction(category=FunctionCategoryConstants.SECURITY, nullOnNull=true)
    public static BinaryType aes_decrypt(String key, String data) throws UnsupportedEncodingException, FunctionExecutionException {
        return aes_decrypt(new BinaryType(key.getBytes("UTF-8")), new BinaryType(data.getBytes("UTF-8"))); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @TeiidFunction(category=FunctionCategoryConstants.SECURITY, nullOnNull=true)
    public static BinaryType aes_decrypt(BinaryType keyBytes, BinaryType dataBytes) throws FunctionExecutionException {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            byte[] keyval = padkey(keyBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //$NON-NLS-1$
            SecretKey keyValue = new SecretKeySpec(keyval,"AES"); //$NON-NLS-1$
            cipher.init(Cipher.DECRYPT_MODE, keyValue, ivspec);
            byte[] encryptedTextByte = org.teiid.core.util.Base64.decode(new String(dataBytes.getBytes()));
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            return new BinaryType(decryptedByte);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new FunctionExecutionException(e);
        }
    }
    
    private static byte[] padkey(BinaryType keyBytes) {
        byte[] padding = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        byte[] bytes = keyBytes.getBytes();
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

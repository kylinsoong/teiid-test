package com.simba.license.interfaces;

import com.simba.commons.codec.binary.Base64;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class SimbaRsaKey
{
  private byte[] mExponent;
  private byte[] mModulus;
  
  public SimbaRsaKey(String paramString1, String paramString2)
  {
    this.mExponent = Base64.decodeBase64(paramString1.getBytes());
    this.mModulus = Base64.decodeBase64(paramString2.getBytes());
  }
  
  public RSAPublicKey getRSAPublicKey()
  {
    RSAPublicKey localRSAPublicKey = null;
    BigInteger localBigInteger1 = new BigInteger(1, this.mExponent);
    BigInteger localBigInteger2 = new BigInteger(1, this.mModulus);
    RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(localBigInteger2, localBigInteger1);
    try
    {
      KeyFactory localKeyFactory = KeyFactory.getInstance("RSA");
      localRSAPublicKey = (RSAPublicKey)localKeyFactory.generatePublic(localRSAPublicKeySpec);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}catch (InvalidKeySpecException localInvalidKeySpecException) {}
    return localRSAPublicKey;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/SimbaRsaKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
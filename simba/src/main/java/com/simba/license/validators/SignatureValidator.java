package com.simba.license.validators;

import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.IValidator;
import com.simba.license.interfaces.LicenseInfo;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

public class SignatureValidator
  implements IValidator
{
  public boolean validate(IValidationInfoProvider paramIValidationInfoProvider, ArrayList<String> paramArrayList)
  {
    boolean bool = false;
    LicenseInfo localLicenseInfo = paramIValidationInfoProvider.getLicenseInfo();
    RSAPublicKey localRSAPublicKey;
    if (localLicenseInfo.getLicenseType().equals("Evaluation")) {
      localRSAPublicKey = paramIValidationInfoProvider.getTrialPublicKey();
    } else {
      localRSAPublicKey = paramIValidationInfoProvider.getProductionPublicKey();
    }
    byte[] arrayOfByte1 = localLicenseInfo.getLicenseInfoAsText().replaceAll("\\s+", "").getBytes();
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 8];
    byte[] arrayOfByte3 = localLicenseInfo.getBitMask();
    for (int i = 0; i < arrayOfByte2.length; i++) {
      arrayOfByte2[i] = (i < arrayOfByte1.length ? arrayOfByte1[i] : arrayOfByte3[(i - arrayOfByte1.length)]);
    }
    Signature localSignature = null;
    try
    {
      localSignature = Signature.getInstance("SHA1withRSA");
      localSignature.initVerify(localRSAPublicKey);
      localSignature.update(arrayOfByte2);
      bool = localSignature.verify(paramIValidationInfoProvider.getLicenseInfo().getLicenseSignature());
      if (!bool) {
        paramArrayList.add("Invalid signature or license was tampered with");
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      str = String.format("Internal Licensing error: %s", new Object[] { localNoSuchAlgorithmException.getMessage() });
      paramArrayList.add(str);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      str = String.format("Invalid signature: %s", new Object[] { localInvalidKeyException.getMessage() });
      paramArrayList.add(str);
    }
    catch (SignatureException localSignatureException)
    {
      String str = String.format("Invalid signature: %s", new Object[] { localSignatureException.getMessage() });
      paramArrayList.add(str);
    }
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/validators/SignatureValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
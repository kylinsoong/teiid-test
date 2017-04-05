package com.simba.license;

import com.simba.license.interfaces.IProductInfo;
import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.IValidator;
import com.simba.license.interfaces.LicenseInfo;
import com.simba.license.interfaces.SimbaLicenseException;
import com.simba.license.validators.ExpiryDateValidator;
import com.simba.license.validators.ProductNameValidator;
import com.simba.license.validators.ProductPlatfromValidator;
import com.simba.license.validators.ProductVersionValidator;
import com.simba.license.validators.SignatureValidator;
import java.util.ArrayList;

public class LicenseUtil
{
  private IValidationInfoProvider mValidationProvider;
  private IValidator[] mValidators = new IValidator[64];
  
  public LicenseUtil(IProductInfo paramIProductInfo, String paramString)
    throws SimbaLicenseException
  {
    this.mValidationProvider = new BasicValidationProvider(paramIProductInfo, paramString);
    initializeValidators();
  }
  
  public boolean validate(ArrayList<String> paramArrayList)
  {
    byte[] arrayOfByte = this.mValidationProvider.getLicenseInfo().getBitMask();
    Object localObject = null;
    if (this.mValidators[com.simba.license.interfaces.ValidatorsEnum.SIGNATURE.bitNumber()] != null) {
      localObject = this.mValidators[com.simba.license.interfaces.ValidatorsEnum.SIGNATURE.bitNumber()];
    } else {
      localObject = new SignatureValidator();
    }
    boolean bool = ((IValidator)localObject).validate(this.mValidationProvider, paramArrayList);
    if (!bool) {
      return false;
    }
    for (int i = 1; i < 64; i++) {
      if (isBitSet(arrayOfByte, i)) {
        if (this.mValidators[i] == null)
        {
          String str = String.format("Validator is not defined for bit %i", new Object[] { Integer.valueOf(i) });
          paramArrayList.add(str);
        }
        else
        {
          bool = this.mValidators[i].validate(this.mValidationProvider, paramArrayList);
          if (!bool) {
            return false;
          }
        }
      }
    }
    return true;
  }
  
  private boolean isBitSet(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt <= 64) {
      return 0 != (paramArrayOfByte[(paramInt / 8)] & 1 << 7 - paramInt % 8);
    }
    return false;
  }
  
  private void initializeValidators()
  {
    this.mValidators[com.simba.license.interfaces.ValidatorsEnum.SIGNATURE.bitNumber()] = new SignatureValidator();
    this.mValidators[com.simba.license.interfaces.ValidatorsEnum.EXPIRY.bitNumber()] = new ExpiryDateValidator();
    this.mValidators[com.simba.license.interfaces.ValidatorsEnum.PRODUCT_NAME.bitNumber()] = new ProductNameValidator();
    this.mValidators[com.simba.license.interfaces.ValidatorsEnum.PRODUCT_VERSION.bitNumber()] = new ProductVersionValidator();
    this.mValidators[com.simba.license.interfaces.ValidatorsEnum.PRODUCT_PLATFORM.bitNumber()] = new ProductPlatfromValidator();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/LicenseUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
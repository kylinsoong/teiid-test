package com.simba.license.validators;

import com.simba.license.interfaces.IProductInfo;
import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.IValidator;
import com.simba.license.interfaces.LicenseInfo;
import java.util.ArrayList;

public class ProductPlatfromValidator
  implements IValidator
{
  public boolean validate(IValidationInfoProvider paramIValidationInfoProvider, ArrayList<String> paramArrayList)
  {
    String str1 = "Any";
    boolean bool = false;
    LicenseInfo localLicenseInfo = paramIValidationInfoProvider.getLicenseInfo();
    String str2 = paramIValidationInfoProvider.getProductInfo().getProductPlatform();
    String str3 = localLicenseInfo.getProductPlatform();
    if ((str2.equals(str1)) || (str3.equals(str1))) {
      return true;
    }
    if (str2.equals(str3))
    {
      bool = true;
    }
    else
    {
      String str4 = String.format("Invalid product platform. Expected \"%s.\"", new Object[] { str2 });
      paramArrayList.add(str4);
    }
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/validators/ProductPlatfromValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
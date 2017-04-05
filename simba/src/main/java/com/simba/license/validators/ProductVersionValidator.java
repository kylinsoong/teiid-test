package com.simba.license.validators;

import com.simba.license.interfaces.IProductInfo;
import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.IValidator;
import com.simba.license.interfaces.LicenseInfo;
import java.util.ArrayList;

public class ProductVersionValidator
  implements IValidator
{
  public boolean validate(IValidationInfoProvider paramIValidationInfoProvider, ArrayList<String> paramArrayList)
  {
    boolean bool = false;
    String str1 = "Any";
    int i = Integer.MAX_VALUE;
    int j = 0;
    LicenseInfo localLicenseInfo = paramIValidationInfoProvider.getLicenseInfo();
    String str2 = paramIValidationInfoProvider.getProductInfo().getProductVersion();
    String str3 = localLicenseInfo.getProductVersion();
    if ((str2.equals(str1)) || (str3.equals(str1))) {
      return true;
    }
    String[] arrayOfString = str2.split("\\D+");
    if ((arrayOfString.length > 0) && (arrayOfString[0].length() > 0)) {
      i = Integer.parseInt(arrayOfString[0]);
    }
    arrayOfString = str3.split("\\D+");
    if ((arrayOfString.length > 0) && (arrayOfString[0].length() > 0)) {
      j = Integer.parseInt(arrayOfString[0]);
    }
    if (j >= i) {
      bool = true;
    } else {
      paramArrayList.add("Invalid product version for this license");
    }
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/validators/ProductVersionValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
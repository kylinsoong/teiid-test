package com.simba.license.validators;

import com.simba.license.interfaces.IProductInfo;
import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.IValidator;
import com.simba.license.interfaces.LicenseInfo;
import java.util.ArrayList;

public class ProductNameValidator
  implements IValidator
{
  public boolean validate(IValidationInfoProvider paramIValidationInfoProvider, ArrayList<String> paramArrayList)
  {
    boolean bool = false;
    LicenseInfo localLicenseInfo = paramIValidationInfoProvider.getLicenseInfo();
    String str = paramIValidationInfoProvider.getProductInfo().getProductName();
    if (str.equals(localLicenseInfo.getProduct())) {
      bool = true;
    } else {
      paramArrayList.add("Invalid product name for this license");
    }
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/validators/ProductNameValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
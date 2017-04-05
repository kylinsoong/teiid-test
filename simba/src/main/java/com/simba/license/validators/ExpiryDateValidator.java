package com.simba.license.validators;

import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.IValidator;
import com.simba.license.interfaces.LicenseInfo;
import java.util.ArrayList;
import java.util.Date;

public class ExpiryDateValidator
  implements IValidator
{
  public boolean validate(IValidationInfoProvider paramIValidationInfoProvider, ArrayList<String> paramArrayList)
  {
    boolean bool = false;
    LicenseInfo localLicenseInfo = paramIValidationInfoProvider.getLicenseInfo();
    Date localDate = new Date();
    if (localDate.before(localLicenseInfo.getExpiry())) {
      bool = true;
    } else {
      paramArrayList.add("License expired");
    }
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/validators/ExpiryDateValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
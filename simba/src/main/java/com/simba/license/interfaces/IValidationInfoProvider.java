package com.simba.license.interfaces;

import java.security.interfaces.RSAPublicKey;

public abstract interface IValidationInfoProvider
{
  public abstract LicenseInfo getLicenseInfo();
  
  public abstract IProductInfo getProductInfo();
  
  public abstract RSAPublicKey getProductionPublicKey();
  
  public abstract RSAPublicKey getTrialPublicKey();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/IValidationInfoProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
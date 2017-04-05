package com.simba.license;

import com.simba.license.interfaces.IProductInfo;
import com.simba.license.interfaces.SimbaLicensePlatform;

public class LicenseModuleProductInfo
  implements IProductInfo
{
  private String mProductName;
  private String mProductVersion;
  private String mProductPlatform;
  
  public LicenseModuleProductInfo(String paramString1, String paramString2)
  {
    this.mProductName = paramString1;
    this.mProductVersion = paramString2;
    this.mProductPlatform = SimbaLicensePlatform.detect();
  }
  
  public LicenseModuleProductInfo(String paramString1, String paramString2, String paramString3)
  {
    this.mProductName = paramString1;
    this.mProductVersion = paramString2;
    this.mProductPlatform = paramString3;
  }
  
  public LicenseModuleProductInfo(IProductInfo paramIProductInfo)
  {
    this(paramIProductInfo.getProductName(), paramIProductInfo.getProductVersion(), paramIProductInfo.getProductPlatform());
  }
  
  public String getProductName()
  {
    return this.mProductName;
  }
  
  public String getProductPlatform()
  {
    return this.mProductPlatform;
  }
  
  public String getProductVersion()
  {
    return this.mProductVersion;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/LicenseModuleProductInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
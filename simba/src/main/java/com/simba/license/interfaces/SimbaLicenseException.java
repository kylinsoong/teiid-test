package com.simba.license.interfaces;

public class SimbaLicenseException
  extends Exception
{
  private String mMessage;
  
  public SimbaLicenseException(String paramString)
  {
    this.mMessage = new String(paramString);
  }
  
  public String getMessage()
  {
    return this.mMessage;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/SimbaLicenseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
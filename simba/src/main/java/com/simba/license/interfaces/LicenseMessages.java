package com.simba.license.interfaces;

public class LicenseMessages
{
  public static final String EXPIRY_VALIDATION_FAILED = "License expired";
  public static final String PLATFORM_VALIDATION_FAILED = "Invalid product platform. Expected \"%s.\"";
  public static final String PRODUCT_NAME_VALIDATION_FAILED = "Invalid product name for this license";
  public static final String VERSION_VALIDATION_FAILED = "Invalid product version for this license";
  public static final String SIGNATURE_VALIDATION_FAILED = "Invalid signature or license was tampered with";
  public static final String SIGNATURE_VALIDATION_EXCEPTION = "Invalid signature: %s";
  public static final String INTERNAL_LICENSING_FAILURE = "Internal Licensing error: %s";
  public static final String INVALID_LICENSE_FORMAT = "Invalid license text format, failed XML parsing. Exception reason: %s";
  public static final String CANNOT_FIND_KEY = "Cannot find valid key string in license";
  public static final String INVALID_EXPIRY_FORMAT = "Invalid_expiry format";
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/LicenseMessages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
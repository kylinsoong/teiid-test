package com.simba.license;

import com.simba.license.interfaces.IProductInfo;
import com.simba.license.interfaces.IValidationInfoProvider;
import com.simba.license.interfaces.LicenseInfo;
import com.simba.license.interfaces.SimbaLicenseException;
import com.simba.license.interfaces.SimbaRsaKey;
import java.security.interfaces.RSAPublicKey;

public class BasicValidationProvider
  implements IValidationInfoProvider
{
  private static final String evalModulus = "ujEmmnItGTZEkG1LY10MeTZo9e9DyDybfEDt74x3dg9pHetSsU/LZEAMDCUVs6/cthQR4y1bNb4eHrH6qxmEivonf8gcoDnrB9kCGrOruGcZPnMvebENlpO3WmAn+Tls6v79skcWfQAPY5um/hmkovLt8VRT3FD3HeYXr2DcBXljR51qDW2tatt/uhMzEK4936ORyYe1iOtgvpXnp07K9H3NCbXLHKcR0fuShvnSo0muiHG7tOD8v52dOXVI86Ob2K2LGSgp/z2bMsQ7CT4gzNJXmcLJIUlmOlRv1EbPhUc/xCaU9kOyacQ6BlMpFeTRMHR6V5i3we40nI+ChkL/NQ==";
  private static final String evalExponent = "AQAB";
  private static final String prodModulus = "tC501NaHXB133CJ/x8Qbju/XPNJzPZC1QuxqL/AS0M3pOIqt5g4vJCeYoS1u9OxHageagbWKwY5y4lF+0INmGEs59CwahUt6cb5rlXojcTwtlPEZGCLJnxuRydGOxAc6rxfKoB5zqLT3gW5w3fdFPRg9TMrET8k9wvKSSa2ZK5osK0O9VrRiTHe+H+NooTNzEec+XdwyHgjkfWz3GSbr3WFp1qV3wcGT24glT8PX0ttn0sPQ9Jq+rhqgCPWj7zeI9PPi8zdw/vqqzldxLokpNDIHDyp1ETai/4Wd5XMKCBNjNmQtli5cM7LbgqKs7aStYM1XBJ1kKiAINa0yb+bVNw==";
  private static final String prodExponent = "AQAB";
  private IProductInfo mProduct;
  private LicenseInfo mLicense;
  private SimbaRsaKey mProductionKey;
  private SimbaRsaKey mTrialKey;
  
  public BasicValidationProvider(IProductInfo paramIProductInfo, String paramString)
    throws SimbaLicenseException
  {
    this.mLicense = new LicenseInfo(paramString);
    this.mProduct = paramIProductInfo;
    this.mProductionKey = new SimbaRsaKey("AQAB", "tC501NaHXB133CJ/x8Qbju/XPNJzPZC1QuxqL/AS0M3pOIqt5g4vJCeYoS1u9OxHageagbWKwY5y4lF+0INmGEs59CwahUt6cb5rlXojcTwtlPEZGCLJnxuRydGOxAc6rxfKoB5zqLT3gW5w3fdFPRg9TMrET8k9wvKSSa2ZK5osK0O9VrRiTHe+H+NooTNzEec+XdwyHgjkfWz3GSbr3WFp1qV3wcGT24glT8PX0ttn0sPQ9Jq+rhqgCPWj7zeI9PPi8zdw/vqqzldxLokpNDIHDyp1ETai/4Wd5XMKCBNjNmQtli5cM7LbgqKs7aStYM1XBJ1kKiAINa0yb+bVNw==");
    this.mTrialKey = new SimbaRsaKey("AQAB", "ujEmmnItGTZEkG1LY10MeTZo9e9DyDybfEDt74x3dg9pHetSsU/LZEAMDCUVs6/cthQR4y1bNb4eHrH6qxmEivonf8gcoDnrB9kCGrOruGcZPnMvebENlpO3WmAn+Tls6v79skcWfQAPY5um/hmkovLt8VRT3FD3HeYXr2DcBXljR51qDW2tatt/uhMzEK4936ORyYe1iOtgvpXnp07K9H3NCbXLHKcR0fuShvnSo0muiHG7tOD8v52dOXVI86Ob2K2LGSgp/z2bMsQ7CT4gzNJXmcLJIUlmOlRv1EbPhUc/xCaU9kOyacQ6BlMpFeTRMHR6V5i3we40nI+ChkL/NQ==");
  }
  
  public LicenseInfo getLicenseInfo()
  {
    return this.mLicense;
  }
  
  public IProductInfo getProductInfo()
  {
    return this.mProduct;
  }
  
  public RSAPublicKey getProductionPublicKey()
  {
    if (this.mProductionKey != null) {
      return this.mProductionKey.getRSAPublicKey();
    }
    return null;
  }
  
  public RSAPublicKey getTrialPublicKey()
  {
    if (this.mTrialKey != null) {
      return this.mTrialKey.getRSAPublicKey();
    }
    return null;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/BasicValidationProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
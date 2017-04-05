package com.simba.utilities;

import com.simba.dsi.core.utilities.ConnSettingRequestMap;
import com.simba.dsi.core.utilities.ConnSettingResponseMap;
import com.simba.dsi.core.utilities.ConnectionSetting;
import com.simba.dsi.core.utilities.Variant;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.GeneralException;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public class PropertyTypeConverter
{
  public static ConnSettingRequestMap toConnSettingRequestMap(Properties paramProperties)
    throws ErrorException
  {
    ConnSettingRequestMap localConnSettingRequestMap = new ConnSettingRequestMap();
    if (null == paramProperties) {
      return localConnSettingRequestMap;
    }
    Enumeration localEnumeration = paramProperties.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = localEnumeration.nextElement().toString();
      try
      {
        Variant localVariant = new Variant(0, paramProperties.get(str).toString());
        localConnSettingRequestMap.setProperty(str, localVariant);
      }
      catch (Exception localException)
      {
        int i = 0;
        if ((localException instanceof SQLException)) {
          i = ((SQLException)localException).getErrorCode();
        } else if ((localException instanceof ErrorException)) {
          throw ((ErrorException)localException);
        }
        throw new GeneralException(localException.getLocalizedMessage(), i, localException);
      }
    }
    return localConnSettingRequestMap;
  }
  
  public static DriverPropertyInfo[] toDriverPropertyInfo(ConnSettingResponseMap paramConnSettingResponseMap)
  {
    if (null == paramConnSettingResponseMap) {
      return new DriverPropertyInfo[0];
    }
    DriverPropertyInfo[] arrayOfDriverPropertyInfo = new DriverPropertyInfo[paramConnSettingResponseMap.size()];
    Iterator localIterator = paramConnSettingResponseMap.getKeysIterator();
    for (int i = 0; localIterator.hasNext(); i++)
    {
      String str = (String)localIterator.next();
      ConnectionSetting localConnectionSetting = paramConnSettingResponseMap.getProperty(str);
      arrayOfDriverPropertyInfo[i] = new DriverPropertyInfo(str, null);
      arrayOfDriverPropertyInfo[i].description = localConnectionSetting.getLabel();
      arrayOfDriverPropertyInfo[i].required = localConnectionSetting.isRequired();
      ArrayList localArrayList = localConnectionSetting.getValues();
      arrayOfDriverPropertyInfo[i].choices = new String[localArrayList.size()];
      for (int j = 0; j < localArrayList.size(); j++) {
        arrayOfDriverPropertyInfo[i].choices[j] = ((Variant)localArrayList.get(j)).getString();
      }
    }
    return arrayOfDriverPropertyInfo;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/utilities/PropertyTypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
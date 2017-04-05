package com.simba.utilities;

import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.exceptions.GeneralException;

public class TypeNames
{
  public static String getTypeClassName(int paramInt)
    throws GeneralException
  {
    String str = "";
    switch (paramInt)
    {
    case 2003: 
      str = "java.sql.Array";
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      str = "java.lang.String";
      break;
    case 2: 
    case 3: 
      str = "java.math.BigDecimal";
      break;
    case 0: 
      str = "null";
      break;
    case -5: 
      str = "java.lang.Long";
      break;
    case 5: 
      str = "java.lang.Integer";
      break;
    case -6: 
      str = "java.lang.Integer";
      break;
    case -7: 
    case 16: 
      str = "java.lang.Boolean";
      break;
    case 4: 
      str = "java.lang.Integer";
      break;
    case 7: 
      str = "java.lang.Float";
      break;
    case 6: 
    case 8: 
      str = "java.lang.Double";
      break;
    case -4: 
    case -3: 
    case -2: 
      str = "[B";
      break;
    case 91: 
      str = "java.sql.Date";
      break;
    case 92: 
      str = "java.sql.Time";
      break;
    case 93: 
      str = "java.sql.Timestamp";
      break;
    case -11: 
      str = "java.util.UUID";
      break;
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      str = "com.simba.dsi.dataengine.utilities.DSITimeSpan";
      break;
    case 101: 
    case 102: 
    case 107: 
      str = "com.simba.dsi.dataengine.utilities.DSIMonthSpan";
      break;
    default: 
      throw new GeneralException(1, JDBCMessageKey.UNKNOWN_DATA_TYPE.name(), new String[] { String.valueOf(paramInt) });
    }
    return str;
  }
  
  public static String getTypeName(int paramInt)
  {
    String str = "";
    switch (paramInt)
    {
    case 2003: 
      str = "Array";
      break;
    case -1: 
    case 1: 
    case 12: 
      str = "String";
      break;
    case 2: 
    case 3: 
      str = "BigDecimal";
      break;
    case 0: 
      str = "null";
      break;
    case -5: 
      str = "long";
      break;
    case 5: 
      str = "short";
      break;
    case -6: 
      str = "byte";
      break;
    case -7: 
    case 16: 
      str = "boolean";
      break;
    case 4: 
      str = "int";
      break;
    case 7: 
      str = "float";
      break;
    case 6: 
    case 8: 
      str = "double";
      break;
    case -4: 
    case -3: 
    case -2: 
      str = "byte[]";
      break;
    case 91: 
      str = "Date";
      break;
    case 92: 
      str = "Time";
      break;
    case 93: 
      str = "Timestamp";
      break;
    case 2000: 
      str = "Object";
      break;
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      str = "DSITimeSpan";
      break;
    case 101: 
    case 102: 
    case 107: 
      str = "DSIMonthSpan";
      break;
    case -11: 
      str = "UUID";
      break;
    default: 
      str = "Unknown";
    }
    return str;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/utilities/TypeNames.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
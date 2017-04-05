package com.simba.utilities.conversion;

import com.simba.dsi.dataengine.utilities.DSIMonthSpan;
import com.simba.dsi.dataengine.utilities.DSITimeSpan;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TimeTz;
import com.simba.dsi.dataengine.utilities.TimestampTz;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.utilities.CalendarSetter;
import com.simba.dsi.utilities.StringConverter;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.TypeNames;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class TypeConverter
{
  private static final int TWO_TO_8 = 256;
  private static final int TWO_TO_16 = 65536;
  private static final long TWO_TO_32 = 4294967296L;
  private static final BigInteger TWO_TO_64 = new BigInteger("18446744073709551616");
  private static Map<Class<?>, Integer> CLASS_TO_TYPE_MAP = new HashMap();
  private static final Map<Integer, List<Integer>> TYPE_CONVERSION_MAP;
  private static final Map<String, String> NORMALIZE_DOUBLE_OR_FLOAT_MAP;
  private static final Map<Integer, List<Integer>> STREAM_FROM_CONVERSION_MAP = new HashMap()
  {
    private static final long serialVersionUID = 131682938025320073L;
  };
  private static final Map<Integer, List<Integer>> STREAM_TO_CONVERSION_MAP = new HashMap()
  {
    private static final long serialVersionUID = 3124019339494993466L;
  };
  
  public static boolean canConvert(int paramInt1, int paramInt2)
  {
    return canConvertFrom(TYPE_CONVERSION_MAP, paramInt1, paramInt2);
  }
  
  public static boolean canConvertStreamFrom(int paramInt1, int paramInt2)
  {
    return canConvertFrom(STREAM_FROM_CONVERSION_MAP, paramInt1, paramInt2);
  }
  
  public static boolean canConvertStreamTo(int paramInt1, int paramInt2)
  {
    return canConvertFrom(STREAM_TO_CONVERSION_MAP, paramInt1, paramInt2);
  }
  
  public static int getSqlType(Object paramObject)
  {
    int i = 0;
    if ((paramObject instanceof DSIMonthSpan)) {
      i = ((DSIMonthSpan)paramObject).getIntervalType();
    } else if ((paramObject instanceof DSITimeSpan)) {
      i = ((DSITimeSpan)paramObject).getIntervalType();
    } else if ((paramObject instanceof Calendar)) {
      i = ((Integer)CLASS_TO_TYPE_MAP.get(Calendar.class)).intValue();
    } else if (CLASS_TO_TYPE_MAP.containsKey(paramObject.getClass())) {
      i = ((Integer)CLASS_TO_TYPE_MAP.get(paramObject.getClass())).intValue();
    }
    return i;
  }
  
  public static BigDecimal toBigDecimal(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (TypeUtilities.isExactNumericType(i))
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return (BigDecimal)paramDataWrapper.getObject();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 3)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    try
    {
      if (TypeUtilities.isBooleanType(i)) {
        return new BigDecimal(toInt(paramDataWrapper, paramIWarningListener));
      }
      if (1 == i) {
        return new BigDecimal(toString(paramDataWrapper, null).trim());
      }
      return new BigDecimal(toString(paramDataWrapper, null));
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(2) });
    }
  }
  
  public static BigDecimal toBigDecimal(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (TypeUtilities.isExactNumericType(i))
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return rescaleBigDecimal((BigDecimal)paramDataWrapper.getObject(), paramTypeMetadata.getScale(), paramIWarningListener);
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 3)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    try
    {
      BigDecimal localBigDecimal = null;
      if (TypeUtilities.isBooleanType(i)) {
        localBigDecimal = new BigDecimal(toInt(paramDataWrapper, paramIWarningListener));
      } else if (TypeUtilities.isCharacterType(i))
      {
        if (1 == i) {
          localBigDecimal = new BigDecimal(((String)paramDataWrapper.getObject()).trim());
        } else {
          localBigDecimal = new BigDecimal((String)paramDataWrapper.getObject());
        }
      }
      else {
        localBigDecimal = new BigDecimal(toString(paramDataWrapper, paramTypeMetadata));
      }
      return rescaleBigDecimal(localBigDecimal, paramTypeMetadata.getScale(), paramIWarningListener);
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(2) });
    }
  }
  
  public static boolean toBoolean(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (-7 == i)
    {
      if (paramDataWrapper.isNull()) {
        return false;
      }
      return paramDataWrapper.getBit().booleanValue();
    }
    if (16 == i)
    {
      if (paramDataWrapper.isNull()) {
        return false;
      }
      return paramDataWrapper.getBoolean().booleanValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, -7)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return false;
    }
    try
    {
      if (TypeUtilities.isCharacterType(i))
      {
        String str = (String)paramDataWrapper.getObject();
        if (1 == i) {
          str = str.trim();
        }
        return (!str.equalsIgnoreCase("false")) && (!str.equals("0")) && (!str.equals("0.0")) && (!str.equalsIgnoreCase("f"));
      }
      return 0.0F != toFloat(paramDataWrapper, paramIWarningListener);
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(16) });
    }
  }
  
  public static byte toByte(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (-6 == i)
    {
      if (paramDataWrapper.isNull()) {
        return 0;
      }
      return paramDataWrapper.getTinyInt().byteValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, -6)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return 0;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return 1;
        }
        return 0;
      case 1: 
        try
        {
          return (byte)(int)Long.parseLong(((String)localObject).trim());
        }
        catch (Exception localException1)
        {
          return (byte)(int)Double.parseDouble(((String)localObject).trim());
        }
      case -1: 
      case 12: 
        try
        {
          return (byte)(int)Long.parseLong((String)localObject);
        }
        catch (Exception localException2)
        {
          return (byte)(int)Double.parseDouble((String)localObject);
        }
      case 2: 
      case 3: 
        return ((BigDecimal)localObject).byteValue();
      }
      return ((Number)localObject).byteValue();
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(-6) });
    }
  }
  
  public static byte[] toBytes(DataWrapper paramDataWrapper)
    throws IncorrectTypeException
  {
    int i = paramDataWrapper.getType();
    if (TypeUtilities.isBinaryType(i))
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      switch (i)
      {
      case -2: 
        return paramDataWrapper.getBinary();
      case -3: 
        return paramDataWrapper.getVarBinary();
      case -4: 
        return paramDataWrapper.getLongVarBinary();
      }
    }
    throw new IncorrectTypeException();
  }
  
  public static java.sql.Date toDate(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    return toDate(paramDataWrapper, null, paramIWarningListener);
  }
  
  public static java.sql.Date toDate(DataWrapper paramDataWrapper, Calendar paramCalendar, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    java.sql.Date localDate = convertToDate(paramDataWrapper, paramCalendar, paramIWarningListener);
    if (null != localDate)
    {
      Object localObject = paramDataWrapper.getObject();
      if ((!(localObject instanceof TimeTz)) && (!(localObject instanceof TimestampTz)) && (!(localObject instanceof String))) {
        localDate = CalendarSetter.getDate(localDate, paramCalendar);
      }
    }
    return localDate;
  }
  
  public static UUID toGUID(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (-11 == i) {
      return paramDataWrapper.getGuid();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, -11)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      return UUID.fromString((String)localObject);
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(-11) });
    }
  }
  
  public static double toDouble(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (8 == i)
    {
      if (paramDataWrapper.isNull()) {
        return 0.0D;
      }
      return paramDataWrapper.getDouble().doubleValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 8)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return 0.0D;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return 1.0D;
        }
        return 0.0D;
      case -1: 
      case 1: 
      case 12: 
        return Double.parseDouble(normalizeInfinityOrNaN((String)localObject));
      case 2: 
      case 3: 
        return ((BigDecimal)localObject).doubleValue();
      }
      return ((Number)localObject).doubleValue();
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(8) });
    }
  }
  
  public static float toFloat(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (7 == i)
    {
      if (paramDataWrapper.isNull()) {
        return 0.0F;
      }
      return paramDataWrapper.getReal().floatValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 7)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return 0.0F;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return 1.0F;
        }
        return 0.0F;
      case -1: 
      case 1: 
      case 12: 
        return Float.parseFloat(normalizeInfinityOrNaN((String)localObject));
      case 2: 
      case 3: 
        return ((BigDecimal)localObject).floatValue();
      }
      return ((Number)localObject).floatValue();
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(7) });
    }
  }
  
  public static int toInt(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (4 == i)
    {
      if (paramDataWrapper.isNull()) {
        return 0;
      }
      return paramDataWrapper.getInteger().intValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 4)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return 0;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return 1;
        }
        return 0;
      case 1: 
        try
        {
          return (int)Long.parseLong(((String)localObject).trim());
        }
        catch (Exception localException1)
        {
          return (int)Double.parseDouble(((String)localObject).trim());
        }
      case -1: 
      case 12: 
        try
        {
          return (int)Long.parseLong((String)localObject);
        }
        catch (Exception localException2)
        {
          return (int)Double.parseDouble((String)localObject);
        }
      case 2: 
      case 3: 
        return ((BigDecimal)localObject).intValue();
      }
      return ((Number)localObject).intValue();
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(4) });
    }
  }
  
  public static Object toInterval(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    int j = paramTypeMetadata.getType();
    if ((TypeUtilities.isIntervalType(i)) && (i == j))
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return paramDataWrapper.getObject();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, j)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -1: 
      case 1: 
      case 12: 
        return IntervalConverter.convertStringToInterval((String)localObject, paramTypeMetadata, paramIWarningListener);
      }
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(4) });
    }
    return null;
  }
  
  public static long toLong(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (-5 == i)
    {
      if (paramDataWrapper.isNull()) {
        return 0L;
      }
      return paramDataWrapper.getBigIntAsLong().longValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, -5)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return 0L;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return 1L;
        }
        return 0L;
      case 1: 
        return new BigDecimal(((String)localObject).trim()).longValue();
      case -1: 
      case 12: 
        return new BigDecimal((String)localObject).longValue();
      case 2: 
      case 3: 
        return ((BigDecimal)localObject).longValue();
      }
      return ((Number)localObject).longValue();
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(-5) });
    }
  }
  
  public static BigInteger toBigInteger(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (-5 == i)
    {
      if (paramDataWrapper.isNull()) {
        return BigInteger.ZERO;
      }
      return paramDataWrapper.getBigInt();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, -5)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return BigInteger.ZERO;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return BigInteger.ONE;
        }
        return BigInteger.ZERO;
      case 1: 
        return new BigInteger(((String)localObject).trim());
      case -1: 
      case 12: 
        return new BigInteger((String)localObject);
      case 2: 
      case 3: 
        return ((BigDecimal)localObject).toBigInteger();
      }
      return new BigInteger(((Number)localObject).toString());
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(-5) });
    }
  }
  
  public static Object toObject(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramTypeMetadata.getType();
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 2000)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    try
    {
      switch (i)
      {
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        return toString(paramDataWrapper, paramTypeMetadata);
      case -5: 
        return Long.valueOf(toLong(paramDataWrapper, paramIWarningListener));
      case -6: 
      case 4: 
      case 5: 
        return Integer.valueOf(toInt(paramDataWrapper, paramIWarningListener));
      case 6: 
      case 8: 
        return Double.valueOf(toDouble(paramDataWrapper, paramIWarningListener));
      case 7: 
        return Float.valueOf(toFloat(paramDataWrapper, paramIWarningListener));
      case 2: 
      case 3: 
        return toBigDecimal(paramDataWrapper, paramIWarningListener);
      case 91: 
        return toDate(paramDataWrapper, paramIWarningListener);
      case 92: 
        return toTime(paramDataWrapper, paramTypeMetadata, paramIWarningListener);
      case 93: 
        return toTimestamp(paramDataWrapper, paramTypeMetadata, paramIWarningListener);
      case 101: 
      case 102: 
      case 103: 
      case 104: 
      case 105: 
      case 106: 
      case 107: 
      case 108: 
      case 109: 
      case 110: 
      case 111: 
      case 112: 
      case 113: 
        return toInterval(paramDataWrapper, paramTypeMetadata, paramIWarningListener);
      case -11: 
        return toGUID(paramDataWrapper, paramIWarningListener);
      case -7: 
      case 16: 
        return Boolean.valueOf(toBoolean(paramDataWrapper, paramIWarningListener));
      }
      return paramDataWrapper.getObject();
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    return null;
  }
  
  public static short toShort(DataWrapper paramDataWrapper, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (5 == i)
    {
      if (paramDataWrapper.isNull()) {
        return 0;
      }
      return paramDataWrapper.getSmallInt().shortValue();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 5)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return 0;
    }
    Object localObject = paramDataWrapper.getObject();
    try
    {
      switch (i)
      {
      case -6: 
      case -5: 
      case -4: 
      case -3: 
      case -2: 
      case 0: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 9: 
      case 10: 
      case 11: 
      case 13: 
      case 14: 
      case 15: 
      default: 
        return ((Number)localObject).shortValue();
      case -7: 
      case 16: 
        if (((Boolean)localObject).booleanValue()) {
          return 1;
        }
        return 0;
      case 1: 
        try
        {
          return (short)(int)Long.parseLong(((String)localObject).trim());
        }
        catch (Exception localException1)
        {
          return (short)(int)Double.parseDouble(((String)localObject).trim());
        }
      case -1: 
      case 12: 
        try
        {
          return (short)(int)Long.parseLong((String)localObject);
        }
        catch (Exception localException2)
        {
          return (short)(int)Double.parseDouble((String)localObject);
        }
      }
      return ((BigDecimal)localObject).shortValue();
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(5) });
    }
  }
  
  public static String toString(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (TypeUtilities.isCharacterType(i))
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return (String)paramDataWrapper.getObject();
    }
    if (((i > 1999) && (i < 2007)) || (70 == i)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    Object localObject1 = paramDataWrapper.getObject();
    Object localObject2;
    Object localObject3;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i5;
    switch (i)
    {
    case -7: 
    case 16: 
      if (((Boolean)localObject1).booleanValue()) {
        return "1";
      }
      return "0";
    case -4: 
    case -3: 
    case -2: 
      localObject2 = new StringBuilder();
      localObject3 = (byte[])localObject1;
      for (int j = 0; j < localObject3.length; j++)
      {
        m = localObject3[j];
        if (0 > m) {
          m += 256;
        }
        String str = Integer.toString(m, 16).toUpperCase();
        if (1 == str.length()) {
          ((StringBuilder)localObject2).append("0");
        }
        ((StringBuilder)localObject2).append(str);
      }
      return ((StringBuilder)localObject2).toString();
    case 2: 
    case 3: 
      return ((BigDecimal)localObject1).toPlainString();
    case 91: 
      localObject2 = (java.sql.Date)localObject1;
      if (((java.sql.Date)localObject2).getTime() < getZeroTimeAD(Calendar.getInstance())) {
        return '-' + ((java.sql.Date)localObject2).toString();
      }
      return ((java.sql.Date)localObject2).toString();
    case 92: 
      localObject2 = new char[] { '0', '0', ':', '0', '0', ':', '0', '0' };
      localObject3 = null;
      if ((localObject1 instanceof TimeTz))
      {
        localObject4 = (TimeTz)localObject1;
        localObject3 = ((TimeTz)localObject4).getTimezoneCalendar();
        ((Calendar)localObject3).clear();
        ((Calendar)localObject3).setTimeInMillis(((TimeTz)localObject4).getTime());
        ((Calendar)localObject3).set(1970, 0, 1);
        localObject1 = new Time(((Calendar)localObject3).getTimeInMillis());
        ((Calendar)localObject3).clear();
      }
      Object localObject4 = (Time)localObject1;
      m = ((Time)localObject4).getHours();
      n = ((Time)localObject4).getMinutes();
      i1 = ((Time)localObject4).getSeconds();
      localObject2[0] = ((char)(48 + m / 10));
      localObject2[1] = ((char)(48 + m % 10));
      localObject2[3] = ((char)(48 + n / 10));
      localObject2[4] = ((char)(48 + n % 10));
      localObject2[6] = ((char)(48 + i1 / 10));
      localObject2[7] = ((char)(48 + i1 % 10));
      i2 = 0;
      if ((paramTypeMetadata.getType() == 93) || (paramTypeMetadata.getType() == 92)) {
        i2 = paramTypeMetadata.getPrecision();
      }
      if (0 >= i2) {
        return new String((char[])localObject2);
      }
      if (3 < i2) {
        i2 = 3;
      }
      if (null == localObject3) {
        localObject3 = Calendar.getInstance();
      }
      ((Calendar)localObject3).setTimeInMillis(((Time)localObject4).getTime());
      i3 = ((Calendar)localObject3).get(14);
      StringBuilder localStringBuilder = new StringBuilder(new String((char[])localObject2));
      localStringBuilder.append(".000");
      for (i5 = 11; i5 > 8; i5--)
      {
        localStringBuilder.setCharAt(i5, (char)(48 + i3 % 10));
        i3 /= 10;
      }
      return localStringBuilder.substring(0, 9 + i2);
    case 93: 
      localObject2 = new char[] { '0', '0', '0', '0', '0', '0', '0', '0', '0', '-', '0', '0', '-', '0', '0', ' ', '0', '0', ':', '0', '0', ':', '0', '0', '.', '0', '0', '0', '0', '0', '0', '0', '0', '0' };
      localObject3 = (Timestamp)localObject1;
      int k = ((Timestamp)localObject3).getYear() + 1900;
      m = ((Timestamp)localObject3).getMonth() + 1;
      n = ((Timestamp)localObject3).getDate();
      i1 = ((Timestamp)localObject3).getHours();
      i2 = ((Timestamp)localObject3).getMinutes();
      i3 = ((Timestamp)localObject3).getSeconds();
      int i4 = 5;
      if (k > 99999999)
      {
        i4 = 0;
        localObject2[0] = ((char)(48 + k / 100000000 % 10));
        localObject2[1] = ((char)(48 + k / 10000000 % 10));
        localObject2[2] = ((char)(48 + k / 1000000 % 10));
        localObject2[3] = ((char)(48 + k / 100000 % 10));
        localObject2[4] = ((char)(48 + k / 10000 % 10));
        localObject2[5] = ((char)(48 + k / 1000 % 10));
      }
      else if (k > 9999999)
      {
        i4 = 1;
        localObject2[1] = ((char)(48 + k / 10000000 % 10));
        localObject2[2] = ((char)(48 + k / 1000000 % 10));
        localObject2[3] = ((char)(48 + k / 100000 % 10));
        localObject2[4] = ((char)(48 + k / 10000 % 10));
        localObject2[5] = ((char)(48 + k / 1000 % 10));
      }
      else if (k > 999999)
      {
        i4 = 2;
        localObject2[2] = ((char)(48 + k / 1000000 % 10));
        localObject2[3] = ((char)(48 + k / 100000 % 10));
        localObject2[4] = ((char)(48 + k / 10000 % 10));
        localObject2[5] = ((char)(48 + k / 1000 % 10));
      }
      else if (k > 99999)
      {
        i4 = 3;
        localObject2[3] = ((char)(48 + k / 100000 % 10));
        localObject2[4] = ((char)(48 + k / 10000 % 10));
        localObject2[5] = ((char)(48 + k / 1000 % 10));
      }
      else if (k > 9999)
      {
        i4 = 4;
        localObject2[4] = ((char)(48 + k / 10000 % 10));
        localObject2[5] = ((char)(48 + k / 1000 % 10));
      }
      else
      {
        localObject2[5] = ((char)(48 + k / 1000));
      }
      localObject2[6] = ((char)(48 + k / 100 % 10));
      localObject2[7] = ((char)(48 + k / 10 % 10));
      localObject2[8] = ((char)(48 + k % 10));
      localObject2[10] = ((char)(48 + m / 10));
      localObject2[11] = ((char)(48 + m % 10));
      localObject2[13] = ((char)(48 + n / 10));
      localObject2[14] = ((char)(48 + n % 10));
      localObject2[16] = ((char)(48 + i1 / 10));
      localObject2[17] = ((char)(48 + i1 % 10));
      localObject2[19] = ((char)(48 + i2 / 10));
      localObject2[20] = ((char)(48 + i2 % 10));
      localObject2[22] = ((char)(48 + i3 / 10));
      localObject2[23] = ((char)(48 + i3 % 10));
      i5 = 0;
      if ((93 == paramTypeMetadata.getType()) || (92 == paramTypeMetadata.getType())) {
        i5 = paramTypeMetadata.getPrecision();
      }
      int i6 = 24 - i4;
      if (0 < i5)
      {
        int i7 = ((Timestamp)localObject3).getNanos();
        localObject2[25] = ((char)(48 + i7 / 100000000));
        localObject2[26] = ((char)(48 + i7 / 10000000 % 10));
        localObject2[27] = ((char)(48 + i7 / 1000000 % 10));
        localObject2[28] = ((char)(48 + i7 / 100000 % 10));
        localObject2[29] = ((char)(48 + i7 / 10000 % 10));
        localObject2[30] = ((char)(48 + i7 / 1000 % 10));
        localObject2[31] = ((char)(48 + i7 / 100 % 10));
        localObject2[32] = ((char)(48 + i7 / 10 % 10));
        localObject2[33] = ((char)(48 + i7 % 10));
        i6 = 25 + i5 - i4;
      }
      Calendar localCalendar = null;
      if ((localObject3 instanceof TimestampTz)) {
        localCalendar = ((TimestampTz)localObject3).getTimezoneCalendar();
      } else {
        localCalendar = Calendar.getInstance();
      }
      if (((Timestamp)localObject3).getTime() < getZeroTimeAD(localCalendar)) {
        return '-' + new String((char[])localObject2, i4, i6);
      }
      return new String((char[])localObject2, i4, i6);
    case 101: 
      return IntervalConverter.convertIntervalYearToString(localObject1, paramTypeMetadata);
    case 102: 
      return IntervalConverter.convertIntervalMonthToString(localObject1, paramTypeMetadata);
    case 103: 
      return IntervalConverter.convertIntervalDayToString(localObject1, paramTypeMetadata);
    case 104: 
      return IntervalConverter.convertIntervalHourToString(localObject1, paramTypeMetadata);
    case 105: 
      return IntervalConverter.convertIntervalMinuteToString(localObject1, paramTypeMetadata);
    case 106: 
      return IntervalConverter.convertIntervalSecondToString(localObject1, paramTypeMetadata);
    case 107: 
      return IntervalConverter.convertIntervalYearToMonthToString(localObject1, paramTypeMetadata);
    case 108: 
      return IntervalConverter.convertIntervalDayToHourToString(localObject1, paramTypeMetadata);
    case 109: 
      return IntervalConverter.convertIntervalDayToMinuteToString(localObject1, paramTypeMetadata);
    case 110: 
      return IntervalConverter.convertIntervalDayToSecondToString(localObject1, paramTypeMetadata);
    case 111: 
      return IntervalConverter.convertIntervalHourToMinuteToString(localObject1, paramTypeMetadata);
    case 112: 
      return IntervalConverter.convertIntervalHourToSecondToString(localObject1, paramTypeMetadata);
    case 113: 
      return IntervalConverter.convertIntervalMinuteToSecondToString(localObject1, paramTypeMetadata);
    }
    return localObject1.toString();
  }
  
  public static Time toTime(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    return convertToTime(paramDataWrapper, null, paramTypeMetadata, paramIWarningListener, true);
  }
  
  public static Time toTime(DataWrapper paramDataWrapper, Calendar paramCalendar, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    Time localTime = convertToTime(paramDataWrapper, paramCalendar, paramTypeMetadata, paramIWarningListener, true);
    if (null != localTime)
    {
      Object localObject = paramDataWrapper.getObject();
      if ((!(localObject instanceof TimeTz)) && (!(localObject instanceof TimestampTz)) && (!(localTime instanceof TimeTz)) && (!(localObject instanceof String))) {
        localTime = CalendarSetter.getTime(localTime, paramCalendar);
      }
    }
    return localTime;
  }
  
  public static Timestamp toTimestamp(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    return convertToTimestamp(paramDataWrapper, null, paramTypeMetadata, paramIWarningListener);
  }
  
  public static Timestamp toTimestamp(DataWrapper paramDataWrapper, Calendar paramCalendar, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    Timestamp localTimestamp = convertToTimestamp(paramDataWrapper, paramCalendar, paramTypeMetadata, paramIWarningListener);
    if (null != localTimestamp)
    {
      Object localObject = paramDataWrapper.getObject();
      if ((!(localObject instanceof TimeTz)) && (!(localObject instanceof TimestampTz)) && (!(localTimestamp instanceof TimestampTz)) && (!(localObject instanceof String))) {
        localTimestamp = CalendarSetter.getTimestamp(localTimestamp, paramCalendar);
      }
      if ((localObject instanceof Time))
      {
        int i = localTimestamp.getNanos();
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(localTimestamp.getTime());
        localCalendar.set(1, 1970);
        localCalendar.set(2, 0);
        localCalendar.set(5, 1);
        localCalendar.set(14, 0);
        localTimestamp = new Timestamp(localCalendar.getTimeInMillis());
        localTimestamp.setNanos(i);
      }
    }
    return localTimestamp;
  }
  
  public static DataWrapper toType(DataWrapper paramDataWrapper, int paramInt, IWarningListener paramIWarningListener)
    throws SQLException, IncorrectTypeException
  {
    try
    {
      TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(paramInt);
      return toType(paramDataWrapper, localTypeMetadata, paramIWarningListener);
    }
    catch (ErrorException localErrorException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localErrorException, paramIWarningListener);
    }
  }
  
  public static DataWrapper toType(DataWrapper paramDataWrapper, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws SQLException, IncorrectTypeException
  {
    int i = paramTypeMetadata.getType();
    DataWrapper localDataWrapper = new DataWrapper();
    Object localObject;
    switch (i)
    {
    case -6: 
      if (paramTypeMetadata.isSigned())
      {
        localDataWrapper.setTinyInt((short)toByte(paramDataWrapper, paramIWarningListener));
      }
      else
      {
        short s = toShort(paramDataWrapper, paramIWarningListener);
        s = (short)((s % 256 + 256) % 256);
        localDataWrapper.setTinyInt(s);
      }
      break;
    case 5: 
      if (paramTypeMetadata.isSigned())
      {
        localDataWrapper.setSmallInt(toShort(paramDataWrapper, paramIWarningListener));
      }
      else
      {
        int j = toInt(paramDataWrapper, paramIWarningListener);
        j = (j % 65536 + 65536) % 65536;
        localDataWrapper.setSmallInt(j);
      }
      break;
    case 4: 
      if (paramTypeMetadata.isSigned())
      {
        localDataWrapper.setInteger(toInt(paramDataWrapper, paramIWarningListener));
      }
      else
      {
        long l = toLong(paramDataWrapper, paramIWarningListener);
        l = (l % 4294967296L + 4294967296L) % 4294967296L;
        localDataWrapper.setInteger(l);
      }
      break;
    case -5: 
      if (paramTypeMetadata.isSigned())
      {
        localDataWrapper.setBigInt(toLong(paramDataWrapper, paramIWarningListener));
      }
      else
      {
        localObject = toBigInteger(paramDataWrapper, paramIWarningListener);
        localObject = ((BigInteger)localObject).mod(TWO_TO_64);
        localDataWrapper.setBigInt((BigInteger)localObject);
      }
      break;
    case 7: 
      localDataWrapper.setReal(toFloat(paramDataWrapper, paramIWarningListener));
      break;
    case 6: 
      localDataWrapper.setFloat(toDouble(paramDataWrapper, paramIWarningListener));
      break;
    case 8: 
      localDataWrapper.setDouble(toDouble(paramDataWrapper, paramIWarningListener));
      break;
    case 3: 
      localObject = toBigDecimal(paramDataWrapper, paramTypeMetadata, paramIWarningListener);
      localDataWrapper.setDecimal((BigDecimal)localObject);
      break;
    case 2: 
      localObject = toBigDecimal(paramDataWrapper, paramTypeMetadata, paramIWarningListener);
      localDataWrapper.setNumeric((BigDecimal)localObject);
      break;
    case -7: 
      localDataWrapper.setBit(toBoolean(paramDataWrapper, paramIWarningListener));
      break;
    case 16: 
      localDataWrapper.setBoolean(toBoolean(paramDataWrapper, paramIWarningListener));
      break;
    case -8: 
    case 1: 
      localDataWrapper.setChar(toString(paramDataWrapper, paramTypeMetadata));
      break;
    case -9: 
    case 12: 
      localDataWrapper.setVarChar(toString(paramDataWrapper, paramTypeMetadata));
      break;
    case -10: 
    case -1: 
      localDataWrapper.setLongVarChar(toString(paramDataWrapper, paramTypeMetadata));
      break;
    case -2: 
      localDataWrapper.setBinary(toBytes(paramDataWrapper));
      break;
    case -3: 
      localDataWrapper.setVarBinary(toBytes(paramDataWrapper));
      break;
    case -4: 
      localDataWrapper.setLongVarBinary(toBytes(paramDataWrapper));
      break;
    case 91: 
      localDataWrapper.setDate(toDate(paramDataWrapper, paramIWarningListener));
      break;
    case 92: 
      localDataWrapper.setTime(toTime(paramDataWrapper, paramTypeMetadata, paramIWarningListener));
      break;
    case 93: 
      localDataWrapper.setTimestamp(toTimestamp(paramDataWrapper, paramTypeMetadata, paramIWarningListener));
      break;
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      localDataWrapper.setInterval(toInterval(paramDataWrapper, paramTypeMetadata, paramIWarningListener));
      break;
    case -11: 
      localDataWrapper.setGuid(toGUID(paramDataWrapper, paramIWarningListener));
      break;
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    case 94: 
    case 95: 
    case 96: 
    case 97: 
    case 98: 
    case 99: 
    case 100: 
    default: 
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_DATA_TYPE, paramIWarningListener, ExceptionType.DATA, new Object[] { String.valueOf(i) });
    }
    if (paramDataWrapper.isNull()) {
      localDataWrapper.setNull(i);
    }
    return localDataWrapper;
  }
  
  private static void addArrayConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2003), new ArrayList()
    {
      private static final long serialVersionUID = -5808381603173623432L;
    });
  }
  
  private static void addBigDecimalConversions()
  {
    ArrayList local4 = new ArrayList()
    {
      private static final long serialVersionUID = 1577877504986853963L;
    };
    TYPE_CONVERSION_MAP.put(Integer.valueOf(3), local4);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2), local4);
  }
  
  private static void addBlobConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2004), new ArrayList()
    {
      private static final long serialVersionUID = -7731683182785746359L;
    });
  }
  
  private static void addBooleanConversions()
  {
    ArrayList local6 = new ArrayList()
    {
      private static final long serialVersionUID = 2301839103144606467L;
    };
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-7), local6);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(16), local6);
  }
  
  private static void addByteArrayConversions()
  {
    ArrayList local7 = new ArrayList()
    {
      private static final long serialVersionUID = -1114162276767314579L;
    };
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-2), local7);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-3), local7);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-4), local7);
  }
  
  private static void addByteConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-6), new ArrayList()
    {
      private static final long serialVersionUID = -6519746940765096630L;
    });
  }
  
  private static void addClobConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2005), new ArrayList()
    {
      private static final long serialVersionUID = -6716762231938641114L;
    });
  }
  
  private static void addDatalinkConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(70), new ArrayList()
    {
      private static final long serialVersionUID = 6722890534148572228L;
    });
  }
  
  private static void addDateConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(91), new ArrayList()
    {
      private static final long serialVersionUID = -8309432154352550135L;
    });
  }
  
  private static void addDoubleConversions()
  {
    ArrayList local12 = new ArrayList()
    {
      private static final long serialVersionUID = 195745121961923503L;
    };
    TYPE_CONVERSION_MAP.put(Integer.valueOf(6), local12);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(8), local12);
  }
  
  private static void addFloatConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(7), new ArrayList()
    {
      private static final long serialVersionUID = 5643763617643704580L;
    });
  }
  
  private static void addGUIDConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-11), new ArrayList()
    {
      private static final long serialVersionUID = 5643763617823704580L;
    });
  }
  
  private static void addIntegerConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(4), new ArrayList()
    {
      private static final long serialVersionUID = 8345823480060895785L;
    });
  }
  
  private static void addIntervalDayConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(103), new ArrayList()
    {
      private static final long serialVersionUID = 4866030494371585298L;
    });
  }
  
  private static void addIntervalDayToHourConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(108), new ArrayList()
    {
      private static final long serialVersionUID = -1674137461171786776L;
    });
  }
  
  private static void addIntervalDayToMinuteConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(109), new ArrayList()
    {
      private static final long serialVersionUID = 1604160148277244646L;
    });
  }
  
  private static void addIntervalDayToSecondConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(110), new ArrayList()
    {
      private static final long serialVersionUID = 7503758923848687080L;
    });
  }
  
  private static void addIntervalHourConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(104), new ArrayList()
    {
      private static final long serialVersionUID = -5527798794271899424L;
    });
  }
  
  private static void addIntervalHourToMinuteConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(111), new ArrayList()
    {
      private static final long serialVersionUID = 4369576614259490766L;
    });
  }
  
  private static void addIntervalHourToSecondConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(112), new ArrayList()
    {
      private static final long serialVersionUID = -2680238796369792914L;
    });
  }
  
  private static void addIntervalMinuteConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(105), new ArrayList()
    {
      private static final long serialVersionUID = -3933329123374503084L;
    });
  }
  
  private static void addIntervalMinuteToSecondConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(113), new ArrayList()
    {
      private static final long serialVersionUID = 8410562587770552421L;
    });
  }
  
  private static void addIntervalMonthConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(102), new ArrayList()
    {
      private static final long serialVersionUID = -4032100586168037610L;
    });
  }
  
  private static void addIntervalSecondConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(106), new ArrayList()
    {
      private static final long serialVersionUID = 7482252078605327034L;
    });
  }
  
  private static void addIntervalYearConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(101), new ArrayList()
    {
      private static final long serialVersionUID = 3319575356233198861L;
    });
  }
  
  private static void addIntervalYearToMonthConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(107), new ArrayList()
    {
      private static final long serialVersionUID = -9112026322270541556L;
    });
  }
  
  private static void addLongConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-5), new ArrayList()
    {
      private static final long serialVersionUID = 360724167690817441L;
    });
  }
  
  private static void addObjectConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2000), new ArrayList()
    {
      private static final long serialVersionUID = 5419872955240700190L;
    });
  }
  
  private static void addRefConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2006), new ArrayList()
    {
      private static final long serialVersionUID = -6712233574615351824L;
    });
  }
  
  private static void addShortConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(5), new ArrayList()
    {
      private static final long serialVersionUID = 6565711157919530708L;
    });
  }
  
  private static void addStringConversions()
  {
    ArrayList local33 = new ArrayList()
    {
      private static final long serialVersionUID = 8390603353416789286L;
    };
    TYPE_CONVERSION_MAP.put(Integer.valueOf(1), local33);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(12), local33);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-1), local33);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-8), local33);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-9), local33);
    TYPE_CONVERSION_MAP.put(Integer.valueOf(-10), local33);
  }
  
  private static void addStructConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(2002), new ArrayList()
    {
      private static final long serialVersionUID = 5758877648898297886L;
    });
  }
  
  private static void addTimeConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(92), new ArrayList()
    {
      private static final long serialVersionUID = -6865504919287790919L;
    });
  }
  
  private static void addTimestampConversions()
  {
    TYPE_CONVERSION_MAP.put(Integer.valueOf(93), new ArrayList()
    {
      private static final long serialVersionUID = 2573081359751242726L;
    });
  }
  
  private static boolean canConvertFrom(Map<Integer, List<Integer>> paramMap, int paramInt1, int paramInt2)
  {
    List localList = (List)paramMap.get(Integer.valueOf(paramInt1));
    if (null == localList) {
      return false;
    }
    return localList.contains(Integer.valueOf(paramInt2));
  }
  
  private static java.sql.Date convertToDate(DataWrapper paramDataWrapper, Calendar paramCalendar, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (91 == i)
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return (java.sql.Date)paramDataWrapper.getObject();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 91)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    try
    {
      switch (i)
      {
      case 93: 
        localObject1 = paramDataWrapper.getObject();
        if ((localObject1 instanceof TimestampTz))
        {
          TimestampTz localTimestampTz = (TimestampTz)localObject1;
          localObject2 = localTimestampTz.getTimezoneCalendar();
          ((Calendar)localObject2).clear();
          ((Calendar)localObject2).setTimeInMillis(localTimestampTz.getTime());
          ((Calendar)localObject2).set(11, 0);
          ((Calendar)localObject2).set(12, 0);
          ((Calendar)localObject2).set(13, 0);
          ((Calendar)localObject2).set(14, 0);
          return new java.sql.Date(((Calendar)localObject2).getTimeInMillis());
        }
        return new java.sql.Date(paramDataWrapper.getTimestamp().getTime());
      }
      if (null == paramCalendar) {
        paramCalendar = Calendar.getInstance();
      }
      Object localObject1 = toString(paramDataWrapper, null);
      int j = ((String)localObject1).indexOf(':');
      Object localObject2 = null;
      if (-1 != j)
      {
        localObject2 = new java.sql.Date(StringConverter.parseTimestamp((String)localObject1, new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone())).getTime());
      }
      else
      {
        localObject2 = StringConverter.parseDate((String)localObject1, new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone()));
        if (null == localObject2) {
          throw new IllegalArgumentException();
        }
      }
      paramCalendar.clear();
      paramCalendar.setTimeInMillis(((java.sql.Date)localObject2).getTime());
      paramCalendar.set(11, 0);
      paramCalendar.set(12, 0);
      paramCalendar.set(13, 0);
      paramCalendar.set(14, 0);
      return new java.sql.Date(paramCalendar.getTimeInMillis());
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(91) });
    }
  }
  
  private static Time convertToTime(DataWrapper paramDataWrapper, Calendar paramCalendar, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener, boolean paramBoolean)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (92 == i)
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return paramDataWrapper.getTime();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 92)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    try
    {
      Calendar localCalendar;
      switch (i)
      {
      case 91: 
        localObject1 = paramDataWrapper.getDate();
        if (paramBoolean)
        {
          localCalendar = Calendar.getInstance();
          localCalendar.setTimeInMillis(((java.sql.Date)localObject1).getTime());
          localCalendar.set(1970, 0, 1);
          return new Time(localCalendar.getTimeInMillis());
        }
        return new Time(((java.sql.Date)localObject1).getTime());
      case 93: 
        localObject1 = paramDataWrapper.getTimestampTz();
        if (paramBoolean)
        {
          localCalendar = ((TimestampTz)localObject1).getTimezoneCalendar();
          localCalendar.clear();
          localCalendar.setTimeInMillis(((TimestampTz)localObject1).getTime());
          localCalendar.set(1970, 0, 1);
          return new Time(localCalendar.getTimeInMillis());
        }
        return new Time(((TimestampTz)localObject1).getTime());
      }
      if (null == paramCalendar) {
        paramCalendar = Calendar.getInstance();
      }
      Object localObject1 = toString(paramDataWrapper, paramTypeMetadata).trim();
      int j = ((String)localObject1).indexOf(' ');
      if (-1 != j)
      {
        java.sql.Date localDate = StringConverter.parseDate(((String)localObject1).substring(0, j), new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone()));
        if (null != localDate) {
          localObject1 = ((String)localObject1).substring(j + 1);
        }
      }
      int k = ((String)localObject1).length();
      int m = ((String)localObject1).indexOf('.');
      int n = ((String)localObject1).indexOf(' ');
      if (-1 == n)
      {
        n = ((String)localObject1).indexOf('+');
        if (-1 == n) {
          n = ((String)localObject1).indexOf('-');
        }
      }
      int i1 = -1 == m ? n : -1 == n ? k : m;
      Object localObject2 = StringConverter.parseTime(((String)localObject1).substring(0, i1), new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone()));
      if (i1 != k)
      {
        if (0 < m)
        {
          int i2 = 3;
          int i3 = k;
          if (n > 0) {
            i3 = n;
          }
          if (m + 3 >= i3) {
            i2 = i3 - m - 1;
          }
          if (0 == i2) {
            throw new IllegalArgumentException();
          }
          int i4 = StringConverter.parseInt((String)localObject1, m + 1, m + i2 + 1);
          while (i2++ < 3) {
            i4 *= 10;
          }
          ((Time)localObject2).setTime(((Time)localObject2).getTime() + i4);
        }
        if (0 < n) {
          localObject2 = convertToTimeTz((Time)localObject2, ((String)localObject1).substring(n, k));
        }
      }
      paramCalendar.clear();
      paramCalendar.setTimeInMillis(((Time)localObject2).getTime());
      if (paramBoolean) {
        paramCalendar.set(1970, 0, 1);
      }
      return new Time(paramCalendar.getTimeInMillis());
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(92) });
    }
  }
  
  private static Timestamp convertToTimestamp(DataWrapper paramDataWrapper, Calendar paramCalendar, TypeMetadata paramTypeMetadata, IWarningListener paramIWarningListener)
    throws IncorrectTypeException, SQLException
  {
    int i = paramDataWrapper.getType();
    if (93 == i)
    {
      if (paramDataWrapper.isNull()) {
        return null;
      }
      return paramDataWrapper.getTimestamp();
    }
    if (!canConvertFrom(TYPE_CONVERSION_MAP, i, 93)) {
      throw new IncorrectTypeException();
    }
    if (paramDataWrapper.isNull()) {
      return null;
    }
    try
    {
      switch (i)
      {
      case 91: 
        return new Timestamp(paramDataWrapper.getDate().getTime());
      case 92: 
        return new Timestamp(paramDataWrapper.getTime().getTime());
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        str = (String)paramDataWrapper.getObject();
        boolean bool1 = str.contains("-");
        boolean bool2 = str.contains(":");
        if ((bool1) && (bool2)) {
          return StringConverter.parseTimestamp(str, new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone()));
        }
        Object localObject;
        if (bool1)
        {
          localObject = StringConverter.parseDate(str, new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone()));
          if (null == localObject) {
            throw new IllegalArgumentException();
          }
          return new Timestamp(((java.sql.Date)localObject).getTime());
        }
        if (bool2)
        {
          localObject = convertToTime(paramDataWrapper, paramCalendar, paramTypeMetadata, paramIWarningListener, false);
          if ((localObject instanceof TimeTz)) {
            localObject = ((TimeTz)localObject).getAdjustedTime();
          }
          Timestamp localTimestamp = new Timestamp(((Time)localObject).getTime());
          int j = str.indexOf('.');
          if (-1 != j)
          {
            int k = str.length();
            int m = j;
            for (;;)
            {
              m++;
              if (m >= k) {
                break;
              }
              n = str.charAt(m);
              if ((n < 48) || (n > 57)) {
                break;
              }
            }
            int n = m - j - 1;
            if (n <= 3) {
              return localTimestamp;
            }
            if (n > 9) {
              throw new IllegalArgumentException();
            }
            int i1 = StringConverter.parseInt(str, j + 1, m);
            i1 *= StringConverter.s_fractionalMultiplier[n];
            localTimestamp.setNanos(i1);
          }
          return localTimestamp;
        }
        throw new IllegalArgumentException();
      }
      String str = toString(paramDataWrapper, paramTypeMetadata);
      return StringConverter.parseTimestamp(str, new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone()));
    }
    catch (Throwable localThrowable)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CONVERT_TO_ERR, paramIWarningListener, ExceptionType.DATA, new Object[] { TypeNames.getTypeName(93) });
    }
  }
  
  private static TimeTz convertToTimeTz(Time paramTime, String paramString)
  {
    String str = paramString.trim();
    if (!Character.isLetter(str.charAt(0)))
    {
      if (Character.isDigit(str.charAt(0))) {
        str = "+" + str;
      }
      str = "GMT" + str;
    }
    TimeZone localTimeZone = TimeZone.getTimeZone(str);
    Calendar localCalendar = Calendar.getInstance(localTimeZone);
    paramTime = CalendarSetter.getTime(paramTime, localCalendar);
    return new TimeTz(paramTime, localCalendar);
  }
  
  private static void initializeClassToTypeMap()
  {
    CLASS_TO_TYPE_MAP.put(BigDecimal.class, Integer.valueOf(3));
    CLASS_TO_TYPE_MAP.put(BigInteger.class, Integer.valueOf(-5));
    CLASS_TO_TYPE_MAP.put(byte[].class, Integer.valueOf(-2));
    CLASS_TO_TYPE_MAP.put(Byte.class, Integer.valueOf(-6));
    CLASS_TO_TYPE_MAP.put(ByteArrayInputStream.class, Integer.valueOf(-2));
    CLASS_TO_TYPE_MAP.put(Boolean.class, Integer.valueOf(-7));
    CLASS_TO_TYPE_MAP.put(java.sql.Date.class, Integer.valueOf(91));
    CLASS_TO_TYPE_MAP.put(Double.class, Integer.valueOf(8));
    CLASS_TO_TYPE_MAP.put(Float.class, Integer.valueOf(7));
    CLASS_TO_TYPE_MAP.put(Integer.class, Integer.valueOf(4));
    CLASS_TO_TYPE_MAP.put(Long.class, Integer.valueOf(-5));
    CLASS_TO_TYPE_MAP.put(Short.class, Integer.valueOf(5));
    CLASS_TO_TYPE_MAP.put(String.class, Integer.valueOf(1));
    CLASS_TO_TYPE_MAP.put(StringReader.class, Integer.valueOf(1));
    CLASS_TO_TYPE_MAP.put(Time.class, Integer.valueOf(92));
    CLASS_TO_TYPE_MAP.put(Timestamp.class, Integer.valueOf(93));
    CLASS_TO_TYPE_MAP.put(UUID.class, Integer.valueOf(-11));
    CLASS_TO_TYPE_MAP.put(java.util.Date.class, Integer.valueOf(93));
    CLASS_TO_TYPE_MAP.put(Calendar.class, Integer.valueOf(93));
  }
  
  private static String normalizeInfinityOrNaN(String paramString)
  {
    String str = (String)NORMALIZE_DOUBLE_OR_FLOAT_MAP.get(paramString.toUpperCase());
    if (str != null) {
      return str;
    }
    return paramString;
  }
  
  private static BigDecimal rescaleBigDecimal(BigDecimal paramBigDecimal, int paramInt, IWarningListener paramIWarningListener)
  {
    int i = paramBigDecimal.scale();
    if (i == paramInt) {
      return paramBigDecimal;
    }
    BigDecimal localBigDecimal = paramBigDecimal.setScale(paramInt, 4);
    if (0 != paramBigDecimal.compareTo(localBigDecimal)) {
      paramIWarningListener.postWarning(new Warning(WarningCode.FRACTIONAL_TRUNCATION, 1, JDBCMessageKey.WARN_FRACTIONAL_TRUNC.name()));
    }
    return localBigDecimal;
  }
  
  private static void addPairsToNormalDoubleOrFloatMap()
  {
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("INF", "Infinity");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("+INF", "+Infinity");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("-INF", "-Infinity");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("INFINITY", "Infinity");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("+INFINITY", "+Infinity");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("-INFINITY", "-Infinity");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("NAN", "NaN");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("+NAN", "NaN");
    NORMALIZE_DOUBLE_OR_FLOAT_MAP.put("-NAN", "NaN");
  }
  
  private static long getZeroTimeAD(Calendar paramCalendar)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(null == paramCalendar ? TimeZone.getDefault() : paramCalendar.getTimeZone());
    localGregorianCalendar.clear();
    localGregorianCalendar.set(1, 0, 1, 0, 0, 0);
    return localGregorianCalendar.getTimeInMillis();
  }
  
  static
  {
    initializeClassToTypeMap();
    TYPE_CONVERSION_MAP = new HashMap();
    NORMALIZE_DOUBLE_OR_FLOAT_MAP = new HashMap();
    addByteConversions();
    addShortConversions();
    addIntegerConversions();
    addLongConversions();
    addFloatConversions();
    addDoubleConversions();
    addBigDecimalConversions();
    addBooleanConversions();
    addStringConversions();
    addByteArrayConversions();
    addDateConversions();
    addTimeConversions();
    addTimestampConversions();
    addGUIDConversions();
    addClobConversions();
    addBlobConversions();
    addRefConversions();
    addDatalinkConversions();
    addArrayConversions();
    addStructConversions();
    addObjectConversions();
    addIntervalDayConversions();
    addIntervalDayToHourConversions();
    addIntervalDayToMinuteConversions();
    addIntervalDayToSecondConversions();
    addIntervalHourConversions();
    addIntervalHourToMinuteConversions();
    addIntervalHourToSecondConversions();
    addIntervalMinuteConversions();
    addIntervalMinuteToSecondConversions();
    addIntervalMonthConversions();
    addIntervalSecondConversions();
    addIntervalYearConversions();
    addIntervalYearToMonthConversions();
    addPairsToNormalDoubleOrFloatMap();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/utilities/conversion/TypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
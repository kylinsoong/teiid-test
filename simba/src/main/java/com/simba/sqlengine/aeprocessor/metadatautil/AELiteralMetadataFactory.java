package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler.LiteralType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.conv.CharConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.conv.ConversionResult.TypeConversionState;
import com.simba.support.exceptions.ErrorException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AELiteralMetadataFactory
{
  private static final Pattern NUM_PATTERN = Pattern.compile("[+-]?0*((?:[1-9]\\d*)?)(?:[.](\\d*?)0*)?(?:[eE]([+-]?\\d+))?$");
  private static final SqlTypes[] INTEGER_TYPE_CAP_ORDER = { SqlTypes.SQL_TINYINT, SqlTypes.SQL_SMALLINT, SqlTypes.SQL_INTEGER, SqlTypes.SQL_BIGINT };
  private AETypeNormalizer m_normalizer;
  private AECoercionProperties m_properties;
  
  public AELiteralMetadataFactory(AETypeNormalizer paramAETypeNormalizer, AECoercionProperties paramAECoercionProperties)
  {
    this.m_normalizer = paramAETypeNormalizer;
    this.m_properties = paramAECoercionProperties;
  }
  
  public ColumnMetadata determineLiteralType(String paramString, ICoercionHandler.LiteralType paramLiteralType)
    throws ErrorException
  {
    switch (paramLiteralType)
    {
    case UNSIGNED_INT: 
      return createIntMetadata(paramString, false);
    case SIGNED_INT: 
      return createIntMetadata(paramString, true);
    case APROX_NUM: 
    case EXACT_NUM: 
      return createNumMetadata(paramString);
    case DATE: 
      return createDateMetadata(paramString);
    case TIME: 
      return createTimeMetaData(paramString);
    case TIMESTAMP: 
      return createTimestampMetadata(paramString);
    case UNKNOWN: 
    case CHAR: 
    case DATATYPE: 
      return createCharMetadata(paramString);
    case BINARY: 
      return createBinaryMetadata(paramString);
    }
    throw new AssertionError("Case not implemented: " + paramString + ", " + paramLiteralType);
  }
  
  private ColumnMetadata createBinaryMetadata(String paramString)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("BINARY Literal is not supported");
  }
  
  private ColumnMetadata createCharMetadata(String paramString)
    throws ErrorException
  {
    long l = paramString.length();
    SqlTypes localSqlTypes = this.m_normalizer.fitBinaryOrCharType(SqlTypes.SQL_CHAR, l);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(localSqlTypes.getSqlType()));
    try
    {
      localColumnMetadata.setColumnLength(l);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw SQLEngineExceptionFactory.numericOverflowException("Column length is too long. ");
    }
    return localColumnMetadata;
  }
  
  private ColumnMetadata createNumMetadata(String paramString)
    throws ErrorException
  {
    PrecisionScale localPrecisionScale = determinePrecisionScale(paramString);
    SqlTypes localSqlTypes1 = null;
    if (localPrecisionScale.precision > this.m_properties.getMaxExactNumPrecision()) {
      localSqlTypes1 = SqlTypes.SQL_DOUBLE;
    } else {
      localSqlTypes1 = SqlTypes.SQL_DECIMAL;
    }
    SqlTypes localSqlTypes2 = this.m_normalizer.normalizeType(localSqlTypes1);
    if (null == localSqlTypes2) {
      if (localSqlTypes1 == SqlTypes.SQL_DECIMAL) {
        localSqlTypes2 = this.m_normalizer.normalizeType(SqlTypes.SQL_DOUBLE);
      } else {
        localSqlTypes2 = this.m_normalizer.normalizeType(SqlTypes.SQL_DECIMAL);
      }
    }
    if (null == localSqlTypes2) {
      throw new IllegalStateException("Lack of fractional number support.");
    }
    if (localSqlTypes2.isExactNum())
    {
      localTypeMetadata = TypeMetadata.createTypeMetadata(localSqlTypes2.getSqlType());
      localTypeMetadata.setPrecision((short)localPrecisionScale.precision);
      localTypeMetadata.setScale((short)localPrecisionScale.scale);
      return new ColumnMetadata(localTypeMetadata);
    }
    if (!localSqlTypes2.isNumber()) {
      throw new IllegalStateException("Number type is normalized to none number type. " + localSqlTypes2.name());
    }
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(localSqlTypes2.getSqlType());
    return new ColumnMetadata(localTypeMetadata);
  }
  
  private ColumnMetadata createTimestampMetadata(String paramString)
    throws ErrorException
  {
    if (!isLegalTimestamp(paramString)) {
      throw SQLEngineExceptionFactory.invalidFormatException("TIMESTAMP", paramString);
    }
    SqlTypes localSqlTypes = this.m_normalizer.normalizeType(SqlTypes.SQL_TIMESTAMP);
    if (localSqlTypes != SqlTypes.SQL_TIMESTAMP) {
      throw new IllegalStateException("Normalized to different type for TIMESTAMP type: result type: " + localSqlTypes);
    }
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(93);
    int i = paramString.indexOf('.');
    if (i != -1)
    {
      short s = (short)(paramString.length() - i - 1);
      localTypeMetadata.setPrecision(s);
      localTypeMetadata.setScale(s);
    }
    return new ColumnMetadata(localTypeMetadata);
  }
  
  private ColumnMetadata createTimeMetaData(String paramString)
    throws ErrorException
  {
    if (!isLegalTime(paramString)) {
      throw SQLEngineExceptionFactory.invalidFormatException("TIME", paramString);
    }
    SqlTypes localSqlTypes = this.m_normalizer.normalizeType(SqlTypes.SQL_TIME);
    if (localSqlTypes != SqlTypes.SQL_TIME) {
      throw new IllegalStateException("Normalized to different type for TIME type: result type: " + localSqlTypes);
    }
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(92);
    int i = paramString.indexOf('.');
    if (i != -1)
    {
      short s = (short)(paramString.length() - i - 1);
      localTypeMetadata.setPrecision(s);
      localTypeMetadata.setScale(s);
    }
    return new ColumnMetadata(localTypeMetadata);
  }
  
  private ColumnMetadata createDateMetadata(String paramString)
    throws ErrorException
  {
    if (!isLegalDate(paramString)) {
      throw SQLEngineExceptionFactory.invalidFormatException("DATE", paramString);
    }
    SqlTypes localSqlTypes = this.m_normalizer.normalizeType(SqlTypes.SQL_DATE);
    if (localSqlTypes != SqlTypes.SQL_DATE) {
      throw new IllegalStateException("Normalized to different type for DATE type: result type: " + localSqlTypes);
    }
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(91);
    return new ColumnMetadata(localTypeMetadata);
  }
  
  private ColumnMetadata createIntMetadata(String paramString, boolean paramBoolean)
    throws ErrorException
  {
    paramString = paramString.trim();
    if (paramString.charAt(0) == '+') {
      paramString = paramString.substring(1).trim();
    } else if ((paramString.charAt(0) == '-') && (!paramBoolean)) {
      throw SQLEngineExceptionFactory.invalidFormatException("Unsigned integer", paramString);
    }
    if (!isLegalInteger(paramString)) {
      throw SQLEngineExceptionFactory.invalidFormatException("Integer", paramString);
    }
    long l = 0L;
    try
    {
      l = Long.parseLong(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      return createColumnForHugeInt(paramString, paramBoolean);
    }
    SqlTypes localSqlTypes1 = null;
    if ((l >= -128L) && (l <= 127L)) {
      localSqlTypes1 = SqlTypes.SQL_TINYINT;
    } else if ((l >= -32768L) && (l <= 32767L)) {
      localSqlTypes1 = SqlTypes.SQL_SMALLINT;
    } else if ((l >= -2147483648L) && (l <= 2147483647L)) {
      localSqlTypes1 = SqlTypes.SQL_INTEGER;
    } else {
      localSqlTypes1 = SqlTypes.SQL_BIGINT;
    }
    SqlTypes localSqlTypes2 = this.m_normalizer.normalizeType(localSqlTypes1);
    if ((localSqlTypes2 != null) && (!localSqlTypes2.isInteger())) {
      throw SQLEngineExceptionFactory.invalidOperationException("Normalized to none integer type: " + localSqlTypes2);
    }
    if ((localSqlTypes2 == null) || (compareIntTypeCapacity(localSqlTypes2, localSqlTypes1) < 0)) {
      return createColumnForHugeInt(paramString, paramBoolean);
    }
    return new ColumnMetadata(TypeMetadata.createTypeMetadata(localSqlTypes2.getSqlType(), true));
  }
  
  private ColumnMetadata createColumnForHugeInt(String paramString, boolean paramBoolean)
    throws ErrorException
  {
    SqlTypes localSqlTypes = null;
    if (paramString.length() <= this.m_properties.getMaxExactNumPrecision()) {
      localSqlTypes = this.m_normalizer.normalizeType(SqlTypes.SQL_DECIMAL);
    }
    if (localSqlTypes == null) {
      localSqlTypes = this.m_normalizer.normalizeType(SqlTypes.SQL_DOUBLE);
    }
    if (localSqlTypes == null) {
      throw SQLEngineExceptionFactory.invalidFormatException("Integer", paramString);
    }
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(localSqlTypes.getSqlType(), paramBoolean);
    if (localSqlTypes.isExactNum())
    {
      localTypeMetadata.setPrecision((short)paramString.length());
      localTypeMetadata.setScale((short)0);
    }
    return new ColumnMetadata(localTypeMetadata);
  }
  
  private static PrecisionScale determinePrecisionScale(String paramString)
    throws ErrorException
  {
    Matcher localMatcher = NUM_PATTERN.matcher(paramString.trim());
    if (!localMatcher.matches()) {
      throw SQLEngineExceptionFactory.invalidFormatException("NUMBER", paramString);
    }
    String str1 = localMatcher.group(1);
    String str2 = localMatcher.group(2);
    String str3 = localMatcher.group(3);
    int i = 0;
    if (str2 == null) {
      str2 = "";
    }
    if (str3 != null) {
      i = str3.charAt(0) == '+' ? Integer.parseInt(str3.substring(1)) : Integer.parseInt(str3);
    }
    if ((str1.length() == 0) && (str2.length() == 0)) {
      return new PrecisionScale(1, 0);
    }
    int j = str1.length() + i;
    int k = str1.length() + str2.length();
    if ((str1.length() == 0) && (j > 0))
    {
      m = numOfLeadingZeroes(str2, j);
      j -= m;
      k -= m;
    }
    if ((str2.length() == 0) && (j < str1.length()))
    {
      m = numOfTrailing0(str1, j - 1);
      k -= m;
    }
    int m = 0;
    int n = 0;
    if (j < 0)
    {
      m = -1 * j + k;
      n = m;
    }
    else if (j > k)
    {
      m = j;
      n = 0;
    }
    else
    {
      m = k;
      n = k - j;
    }
    return new PrecisionScale(m, n);
  }
  
  private static int numOfLeadingZeroes(String paramString, int paramInt)
  {
    assert (paramInt >= 0);
    assert (paramString != null);
    assert (!paramString.equals(""));
    for (int i = 0; (i < paramInt) && (i < paramString.length()); i++) {
      if (paramString.charAt(i) != '0') {
        return i;
      }
    }
    return Math.min(paramString.length(), paramInt);
  }
  
  private static int numOfTrailing0(String paramString, int paramInt)
  {
    assert (paramString != null);
    assert (!paramString.equals(""));
    for (int i = paramString.length() - 1; (i > paramInt) && (i >= 0); i--) {
      if (paramString.charAt(i) != '0') {
        return paramString.length() - i - 1;
      }
    }
    return paramString.length() - Math.max(0, paramInt + 1);
  }
  
  private static boolean isLegalTime(String paramString)
  {
    return isLegalTimestamp("1997-05-06 " + paramString);
  }
  
  private static boolean isLegalDate(String paramString)
  {
    ConversionResult localConversionResult = new ConversionResult();
    CharConverter.toDate(paramString, localConversionResult, null);
    return (ConversionResult.TypeConversionState.SUCCESS == localConversionResult.getState()) || (ConversionResult.TypeConversionState.DATETIME_OVERFLOW == localConversionResult.getState());
  }
  
  private static boolean isLegalTimestamp(String paramString)
  {
    ConversionResult localConversionResult = new ConversionResult();
    CharConverter.tsStrToTimestamp(paramString, localConversionResult, (short)9, null);
    return (ConversionResult.TypeConversionState.SUCCESS == localConversionResult.getState()) || (ConversionResult.TypeConversionState.FRAC_TRUNCATION_ROUNDED_DOWN == localConversionResult.getState());
  }
  
  private static boolean isLegalInteger(String paramString)
  {
    if (paramString.charAt(0) == '-') {
      paramString = paramString.substring(1);
    }
    if (paramString.length() < 1) {
      return false;
    }
    for (int i = 0; i < paramString.length(); i++) {
      if ((paramString.charAt(i) > '9') || (paramString.charAt(i) < '0')) {
        return false;
      }
    }
    return true;
  }
  
  private static int compareIntTypeCapacity(SqlTypes paramSqlTypes1, SqlTypes paramSqlTypes2)
  {
    assert ((paramSqlTypes1.isInteger()) && (paramSqlTypes2.isInteger()));
    if (!$assertionsDisabled) {
      if (!Arrays.asList(INTEGER_TYPE_CAP_ORDER).containsAll(Arrays.asList(new SqlTypes[] { paramSqlTypes1, paramSqlTypes2 }))) {
        throw new AssertionError();
      }
    }
    if (paramSqlTypes1 == paramSqlTypes2) {
      return 0;
    }
    for (SqlTypes localSqlTypes : INTEGER_TYPE_CAP_ORDER)
    {
      if (localSqlTypes == paramSqlTypes1) {
        return -1;
      }
      if (localSqlTypes == paramSqlTypes2) {
        return 1;
      }
    }
    throw new IllegalStateException("Logic error.");
  }
  
  private static final class PrecisionScale
  {
    public final int precision;
    public final int scale;
    
    public PrecisionScale(int paramInt1, int paramInt2)
    {
      this.precision = paramInt1;
      this.scale = paramInt2;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AELiteralMetadataFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
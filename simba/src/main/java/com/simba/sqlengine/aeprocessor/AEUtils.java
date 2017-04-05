package com.simba.sqlengine.aeprocessor;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.dsiext.dataengine.utils.DSISqlConversionType;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.InvalidOperationException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class AEUtils
{
  private static final Map<DSISqlConversionType, EnumSet<DSISqlConversionType>> CONVERSIONS;
  
  private AEUtils()
  {
    throw new UnsupportedOperationException("Cannot instantiate AEUtils class.");
  }
  
  public static AEQTableName adjustCatalogAndSchema(AEQTableName paramAEQTableName, SqlDataEngineContext paramSqlDataEngineContext, boolean paramBoolean)
    throws ErrorException
  {
    AEQTableName.AEQTableNameBuilder localAEQTableNameBuilder = new AEQTableName.AEQTableNameBuilder(paramAEQTableName);
    boolean bool1 = hasCatalogSupport(paramSqlDataEngineContext);
    boolean bool2 = paramAEQTableName.hasCatalogName();
    String str;
    if ((paramAEQTableName.hasCatalogName()) && (!bool1))
    {
      str = SQLEngineMessageKey.CATALOG_NOT_SUPPORTED.name();
      throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(str);
    }
    if ((paramAEQTableName.hasSchemaName()) && (!hasSchemaSupport(paramSqlDataEngineContext))) {
      if ((bool1) && (!bool2))
      {
        localAEQTableNameBuilder.setCatalogName(paramAEQTableName.getSchemaName());
        localAEQTableNameBuilder.setSchemaName("");
        bool2 = true;
      }
      else
      {
        str = SQLEngineMessageKey.SCHEMA_NOT_SUPPORTED.name();
        throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(str);
      }
    }
    if ((paramBoolean) && (!bool2) && (bool1))
    {
      str = currentCatalogName(paramSqlDataEngineContext);
      if (str.length() > 0) {
        localAEQTableNameBuilder.setCatalogName(str);
      }
    }
    return localAEQTableNameBuilder.build();
  }
  
  public static void checkReadOnly(SqlDataEngineContext paramSqlDataEngineContext, String paramString)
    throws ErrorException
  {
    Variant localVariant = paramSqlDataEngineContext.getConnProperty(16);
    assert ((null != localVariant) && (localVariant.getType() == 3));
    try
    {
      if (localVariant.getLong() == 1L) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_OP_DRIVER_READ_ONLY.name(), new String[] { paramString });
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new AssertionError(localIncorrectTypeException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
  }
  
  public static String getIdentifierString(IPTNode paramIPTNode)
    throws ErrorException
  {
    if (paramIPTNode.isEmptyNode()) {
      return "";
    }
    if ((paramIPTNode instanceof PTIdentifierNode)) {
      return ((PTIdentifierNode)paramIPTNode).getIdentifier();
    }
    throw new InvalidOperationException(7, SQLEngineMessageKey.INVALID_OPERATION.name(), new String[] { "Wrong parse node type used to get identifier." });
  }
  
  public static boolean isTypeNumeric(short paramShort)
  {
    return (TypeUtilities.isIntegerType(paramShort)) || (TypeUtilities.isApproximateNumericType(paramShort)) || (TypeUtilities.isExactNumericType(paramShort)) || (-7 == paramShort);
  }
  
  public static boolean isConversionLegal(int paramInt1, int paramInt2)
    throws ErrorException
  {
    DSISqlConversionType localDSISqlConversionType1 = DSISqlConversionType.fromSqlType(paramInt1);
    DSISqlConversionType localDSISqlConversionType2 = DSISqlConversionType.fromSqlType(paramInt2);
    return ((EnumSet)CONVERSIONS.get(localDSISqlConversionType1)).contains(localDSISqlConversionType2);
  }
  
  public static boolean isAnyConversionLegal(int paramInt1, int paramInt2, int... paramVarArgs)
    throws ErrorException
  {
    DSISqlConversionType localDSISqlConversionType1 = DSISqlConversionType.fromSqlType(paramInt1);
    DSISqlConversionType localDSISqlConversionType2 = DSISqlConversionType.fromSqlType(paramInt2);
    EnumSet localEnumSet = (EnumSet)CONVERSIONS.get(localDSISqlConversionType1);
    if (localEnumSet.contains(localDSISqlConversionType2)) {
      return true;
    }
    for (int k : paramVarArgs) {
      if (localEnumSet.contains(DSISqlConversionType.fromSqlType(k))) {
        return true;
      }
    }
    return false;
  }
  
  public static String sqlQuoted(String paramString)
  {
    return "\"" + paramString.replace("\"", "\"\"") + "\"";
  }
  
  private static String currentCatalogName(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return paramSqlDataEngineContext.getConnProperty(22).getString();
  }
  
  private static boolean hasCatalogSupport(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return "Y".equals(paramSqlDataEngineContext.getConnProperty(9).getString());
  }
  
  private static boolean hasSchemaSupport(SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    return paramSqlDataEngineContext.getConnProperty(99).getString().length() > 0;
  }
  
  private static <E extends Enum<E>> EnumSet<E> unionOf(EnumSet<E> paramEnumSet1, EnumSet<E> paramEnumSet2)
  {
    EnumSet localEnumSet = EnumSet.copyOf(paramEnumSet1);
    localEnumSet.addAll(paramEnumSet2);
    return localEnumSet;
  }
  
  private static <E extends Enum<E>> EnumSet<E> unionOf(EnumSet<E> paramEnumSet1, EnumSet<E> paramEnumSet2, EnumSet<E> paramEnumSet3)
  {
    EnumSet localEnumSet = EnumSet.copyOf(paramEnumSet1);
    localEnumSet.addAll(paramEnumSet2);
    localEnumSet.addAll(paramEnumSet3);
    return localEnumSet;
  }
  
  private static <E extends Enum<E>> EnumSet<E> unionOf(EnumSet<E> paramEnumSet1, EnumSet<E> paramEnumSet2, EnumSet<E> paramEnumSet3, EnumSet<E> paramEnumSet4)
  {
    EnumSet localEnumSet = EnumSet.copyOf(paramEnumSet1);
    localEnumSet.addAll(paramEnumSet2);
    localEnumSet.addAll(paramEnumSet3);
    localEnumSet.addAll(paramEnumSet4);
    return localEnumSet;
  }
  
  static
  {
    EnumMap localEnumMap = new EnumMap(DSISqlConversionType.class);
    EnumSet localEnumSet1 = EnumSet.of(DSISqlConversionType.DSI_CVT_CHAR, new DSISqlConversionType[] { DSISqlConversionType.DSI_CVT_VARCHAR, DSISqlConversionType.DSI_CVT_LONGVARCHAR, DSISqlConversionType.DSI_CVT_WCHAR, DSISqlConversionType.DSI_CVT_WVARCHAR, DSISqlConversionType.DSI_CVT_WLONGVARCHAR });
    EnumSet localEnumSet2 = EnumSet.of(DSISqlConversionType.DSI_CVT_BINARY, DSISqlConversionType.DSI_CVT_VARBINARY);
    EnumSet localEnumSet3 = EnumSet.of(DSISqlConversionType.DSI_CVT_BINARY, DSISqlConversionType.DSI_CVT_VARBINARY, DSISqlConversionType.DSI_CVT_LONGVARBINARY);
    EnumSet localEnumSet4 = EnumSet.of(DSISqlConversionType.DSI_CVT_INTERVAL_DAY_SECOND, DSISqlConversionType.DSI_CVT_INTERVAL_YEAR_MONTH);
    EnumSet localEnumSet5 = EnumSet.of(DSISqlConversionType.DSI_CVT_DATE, DSISqlConversionType.DSI_CVT_TIME, DSISqlConversionType.DSI_CVT_TIMESTAMP);
    EnumSet localEnumSet6 = EnumSet.of(DSISqlConversionType.DSI_CVT_TINYINT, DSISqlConversionType.DSI_CVT_SMALLINT, DSISqlConversionType.DSI_CVT_INTEGER, DSISqlConversionType.DSI_CVT_BIGINT);
    EnumSet localEnumSet7 = EnumSet.of(DSISqlConversionType.DSI_CVT_NUMERIC, new DSISqlConversionType[] { DSISqlConversionType.DSI_CVT_DECIMAL, DSISqlConversionType.DSI_CVT_TINYINT, DSISqlConversionType.DSI_CVT_SMALLINT, DSISqlConversionType.DSI_CVT_INTEGER, DSISqlConversionType.DSI_CVT_BIGINT, DSISqlConversionType.DSI_CVT_REAL, DSISqlConversionType.DSI_CVT_FLOAT, DSISqlConversionType.DSI_CVT_DOUBLE });
    localEnumMap.put(DSISqlConversionType.DSI_CVT_CHAR, EnumSet.allOf(DSISqlConversionType.class));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_VARCHAR, EnumSet.allOf(DSISqlConversionType.class));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_LONGVARCHAR, EnumSet.complementOf(localEnumSet3));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_WCHAR, EnumSet.allOf(DSISqlConversionType.class));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_WVARCHAR, EnumSet.allOf(DSISqlConversionType.class));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_WLONGVARCHAR, EnumSet.complementOf(localEnumSet3));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_BIT, EnumSet.complementOf(unionOf(localEnumSet4, localEnumSet5, EnumSet.of(DSISqlConversionType.DSI_CVT_GUID))));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_BOOLEAN, EnumSet.complementOf(unionOf(localEnumSet4, localEnumSet5, EnumSet.of(DSISqlConversionType.DSI_CVT_GUID))));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_BINARY, unionOf(localEnumSet1, localEnumSet3));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_VARBINARY, unionOf(localEnumSet1, localEnumSet3));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_LONGVARBINARY, unionOf(localEnumSet1, localEnumSet3));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_NUMERIC, unionOf(localEnumSet1, localEnumSet7, localEnumSet4));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_DECIMAL, unionOf(localEnumSet1, localEnumSet7, localEnumSet4));
    EnumSet localEnumSet8 = unionOf(localEnumSet1, localEnumSet7, localEnumSet4, EnumSet.of(DSISqlConversionType.DSI_CVT_BIT, DSISqlConversionType.DSI_CVT_BOOLEAN, DSISqlConversionType.DSI_CVT_BINARY, DSISqlConversionType.DSI_CVT_VARBINARY));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_TINYINT, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_SMALLINT, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_INTEGER, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_BIGINT, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_REAL, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_FLOAT, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_DOUBLE, EnumSet.copyOf(localEnumSet8));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_DATE, unionOf(localEnumSet1, localEnumSet2, EnumSet.of(DSISqlConversionType.DSI_CVT_DATE, DSISqlConversionType.DSI_CVT_TIMESTAMP)));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_TIME, unionOf(localEnumSet1, localEnumSet2, EnumSet.of(DSISqlConversionType.DSI_CVT_TIME, DSISqlConversionType.DSI_CVT_TIMESTAMP)));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_TIMESTAMP, unionOf(localEnumSet1, localEnumSet2, localEnumSet5));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_INTERVAL_YEAR_MONTH, unionOf(localEnumSet1, localEnumSet2, localEnumSet6, EnumSet.of(DSISqlConversionType.DSI_CVT_NUMERIC, DSISqlConversionType.DSI_CVT_DECIMAL, DSISqlConversionType.DSI_CVT_INTERVAL_YEAR_MONTH)));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_INTERVAL_DAY_SECOND, unionOf(localEnumSet1, localEnumSet2, localEnumSet6, EnumSet.of(DSISqlConversionType.DSI_CVT_NUMERIC, DSISqlConversionType.DSI_CVT_DECIMAL, DSISqlConversionType.DSI_CVT_INTERVAL_DAY_SECOND)));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_GUID, unionOf(localEnumSet1, localEnumSet2, EnumSet.of(DSISqlConversionType.DSI_CVT_GUID)));
    localEnumMap.put(DSISqlConversionType.DSI_CVT_NULL, EnumSet.allOf(DSISqlConversionType.class));
    CONVERSIONS = Collections.unmodifiableMap(localEnumMap);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/AEUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
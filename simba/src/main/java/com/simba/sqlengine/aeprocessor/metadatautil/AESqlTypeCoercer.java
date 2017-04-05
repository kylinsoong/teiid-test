package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.sqlengine.aeprocessor.metadatautil.generated.CoercionTable;
import com.simba.sqlengine.aeprocessor.metadatautil.generated.CoercionTable.Operation;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

class AESqlTypeCoercer
{
  private CoercionTable m_coercionTable = new CoercionTable();
  
  public int coerceType(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramIColumnInfo1.getType());
    SqlTypes localSqlTypes2 = SqlTypes.getValueOf(paramIColumnInfo2.getType());
    if (((paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.CONCAT) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.LIKE)) && ((!isAnyCharType(localSqlTypes1)) || (!isAnyCharType(localSqlTypes2))))
    {
      if (isAnyCharType(localSqlTypes1)) {
        return localSqlTypes1.getSqlType();
      }
      if (isAnyCharType(localSqlTypes2)) {
        return localSqlTypes2.getSqlType();
      }
      return SqlTypes.SQL_CHAR.getSqlType();
    }
    CoercionTable.Operation localOperation = getCoercionTableOpertion(paramCoercionOperation);
    if ((localSqlTypes1.isInteger()) && (!paramIColumnInfo1.isSigned()) && ((!localSqlTypes2.isNumber()) || (paramIColumnInfo2.isSigned()))) {
      localSqlTypes1 = upIntegerType(localSqlTypes1);
    } else if ((localSqlTypes2.isInteger()) && (!paramIColumnInfo2.isSigned()) && ((!localSqlTypes1.isNumber()) || (paramIColumnInfo1.isSigned()))) {
      localSqlTypes2 = upIntegerType(localSqlTypes2);
    }
    int i = localSqlTypes1.getSqlType();
    int j = localSqlTypes2.getSqlType();
    if (!this.m_coercionTable.isCoercionSupported(localOperation, i, j)) {
      throw SQLEngineExceptionFactory.unsupportedTypesException(i + " or " + j);
    }
    int k = this.m_coercionTable.getEntry(localOperation, i, j);
    if (k == Integer.MIN_VALUE) {
      throw SQLEngineExceptionFactory.incompatibleTypesException(localOperation.name(), localSqlTypes1.name() + " and " + localSqlTypes2.name());
    }
    return k;
  }
  
  public void overrideCoercionType(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, int paramInt1, int paramInt2, int paramInt3)
    throws ErrorException
  {
    if ((paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.LIKE) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.CONCAT)) {
      throw new IllegalArgumentException("can not override coercion type for LIKE or CONCAT operation.");
    }
    CoercionTable.Operation localOperation = getCoercionTableOpertion(paramCoercionOperation);
    if (!this.m_coercionTable.isCoercionSupported(localOperation, paramInt1, paramInt2)) {
      throw SQLEngineExceptionFactory.unsupportedTypesException(paramInt1 + " or " + paramInt2);
    }
    this.m_coercionTable.overrideEntry(localOperation, paramInt1, paramInt2, paramInt3);
  }
  
  private CoercionTable.Operation getCoercionTableOpertion(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation)
  {
    switch (paramCoercionOperation)
    {
    case COMPARISON: 
    case LIKE: 
    case IN: 
      return CoercionTable.Operation.BOOLEAN;
    case SET_OPERATION: 
      return CoercionTable.Operation.SET_OPERATION;
    case DIVISION: 
      return CoercionTable.Operation.DIVISION;
    case MINUS: 
      return CoercionTable.Operation.BINARYMINUS;
    case CONCAT: 
    case PLUS: 
      return CoercionTable.Operation.BINARYPLUS;
    case MULTIPLICATION: 
      return CoercionTable.Operation.MULTIPLICATION;
    }
    throw new IllegalArgumentException("Unsupported operation: " + paramCoercionOperation.name());
  }
  
  public static SqlTypes upIntegerType(SqlTypes paramSqlTypes)
  {
    assert (paramSqlTypes.isInteger());
    switch (paramSqlTypes)
    {
    case SQL_TINYINT: 
      return SqlTypes.SQL_SMALLINT;
    case SQL_SMALLINT: 
      return SqlTypes.SQL_INTEGER;
    case SQL_INTEGER: 
    case SQL_BIGINT: 
      return SqlTypes.SQL_BIGINT;
    }
    throw new IllegalArgumentException("type must be an integer type: " + paramSqlTypes);
  }
  
  private static boolean isAnyCharType(SqlTypes paramSqlTypes)
  {
    return (paramSqlTypes.isWChar()) || (paramSqlTypes.isChar());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AESqlTypeCoercer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
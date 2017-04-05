package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;

class AEMetadataCoercer
{
  private AECoercionProperties m_properties;
  
  public AEMetadataCoercer(AECoercionProperties paramAECoercionProperties)
  {
    this.m_properties = paramAECoercionProperties;
  }
  
  public long calcColumnLength(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramIColumn.getTypeMetadata().getType());
    if (localSqlTypes1.isBinary())
    {
      SqlTypes localSqlTypes2 = SqlTypes.getValueOf(paramIColumnInfo1.getType());
      SqlTypes localSqlTypes3 = SqlTypes.getValueOf(paramIColumnInfo2.getType());
      if ((paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.SET_OPERATION) && (localSqlTypes2.isBinary()) && (localSqlTypes3.isBinary())) {
        return Math.max(paramIColumnInfo1.getColumnLength(), paramIColumnInfo2.getColumnLength());
      }
      throw SQLEngineExceptionFactory.invalidOperationException("Operation for coercion types is not compatible: Operation: " + paramCoercionOperation.name() + "type1: " + paramIColumnInfo1.getType() + "type2: " + paramIColumnInfo2.getType());
    }
    if ((localSqlTypes1.isChar()) || (localSqlTypes1.isWChar()))
    {
      if ((paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.COMPARISON) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.LIKE) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.SET_OPERATION) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.IN)) {
        return Math.max(paramIColumnInfo1.getDisplaySize(), paramIColumnInfo2.getDisplaySize());
      }
      if ((paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.PLUS) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.CONCAT))
      {
        long l = paramIColumnInfo1.getDisplaySize() + paramIColumnInfo2.getDisplaySize();
        return l > 4294967295L ? 4294967295L : l;
      }
      throw SQLEngineExceptionFactory.invalidOperationException("Operation for coercion types is not compatiable: Operation: " + paramCoercionOperation.name() + "type1: " + paramIColumnInfo1.getType() + "type2: " + paramIColumnInfo2.getType());
    }
    return paramIColumn.getColumnLength();
  }
  
  public AEMetadataCoercionHandler.PrecisionScale calcPrecisionScale(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    SqlTypes localSqlTypes = SqlTypes.getValueOf(paramIColumn.getTypeMetadata().getType());
    if (isTimeOrTimestamp(localSqlTypes)) {
      return coerceDatetimePrecisionScale(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
    }
    if (localSqlTypes.isExactNum()) {
      return coerceExactNumPrecisionScale(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
    }
    return new AEMetadataCoercionHandler.PrecisionScale(paramIColumn.getTypeMetadata().getPrecision(), paramIColumn.getTypeMetadata().getScale());
  }
  
  public boolean coerceIsCurrency(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
  {
    return ((paramIColumnInfo1.isCurrency()) || (paramIColumnInfo2.isCurrency())) && ((paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.DIVISION) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.PLUS) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.MINUS) || (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.MULTIPLICATION));
  }
  
  public boolean coerceIsSigned(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramIColumn.getTypeMetadata().getType());
    SqlTypes localSqlTypes2 = SqlTypes.getValueOf(paramIColumnInfo1.getType());
    SqlTypes localSqlTypes3 = SqlTypes.getValueOf(paramIColumnInfo2.getType());
    if ((!localSqlTypes1.isNumber()) || (localSqlTypes1 == SqlTypes.SQL_BIT)) {
      return false;
    }
    if (paramCoercionOperation == AEMetadataCoercionHandler.CoercionOperation.MINUS) {
      return true;
    }
    if ((!localSqlTypes2.isNumber()) || (!localSqlTypes3.isNumber())) {
      return true;
    }
    return (paramIColumnInfo1.isSigned()) || (paramIColumnInfo2.isSigned());
  }
  
  public Nullable coerceNullable(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
  {
    Nullable localNullable1 = paramIColumnInfo1.getNullable();
    Nullable localNullable2 = paramIColumnInfo2.getNullable();
    if ((localNullable1 == Nullable.NULLABLE) || (localNullable2 == Nullable.NULLABLE)) {
      return Nullable.NULLABLE;
    }
    if ((localNullable1 == Nullable.UNKNOWN) || (localNullable2 == Nullable.UNKNOWN)) {
      return Nullable.UNKNOWN;
    }
    return Nullable.NO_NULLS;
  }
  
  public boolean coerceIsCaseSensitive(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
  {
    return (paramIColumnInfo1.isCaseSensitive()) || (paramIColumnInfo2.isCaseSensitive());
  }
  
  public Searchable coerceSearchability(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
  {
    Searchable localSearchable1 = paramIColumnInfo1.getSearchable();
    Searchable localSearchable2 = paramIColumnInfo2.getSearchable();
    if ((localSearchable1 == Searchable.PREDICATE_NONE) || (localSearchable2 == Searchable.PREDICATE_NONE)) {
      return Searchable.PREDICATE_NONE;
    }
    if (((localSearchable1 == Searchable.PREDICATE_CHAR ? 1 : 0) | (localSearchable2 == Searchable.PREDICATE_CHAR ? 1 : 0)) != 0) {
      return Searchable.PREDICATE_CHAR;
    }
    if ((localSearchable1 == Searchable.PREDICATE_BASIC) || (localSearchable2 == Searchable.PREDICATE_BASIC)) {
      return Searchable.PREDICATE_BASIC;
    }
    return Searchable.SEARCHABLE;
  }
  
  public Updatable coerceUpdatability(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
  {
    return Updatable.READ_ONLY;
  }
  
  public boolean coerceIsAutoUnique(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
  {
    return false;
  }
  
  private AEMetadataCoercionHandler.PrecisionScale coerceExactNumPrecisionScale(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramIColumnInfo1.getType());
    SqlTypes localSqlTypes2 = SqlTypes.getValueOf(paramIColumnInfo2.getType());
    short s = -1;
    int i = -1;
    if ((localSqlTypes1.isNumber()) && (localSqlTypes2.isNumber()))
    {
      int j = paramIColumnInfo1.getScale();
      int k = paramIColumnInfo2.getScale();
      int m = paramIColumnInfo1.getPrecision();
      int n = paramIColumnInfo2.getPrecision();
      int i1 = (short)Math.max(j, k);
      int i2 = (short)Math.max(m - j, n - k);
      switch (paramCoercionOperation)
      {
      case COMPARISON: 
      case IN: 
      case SET_OPERATION: 
        s = (short)(i2 + i1);
        i = i1;
        break;
      case DIVISION: 
        s = (short)(m - j + k + Math.max(6, j + n + 1));
        i = (short)Math.max(6, j + n + 1);
        break;
      case MULTIPLICATION: 
        s = (short)(m + n + 1);
        i = (short)(j + k);
        break;
      case PLUS: 
      case MINUS: 
        s = (short)(i1 + i2 + 1);
        i = i1;
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidOperationException("Coercion result does not match coercion operation: Operator: " + paramCoercionOperation.name() + " , type1: " + localSqlTypes1.name() + ", type2: " + localSqlTypes2.name());
      }
      if (s > this.m_properties.getMaxExactNumPrecision())
      {
        i = (short)Math.max(Math.min(k, j), i - (s - this.m_properties.getMaxExactNumPrecision()));
        s = this.m_properties.getMaxExactNumPrecision();
      }
    }
    else if (localSqlTypes1.isExactNum())
    {
      s = paramIColumnInfo1.getPrecision();
      i = paramIColumnInfo1.getScale();
    }
    else if (localSqlTypes2.isExactNum())
    {
      s = paramIColumnInfo2.getPrecision();
      i = paramIColumnInfo2.getScale();
    }
    else
    {
      s = this.m_properties.getMaxExactNumPrecision();
      i = this.m_properties.getDefaultExactNumScale();
    }
    return new AEMetadataCoercionHandler.PrecisionScale(s, i);
  }
  
  private AEMetadataCoercionHandler.PrecisionScale coerceDatetimePrecisionScale(AEMetadataCoercionHandler.CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramIColumnInfo1.getType());
    SqlTypes localSqlTypes2 = SqlTypes.getValueOf(paramIColumnInfo2.getType());
    SqlTypes localSqlTypes3 = SqlTypes.getValueOf(paramIColumn.getTypeMetadata().getType());
    assert ((localSqlTypes3 == SqlTypes.SQL_TIME) || (localSqlTypes3 == SqlTypes.SQL_TIMESTAMP));
    short s = -1;
    if ((isTimeOrTimestamp(localSqlTypes1)) && (isTimeOrTimestamp(localSqlTypes2))) {
      s = (short)Math.max(paramIColumnInfo1.getPrecision(), paramIColumnInfo2.getPrecision());
    } else if (isTimeOrTimestamp(localSqlTypes1)) {
      s = paramIColumnInfo1.getPrecision();
    } else if (isTimeOrTimestamp(localSqlTypes2)) {
      s = paramIColumnInfo2.getPrecision();
    } else if (localSqlTypes3 == SqlTypes.SQL_TIME) {
      s = this.m_properties.getMaxTimePrecision();
    } else {
      s = this.m_properties.getMaxTimestampPrecision();
    }
    return new AEMetadataCoercionHandler.PrecisionScale(s, s);
  }
  
  private boolean isTimeOrTimestamp(SqlTypes paramSqlTypes)
  {
    return (paramSqlTypes == SqlTypes.SQL_TIME) || (paramSqlTypes == SqlTypes.SQL_TIMESTAMP);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AEMetadataCoercer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
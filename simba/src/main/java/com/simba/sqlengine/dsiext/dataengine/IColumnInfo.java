package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.support.exceptions.ErrorException;

public abstract interface IColumnInfo
{
  public abstract int getIntervalPrecision();
  
  public abstract short getPrecision();
  
  public abstract short getScale();
  
  public abstract short getType();
  
  public abstract boolean isCurrency();
  
  public abstract boolean isSigned();
  
  public abstract boolean isSortable();
  
  public abstract long getColumnLength();
  
  public abstract long getDisplaySize()
    throws ErrorException;
  
  public abstract Nullable getNullable();
  
  public abstract Searchable getSearchable();
  
  public abstract Updatable getUpdatable();
  
  public abstract boolean isAutoUnique();
  
  public abstract boolean isCaseSensitive();
  
  public abstract boolean isDefinitelyWritable();
  
  public abstract ColumnType getColumnType();
  
  public abstract String getLiteralString();
  
  public static enum ColumnType
  {
    LITERAL,  COLUMN,  NULL,  PARAMETER_SET,  PARAMETER_UNSET,  SCALAR_FUNCTION,  NEGATE,  PLUS,  MINUS,  DIVISION,  MULIPLICATION,  CONCATENATION,  SET_FUNCTION,  CASE_EXPRESSION,  SCALAR_SUBQUERY;
    
    private ColumnType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/IColumnInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
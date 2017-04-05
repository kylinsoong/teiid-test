package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public abstract interface ICoercionHandler
{
  public abstract IColumn coerceComparisonColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coerceConcatColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coerceInColumns(IColumnInfo paramIColumnInfo, List<? extends IColumnInfo> paramList)
    throws ErrorException;
  
  public abstract IColumn coerceLikeColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coerceUnionColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coerceDivisionColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coerceMinusColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coercePlusColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn coerceMultiplicationColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException;
  
  public abstract IColumn determineLiteralColumn(String paramString, LiteralType paramLiteralType)
    throws ErrorException;
  
  public abstract IColumn coerceUnaryMinusColumn(IColumnInfo paramIColumnInfo)
    throws ErrorException;
  
  public static enum LiteralType
  {
    UNSIGNED_INT,  SIGNED_INT,  APROX_NUM,  DATATYPE,  EXACT_NUM,  DATE,  TIME,  TIMESTAMP,  CHAR,  BINARY,  UNKNOWN;
    
    private LiteralType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ICoercionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
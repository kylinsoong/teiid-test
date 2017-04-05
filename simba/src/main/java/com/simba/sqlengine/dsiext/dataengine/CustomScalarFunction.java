package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public abstract class CustomScalarFunction
{
  private final String m_name;
  
  protected CustomScalarFunction(String paramString)
  {
    if (null == paramString) {
      throw new NullPointerException("name");
    }
    this.m_name = paramString;
  }
  
  public abstract void execute(List<? extends ISqlDataWrapper> paramList)
    throws ErrorException;
  
  public abstract List<IColumn> getInputMetadata();
  
  public final String getName()
  {
    return this.m_name;
  }
  
  public abstract IColumn getOutputMetadata();
  
  public abstract boolean retrieveData(ISqlDataWrapper paramISqlDataWrapper, long paramLong1, long paramLong2)
    throws ErrorException;
  
  public abstract void updateMetadata(List<IColumnInfo> paramList, boolean paramBoolean)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/CustomScalarFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
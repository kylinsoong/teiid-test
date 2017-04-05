package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.sql.Time;

public final class SqlTimeDataWrapper
  extends DefaultSqlDataWrapper
{
  private Time m_data;
  private boolean m_isSet = false;
  
  public Time getTime()
    throws ErrorException
  {
    checkIsSet();
    return this.m_data;
  }
  
  public int getType()
  {
    return 92;
  }
  
  public boolean isNull()
    throws ErrorException
  {
    checkIsSet();
    return null == this.m_data;
  }
  
  public boolean isSet()
  {
    return this.m_isSet;
  }
  
  public void setTime(Time paramTime)
  {
    this.m_isSet = true;
    this.m_data = paramTime;
  }
  
  public void setNull()
  {
    this.m_isSet = true;
    this.m_data = null;
  }
  
  public void setValue(DataWrapper paramDataWrapper)
    throws ErrorException
  {
    try
    {
      switch (paramDataWrapper.getType())
      {
      case 92: 
        setTime(paramDataWrapper.getTime());
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(paramDataWrapper.getType());
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new SQLEngineException(SQLEngineMessageKey.INVALID_OPERATION.name(), localIncorrectTypeException);
    }
  }
  
  public void retrieveData(DataWrapper paramDataWrapper)
    throws ErrorException
  {
    checkIsSet();
    paramDataWrapper.setTime(this.m_data);
  }
  
  private void checkIsSet()
    throws ErrorException
  {
    if (!this.m_isSet) {
      throw SQLEngineExceptionFactory.requestedDataNotSet();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlTimeDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;

public final class SqlCharDataWrapper
  extends DefaultSqlDataWrapper
{
  private String m_data;
  private boolean m_isSet = false;
  private final int m_type;
  
  public SqlCharDataWrapper(int paramInt)
  {
    assert ((paramInt == 1) || (paramInt == 12) || (paramInt == -1) || (paramInt == -8) || (paramInt == -9) || (paramInt == -10));
    this.m_type = paramInt;
  }
  
  public String getChar()
    throws ErrorException
  {
    checkIsSet();
    return this.m_data;
  }
  
  public int getType()
  {
    return this.m_type;
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
  
  public void setChar(String paramString)
  {
    this.m_isSet = true;
    this.m_data = paramString;
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
      String str = null;
      switch (paramDataWrapper.getType())
      {
      case -8: 
      case 1: 
        str = paramDataWrapper.getChar();
        break;
      case -9: 
      case 12: 
        str = paramDataWrapper.getVarChar();
        break;
      case -10: 
      case -1: 
        str = paramDataWrapper.getLongVarChar();
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(paramDataWrapper.getType());
      }
      setChar(str);
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
    switch (this.m_type)
    {
    case 1: 
      paramDataWrapper.setChar(this.m_data);
      break;
    case 12: 
      paramDataWrapper.setVarChar(this.m_data);
      break;
    case -1: 
      paramDataWrapper.setLongVarChar(this.m_data);
      break;
    case -8: 
      paramDataWrapper.setWChar(this.m_data);
      break;
    case -9: 
      paramDataWrapper.setWVarChar(this.m_data);
      break;
    case -10: 
      paramDataWrapper.setWLongVarChar(this.m_data);
      break;
    default: 
      throw new IllegalStateException();
    }
  }
  
  private void checkIsSet()
    throws ErrorException
  {
    if (!this.m_isSet) {
      throw SQLEngineExceptionFactory.requestedDataNotSet();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlCharDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
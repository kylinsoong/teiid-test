package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;

public final class SqlBinaryDataWrapper
  extends DefaultSqlDataWrapper
{
  private byte[] m_data;
  private boolean m_isSet = false;
  private final int m_type;
  
  public SqlBinaryDataWrapper(int paramInt)
  {
    assert ((paramInt == -2) || (paramInt == -3) || (paramInt == -4));
    this.m_type = paramInt;
  }
  
  public byte[] getBinary()
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
    return this.m_data == null;
  }
  
  public boolean isSet()
  {
    return this.m_isSet;
  }
  
  public void setBinary(byte[] paramArrayOfByte)
  {
    this.m_isSet = true;
    this.m_data = paramArrayOfByte;
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
      byte[] arrayOfByte = null;
      switch (paramDataWrapper.getType())
      {
      case -2: 
        arrayOfByte = paramDataWrapper.getBinary();
        break;
      case -3: 
        arrayOfByte = paramDataWrapper.getVarBinary();
        break;
      case -4: 
        arrayOfByte = paramDataWrapper.getLongVarBinary();
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(paramDataWrapper.getType());
      }
      setBinary(arrayOfByte);
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
    case -2: 
      paramDataWrapper.setBinary(this.m_data);
      break;
    case -3: 
      paramDataWrapper.setVarBinary(this.m_data);
      break;
    case -4: 
      paramDataWrapper.setLongVarBinary(this.m_data);
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlBinaryDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
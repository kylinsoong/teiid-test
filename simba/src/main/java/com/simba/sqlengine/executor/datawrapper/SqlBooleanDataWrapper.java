package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public final class SqlBooleanDataWrapper
  extends DefaultSqlDataWrapper
{
  private boolean m_data;
  private boolean m_isSet = false;
  private boolean m_isNull = false;
  private final int m_type;
  
  public SqlBooleanDataWrapper(int paramInt)
  {
    assert ((paramInt == -7) || (paramInt == 16));
    this.m_type = paramInt;
  }
  
  public BigInteger getBigInt()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    if (this.m_data) {
      return BigInteger.ONE;
    }
    return BigInteger.ZERO;
  }
  
  public boolean getBoolean()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    return this.m_data;
  }
  
  public double getDouble()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    if (this.m_data) {
      return 1.0D;
    }
    return 0.0D;
  }
  
  public float getReal()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    if (this.m_data) {
      return 1.0F;
    }
    return 0.0F;
  }
  
  public long getInteger()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    if (this.m_data) {
      return 1L;
    }
    return 0L;
  }
  
  public int getSmallInt()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    if (this.m_data) {
      return 1;
    }
    return 0;
  }
  
  public short getTinyInt()
    throws ErrorException, NullPointerException
  {
    checkValidRetrieval();
    if (this.m_data) {
      return 1;
    }
    return 0;
  }
  
  public int getType()
  {
    return this.m_type;
  }
  
  public boolean isNull()
  {
    return this.m_isNull;
  }
  
  public boolean isSet()
  {
    return this.m_isSet;
  }
  
  public void setBoolean(boolean paramBoolean)
  {
    this.m_isSet = true;
    this.m_data = paramBoolean;
    this.m_isNull = false;
  }
  
  public void setNull()
  {
    this.m_isSet = true;
    this.m_isNull = true;
  }
  
  public void setValue(DataWrapper paramDataWrapper)
    throws ErrorException
  {
    if (paramDataWrapper.isNull()) {
      setNull();
    } else {
      try
      {
        switch (paramDataWrapper.getType())
        {
        case 16: 
          setBoolean(paramDataWrapper.getBoolean().booleanValue());
          break;
        case -7: 
          setBoolean(paramDataWrapper.getBit().booleanValue());
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
  }
  
  public void retrieveData(DataWrapper paramDataWrapper)
    throws ErrorException
  {
    if (!this.m_isSet) {
      throw new IllegalStateException("ISqlDataWrapper is not set with data for retrieval");
    }
    if (this.m_isNull)
    {
      paramDataWrapper.setNull(this.m_type);
      return;
    }
    switch (this.m_type)
    {
    case 16: 
      paramDataWrapper.setBoolean(this.m_data);
      break;
    case -7: 
      paramDataWrapper.setBit(this.m_data);
      break;
    default: 
      throw new IllegalStateException();
    }
  }
  
  private void checkValidRetrieval()
    throws ErrorException
  {
    if (!this.m_isSet) {
      throw SQLEngineExceptionFactory.requestedDataNotSet();
    }
    if (this.m_isNull) {
      throw new NullPointerException("Data requested, but the data is null.");
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlBooleanDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
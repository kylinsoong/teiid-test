package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public final class SqlIntegerDataWrapper
  extends DefaultSqlDataWrapper
{
  private long m_data;
  private boolean m_isSet = false;
  private boolean m_isNull = false;
  
  public BigInteger getBigInt()
    throws ErrorException, NullPointerException
  {
    return BigInteger.valueOf(getData());
  }
  
  public double getDouble()
    throws ErrorException, NullPointerException
  {
    return getData();
  }
  
  public long getInteger()
    throws ErrorException, NullPointerException
  {
    return getData();
  }
  
  public int getType()
  {
    return 4;
  }
  
  public boolean isSet()
  {
    return this.m_isSet;
  }
  
  public boolean isNull()
  {
    return this.m_isNull;
  }
  
  public void setBoolean(boolean paramBoolean)
  {
    this.m_isSet = true;
    this.m_isNull = false;
    if (paramBoolean) {
      this.m_data = 1L;
    } else {
      this.m_data = 0L;
    }
  }
  
  public void setInteger(long paramLong)
  {
    setData(paramLong);
  }
  
  public void setSmallInt(int paramInt)
  {
    setData(paramInt);
  }
  
  public void setTinyInt(short paramShort)
  {
    setData(paramShort);
  }
  
  public void setNull()
  {
    this.m_isSet = true;
    this.m_isNull = true;
  }
  
  private long getData()
    throws ErrorException
  {
    if (!this.m_isSet) {
      throw SQLEngineExceptionFactory.requestedDataNotSet();
    }
    if (this.m_isNull) {
      throw new NullPointerException("Data requested, but the data is null.");
    }
    return this.m_data;
  }
  
  private void setData(long paramLong)
  {
    this.m_isSet = true;
    this.m_data = paramLong;
    this.m_isNull = false;
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
        case 4: 
          setInteger(paramDataWrapper.getInteger().longValue());
          break;
        case 5: 
          setSmallInt(paramDataWrapper.getSmallInt().intValue());
          break;
        case -6: 
          setTinyInt(paramDataWrapper.getTinyInt().shortValue());
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
      paramDataWrapper.setNull(4);
      return;
    }
    paramDataWrapper.setInteger(this.m_data);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlIntegerDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
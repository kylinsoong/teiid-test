package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;

public final class SqlBigIntDataWrapper
  extends DefaultSqlDataWrapper
{
  private BigInteger m_data = null;
  private boolean m_isSet = false;
  
  public BigInteger getBigInt()
    throws ErrorException
  {
    checkIsSet();
    return this.m_data;
  }
  
  public int getType()
  {
    return -5;
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
  
  public void setBoolean(boolean paramBoolean)
  {
    this.m_isSet = true;
    if (paramBoolean) {
      this.m_data = BigInteger.ONE;
    } else {
      this.m_data = BigInteger.ZERO;
    }
  }
  
  public void setBigInt(BigInteger paramBigInteger)
  {
    this.m_isSet = true;
    this.m_data = paramBigInteger;
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
    this.m_data = null;
    this.m_isSet = true;
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
        case -5: 
          setBigInt(paramDataWrapper.getBigInt());
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
    checkIsSet();
    paramDataWrapper.setBigInt(this.m_data);
  }
  
  private void checkIsSet()
    throws ErrorException
  {
    if (!this.m_isSet) {
      throw SQLEngineExceptionFactory.requestedDataNotSet();
    }
  }
  
  private void setData(long paramLong)
  {
    this.m_isSet = true;
    this.m_data = BigInteger.valueOf(paramLong);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlBigIntDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
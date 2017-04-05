package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;

public final class SqlDoubleDataWrapper
  extends DefaultSqlDataWrapper
{
  private double m_data;
  private boolean m_isSet = false;
  private boolean m_isNull = false;
  private final int m_type;
  
  public SqlDoubleDataWrapper(int paramInt)
  {
    assert ((paramInt == 8) || (paramInt == 6));
    this.m_type = paramInt;
  }
  
  public double getDouble()
    throws ErrorException, NullPointerException
  {
    if (!this.m_isSet) {
      throw SQLEngineExceptionFactory.requestedDataNotSet();
    }
    if (this.m_isNull) {
      throw new NullPointerException("Data requested, but the data is null.");
    }
    return this.m_data;
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
    this.m_isNull = false;
    if (paramBoolean) {
      this.m_data = 1.0D;
    } else {
      this.m_data = 0.0D;
    }
  }
  
  public void setDouble(double paramDouble)
  {
    setData(paramDouble);
  }
  
  public void setInteger(long paramLong)
  {
    setData(paramLong);
  }
  
  public void setReal(float paramFloat)
  {
    setData(paramFloat);
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
  
  private void setData(double paramDouble)
  {
    this.m_isSet = true;
    this.m_data = paramDouble;
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
        case 8: 
          setDouble(paramDataWrapper.getDouble().doubleValue());
          break;
        case 6: 
          setDouble(paramDataWrapper.getFloat().doubleValue());
          break;
        case 7: 
          setReal(paramDataWrapper.getReal().floatValue());
          break;
        case -5: 
        case -4: 
        case -3: 
        case -2: 
        case -1: 
        case 0: 
        case 1: 
        case 2: 
        case 3: 
        case 9: 
        case 10: 
        case 11: 
        case 12: 
        case 13: 
        case 14: 
        case 15: 
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
    case 8: 
      paramDataWrapper.setDouble(this.m_data);
      break;
    case 6: 
      paramDataWrapper.setFloat(this.m_data);
      break;
    default: 
      throw new IllegalStateException("Data must be SQL_DOUBLE or SQL_FLOAT.");
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlDoubleDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;

public final class SqlRealDataWrapper
  extends DefaultSqlDataWrapper
{
  private float m_data;
  private boolean m_isSet = false;
  private boolean m_isNull = false;
  
  public double getDouble()
    throws ErrorException, NullPointerException
  {
    return getData();
  }
  
  public float getReal()
    throws ErrorException, NullPointerException
  {
    return getData();
  }
  
  public int getType()
  {
    return 7;
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
      this.m_data = 1.0F;
    } else {
      this.m_data = 0.0F;
    }
  }
  
  public void setReal(float paramFloat)
  {
    this.m_isSet = true;
    this.m_data = paramFloat;
    this.m_isNull = false;
  }
  
  public void setSmallInt(int paramInt)
  {
    this.m_isSet = true;
    this.m_data = paramInt;
    this.m_isNull = false;
  }
  
  public void setTinyInt(short paramShort)
  {
    this.m_isSet = true;
    this.m_data = paramShort;
    this.m_isNull = false;
  }
  
  private float getData()
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
        case 5: 
          setSmallInt(paramDataWrapper.getSmallInt().intValue());
          break;
        case -6: 
          setTinyInt(paramDataWrapper.getTinyInt().shortValue());
          break;
        case 7: 
          setReal(paramDataWrapper.getReal().floatValue());
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
      paramDataWrapper.setNull(7);
      return;
    }
    paramDataWrapper.setReal(this.m_data);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlRealDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
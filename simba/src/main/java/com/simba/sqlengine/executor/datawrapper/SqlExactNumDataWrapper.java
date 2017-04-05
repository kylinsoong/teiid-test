package com.simba.sqlengine.executor.datawrapper;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;

public final class SqlExactNumDataWrapper
  extends DefaultSqlDataWrapper
{
  private BigDecimal m_data;
  private boolean m_isSet = false;
  private final int m_type;
  
  public SqlExactNumDataWrapper(int paramInt)
  {
    assert ((paramInt == 3) || (paramInt == 2));
    this.m_type = paramInt;
  }
  
  public BigDecimal getExactNumber()
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
  
  public void setExactNumber(BigDecimal paramBigDecimal)
  {
    this.m_isSet = true;
    this.m_data = paramBigDecimal;
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
      BigDecimal localBigDecimal = null;
      switch (paramDataWrapper.getType())
      {
      case 3: 
        localBigDecimal = paramDataWrapper.getDecimal();
        break;
      case 2: 
        localBigDecimal = paramDataWrapper.getNumeric();
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidSqlTypeForWrapperException(paramDataWrapper.getType());
      }
      setExactNumber(localBigDecimal);
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
    case 3: 
      paramDataWrapper.setDecimal(this.m_data);
      break;
    case 2: 
      paramDataWrapper.setNumeric(this.m_data);
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/datawrapper/SqlExactNumDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.utilities.DataWrapper;

public class ProcedureParameterValue
{
  private boolean m_isDefault;
  private DataWrapper m_data;
  
  public ProcedureParameterValue(DataWrapper paramDataWrapper, boolean paramBoolean)
  {
    this.m_isDefault = paramBoolean;
    this.m_data = paramDataWrapper;
  }
  
  public DataWrapper getData()
  {
    return this.m_data;
  }
  
  public boolean isDefaultValue()
  {
    return this.m_isDefault;
  }
  
  public boolean isNull()
  {
    return (null == this.m_data) || (this.m_data.isNull());
  }
  
  public void setNull()
  {
    if (null != this.m_data) {
      this.m_data.setNull(this.m_data.getType());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ProcedureParameterValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
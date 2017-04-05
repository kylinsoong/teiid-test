package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.exceptions.DefaultParamException;
import com.simba.dsi.exceptions.ParamAlreadyPushedException;

public final class ParameterInputValue
{
  private ParameterMetadata m_metadata;
  private boolean m_isPushed;
  private boolean m_isDefault;
  private DataWrapper m_data;
  
  public ParameterInputValue(ParameterMetadata paramParameterMetadata, boolean paramBoolean1, boolean paramBoolean2, DataWrapper paramDataWrapper)
  {
    this.m_metadata = paramParameterMetadata;
    this.m_isPushed = paramBoolean1;
    this.m_isDefault = paramBoolean2;
    this.m_data = paramDataWrapper;
  }
  
  public ParameterInputValue(ParameterMetadata paramParameterMetadata, boolean paramBoolean, DataWrapper paramDataWrapper)
  {
    this.m_metadata = paramParameterMetadata;
    this.m_isPushed = paramBoolean;
    this.m_isDefault = false;
    this.m_data = paramDataWrapper;
  }
  
  public ParameterInputValue(ParameterMetadata paramParameterMetadata, DataWrapper paramDataWrapper)
  {
    this.m_metadata = paramParameterMetadata;
    this.m_isPushed = false;
    this.m_isDefault = false;
    this.m_data = paramDataWrapper;
  }
  
  public DataWrapper getData()
    throws ParamAlreadyPushedException, DefaultParamException
  {
    if (this.m_isPushed) {
      throw new ParamAlreadyPushedException();
    }
    if (this.m_isDefault) {
      throw new DefaultParamException();
    }
    return this.m_data;
  }
  
  public ParameterMetadata getMetadata()
  {
    return this.m_metadata;
  }
  
  public boolean isDefaultValue()
  {
    return this.m_isDefault;
  }
  
  public boolean isNull()
  {
    return (null == this.m_data) || (this.m_data.isNull());
  }
  
  public boolean isPushed()
  {
    return this.m_isPushed;
  }
  
  public void setIsDefaultValue(boolean paramBoolean)
  {
    this.m_isDefault = paramBoolean;
  }
  
  public void setPushed(boolean paramBoolean)
  {
    this.m_isPushed = paramBoolean;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ParameterInputValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
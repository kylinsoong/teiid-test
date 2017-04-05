package com.simba.dsi.dataengine.utilities;

public class ParameterOutputValue
{
  private ParameterMetadata m_metadata;
  private DataWrapper m_data = new DataWrapper();
  
  protected ParameterOutputValue(ParameterMetadata paramParameterMetadata)
  {
    this.m_metadata = paramParameterMetadata;
  }
  
  public DataWrapper getData()
  {
    return this.m_data;
  }
  
  public ParameterMetadata getMetadata()
  {
    return this.m_metadata;
  }
  
  public void setNull()
  {
    this.m_data.setNull(this.m_metadata.getTypeMetadata().getType());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ParameterOutputValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
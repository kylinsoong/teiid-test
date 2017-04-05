package com.simba.dsi.dataengine.utilities;

public class MetadataColumn
  extends ColumnMetadata
{
  private MetadataSourceColumnTag m_columnTag;
  private boolean m_isNullColumn;
  
  public MetadataColumn(TypeMetadata paramTypeMetadata, MetadataSourceColumnTag paramMetadataSourceColumnTag)
    throws NullPointerException
  {
    super(paramTypeMetadata);
    this.m_columnTag = paramMetadataSourceColumnTag;
  }
  
  public MetadataSourceColumnTag getColumnTag()
  {
    return this.m_columnTag;
  }
  
  public boolean isNullColumn()
  {
    return this.m_isNullColumn;
  }
  
  public void setNullColumn(boolean paramBoolean)
  {
    this.m_isNullColumn = paramBoolean;
  }
  
  public String toString()
  {
    if (isUnnamed()) {
      return "DDSIMetadataColumn: <null>";
    }
    return "MetadataColumn: " + getName();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/MetadataColumn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
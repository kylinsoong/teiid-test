package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;

public class MetadataColumnInfo
  extends AEAbstractColumnInfo
{
  private IColumn m_columnMetadata;
  private IColumnInfo.ColumnType m_columnType;
  
  public MetadataColumnInfo(IColumn paramIColumn, IColumnInfo.ColumnType paramColumnType)
  {
    this.m_columnMetadata = paramIColumn;
    this.m_columnType = paramColumnType;
  }
  
  public IColumnInfo.ColumnType getColumnType()
  {
    return this.m_columnType;
  }
  
  public String getLiteralString()
  {
    return null;
  }
  
  public IColumn getColumnMetadata()
  {
    return this.m_columnMetadata;
  }
  
  public void setColumnMetadata(IColumn paramIColumn)
  {
    if (paramIColumn == null) {
      throw new NullPointerException("Can not set null column metadata.");
    }
    this.m_columnMetadata = paramIColumn;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/MetadataColumnInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
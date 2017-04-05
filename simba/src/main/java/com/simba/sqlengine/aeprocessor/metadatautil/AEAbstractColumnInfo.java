package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.support.exceptions.ErrorException;

public abstract class AEAbstractColumnInfo
  implements IColumnInfo
{
  public int getIntervalPrecision()
  {
    return getColumnMetadata().getTypeMetadata().getIntervalPrecision();
  }
  
  public short getPrecision()
  {
    return getColumnMetadata().getTypeMetadata().getPrecision();
  }
  
  public short getScale()
  {
    return getColumnMetadata().getTypeMetadata().getScale();
  }
  
  public short getType()
  {
    return getColumnMetadata().getTypeMetadata().getType();
  }
  
  public boolean isCurrency()
  {
    return getColumnMetadata().getTypeMetadata().isCurrency();
  }
  
  public boolean isSigned()
  {
    return getColumnMetadata().getTypeMetadata().isSigned();
  }
  
  public boolean isSortable()
  {
    return getColumnMetadata().getTypeMetadata().isSortable();
  }
  
  public long getColumnLength()
  {
    return getColumnMetadata().getColumnLength();
  }
  
  public Nullable getNullable()
  {
    return getColumnMetadata().getNullable();
  }
  
  public Searchable getSearchable()
  {
    return getColumnMetadata().getSearchable();
  }
  
  public Updatable getUpdatable()
  {
    return getColumnMetadata().getUpdatable();
  }
  
  public boolean isAutoUnique()
  {
    return getColumnMetadata().isAutoUnique();
  }
  
  public boolean isCaseSensitive()
  {
    return getColumnMetadata().isCaseSensitive();
  }
  
  public boolean isDefinitelyWritable()
  {
    return getColumnMetadata().isDefinitelyWritable();
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getColumnType() + ", type=" + getType() + "]";
  }
  
  public long getDisplaySize()
    throws ErrorException
  {
    return getColumnMetadata().getDisplaySize();
  }
  
  protected abstract IColumn getColumnMetadata();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AEAbstractColumnInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
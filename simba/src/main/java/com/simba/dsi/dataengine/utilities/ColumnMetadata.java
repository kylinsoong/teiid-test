package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.support.exceptions.ErrorException;

public class ColumnMetadata
  implements IColumn
{
  private String m_label;
  private String m_name;
  private String m_catalogName;
  private String m_schemaName;
  private String m_tableName;
  private TypeMetadata m_typeMetadata;
  private Updatable m_updatable;
  private Searchable m_searchable;
  private Nullable m_nullable;
  private long m_columnLength;
  private boolean m_isAutoUnique;
  private boolean m_isCaseSensitive;
  
  public ColumnMetadata(TypeMetadata paramTypeMetadata)
    throws NullPointerException
  {
    if (null == paramTypeMetadata) {
      throw new NullPointerException();
    }
    this.m_typeMetadata = paramTypeMetadata;
    this.m_label = "";
    this.m_name = "";
    this.m_catalogName = "";
    this.m_schemaName = "";
    this.m_tableName = "";
    this.m_updatable = Updatable.UNKNOWN;
    this.m_searchable = Searchable.SEARCHABLE;
    this.m_nullable = Nullable.UNKNOWN;
    this.m_columnLength = 0L;
    this.m_isAutoUnique = false;
    this.m_isCaseSensitive = false;
  }
  
  public static ColumnMetadata copyOf(IColumn paramIColumn)
  {
    ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.copyOf(paramIColumn.getTypeMetadata()));
    localColumnMetadata.setAutoUnique(paramIColumn.isAutoUnique());
    localColumnMetadata.setCaseSensitive(paramIColumn.isCaseSensitive());
    localColumnMetadata.setCatalogName(paramIColumn.getCatalogName());
    try
    {
      localColumnMetadata.setColumnLength(paramIColumn.getColumnLength());
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new RuntimeException(localNumericOverflowException);
    }
    localColumnMetadata.setLabel(paramIColumn.getLabel());
    localColumnMetadata.setName(paramIColumn.getName());
    localColumnMetadata.setNullable(paramIColumn.getNullable());
    localColumnMetadata.setSchemaName(paramIColumn.getSchemaName());
    localColumnMetadata.setSearchable(paramIColumn.getSearchable());
    localColumnMetadata.setTableName(paramIColumn.getTableName());
    localColumnMetadata.setTypeMetadata(TypeMetadata.copyOf(paramIColumn.getTypeMetadata()));
    localColumnMetadata.setUpdatable(paramIColumn.getUpdatable());
    return localColumnMetadata;
  }
  
  public String getCatalogName()
  {
    return this.m_catalogName;
  }
  
  public long getColumnLength()
  {
    return this.m_columnLength;
  }
  
  public long getDisplaySize()
    throws ErrorException
  {
    return TypeUtilities.getDisplaySize(this.m_typeMetadata, this.m_columnLength);
  }
  
  public String getLabel()
  {
    return this.m_label;
  }
  
  public String getName()
  {
    return this.m_name;
  }
  
  public Nullable getNullable()
  {
    return this.m_nullable;
  }
  
  public String getSchemaName()
  {
    return this.m_schemaName;
  }
  
  public Searchable getSearchable()
  {
    return this.m_searchable;
  }
  
  public String getTableName()
  {
    return this.m_tableName;
  }
  
  public TypeMetadata getTypeMetadata()
  {
    return this.m_typeMetadata;
  }
  
  public Updatable getUpdatable()
  {
    return this.m_updatable;
  }
  
  public boolean isAutoUnique()
  {
    return this.m_isAutoUnique;
  }
  
  public boolean isCaseSensitive()
  {
    return this.m_isCaseSensitive;
  }
  
  public boolean isDefinitelyWritable()
  {
    return Updatable.WRITE == this.m_updatable;
  }
  
  public boolean isUnnamed()
  {
    return null == this.m_name;
  }
  
  public void setAutoUnique(boolean paramBoolean)
  {
    this.m_isAutoUnique = paramBoolean;
  }
  
  public void setCaseSensitive(boolean paramBoolean)
  {
    this.m_isCaseSensitive = paramBoolean;
  }
  
  public void setCatalogName(String paramString)
  {
    this.m_catalogName = paramString;
  }
  
  public void setColumnLength(long paramLong)
    throws NumericOverflowException
  {
    if ((paramLong > 4294967295L) || (paramLong < 0L)) {
      throw new NumericOverflowException();
    }
    this.m_columnLength = paramLong;
  }
  
  public void setLabel(String paramString)
  {
    this.m_label = paramString;
  }
  
  public void setName(String paramString)
  {
    this.m_name = paramString;
  }
  
  public void setNullable(Nullable paramNullable)
  {
    this.m_nullable = paramNullable;
  }
  
  public void setSchemaName(String paramString)
  {
    this.m_schemaName = paramString;
  }
  
  public void setSearchable(Searchable paramSearchable)
  {
    this.m_searchable = paramSearchable;
  }
  
  public void setTableName(String paramString)
  {
    this.m_tableName = paramString;
  }
  
  public void setTypeMetadata(TypeMetadata paramTypeMetadata)
    throws NullPointerException
  {
    if (null == paramTypeMetadata) {
      throw new NullPointerException();
    }
    this.m_typeMetadata = paramTypeMetadata;
  }
  
  public void setUpdatable(Updatable paramUpdatable)
  {
    this.m_updatable = paramUpdatable;
  }
  
  public String toString()
  {
    if (isUnnamed()) {
      return "ColumnMetadata: <null>";
    }
    return "ColumnMetadata: " + getName();
  }
  
  public int hashCode()
  {
    int i = 1;
    i = 31 * i + (this.m_catalogName == null ? 0 : this.m_catalogName.hashCode());
    i = 31 * i + (int)(this.m_columnLength ^ this.m_columnLength >>> 32);
    i = 31 * i + (this.m_isAutoUnique ? 1231 : 1237);
    i = 31 * i + (this.m_isCaseSensitive ? 1231 : 1237);
    i = 31 * i + (this.m_label == null ? 0 : this.m_label.hashCode());
    i = 31 * i + (this.m_name == null ? 0 : this.m_name.hashCode());
    i = 31 * i + (this.m_nullable == null ? 0 : this.m_nullable.hashCode());
    i = 31 * i + (this.m_schemaName == null ? 0 : this.m_schemaName.hashCode());
    i = 31 * i + (this.m_searchable == null ? 0 : this.m_searchable.hashCode());
    i = 31 * i + (this.m_tableName == null ? 0 : this.m_tableName.hashCode());
    i = 31 * i + (this.m_typeMetadata == null ? 0 : this.m_typeMetadata.hashCode());
    i = 31 * i + (this.m_updatable == null ? 0 : this.m_updatable.hashCode());
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    ColumnMetadata localColumnMetadata = (ColumnMetadata)paramObject;
    if (this.m_catalogName == null)
    {
      if (localColumnMetadata.m_catalogName != null) {
        return false;
      }
    }
    else if (!this.m_catalogName.equals(localColumnMetadata.m_catalogName)) {
      return false;
    }
    if (this.m_columnLength != localColumnMetadata.m_columnLength) {
      return false;
    }
    if (this.m_isAutoUnique != localColumnMetadata.m_isAutoUnique) {
      return false;
    }
    if (this.m_isCaseSensitive != localColumnMetadata.m_isCaseSensitive) {
      return false;
    }
    if (this.m_label == null)
    {
      if (localColumnMetadata.m_label != null) {
        return false;
      }
    }
    else if (!this.m_label.equals(localColumnMetadata.m_label)) {
      return false;
    }
    if (this.m_name == null)
    {
      if (localColumnMetadata.m_name != null) {
        return false;
      }
    }
    else if (!this.m_name.equals(localColumnMetadata.m_name)) {
      return false;
    }
    if (this.m_nullable != localColumnMetadata.m_nullable) {
      return false;
    }
    if (this.m_schemaName == null)
    {
      if (localColumnMetadata.m_schemaName != null) {
        return false;
      }
    }
    else if (!this.m_schemaName.equals(localColumnMetadata.m_schemaName)) {
      return false;
    }
    if (this.m_searchable != localColumnMetadata.m_searchable) {
      return false;
    }
    if (this.m_tableName == null)
    {
      if (localColumnMetadata.m_tableName != null) {
        return false;
      }
    }
    else if (!this.m_tableName.equals(localColumnMetadata.m_tableName)) {
      return false;
    }
    if (this.m_typeMetadata == null)
    {
      if (localColumnMetadata.m_typeMetadata != null) {
        return false;
      }
    }
    else if (!this.m_typeMetadata.equals(localColumnMetadata.m_typeMetadata)) {
      return false;
    }
    return this.m_updatable == localColumnMetadata.m_updatable;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ColumnMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
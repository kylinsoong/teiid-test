package com.simba.dsi.dataengine.interfaces;

import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.support.exceptions.ErrorException;

public abstract interface IColumn
{
  public abstract String getCatalogName();
  
  public abstract long getColumnLength();
  
  public abstract long getDisplaySize()
    throws ErrorException;
  
  public abstract String getLabel();
  
  public abstract String getName();
  
  public abstract Nullable getNullable();
  
  public abstract String getSchemaName();
  
  public abstract Searchable getSearchable();
  
  public abstract String getTableName();
  
  public abstract TypeMetadata getTypeMetadata();
  
  public abstract Updatable getUpdatable();
  
  public abstract boolean isAutoUnique();
  
  public abstract boolean isCaseSensitive();
  
  public abstract boolean isDefinitelyWritable();
  
  public abstract boolean isUnnamed();
}
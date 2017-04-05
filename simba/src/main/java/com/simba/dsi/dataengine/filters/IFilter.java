package com.simba.dsi.dataengine.filters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;

public abstract interface IFilter
{
  public abstract boolean filter(DataWrapper paramDataWrapper);
  
  public abstract MetadataSourceColumnTag getColumnTag();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/IFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.dsi.dataengine.interfaces;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.support.exceptions.ErrorException;

public abstract interface IMetadataSource
{
  public abstract void close();
  
  public abstract void closeCursor()
    throws ErrorException;
  
  public abstract boolean getMetadata(MetadataSourceColumnTag paramMetadataSourceColumnTag, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException;
  
  public abstract boolean hasMoreRows()
    throws ErrorException;
  
  public abstract boolean moveToNextRow();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
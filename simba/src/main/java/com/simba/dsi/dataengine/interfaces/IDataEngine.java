package com.simba.dsi.dataengine.interfaces;

import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.dataengine.utilities.OrderType;
import com.simba.dsi.exceptions.ParsingException;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IDataEngine
{
  public abstract void close();
  
  public abstract IResultSet makeNewMetadataResult(MetadataSourceID paramMetadataSourceID, ArrayList<String> paramArrayList, String paramString1, String paramString2, boolean paramBoolean, OrderType paramOrderType)
    throws ErrorException;
  
  public abstract IResultSet makeNewMetadataResult(MetadataSourceID paramMetadataSourceID, ArrayList<String> paramArrayList, String paramString1, String paramString2, boolean paramBoolean)
    throws ErrorException;
  
  public abstract IQueryExecutor prepare(String paramString)
    throws ParsingException, ErrorException;
  
  public abstract IQueryExecutor prepareBatch(List<String> paramList)
    throws ParsingException, ErrorException;
  
  public abstract void setDirectExecute();
  
  public abstract void setMetadataNeeded(boolean paramBoolean);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/interfaces/IDataEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
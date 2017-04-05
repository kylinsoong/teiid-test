package com.simba.dsi.dataengine.impl;

import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;

public class DSIEmptyResultSet
  extends DSISimpleResultSet
{
  protected void doCloseCursor()
    throws ErrorException
  {}
  
  protected boolean doMoveToNextRow()
    throws ErrorException
  {
    return false;
  }
  
  public boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    return false;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return 0L;
  }
  
  public ArrayList<ColumnMetadata> getSelectColumns()
    throws ErrorException
  {
    return new ArrayList();
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    return false;
  }
  
  public boolean hasRowCount()
  {
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIEmptyResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.dsi.dataengine.impl;

import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DSIMonthSpan;
import com.simba.dsi.dataengine.utilities.DSITimeSpan;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.support.exceptions.ErrorException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public abstract class DSIArray
  implements IArray
{
  public Object createArray(long paramLong, int paramInt)
    throws ErrorException
  {
    Class localClass = getComponentClass();
    Iterator localIterator = createIterator(paramLong, paramInt);
    ArrayList localArrayList = new ArrayList();
    while (localIterator.hasNext()) {
      localArrayList.add(localIterator.next());
    }
    return localArrayList.toArray((Object[])Array.newInstance(localClass, localArrayList.size()));
  }
  
  public IResultSet createResultSet(long paramLong, int paramInt)
  {
    Iterator localIterator = createIterator(paramLong, paramInt);
    IColumn localIColumn = getBaseColumn();
    return new DSIArrayResultSet(localIterator, localIColumn, paramLong, paramInt);
  }
  
  public void free() {}
  
  public abstract IColumn getBaseColumn();
  
  protected abstract Iterator<?> createIterator(long paramLong, int paramInt);
  
  protected Class<?> getComponentClass()
  {
    int i = getBaseColumn().getTypeMetadata().getType();
    if (TypeUtilities.isCharacterType(i)) {
      return String.class;
    }
    if (TypeUtilities.isBinaryType(i)) {
      return byte[].class;
    }
    switch (i)
    {
    case 2003: 
      return IArray.class;
    case 2: 
    case 3: 
      return BigDecimal.class;
    case -7: 
    case 16: 
      return Boolean.class;
    case -6: 
    case 4: 
    case 5: 
      return Integer.class;
    case -5: 
      return Long.class;
    case 7: 
      return Float.class;
    case 6: 
    case 8: 
      return Double.class;
    case 91: 
      return Date.class;
    case 92: 
      return Time.class;
    case 93: 
      return Timestamp.class;
    case -11: 
      return UUID.class;
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      return DSITimeSpan.class;
    case 101: 
    case 102: 
    case 107: 
      return DSIMonthSpan.class;
    }
    return Object.class;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
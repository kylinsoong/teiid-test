package com.simba.sqlengine.executor.etree.temptable.column;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ColumnSliceArray
  implements Iterable<IColumnSlice>, Serializable
{
  private static final long serialVersionUID = -1645573716418355541L;
  private final IColumnSlice[] m_data;
  private final int[] m_columnMapping;
  private final int m_numRows;
  
  public ColumnSliceArray(IColumnSlice[] paramArrayOfIColumnSlice, int[] paramArrayOfInt, int paramInt)
  {
    this.m_data = paramArrayOfIColumnSlice;
    this.m_numRows = paramInt;
    this.m_columnMapping = paramArrayOfInt;
  }
  
  public IColumnSlice get(int paramInt)
  {
    return this.m_data[this.m_columnMapping[paramInt]];
  }
  
  public Iterator<IColumnSlice> iterator()
  {
    return new DataIterator(null);
  }
  
  public int numRows()
  {
    return this.m_numRows;
  }
  
  public int simulatedNumColumns()
  {
    return this.m_columnMapping.length;
  }
  
  public int trueNumColumns()
  {
    return this.m_data.length;
  }
  
  private class DataIterator
    implements Iterator<IColumnSlice>
  {
    int m_currentIndex = 0;
    
    private DataIterator() {}
    
    public boolean hasNext()
    {
      return this.m_currentIndex < ColumnSliceArray.this.m_data.length;
    }
    
    public IColumnSlice next()
    {
      if (this.m_currentIndex >= ColumnSliceArray.this.m_data.length) {
        throw new NoSuchElementException();
      }
      IColumnSlice localIColumnSlice = ColumnSliceArray.this.m_data[this.m_currentIndex];
      this.m_currentIndex += 1;
      return localIColumnSlice;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/ColumnSliceArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
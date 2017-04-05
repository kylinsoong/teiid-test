package com.simba.dsi.dataengine.impl;

import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class DSISimpleArray
  extends DSIArray
{
  protected Object m_array;
  protected int m_length;
  private IColumn m_arrayMetadata;
  private IColumn m_nestedMetadata;
  
  public DSISimpleArray(Object paramObject, IColumn paramIColumn1, IColumn paramIColumn2)
  {
    if (!paramObject.getClass().isArray()) {
      throw new ClassCastException("Not an array: " + paramObject.getClass());
    }
    this.m_arrayMetadata = paramIColumn1;
    this.m_nestedMetadata = paramIColumn2;
    this.m_array = paramObject;
    this.m_length = java.lang.reflect.Array.getLength(paramObject);
  }
  
  public Object createArray(long paramLong, int paramInt)
    throws ErrorException
  {
    if (0L > paramLong) {
      throw new IndexOutOfBoundsException(String.valueOf(paramLong));
    }
    int i = this.m_length;
    if (paramLong >= i)
    {
      if (isMultidimensional()) {
        return new DSISimpleArray[0];
      }
      return java.lang.reflect.Array.newInstance(this.m_array.getClass().getComponentType(), 0);
    }
    int j = (int)paramLong;
    int k = i - j;
    if ((-1L != paramInt) && (paramInt < k)) {
      k = paramInt;
    }
    Object localObject1 = this.m_array;
    if (isMultidimensional())
    {
      localObject2 = new DSISimpleArray[k];
      localObject3 = getArrayColumn();
      IColumn localIColumn = getNestedColumn();
      for (int m = 0; m < k; m++)
      {
        Object localObject4 = java.lang.reflect.Array.get(localObject1, j + m);
        localObject2[m] = new DSISimpleArray(localObject4, (IColumn)localObject3, localIColumn);
      }
      return localObject2;
    }
    Object localObject2 = localObject1.getClass().getComponentType();
    Object localObject3 = java.lang.reflect.Array.newInstance((Class)localObject2, k);
    System.arraycopy(localObject1, j, localObject3, 0, k);
    return localObject3;
  }
  
  public IColumn getBaseColumn()
  {
    if (isMultidimensional()) {
      return getArrayColumn();
    }
    return getNestedColumn();
  }
  
  protected Iterator<?> createIterator(long paramLong, int paramInt)
  {
    int i = this.m_length;
    if (paramLong >= i) {
      return Collections.emptyList().iterator();
    }
    int j = (int)paramLong;
    int k = i - j;
    if ((-1L != paramInt) && (paramInt < k)) {
      k = paramInt;
    }
    return new DSISimpleArrayIterator(this.m_array, j, k);
  }
  
  protected IColumn getArrayColumn()
  {
    return this.m_arrayMetadata;
  }
  
  protected IColumn getNestedColumn()
  {
    return this.m_nestedMetadata;
  }
  
  protected boolean isMultidimensional()
  {
    return this.m_array.getClass().getComponentType().isArray();
  }
  
  public String getStringRepresentation()
  {
    return toString();
  }
  
  protected final class DSISimpleArrayIterator
    implements Iterator<Object>
  {
    private int m_offset;
    private int m_count;
    private Object m_data;
    private boolean m_isNested;
    
    public DSISimpleArrayIterator(Object paramObject, int paramInt1, int paramInt2)
    {
      this.m_data = paramObject;
      this.m_offset = paramInt1;
      this.m_count = paramInt2;
      this.m_isNested = DSISimpleArray.this.isMultidimensional();
    }
    
    public boolean hasNext()
    {
      return 0 < this.m_count;
    }
    
    public Object next()
    {
      if (0 >= this.m_count) {
        throw new NoSuchElementException();
      }
      this.m_count -= 1;
      Object localObject = java.lang.reflect.Array.get(this.m_data, this.m_offset++);
      assert (!(localObject instanceof IArray)) : "IArray provided.";
      assert (!(localObject instanceof java.sql.Array)) : "java.sql.Array provided.";
      if (!this.m_isNested) {
        return localObject;
      }
      return new DSISimpleArray(localObject, DSISimpleArray.this.getArrayColumn(), DSISimpleArray.this.getNestedColumn());
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("remove()");
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSISimpleArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
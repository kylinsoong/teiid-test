package com.simba.sqlengine.executor.etree.relation.join;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

class ResizeableByteArrayOS
  extends OutputStream
  implements Closeable, Flushable
{
  private byte[] m_buff;
  private int m_count = 0;
  
  public ResizeableByteArrayOS()
  {
    this(32);
  }
  
  public ResizeableByteArrayOS(int paramInt)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("Negative initial size: " + paramInt);
    }
    this.m_buff = new byte[paramInt];
  }
  
  public void close() {}
  
  public void reset()
  {
    this.m_count = 0;
  }
  
  public byte[] toByteArray()
  {
    return Arrays.copyOf(this.m_buff, this.m_count);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
      throw new IndexOutOfBoundsException();
    }
    if (paramInt2 == 0) {
      return;
    }
    int i = this.m_count + paramInt2;
    if (i > this.m_buff.length) {
      this.m_buff = Arrays.copyOf(this.m_buff, Math.max(this.m_buff.length << 1, i));
    }
    System.arraycopy(paramArrayOfByte, paramInt1, this.m_buff, this.m_count, paramInt2);
    this.m_count = i;
  }
  
  public void write(int paramInt)
    throws IOException
  {
    int i = this.m_count + 1;
    if (i > this.m_buff.length) {
      this.m_buff = Arrays.copyOf(this.m_buff, Math.max(this.m_buff.length << 1, i));
    }
    this.m_buff[this.m_count] = ((byte)paramInt);
    this.m_count = i;
  }
  
  public void writeToOutputStream(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(this.m_buff, 0, this.m_count);
  }
  
  public void destroyBuffer(int paramInt)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("Negative initial size: " + paramInt);
    }
    this.m_count = 0;
    this.m_buff = new byte[paramInt];
  }
  
  public int size()
  {
    return this.m_count;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/ResizeableByteArrayOS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.streams.resultset;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.streams.IStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractInputStream
  extends InputStream
  implements IStream
{
  protected IResultSet m_result = null;
  protected int m_column = 0;
  protected int m_readOffset = 0;
  protected boolean m_moreData = true;
  protected byte[] m_buffer = new byte[0];
  protected int m_bufferOffset = 0;
  protected int m_bufferSize = 0;
  
  protected AbstractInputStream(IResultSet paramIResultSet, int paramInt1, int paramInt2)
  {
    this.m_result = paramIResultSet;
    this.m_column = paramInt1;
    this.m_bufferSize = paramInt2;
  }
  
  public abstract int available()
    throws IOException;
  
  public void close()
    throws IOException
  {
    this.m_buffer = null;
    this.m_result = null;
  }
  
  public abstract int read()
    throws IOException;
  
  protected boolean isClosed()
  {
    return null == this.m_result;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultset/AbstractInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
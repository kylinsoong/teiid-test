package com.simba.streams.resultset;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import java.io.IOException;

public class BinaryStream
  extends AbstractInputStream
{
  public BinaryStream(IResultSet paramIResultSet, int paramInt1, int paramInt2)
  {
    super(paramIResultSet, paramInt1, paramInt2);
  }
  
  public int available()
    throws IOException
  {
    if (isClosed()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CLOSED.name());
    }
    return this.m_buffer.length - this.m_bufferOffset;
  }
  
  public int read()
    throws IOException
  {
    if (isClosed()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CLOSED.name());
    }
    if (this.m_buffer.length > this.m_bufferOffset) {
      return adjustByte(this.m_buffer[(this.m_bufferOffset++)]);
    }
    if (this.m_moreData)
    {
      fetchNewData();
      if (0 == this.m_buffer.length) {
        return -1;
      }
      return adjustByte(this.m_buffer[(this.m_bufferOffset++)]);
    }
    return -1;
  }
  
  protected void fetchNewData()
    throws IOException
  {
    try
    {
      DataWrapper localDataWrapper = new DataWrapper();
      this.m_moreData = this.m_result.getData(this.m_column, this.m_readOffset, this.m_bufferSize, localDataWrapper);
      this.m_bufferOffset = 0;
      this.m_readOffset += this.m_bufferSize;
      this.m_buffer = ((byte[])localDataWrapper.getObject());
    }
    catch (Exception localException)
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_READ_ERROR.name());
    }
  }
  
  private int adjustByte(byte paramByte)
  {
    if (0 > paramByte) {
      return paramByte + 256;
    }
    return paramByte;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultset/BinaryStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
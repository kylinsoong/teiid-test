package com.simba.streams.resultset;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import java.io.IOException;

public abstract class AbstractCharacterStream
  extends AbstractInputStream
{
  private final String m_charSet;
  
  protected AbstractCharacterStream(IResultSet paramIResultSet, int paramInt1, String paramString, int paramInt2)
  {
    super(paramIResultSet, paramInt1, paramInt2);
    this.m_charSet = paramString;
  }
  
  public int available()
    throws IOException
  {
    if (isClosed()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CLOSED.name());
    }
    return 0;
  }
  
  public int read()
    throws IOException
  {
    if (isClosed()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CLOSED.name());
    }
    if (this.m_buffer.length <= this.m_bufferOffset)
    {
      if (!this.m_moreData) {
        return -1;
      }
      fetchNewData();
      if (0 == this.m_buffer.length) {
        return -1;
      }
    }
    return this.m_buffer[(this.m_bufferOffset++)];
  }
  
  private void fetchNewData()
    throws IOException
  {
    try
    {
      DataWrapper localDataWrapper = new DataWrapper();
      this.m_moreData = this.m_result.getData(this.m_column, this.m_readOffset, this.m_bufferSize, localDataWrapper);
      this.m_bufferOffset = 0;
      this.m_readOffset += this.m_bufferSize;
      this.m_buffer = ((String)localDataWrapper.getObject()).getBytes(this.m_charSet);
      if ((1 < this.m_buffer.length) && (this.m_charSet.startsWith("UTF-16")) && (((-1 == this.m_buffer[0]) && (-2 == this.m_buffer[1])) || ((-2 == this.m_buffer[0]) && (-1 == this.m_buffer[1])))) {
        this.m_bufferOffset = 2;
      }
    }
    catch (Exception localException)
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_READ_ERROR.name());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultset/AbstractCharacterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
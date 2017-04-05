package com.simba.streams.resultsetinput;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ExceptionType;
import java.io.InputStream;
import java.sql.SQLException;

public abstract class AbstractDataStream
{
  protected static final int MAX_FETCH_SIZE = 4000;
  protected static final int UNKNOWN_SIZE = -1;
  protected int m_numRead = 0;
  protected long m_streamLength = 0L;
  protected long m_lastChunkReadSize = 0L;
  protected InputStream m_stream = null;
  protected int m_type;
  protected IWarningListener m_warningListener = null;
  
  protected AbstractDataStream(InputStream paramInputStream, long paramLong, int paramInt, IWarningListener paramIWarningListener)
  {
    this.m_stream = paramInputStream;
    this.m_streamLength = paramLong;
    this.m_type = paramInt;
    this.m_warningListener = paramIWarningListener;
  }
  
  public void close()
  {
    try
    {
      this.m_stream.close();
    }
    catch (Exception localException) {}
    this.m_stream = null;
  }
  
  public abstract Pair<DataWrapper, Long> getNextValue()
    throws SQLException;
  
  public boolean hasMoreData()
    throws SQLException
  {
    if (isClosed()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_CLOSED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    if (-1L != this.m_streamLength) {
      return this.m_numRead < this.m_streamLength;
    }
    return -1L != this.m_lastChunkReadSize;
  }
  
  protected int getNumToFetch()
  {
    if (-1L != this.m_streamLength)
    {
      long l = this.m_streamLength - this.m_numRead;
      return (int)Math.min(4000L, l);
    }
    return 4000;
  }
  
  protected boolean isClosed()
  {
    return null == this.m_stream;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultsetinput/AbstractDataStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
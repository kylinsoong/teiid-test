package com.simba.streams.resultsetinput;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ExceptionType;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

public class CharacterDataStream
  extends AbstractDataStream
{
  private Reader m_reader = null;
  
  public CharacterDataStream(Reader paramReader, long paramLong, int paramInt, IWarningListener paramIWarningListener)
  {
    super(null, paramLong, paramInt, paramIWarningListener);
    this.m_reader = paramReader;
  }
  
  public void close()
  {
    super.close();
    if (!isClosed())
    {
      try
      {
        this.m_reader.close();
      }
      catch (Exception localException) {}
      this.m_reader = null;
    }
  }
  
  public Pair<DataWrapper, Long> getNextValue()
    throws SQLException
  {
    if (isClosed()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_CLOSED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    if (!hasMoreData()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_EMPTY, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    int i = getNumToFetch();
    StringBuffer localStringBuffer = new StringBuffer();
    int j = 0;
    int k = Integer.MAX_VALUE;
    while (((-1L == this.m_streamLength) && (-1 != k)) || ((-1L != this.m_streamLength) && (j + this.m_numRead < i)))
    {
      try
      {
        k = this.m_reader.read();
      }
      catch (IOException localIOException)
      {
        throw ExceptionConverter.getInstance().toSQLException(localIOException, this.m_warningListener);
      }
      if (((-1L == this.m_streamLength) && (-1 != k)) || (-1L != this.m_streamLength))
      {
        localStringBuffer.append((char)k);
        j++;
      }
    }
    if (-1 == k)
    {
      if (-1L != this.m_streamLength) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_UNEXPECTED_END, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      this.m_lastChunkReadSize = -1L;
    }
    else
    {
      this.m_lastChunkReadSize = j;
    }
    this.m_numRead += j;
    DataWrapper localDataWrapper = new DataWrapper();
    try
    {
      if (12 == this.m_type) {
        localDataWrapper.setVarChar(localStringBuffer.toString());
      } else {
        localDataWrapper.setLongVarChar(localStringBuffer.toString());
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_CORRUPT_UTF, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    return new Pair(localDataWrapper, Long.valueOf(j * 2));
  }
  
  protected boolean isClosed()
  {
    return null == this.m_reader;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultsetinput/CharacterDataStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
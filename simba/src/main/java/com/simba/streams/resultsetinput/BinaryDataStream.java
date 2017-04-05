package com.simba.streams.resultsetinput;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ExceptionType;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class BinaryDataStream
  extends AbstractDataStream
{
  public BinaryDataStream(InputStream paramInputStream, long paramLong, int paramInt, IWarningListener paramIWarningListener)
  {
    super(paramInputStream, paramLong, paramInt, paramIWarningListener);
  }
  
  public Pair<DataWrapper, Long> getNextValue()
    throws SQLException
  {
    if (isClosed()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_CLOSED, this.m_warningListener, ExceptionType.DATA, new Object[0]);
    }
    if (!hasMoreData()) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_EMPTY, this.m_warningListener, ExceptionType.DATA, new Object[0]);
    }
    int i = getNumToFetch();
    Object localObject1 = new byte[i];
    int j = 0;
    try
    {
      j = this.m_stream.read((byte[])localObject1, 0, i);
    }
    catch (IOException localIOException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localIOException, this.m_warningListener);
    }
    this.m_numRead += j;
    if (-1L != this.m_streamLength)
    {
      if (-1 == j) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.STREAM_UNEXPECTED_END, this.m_warningListener, ExceptionType.DATA, new Object[0]);
      }
      localObject2 = new DataWrapper();
      if (-3 == this.m_type) {
        ((DataWrapper)localObject2).setVarBinary((byte[])localObject1);
      } else {
        ((DataWrapper)localObject2).setLongVarBinary((byte[])localObject1);
      }
      return new Pair(localObject2, Long.valueOf(j));
    }
    this.m_lastChunkReadSize = j;
    if ((j < localObject1.length) && (-1 != j))
    {
      localObject2 = new byte[j];
      System.arraycopy(localObject1, 0, localObject2, 0, j);
      localObject1 = localObject2;
      this.m_lastChunkReadSize = -1L;
    }
    Object localObject2 = new DataWrapper();
    if (-3 == this.m_type) {
      ((DataWrapper)localObject2).setVarBinary((byte[])localObject1);
    } else {
      ((DataWrapper)localObject2).setLongVarBinary((byte[])localObject1);
    }
    return new Pair(localObject2, Long.valueOf(j));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultsetinput/BinaryDataStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
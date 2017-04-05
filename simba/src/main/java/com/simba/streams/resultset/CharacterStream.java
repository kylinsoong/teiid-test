package com.simba.streams.resultset;

import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.streams.IStream;
import java.io.IOException;
import java.io.Reader;

public class CharacterStream
  extends Reader
  implements IStream
{
  private IResultSet m_result = null;
  private int m_column = 0;
  private int m_resultSetOffset = 0;
  private boolean m_hasMoreData = true;
  private int m_stringOffset = 0;
  private String m_stringBuffer = "";
  private int m_bufferSize = 0;
  private DataWrapper m_data = new DataWrapper();
  
  public CharacterStream(IResultSet paramIResultSet, int paramInt1, int paramInt2)
  {
    this.m_result = paramIResultSet;
    this.m_column = paramInt1;
    this.m_bufferSize = paramInt2;
  }
  
  public void close()
    throws IOException
  {
    this.m_result = null;
    this.m_stringBuffer = null;
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (null == paramArrayOfChar) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_INVALID_BUFFER.name());
    }
    if ((0 > paramInt1) || ((paramInt1 != 0) && (paramArrayOfChar.length <= paramInt1))) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_INVALID_OFFSET.name(), new String[] { String.valueOf(paramInt1), String.valueOf(paramArrayOfChar.length) });
    }
    int i = 0;
    int j = Math.min(paramArrayOfChar.length - paramInt1, paramInt2);
    do
    {
      if ((this.m_stringOffset >= this.m_stringBuffer.length()) && (!fetchMoreData()))
      {
        if (i == 0) {
          return -1;
        }
        return i;
      }
      paramArrayOfChar[(i++ + paramInt1)] = this.m_stringBuffer.charAt(this.m_stringOffset++);
    } while (i < j);
    return i;
  }
  
  private boolean fetchMoreData()
    throws InputOutputException
  {
    if (!this.m_hasMoreData) {
      return false;
    }
    try
    {
      this.m_hasMoreData = this.m_result.getData(this.m_column, this.m_resultSetOffset, this.m_bufferSize, this.m_data);
      this.m_stringOffset = 0;
      this.m_resultSetOffset += this.m_bufferSize;
      this.m_stringBuffer = ((String)this.m_data.getObject());
    }
    catch (Exception localException)
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_READ_ERROR.name());
    }
    return 0 != this.m_stringBuffer.length();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultset/CharacterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
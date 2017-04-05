package com.simba.streams.parameters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import java.io.IOException;
import java.io.InputStream;

public class UnicodeParameterStream
  extends AbstractParameterStream
{
  public UnicodeParameterStream(InputStream paramInputStream, int paramInt)
  {
    super(paramInputStream, paramInt);
  }
  
  @Deprecated
  public UnicodeParameterStream(InputStream paramInputStream, int paramInt1, ParameterMetadata paramParameterMetadata, int paramInt2)
  {
    super(paramInputStream, paramInt1, paramParameterMetadata, paramInt2);
  }
  
  public ParameterInputValue getNextValue()
    throws IOException
  {
    if (isClosed()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CLOSED.name());
    }
    if (!hasMoreData()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_EMPTY.name());
    }
    if (null == this.m_parameterMetadata) {
      throw new InputOutputException(1, JDBCMessageKey.NULL_PARAM_METADATA.name());
    }
    if (!this.m_valuesPushed) {
      this.m_valuesPushed = true;
    }
    int i = getNumToFetch();
    Object localObject = new byte[i];
    int j = this.m_stream.read((byte[])localObject);
    this.m_numRead += j;
    DataWrapper localDataWrapper = new DataWrapper();
    if (-1L != this.m_streamLength)
    {
      if (-1 == j) {
        throw new InputOutputException(1, JDBCMessageKey.STREAM_UNEXPECTED_END.name());
      }
    }
    else
    {
      this.m_lastChunkReadSize = j;
      if ((j < localObject.length) && (-1 != j))
      {
        byte[] arrayOfByte = new byte[j];
        System.arraycopy(localObject, 0, arrayOfByte, 0, j);
        localObject = arrayOfByte;
        this.m_lastChunkReadSize = -1L;
      }
      else if (-1 == j)
      {
        localObject = new byte[0];
      }
    }
    try
    {
      if (12 == this.m_parameterMetadata.getTypeMetadata().getType()) {
        localDataWrapper.setVarChar(new String((byte[])localObject, "UTF-16"));
      } else {
        localDataWrapper.setLongVarChar(new String((byte[])localObject, "UTF-16"));
      }
    }
    catch (Exception localException)
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CORRUPT_UTF.name());
    }
    return new ParameterInputValue(this.m_parameterMetadata, localDataWrapper);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/parameters/UnicodeParameterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
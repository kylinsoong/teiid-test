package com.simba.streams.parameters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import java.io.IOException;
import java.io.InputStream;

public class BinaryParameterStream
  extends AbstractParameterStream
{
  public BinaryParameterStream(InputStream paramInputStream, long paramLong)
  {
    super(paramInputStream, paramLong);
  }
  
  @Deprecated
  public BinaryParameterStream(InputStream paramInputStream, long paramLong, ParameterMetadata paramParameterMetadata, int paramInt)
  {
    super(paramInputStream, paramLong, paramParameterMetadata, paramInt);
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
    Object localObject1 = new byte[i];
    int j = 0;
    try
    {
      j = this.m_stream.read((byte[])localObject1, 0, i);
    }
    catch (IOException localIOException)
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_UNEXPECTED_END.name());
    }
    this.m_numRead += j;
    if (-1L != this.m_streamLength)
    {
      if (-1 == j) {
        throw new InputOutputException(1, JDBCMessageKey.STREAM_UNEXPECTED_END.name());
      }
    }
    else
    {
      this.m_lastChunkReadSize = j;
      if ((j < localObject1.length) && (-1 != j))
      {
        localObject2 = new byte[j];
        System.arraycopy(localObject1, 0, localObject2, 0, j);
        localObject1 = localObject2;
        this.m_lastChunkReadSize = -1L;
      }
      else if (-1 == j)
      {
        localObject1 = new byte[0];
      }
    }
    Object localObject2 = new DataWrapper();
    if (-3 == this.m_parameterMetadata.getTypeMetadata().getType()) {
      ((DataWrapper)localObject2).setVarBinary((byte[])localObject1);
    } else {
      ((DataWrapper)localObject2).setLongVarBinary((byte[])localObject1);
    }
    return new ParameterInputValue(this.m_parameterMetadata, (DataWrapper)localObject2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/parameters/BinaryParameterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
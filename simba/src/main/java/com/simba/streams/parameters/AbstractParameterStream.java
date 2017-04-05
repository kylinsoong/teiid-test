package com.simba.streams.parameters;

import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.utilities.ReferenceEqualityWrapper;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractParameterStream
{
  protected static final int MAX_FETCH_SIZE = 4000;
  protected static final int UNKNOWN_SIZE = -1;
  protected ParameterMetadata m_parameterMetadata = null;
  protected long m_numRead = 0L;
  protected long m_lastChunkReadSize = 0L;
  protected long m_streamLength = 0L;
  protected InputStream m_stream = null;
  protected ReferenceEqualityWrapper m_streamWrapper = null;
  @Deprecated
  protected int m_type;
  protected boolean m_valuesPushed = false;
  
  protected AbstractParameterStream(InputStream paramInputStream, long paramLong)
  {
    this.m_stream = paramInputStream;
    this.m_streamWrapper = new ReferenceEqualityWrapper(this.m_stream);
    this.m_streamLength = paramLong;
  }
  
  @Deprecated
  protected AbstractParameterStream(InputStream paramInputStream, long paramLong, ParameterMetadata paramParameterMetadata, int paramInt)
  {
    this.m_stream = paramInputStream;
    this.m_streamWrapper = new ReferenceEqualityWrapper(this.m_stream);
    this.m_streamLength = paramLong;
    this.m_parameterMetadata = paramParameterMetadata;
    this.m_type = paramInt;
  }
  
  public void close()
  {
    if (null != this.m_stream)
    {
      try
      {
        this.m_stream.close();
      }
      catch (Exception localException) {}
      this.m_stream = null;
    }
    this.m_parameterMetadata = null;
  }
  
  public ParameterMetadata getMetadata()
  {
    return this.m_parameterMetadata;
  }
  
  public abstract ParameterInputValue getNextValue()
    throws IOException;
  
  public boolean hasMoreData()
    throws IOException
  {
    if (isClosed()) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CLOSED.name());
    }
    if (-1L != this.m_streamLength) {
      return this.m_numRead < this.m_streamLength;
    }
    return this.m_lastChunkReadSize != -1L;
  }
  
  public ReferenceEqualityWrapper getEqualityStreamWrapper()
  {
    return this.m_streamWrapper;
  }
  
  public void setParameterMetadata(ParameterMetadata paramParameterMetadata)
    throws InputOutputException
  {
    if (!this.m_valuesPushed)
    {
      this.m_parameterMetadata = paramParameterMetadata;
      this.m_type = this.m_parameterMetadata.getTypeMetadata().getType();
    }
    else
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_METADATA_SET_ERROR.name());
    }
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/parameters/AbstractParameterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
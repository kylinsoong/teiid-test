package com.simba.streams.parameters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.ParameterInputValue;
import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.utilities.ReferenceEqualityWrapper;
import java.io.IOException;
import java.io.Reader;

public class CharacterParameterStream
  extends AbstractParameterStream
{
  private Reader m_reader = null;
  
  public CharacterParameterStream(Reader paramReader, long paramLong)
  {
    super(null, paramLong);
    this.m_reader = paramReader;
    this.m_streamWrapper = new ReferenceEqualityWrapper(this.m_reader);
  }
  
  @Deprecated
  public CharacterParameterStream(Reader paramReader, long paramLong, ParameterMetadata paramParameterMetadata, int paramInt)
  {
    super(null, paramLong, paramParameterMetadata, paramInt);
    this.m_reader = paramReader;
    this.m_streamWrapper = new ReferenceEqualityWrapper(this.m_reader);
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
    Object localObject = new char[i];
    int j = this.m_reader.read((char[])localObject);
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
        char[] arrayOfChar = new char[j];
        System.arraycopy(localObject, 0, arrayOfChar, 0, j);
        localObject = arrayOfChar;
        this.m_lastChunkReadSize = -1L;
      }
      else if (-1 == j)
      {
        localObject = new char[0];
      }
    }
    try
    {
      if (12 == this.m_parameterMetadata.getTypeMetadata().getType()) {
        localDataWrapper.setVarChar(new String((char[])localObject));
      } else {
        localDataWrapper.setLongVarChar(new String((char[])localObject));
      }
    }
    catch (Exception localException)
    {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_CORRUPT_UTF.name());
    }
    return new ParameterInputValue(this.m_parameterMetadata, localDataWrapper);
  }
  
  protected boolean isClosed()
  {
    return null == this.m_reader;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/parameters/CharacterParameterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
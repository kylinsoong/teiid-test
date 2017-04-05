package com.simba.streams.parametersoutput;

import com.simba.dsi.exceptions.InputOutputException;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.streams.IStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CharacterParameterDataStream
  extends InputStreamReader
  implements IStream
{
  public CharacterParameterDataStream(byte[] paramArrayOfByte)
    throws UnsupportedEncodingException
  {
    super(new ByteArrayInputStream(paramArrayOfByte), "UTF-16");
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (null == paramArrayOfChar) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_INVALID_BUFFER.name());
    }
    if ((0 > paramInt1) || (paramArrayOfChar.length < paramInt1)) {
      throw new InputOutputException(1, JDBCMessageKey.STREAM_INVALID_OFFSET.name(), new String[] { String.valueOf(paramInt1), String.valueOf(paramArrayOfChar.length) });
    }
    int i = 0;
    int j = paramArrayOfChar.length - paramInt1;
    for (i = 0; (i < j) && (i < paramInt2); i++)
    {
      int k = super.read();
      if (-1 == k) {
        return -1;
      }
      paramArrayOfChar[(i + paramInt1)] = ((char)k);
    }
    return i;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/parametersoutput/CharacterParameterDataStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
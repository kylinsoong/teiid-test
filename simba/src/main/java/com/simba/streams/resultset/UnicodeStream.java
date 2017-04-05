package com.simba.streams.resultset;

import com.simba.dsi.dataengine.interfaces.IResultSet;

public class UnicodeStream
  extends AbstractCharacterStream
{
  public UnicodeStream(IResultSet paramIResultSet, int paramInt1, int paramInt2)
  {
    super(paramIResultSet, paramInt1, "UTF-16", paramInt2);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/resultset/UnicodeStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
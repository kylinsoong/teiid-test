package com.simba.streams;

import java.io.IOException;

public abstract interface IStream
{
  @Deprecated
  public static final int MAX_FETCH_SIZE = 4000;
  public static final int NO_MORE_DATA = -1;
  
  public abstract void close()
    throws IOException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/streams/IStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
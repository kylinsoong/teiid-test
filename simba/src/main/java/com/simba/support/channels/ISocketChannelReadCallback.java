package com.simba.support.channels;

import com.simba.support.exceptions.ErrorException;
import java.nio.ByteBuffer;

public abstract interface ISocketChannelReadCallback
{
  public abstract void read(ByteBuffer paramByteBuffer)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/channels/ISocketChannelReadCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
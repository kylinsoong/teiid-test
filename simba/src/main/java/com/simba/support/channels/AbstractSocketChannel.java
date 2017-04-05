package com.simba.support.channels;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSocketChannel
  implements Closeable
{
  protected static final ExceptionBuilder EXCEPTION_BUILDER = new ExceptionBuilder(8);
  protected static final int SERVER_CLOSED_CONNECTION = -1;
  protected final SocketChannel m_internal;
  protected final ILogger m_log;
  private final AtomicBoolean m_closed = new AtomicBoolean(false);
  protected final ISocketChannelReadCallback m_readCallback;
  
  public AbstractSocketChannel(SocketChannel paramSocketChannel, ISocketChannelReadCallback paramISocketChannelReadCallback, ILogger paramILogger)
    throws ErrorException
  {
    this.m_internal = paramSocketChannel;
    this.m_readCallback = paramISocketChannelReadCallback;
    this.m_log = paramILogger;
  }
  
  public SocketChannel getSocketChannel()
  {
    return this.m_internal;
  }
  
  public abstract int getReadBufferCapacity();
  
  public void close()
  {
    LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    try
    {
      this.m_closed.set(true);
      this.m_internal.close();
    }
    catch (IOException localIOException)
    {
      LogUtilities.logFatal(localIOException, this.m_log);
    }
  }
  
  public boolean isClosed()
  {
    LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    return this.m_closed.get();
  }
  
  public abstract boolean write(ByteBuffer paramByteBuffer)
    throws ErrorException;
  
  public abstract void read()
    throws ErrorException;
  
  protected void readCallback(ByteBuffer paramByteBuffer)
    throws ErrorException
  {
    paramByteBuffer.flip();
    paramByteBuffer.rewind();
    this.m_readCallback.read(paramByteBuffer);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/channels/AbstractSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
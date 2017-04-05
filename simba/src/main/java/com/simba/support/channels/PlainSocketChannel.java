package com.simba.support.channels;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class PlainSocketChannel
  extends AbstractSocketChannel
{
  protected static final int READ_BUFFER_CAPACITY = 512000;
  private ByteBuffer m_readBuffer = ByteBuffer.allocateDirect(512000);
  
  public PlainSocketChannel(SocketChannel paramSocketChannel, ISocketChannelReadCallback paramISocketChannelReadCallback, ILogger paramILogger)
    throws ErrorException
  {
    super(paramSocketChannel, paramISocketChannelReadCallback, paramILogger);
    this.m_readBuffer.order(ByteOrder.BIG_ENDIAN);
  }
  
  public boolean write(ByteBuffer paramByteBuffer)
    throws ErrorException
  {
    if ((null == this.m_internal) || (isClosed())) {
      throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name());
    }
    int i = 0;
    while (paramByteBuffer.hasRemaining()) {
      try
      {
        i = this.m_internal.write(paramByteBuffer);
        if (this.m_log.isEnabled()) {
          LogUtilities.logDebug("Writing " + i + " bytes", this.m_log);
        }
        if (0 == i) {
          return true;
        }
      }
      catch (IOException localIOException)
      {
        if (this.m_log.isEnabled()) {
          LogUtilities.logDebug(localIOException, this.m_log);
        }
        String str = localIOException.getMessage();
        if (null != str) {
          throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), str, localIOException);
        }
        throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_SERVER_CLOSED.name(), localIOException);
      }
    }
    return true;
  }
  
  public void read()
    throws ErrorException
  {
    int i = -2;
    do
    {
      try
      {
        i = this.m_internal.read(this.m_readBuffer);
      }
      catch (IOException localIOException)
      {
        LogUtilities.logFatal(localIOException, this.m_log);
        String str = localIOException.getMessage();
        if (null != str) {
          throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), str, localIOException);
        }
        throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_SERVER_CLOSED.name(), localIOException);
      }
      if (this.m_log.isEnabled()) {
        LogUtilities.logDebug("Bytes read from channel: " + i, this.m_log);
      }
      if (-1 == i)
      {
        readCallback(this.m_readBuffer);
        throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_SERVER_CLOSED.name());
      }
    } while (i > 0);
    readCallback(this.m_readBuffer);
  }
  
  public int getReadBufferCapacity()
  {
    return 512000;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/channels/PlainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.support.channels;

import com.simba.support.ILogger;
import com.simba.support.LogLevel;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.SocketChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class TLSSocketChannel
  extends AbstractSocketChannel
{
  private static final String PROTOCOL_SSL = "SSL";
  private static final int UNFLIPPED_STATE = -1;
  private SSLContext m_sslContext;
  private SSLEngine m_sslEngine;
  private SSLSession m_sslSession;
  private ByteBuffer m_sendEncryptedBuffer;
  private ByteBuffer m_recvEncryptedBuffer;
  private ByteBuffer m_recvClearTextBuffer;
  private ByteBuffer m_sendHandshakeClearTextBuffer;
  private ByteBuffer m_recvHandshakeClearTextBuffer;
  private SSLWriteEngineLock m_sslengineWriteLock = new SSLWriteEngineLock(null);
  private SSLReadEngineLock m_sslengineReadLock = new SSLReadEngineLock(null);
  
  public TLSSocketChannel(SocketChannel paramSocketChannel, ISocketChannelReadCallback paramISocketChannelReadCallback, String paramString, int paramInt, IHostNameValidator paramIHostNameValidator, boolean paramBoolean, KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager, ILogger paramILogger)
    throws ErrorException
  {
    super(paramSocketChannel, paramISocketChannelReadCallback, paramILogger);
    System.setProperty("com.sun.net.ssl.rsaPreMasterSecretFix", "true");
    try
    {
      this.m_sslContext = SSLContext.getInstance("TLS");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      LogUtilities.logError(localNoSuchAlgorithmException, this.m_log);
      throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localNoSuchAlgorithmException.getMessage(), localNoSuchAlgorithmException);
    }
    try
    {
      this.m_sslContext.init(paramArrayOfKeyManager, paramArrayOfTrustManager, new SecureRandom());
    }
    catch (KeyManagementException localKeyManagementException)
    {
      LogUtilities.logFatal(localKeyManagementException, this.m_log);
      throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localKeyManagementException.getMessage(), localKeyManagementException);
    }
    this.m_sslEngine = this.m_sslContext.createSSLEngine(paramString, paramInt);
    if (paramBoolean) {
      paramIHostNameValidator.enableHostnameValidation(this.m_sslEngine, this.m_log);
    }
    this.m_sslEngine.setEnabledProtocols(removeSSLProtocols(this.m_sslEngine.getEnabledProtocols()));
    this.m_sslEngine.setUseClientMode(true);
    this.m_sslSession = this.m_sslEngine.getSession();
    this.m_recvEncryptedBuffer = ByteBuffer.allocateDirect(this.m_sslSession.getPacketBufferSize());
    this.m_recvClearTextBuffer = ByteBuffer.allocateDirect(this.m_sslSession.getApplicationBufferSize());
    this.m_sendEncryptedBuffer = ByteBuffer.allocateDirect(this.m_sslSession.getPacketBufferSize());
    this.m_sendHandshakeClearTextBuffer = ByteBuffer.allocateDirect(this.m_sslSession.getApplicationBufferSize());
    this.m_recvHandshakeClearTextBuffer = ByteBuffer.allocateDirect(this.m_sslSession.getApplicationBufferSize());
    this.m_recvEncryptedBuffer.order(ByteOrder.BIG_ENDIAN);
    this.m_recvClearTextBuffer.order(ByteOrder.BIG_ENDIAN);
    this.m_sendEncryptedBuffer.order(ByteOrder.BIG_ENDIAN);
    doHandshake();
  }
  
  private String[] removeSSLProtocols(String[] paramArrayOfString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Input protocols = '");
    ArrayList localArrayList = new ArrayList();
    for (String str2 : paramArrayOfString)
    {
      localStringBuffer.append(str2);
      localStringBuffer.append(",");
      if ((null != str2) && (!str2.toUpperCase().contains("SSL"))) {
        localArrayList.add(str2);
      }
    }
    localStringBuffer.append("', enabled protocols = '");
    ??? = localArrayList.iterator();
    while (((Iterator)???).hasNext())
    {
      String str1 = (String)((Iterator)???).next();
      localStringBuffer.append(str1);
      localStringBuffer.append(",");
    }
    localStringBuffer.append("'");
    LogUtilities.logDebug(localStringBuffer.toString(), this.m_log);
    return (String[])localArrayList.toArray(new String[localArrayList.size()]);
  }
  
  public synchronized void read()
    throws ErrorException
  {
    SSLEngineResult localSSLEngineResult = null;
    int i = -1;
    int j = -2;
    synchronized (this.m_sslengineReadLock)
    {
      do
      {
        do
        {
          try
          {
            j = this.m_internal.read(this.m_recvEncryptedBuffer);
          }
          catch (IOException localIOException)
          {
            LogUtilities.logError(localIOException, this.m_log);
            throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localIOException.getMessage(), localIOException);
          }
          if (this.m_log.isEnabled()) {
            LogUtilities.logDebug("Bytes read from ssl channel: " + j, this.m_log);
          }
          if (-1 == j) {
            throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_SERVER_CLOSED.name());
          }
          do
          {
            try
            {
              i = this.m_recvEncryptedBuffer.position();
              this.m_recvEncryptedBuffer.flip();
              localSSLEngineResult = this.m_sslEngine.unwrap(this.m_recvEncryptedBuffer, this.m_recvClearTextBuffer);
              if (LogUtilities.shouldLogLevel(LogLevel.DEBUG, this.m_log)) {
                LogUtilities.logDebug("ReadMessages:UNWRAP:Consume:" + localSSLEngineResult.bytesConsumed() + ":Produced:" + localSSLEngineResult.bytesProduced() + ":Position:" + this.m_recvEncryptedBuffer.position(), this.m_log);
              }
            }
            catch (SSLProtocolException localSSLProtocolException)
            {
              LogUtilities.logFatal(localSSLProtocolException, this.m_log);
              throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localSSLProtocolException.getMessage(), localSSLProtocolException);
            }
            catch (SSLException localSSLException)
            {
              LogUtilities.logFatal(localSSLException, this.m_log);
              throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localSSLException.getMessage(), localSSLException);
            }
            catch (ReadOnlyBufferException localReadOnlyBufferException)
            {
              LogUtilities.logFatal(localReadOnlyBufferException, this.m_log);
              throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localReadOnlyBufferException.getMessage(), localReadOnlyBufferException);
            }
            catch (IllegalArgumentException localIllegalArgumentException)
            {
              LogUtilities.logFatal(localIllegalArgumentException, this.m_log);
              throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localIllegalArgumentException.getMessage(), localIllegalArgumentException);
            }
            catch (IllegalStateException localIllegalStateException)
            {
              LogUtilities.logFatal(localIllegalStateException, this.m_log);
              throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localIllegalStateException.getMessage(), localIllegalStateException);
            }
            if (null != localSSLEngineResult) {
              switch (localSSLEngineResult.getStatus())
              {
              case OK: 
                LogUtilities.logDebug("ReadMessages:OK:" + localSSLEngineResult.getHandshakeStatus(), this.m_log);
                switch (localSSLEngineResult.getHandshakeStatus())
                {
                case NEED_TASK: 
                case NEED_UNWRAP: 
                case NEED_WRAP: 
                  boolean bool = LogUtilities.shouldLogLevel(LogLevel.DEBUG, this.m_log);
                  if (bool) {
                    LogUtilities.logDebug("Handshake request detected: " + localSSLEngineResult.getHandshakeStatus() + ":EncReadBuffer.remaining():" + this.m_recvEncryptedBuffer.remaining() + ":ClearTextBuffer.remaining():" + this.m_recvClearTextBuffer.remaining(), this.m_log);
                  }
                  try
                  {
                    if (bool) {
                      LogUtilities.logDebug("ReadMessages:OK:NEED_TASK:PreReadMessages:Before:Flip:0:EncHSRecvBuffer.remaining:" + this.m_recvEncryptedBuffer.remaining() + ":EncHSRecvBuffer.pos:" + this.m_recvEncryptedBuffer.position() + ":EncHSRecvBuffer.limit:" + this.m_recvEncryptedBuffer.limit(), this.m_log);
                    }
                    this.m_recvEncryptedBuffer.clear();
                    if (bool) {
                      LogUtilities.logDebug("ReadMessages:OK:NEED_TASK:BEGINNING:0:EncHSRecvBuffer.remaining:" + this.m_recvEncryptedBuffer.remaining() + ":EncHSRecvBuffer.pos:" + this.m_recvEncryptedBuffer.position() + ":EncHSRecvBuffer.limit:" + this.m_recvEncryptedBuffer.limit(), this.m_log);
                    }
                    doHandshake();
                  }
                  catch (Exception localException)
                  {
                    LogUtilities.logError(localException, this.m_log);
                    throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localException.getMessage(), localException);
                  }
                case FINISHED: 
                case NOT_HANDSHAKING: 
                default: 
                  i = -1;
                  readCallback(this.m_recvClearTextBuffer);
                  if (this.m_recvEncryptedBuffer.hasRemaining()) {
                    this.m_recvEncryptedBuffer.compact();
                  } else {
                    this.m_recvEncryptedBuffer.clear();
                  }
                  break;
                }
                break;
              case BUFFER_OVERFLOW: 
                LogUtilities.logDebug("ReadMessages:BUFFER_OVERFLOW", this.m_log);
                if (i != -1)
                {
                  this.m_recvEncryptedBuffer.position(i);
                  this.m_recvEncryptedBuffer.limit(this.m_recvEncryptedBuffer.capacity());
                }
                this.m_recvClearTextBuffer = resizeBuffer(this.m_recvClearTextBuffer, this.m_sslEngine.getSession().getApplicationBufferSize());
                break;
              case BUFFER_UNDERFLOW: 
                LogUtilities.logDebug("ReadMessages:BUFFER_UNDERFLOW", this.m_log);
                if (i != -1)
                {
                  this.m_recvEncryptedBuffer.position(i);
                  this.m_recvEncryptedBuffer.limit(this.m_recvEncryptedBuffer.capacity());
                }
                break;
              case CLOSED: 
                LogUtilities.logDebug("ReadMessages:CLOSED", this.m_log);
              }
            } else {
              LogUtilities.logDebug("ReadMessages:Result was null but there was no exception.", this.m_log);
            }
          } while ((this.m_recvEncryptedBuffer.position() > 0) && (null != localSSLEngineResult) && (localSSLEngineResult.getStatus() == SSLEngineResult.Status.OK));
        } while (null == localSSLEngineResult);
        if (null == localSSLEngineResult) {
          break;
        }
      } while ((localSSLEngineResult.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) || (localSSLEngineResult.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW));
    }
  }
  
  public boolean write(ByteBuffer paramByteBuffer)
    throws ErrorException
  {
    if (null == this.m_sendEncryptedBuffer)
    {
      this.m_sendEncryptedBuffer = ByteBuffer.allocateDirect(this.m_sslSession.getPacketBufferSize());
      this.m_sendEncryptedBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    synchronized (this.m_sslengineWriteLock)
    {
      try
      {
        if (!writeEncryptedBuffer()) {
          return false;
        }
        if (!paramByteBuffer.hasRemaining()) {
          return true;
        }
        this.m_sendEncryptedBuffer.clear();
        while (paramByteBuffer.hasRemaining())
        {
          SSLEngineResult localSSLEngineResult = null;
          if (this.m_log.isEnabled()) {
            LogUtilities.logDebug("WriteMessages:WRAP:BEFORE:0:writeBuffer.remaining:" + paramByteBuffer.remaining() + ":writeBuffer.pos:" + paramByteBuffer.position() + ":writeBuffer.limit:" + paramByteBuffer.limit(), this.m_log);
          }
          localSSLEngineResult = this.m_sslEngine.wrap(paramByteBuffer, this.m_sendEncryptedBuffer);
          if (this.m_log.isEnabled()) {
            LogUtilities.logDebug("WriteMessages:WRAP:AFTER:0:writeBuffer.remaining:" + paramByteBuffer.remaining() + ":writeBuffer.pos:" + paramByteBuffer.position() + ":writeBuffer.limit:" + paramByteBuffer.limit() + ":produced:" + localSSLEngineResult.bytesProduced() + ":consumed:" + localSSLEngineResult.bytesConsumed(), this.m_log);
          }
          this.m_sendEncryptedBuffer.flip();
          switch (localSSLEngineResult.getStatus())
          {
          case OK: 
            if (this.m_log.isEnabled()) {
              LogUtilities.logDebug("WriteMessages:OK:" + localSSLEngineResult.getHandshakeStatus(), this.m_log);
            }
            switch (localSSLEngineResult.getHandshakeStatus())
            {
            case NEED_TASK: 
            case NEED_UNWRAP: 
            case NEED_WRAP: 
              try
              {
                if (this.m_log.isEnabled()) {
                  LogUtilities.logDebug("WriteMessages:OK:NEED_TASK:BEGINNING:0:EncHSRecvBuffer.remaining:" + this.m_recvEncryptedBuffer.remaining() + ":EncHSRecvBuffer.pos:" + this.m_recvEncryptedBuffer.position() + ":EncHSRecvBuffer.limit:" + this.m_recvEncryptedBuffer.limit() + ":TempEncHSRecvBuffer.remaining:", this.m_log);
                }
                doHandshake();
                this.m_sendEncryptedBuffer.flip();
                this.m_sendEncryptedBuffer.limit(this.m_sendEncryptedBuffer.capacity());
              }
              catch (Exception localException)
              {
                LogUtilities.logError(localException, this.m_log);
                throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localException.getMessage(), localException);
              }
            case FINISHED: 
            case NOT_HANDSHAKING: 
            default: 
              if (!writeEncryptedBuffer()) {
                return false;
              }
              if (this.m_log.isEnabled()) {
                LogUtilities.logDebug("Wrote Messages - " + this.m_sslEngine.getHandshakeStatus(), this.m_log);
              }
              break;
            }
            break;
          case BUFFER_OVERFLOW: 
            if (this.m_log.isEnabled()) {
              LogUtilities.logDebug("WriteMessages:BUFFER_OVERFLOW:Expanding to " + this.m_sslEngine.getSession().getPacketBufferSize() + " bytes.", this.m_log);
            }
            this.m_sendEncryptedBuffer = resizeBuffer(this.m_sendEncryptedBuffer, this.m_sslEngine.getSession().getPacketBufferSize());
            break;
          case BUFFER_UNDERFLOW: 
            if (this.m_log.isEnabled()) {
              LogUtilities.logDebug("WriteMessages:BUFFER_UNDERFLOW:Should not happen", this.m_log);
            }
            break;
          case CLOSED: 
            LogUtilities.logDebug("WriteMessages:CLOSED", this.m_log);
            return true;
          }
        }
      }
      catch (IOException localIOException)
      {
        LogUtilities.logError(localIOException, this.m_log);
        throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localIOException.getMessage(), localIOException);
      }
    }
    return true;
  }
  
  private boolean writeEncryptedBuffer()
    throws IOException
  {
    while (this.m_sendEncryptedBuffer.hasRemaining())
    {
      int i = this.m_internal.write(this.m_sendEncryptedBuffer);
      if (-1 == i)
      {
        if (this.m_log.isEnabled()) {
          LogUtilities.logDebug("WriteMessages:OK:Closed", this.m_log);
        }
        return true;
      }
      if (0 == i)
      {
        if (this.m_log.isEnabled()) {
          LogUtilities.logDebug("WriteMessages:OK:0 Bytes Written", this.m_log);
        }
        return false;
      }
      if (this.m_log.isEnabled()) {
        LogUtilities.logDebug("WriteMessages:OK:" + i + " bytes sent.", this.m_log);
      }
    }
    return true;
  }
  
  public void close()
  {
    try
    {
      this.m_recvClearTextBuffer.flip();
      SSLEngineResult localSSLEngineResult = this.m_sslEngine.unwrap(this.m_recvClearTextBuffer, this.m_recvEncryptedBuffer);
      if (localSSLEngineResult.getStatus() != SSLEngineResult.Status.CLOSED) {
        this.m_sslEngine.closeOutbound();
      }
    }
    catch (Exception localException)
    {
      LogUtilities.logError(localException, this.m_log);
    }
    super.close();
  }
  
  public int getReadBufferCapacity()
  {
    return this.m_recvEncryptedBuffer.capacity();
  }
  
  private void doHandshake()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
    int i = -1;
    int j = 0;
    int k = 0;
    logHandshakeAction("BEGIN:", i, j, k);
    try
    {
      this.m_sslEngine.beginHandshake();
    }
    catch (SSLException localSSLException1)
    {
      LogUtilities.logError(localSSLException1, this.m_log);
      throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localSSLException1.getMessage(), localSSLException1);
    }
    k = j = 0;
    i = readBytes("BEGIN:");
    SSLEngineResult.HandshakeStatus localHandshakeStatus1 = this.m_sslEngine.getHandshakeStatus();
    for (SSLEngineResult.HandshakeStatus localHandshakeStatus2 = localHandshakeStatus1; (SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING != localHandshakeStatus2) && (SSLEngineResult.HandshakeStatus.FINISHED != localHandshakeStatus2); localHandshakeStatus2 = localHandshakeStatus1)
    {
      SSLEngineResult localSSLEngineResult = null;
      SSLEngineResult.Status localStatus = null;
      SSLEngineResult.HandshakeStatus localHandshakeStatus3 = null;
      switch (localHandshakeStatus1)
      {
      case NEED_UNWRAP: 
        logHandshakeAction("NEED_UNWRAP:BEFORE:UNWRAP:", i, j, k);
        try
        {
          localSSLEngineResult = this.m_sslEngine.unwrap(this.m_recvEncryptedBuffer, this.m_recvHandshakeClearTextBuffer);
        }
        catch (SSLException localSSLException2)
        {
          LogUtilities.logError("Handshake count: 0", this.m_log);
          LogUtilities.logError(localSSLException2, this.m_log);
          throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localSSLException2.getMessage(), localSSLException2);
        }
        j += localSSLEngineResult.bytesConsumed();
        k += localSSLEngineResult.bytesProduced();
        localHandshakeStatus3 = localSSLEngineResult.getHandshakeStatus();
        localStatus = localSSLEngineResult.getStatus();
        LogUtilities.logDebug("\nHandshake:NEED_UNWRAP:UNWRAP:RESULT: UnwrapStatus:" + localStatus + " - UnwrapHandshakeStatus:" + localHandshakeStatus3 + "\n\tBytesConsumed:" + localSSLEngineResult.bytesConsumed() + "\n\tBytesProduced:" + localSSLEngineResult.bytesProduced(), this.m_log);
        logHandshakeAction("NEED_UNWRAP:AFTER:UNWRAP:", i, j, k);
        switch (localStatus)
        {
        case OK: 
          LogUtilities.logDebug("\nHandshake:NEED_UNWRAP:OK", this.m_log);
          break;
        case BUFFER_OVERFLOW: 
          LogUtilities.logDebug("\nHandshake:NEED_UNWRAP:BUFFER_OVERFLOW", this.m_log);
          this.m_recvHandshakeClearTextBuffer = resizeBuffer(this.m_recvHandshakeClearTextBuffer, this.m_sslSession.getApplicationBufferSize());
          break;
        case BUFFER_UNDERFLOW: 
          logHandshakeAction("NEED_UNWRAP:BUFFER_UNDERFLOW:BEFORE:", i, j, k);
          if ((this.m_recvEncryptedBuffer.remaining() == 0) && (this.m_recvEncryptedBuffer.position() == this.m_recvEncryptedBuffer.limit()))
          {
            LogUtilities.logDebug("\nBuffer has been consumed", this.m_log);
            this.m_recvEncryptedBuffer.clear();
            i = -1;
          }
          else
          {
            LogUtilities.logDebug("\nBuffer has NOT been consumed. Remaining : " + this.m_recvEncryptedBuffer.remaining(), this.m_log);
          }
          if ((this.m_recvEncryptedBuffer.remaining() > 0) && (this.m_recvEncryptedBuffer.remaining() != this.m_recvEncryptedBuffer.capacity())) {
            compact("NEED_UNWRAP:BUFFER_UNDERFLOW", i, j, k);
          }
          logHandshakeAction("NEED_UNWRAP:BUFFER_UNDERFLOW:BEFORE:READ:", i, j, k);
          k = j = 0;
          i = readBytes("NEED_UNWRAP:BUFFER_UNDERFLOW");
          break;
        case CLOSED: 
          LogUtilities.logDebug("Handshake:NEED_UNWRAP:CLOSED", this.m_log);
        }
        break;
      case NEED_WRAP: 
        LogUtilities.logDebug("\nHandshake:NEED_WRAP", this.m_log);
        this.m_sendEncryptedBuffer.clear();
        localSSLEngineResult = null;
        try
        {
          LogUtilities.logDebug("\nHandshake:NEED_WRAP:WRAP", this.m_log);
          localSSLEngineResult = this.m_sslEngine.wrap(this.m_sendHandshakeClearTextBuffer, this.m_sendEncryptedBuffer);
          j += localSSLEngineResult.bytesConsumed();
          k += localSSLEngineResult.bytesProduced();
          localHandshakeStatus3 = localSSLEngineResult.getHandshakeStatus();
          localStatus = localSSLEngineResult.getStatus();
          if (this.m_log.isEnabled()) {
            LogUtilities.logDebug("\nHandshake:NEED_WRAP:WRAP:RESULT: WrapStatus:" + localStatus + " - WrapHandshakeStatus:" + localHandshakeStatus3 + "\n\tBytesConsumed:" + localSSLEngineResult.bytesConsumed() + "\n\tBytesProduced:" + localSSLEngineResult.bytesProduced(), this.m_log);
          }
        }
        catch (SSLException localSSLException3)
        {
          LogUtilities.logError(localSSLException3, this.m_log);
          throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localSSLException3.getMessage(), localSSLException3);
        }
        switch (localSSLEngineResult.getStatus())
        {
        case OK: 
          flip("NEED_WRAP:OK", i, j, k, false);
          int m = 0;
          int n = m;
          LogUtilities.logDebug("NEED_WRAP:OK:BEFORE:WRITE:", this.m_log);
          while (this.m_sendEncryptedBuffer.hasRemaining()) {
            try
            {
              n = this.m_internal.write(this.m_sendEncryptedBuffer);
              m += n;
              if (this.m_log.isEnabled())
              {
                if (n < 0) {
                  LogUtilities.logDebug("\nHandshake:NEED_WRAP:OK:0B written, conn closed.", this.m_log);
                }
                LogUtilities.logDebug("\nHandshake:NEED_WRAP:OK:WRITE\n\tWritten: " + n + " bytes." + "\n\tTotal Written: " + m + " bytes.", this.m_log);
              }
            }
            catch (IOException localIOException)
            {
              LogUtilities.logError(localIOException, this.m_log);
              throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localIOException.getMessage(), localIOException);
            }
          }
          if (this.m_log.isEnabled()) {
            LogUtilities.logDebug("\nHandshake:NEED_WRAP:OK:AFTER:WRITE\n\tFinal Total Written: " + m + " bytes total.", this.m_log);
          }
          break;
        case BUFFER_OVERFLOW: 
          LogUtilities.logDebug("\nHandshake:NEED_WRAP:BUFFER_OVERFLOW", this.m_log);
          break;
        case BUFFER_UNDERFLOW: 
          LogUtilities.logDebug("\nHandshake:NEED_WRAP:BUFFER_UNDERFLOW", this.m_log);
          break;
        case CLOSED: 
          LogUtilities.logDebug("\nHandshake:NEED_WRAP:CLOSED", this.m_log);
        }
        break;
      case NEED_TASK: 
        needTask();
        break;
      case FINISHED: 
        LogUtilities.logDebug("\nHandshake:FINISHED", this.m_log);
        break;
      case NOT_HANDSHAKING: 
        LogUtilities.logDebug("\nHandshake:NOT_HANDSHAKING", this.m_log);
      }
      localHandshakeStatus1 = this.m_sslEngine.getHandshakeStatus();
      if (this.m_log.isEnabled())
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("\n");
        localStringBuilder.append(localHandshakeStatus2);
        localStringBuilder.append(":SSL_ENGINE_STATUS: ");
        localStringBuilder.append("UpdatedHandshakeStatus: ");
        localStringBuilder.append(localHandshakeStatus3);
        localStringBuilder.append(" - EngineHandshakeStatus: ");
        localStringBuilder.append(localHandshakeStatus1);
        if ((null != localHandshakeStatus3) && (localHandshakeStatus3 != localHandshakeStatus1)) {
          localStringBuilder.append(" !!!!!!!!!!!!!!!");
        }
        LogUtilities.logDebug(localStringBuilder.toString(), this.m_log);
      }
    }
    if (i == -1)
    {
      LogUtilities.logDebug("\nBuffer has been consumed", this.m_log);
      this.m_recvEncryptedBuffer.compact();
    }
    else
    {
      this.m_recvEncryptedBuffer.position(j);
      this.m_recvEncryptedBuffer.limit(i);
      this.m_recvEncryptedBuffer.compact();
    }
    logHandshakeAction("FINSIHED:", i, j, k);
    LogUtilities.logDebug("\nHandshake: All done.", this.m_log);
  }
  
  private ByteBuffer resizeBuffer(ByteBuffer paramByteBuffer, int paramInt)
  {
    if (paramInt > paramByteBuffer.remaining())
    {
      if (LogUtilities.shouldLogLevel(LogLevel.DEBUG, this.m_log)) {
        LogUtilities.logDebug("Buffer resize - Remaining: " + paramByteBuffer.remaining() + ", Limit: " + paramByteBuffer.limit() + ", SSLEngine.???Size: " + paramInt + ", new size will be: " + (paramInt + paramByteBuffer.limit()), this.m_log);
      }
      ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(paramInt + paramByteBuffer.limit());
      paramByteBuffer.flip();
      localByteBuffer.put(paramByteBuffer);
      return localByteBuffer;
    }
    return paramByteBuffer;
  }
  
  private void logHandshakeAction(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    if (LogUtilities.shouldLogLevel(LogLevel.DEBUG, this.m_log))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("\nHandshake:");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\n\tRead:");
      localStringBuilder.append(paramInt1);
      if ((paramInt1 != -1) && (paramInt1 < paramInt2)) {
        localStringBuilder.append(" !!");
      }
      localStringBuilder.append("\n\tConsumed Since Read:");
      localStringBuilder.append(paramInt2);
      if ((paramInt1 != -1) && (paramInt1 < paramInt2)) {
        localStringBuilder.append(" !!");
      }
      localStringBuilder.append("\n\tProduced Since Read:");
      localStringBuilder.append(paramInt3);
      localStringBuilder.append("\n\tEncHSRecvBuffer.remaining:");
      localStringBuilder.append(this.m_recvEncryptedBuffer.remaining());
      localStringBuilder.append("\n\tEncHSRecvBuffer.pos:");
      localStringBuilder.append(this.m_recvEncryptedBuffer.position());
      localStringBuilder.append("\n\tEncHSRecvBuffer.limit:");
      localStringBuilder.append(this.m_recvEncryptedBuffer.limit());
      localStringBuilder.append("\n\tEncHSRecvBuffer.capacity:");
      localStringBuilder.append(this.m_recvEncryptedBuffer.capacity());
      localStringBuilder.append("\n\tClearHSRecvBuffer.remaining:");
      localStringBuilder.append(this.m_recvHandshakeClearTextBuffer.remaining());
      localStringBuilder.append("\n\tClearHSRecvBuffer.pos:");
      localStringBuilder.append(this.m_recvHandshakeClearTextBuffer.position());
      localStringBuilder.append("\n\tCLearHSRecvBuffer.limit:");
      localStringBuilder.append(this.m_recvHandshakeClearTextBuffer.limit());
      localStringBuilder.append("\n\tCLearHSRecvBuffer.capacity:");
      localStringBuilder.append(this.m_recvHandshakeClearTextBuffer.capacity());
      LogUtilities.logDebug(localStringBuilder.toString(), this.m_log);
    }
  }
  
  private void compact(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    logHandshakeAction(paramString + ":BEFORE:COMPACT:", paramInt1, paramInt2, paramInt3);
    this.m_recvEncryptedBuffer.compact();
    logHandshakeAction(paramString + ":AFTER:COMPACT:", paramInt1, paramInt2, paramInt3);
  }
  
  private void flip(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      logHandshakeAction(paramString + ":BEFORE:FLIP:", paramInt1, paramInt2, paramInt3);
      this.m_recvEncryptedBuffer.flip();
      logHandshakeAction(paramString + ":AFTER:FLIP:", paramInt1, paramInt2, paramInt3);
    }
    else
    {
      this.m_sendEncryptedBuffer.flip();
    }
  }
  
  private void needTask()
  {
    LogUtilities.logDebug("\nHandshake:NEED_TASK", this.m_log);
    Runnable localRunnable;
    while ((localRunnable = this.m_sslEngine.getDelegatedTask()) != null)
    {
      LogUtilities.logDebug("\nHandshake:NEED_TASK:Starting delegate task.", this.m_log);
      localRunnable.run();
    }
  }
  
  private int readBytes(String paramString)
    throws ErrorException
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    try
    {
      do
      {
        i = j = 0;
        k = this.m_internal.read(this.m_recvEncryptedBuffer);
        m += k;
        if (this.m_log.isEnabled()) {
          LogUtilities.logDebug("\nHandshake:" + paramString + ":READ:" + "\n\tRead: " + k + " bytes." + "\n\tTotal Read: " + m + " bytes.", this.m_log);
        }
      } while (k > 0);
    }
    catch (IOException localIOException)
    {
      LogUtilities.logError("Handshake count: 0", this.m_log);
      LogUtilities.logError(localIOException, this.m_log);
      throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_GENERAL_ERR.name(), localIOException.getMessage(), localIOException);
    }
    if (this.m_log.isEnabled()) {
      LogUtilities.logDebug("\nHandshake:" + paramString + ":AFTER:READ:" + "\n\tFinal Total Read: " + m + " bytes total.", this.m_log);
    }
    if (m < 0)
    {
      if (this.m_log.isEnabled()) {
        LogUtilities.logDebug("Handshake count: 0", this.m_log);
      }
      throw EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.CHANNEL_SERVER_CLOSED.name());
    }
    flip(paramString, m, j, i, true);
    return m;
  }
  
  private class SSLWriteEngineLock
  {
    private SSLWriteEngineLock() {}
  }
  
  private class SSLReadEngineLock
  {
    private SSLReadEngineLock() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/channels/TLSSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
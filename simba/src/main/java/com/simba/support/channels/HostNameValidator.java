package com.simba.support.channels;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

public final class HostNameValidator
  implements IHostNameValidator
{
  public void enableHostnameValidation(SSLEngine paramSSLEngine, ILogger paramILogger)
    throws ErrorException
  {
    try
    {
      SSLParameters localSSLParameters = new SSLParameters();
      Method localMethod = localSSLParameters.getClass().getMethod("setEndpointIdentificationAlgorithm", new Class[] { String.class });
      localMethod.invoke(localSSLParameters, new Object[] { "HTTPS" });
      paramSSLEngine.setSSLParameters(localSSLParameters);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      LogUtilities.logError(localNoSuchMethodException, paramILogger);
      throw AbstractSocketChannel.EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.TLS_HOSTNAME_VALIDATION_ERR_JVM_NOT_CAPABLE.name(), localNoSuchMethodException.getMessage(), localNoSuchMethodException);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      LogUtilities.logError(localIllegalArgumentException, paramILogger);
      throw AbstractSocketChannel.EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.TLS_HOSTNAME_VALIDATION_ERR_JVM_NOT_CAPABLE.name(), localIllegalArgumentException.getMessage(), localIllegalArgumentException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      LogUtilities.logError(localIllegalAccessException, paramILogger);
      throw AbstractSocketChannel.EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.TLS_HOSTNAME_VERIFICATION_ERR_JVM_PREVENTING_ACCESS.name(), localIllegalAccessException.getMessage(), localIllegalAccessException);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      LogUtilities.logError(localInvocationTargetException, paramILogger);
      throw AbstractSocketChannel.EXCEPTION_BUILDER.createGeneralException(SocketChannelMessageKey.TLS_HOSTNAME_VERIFICATION_ERR.name(), localInvocationTargetException.getMessage(), localInvocationTargetException);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/channels/HostNameValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
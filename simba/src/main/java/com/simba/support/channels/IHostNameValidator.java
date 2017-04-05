package com.simba.support.channels;

import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import javax.net.ssl.SSLEngine;

public abstract interface IHostNameValidator
{
  public abstract void enableHostnameValidation(SSLEngine paramSSLEngine, ILogger paramILogger)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/channels/IHostNameValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.support.security;

import com.simba.support.ObjectWrapper;

public abstract interface ICredentialFactory
{
  public abstract ICredentials getLocalCredentials();
  
  public abstract ICredentials getAuthenticatedCredentials(byte[] paramArrayOfByte, ObjectWrapper<byte[]> paramObjectWrapper)
    throws Exception;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/ICredentialFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
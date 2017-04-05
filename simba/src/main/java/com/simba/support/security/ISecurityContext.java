package com.simba.support.security;

public abstract interface ISecurityContext
{
  public abstract byte[] getToken()
    throws Exception;
  
  public abstract byte[] updateToken(byte[] paramArrayOfByte)
    throws Exception;
  
  public abstract void setRequestMutualAuthentication(boolean paramBoolean)
    throws Exception;
  
  public abstract boolean getRequestMutualAuthentication();
  
  public abstract void setRequestDelegation(boolean paramBoolean)
    throws Exception;
  
  public abstract boolean getRequestDelegation();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/ISecurityContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
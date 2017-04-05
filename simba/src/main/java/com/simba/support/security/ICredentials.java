package com.simba.support.security;

import java.security.PrivilegedAction;

public abstract interface ICredentials
{
  public abstract ISecurityContext getSecurityContext(String paramString)
    throws Exception;
  
  public abstract String getName()
    throws Exception;
  
  public abstract Object executeAs(PrivilegedAction paramPrivilegedAction)
    throws Exception;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/ICredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
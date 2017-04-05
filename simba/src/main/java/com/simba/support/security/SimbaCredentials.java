package com.simba.support.security;

import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

abstract class SimbaCredentials
  implements ICredentials
{
  abstract GSSCredential getCredentialHandle()
    throws GSSException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/SimbaCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
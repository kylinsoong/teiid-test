package com.simba.support.security;

import com.simba.support.ILogger;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import sun.security.jgss.GSSUtil;

class SimbaAuthenticatedCredentials
  extends SimbaCredentials
{
  private final ILogger m_logger = null;
  private final GSSContext m_remoteContext;
  private final GSSManager m_gssManager;
  private Subject m_subject = null;
  
  public SimbaAuthenticatedCredentials(ILogger paramILogger, GSSManager paramGSSManager, GSSContext paramGSSContext)
  {
    this.m_remoteContext = paramGSSContext;
    this.m_gssManager = paramGSSManager;
  }
  
  GSSCredential getCredentialHandle()
    throws GSSException
  {
    return this.m_remoteContext.getDelegCred();
  }
  
  public ISecurityContext getSecurityContext(String paramString)
    throws Exception
  {
    return new SimbaSecurityContext(this, this.m_logger, paramString, this.m_gssManager);
  }
  
  public String getName()
    throws Exception
  {
    return this.m_remoteContext.getSrcName().toString();
  }
  
  public Object executeAs(PrivilegedAction paramPrivilegedAction)
    throws Exception
  {
    if (null == this.m_subject)
    {
      GSSCredential localGSSCredential;
      try
      {
        localGSSCredential = this.m_remoteContext.getDelegCred();
      }
      catch (GSSException localGSSException)
      {
        localGSSCredential = null;
      }
      this.m_subject = GSSUtil.getSubject(this.m_remoteContext.getSrcName(), localGSSCredential);
    }
    return Subject.doAsPrivileged(this.m_subject, paramPrivilegedAction, null);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/SimbaAuthenticatedCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
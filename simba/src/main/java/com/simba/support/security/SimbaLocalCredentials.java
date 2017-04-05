package com.simba.support.security;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import sun.security.jgss.GSSUtil;

class SimbaLocalCredentials
  extends SimbaCredentials
{
  private final boolean m_inbound;
  private final GSSManager m_manager;
  private final ILogger m_logger;
  private GSSCredential m_credential;
  private Subject m_subject;
  
  SimbaLocalCredentials(ILogger paramILogger, GSSManager paramGSSManager, boolean paramBoolean)
  {
    this.m_inbound = paramBoolean;
    this.m_manager = paramGSSManager;
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramILogger, paramGSSManager });
  }
  
  GSSCredential getCredentialHandle()
    throws GSSException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null == this.m_credential) {
      DelayAcquireCredentials();
    }
    return this.m_credential;
  }
  
  public ISecurityContext getSecurityContext(String paramString)
    throws GSSException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString });
    return new SimbaSecurityContext(this, this.m_logger, paramString, this.m_manager);
  }
  
  public String getName()
    throws GSSException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null == this.m_credential) {
      DelayAcquireCredentials();
    }
    return this.m_credential.getName().toString();
  }
  
  public Object executeAs(PrivilegedAction paramPrivilegedAction)
    throws Exception
  {
    if (null == this.m_subject) {
      this.m_subject = GSSUtil.getSubject(this.m_credential.getName(), this.m_credential);
    }
    return Subject.doAsPrivileged(this.m_subject, paramPrivilegedAction, null);
  }
  
  private void DelayAcquireCredentials()
    throws GSSException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    this.m_credential = this.m_manager.createCredential(this.m_inbound ? 2 : 1);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/SimbaLocalCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
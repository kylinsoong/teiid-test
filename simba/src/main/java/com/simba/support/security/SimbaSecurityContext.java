package com.simba.support.security;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

class SimbaSecurityContext
  implements ISecurityContext
{
  private final SimbaCredentials m_credentials;
  private final GSSManager m_manager;
  private final String m_spn;
  private final ILogger m_logger;
  private GSSContext m_context;
  private static Oid GSS_KRB5_MECH_OID;
  private boolean m_requestMutualAuthentication = false;
  private boolean m_requestDelegation = false;
  
  public SimbaSecurityContext(SimbaCredentials paramSimbaCredentials, ILogger paramILogger, String paramString, GSSManager paramGSSManager)
    throws GSSException
  {
    if (null == GSS_KRB5_MECH_OID) {
      GSS_KRB5_MECH_OID = new Oid("1.2.840.113554.1.2.2");
    }
    this.m_manager = paramGSSManager;
    this.m_credentials = paramSimbaCredentials;
    this.m_spn = paramString;
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSimbaCredentials, paramILogger, paramString, paramGSSManager });
  }
  
  public byte[] getToken()
    throws GSSException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return updateToken(null);
  }
  
  public byte[] updateToken(byte[] paramArrayOfByte)
    throws GSSException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramArrayOfByte });
    if (null == paramArrayOfByte)
    {
      GSSName localGSSName = this.m_manager.createName(this.m_spn, null, GSS_KRB5_MECH_OID);
      this.m_context = this.m_manager.createContext(localGSSName, GSS_KRB5_MECH_OID, this.m_credentials.getCredentialHandle(), 0);
      this.m_context.requestMutualAuth(this.m_requestMutualAuthentication);
      this.m_context.requestCredDeleg(this.m_requestDelegation);
      paramArrayOfByte = new byte[0];
    }
    return this.m_context.initSecContext(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void setRequestMutualAuthentication(boolean paramBoolean)
    throws Exception
  {
    if (null != this.m_context) {
      this.m_context.requestMutualAuth(paramBoolean);
    }
    this.m_requestMutualAuthentication = paramBoolean;
  }
  
  public boolean getRequestMutualAuthentication()
  {
    return this.m_requestMutualAuthentication;
  }
  
  public void setRequestDelegation(boolean paramBoolean)
    throws Exception
  {
    if (null != this.m_context) {
      this.m_context.requestCredDeleg(paramBoolean);
    }
    this.m_requestDelegation = paramBoolean;
  }
  
  public boolean getRequestDelegation()
  {
    return this.m_requestDelegation;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/SimbaSecurityContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
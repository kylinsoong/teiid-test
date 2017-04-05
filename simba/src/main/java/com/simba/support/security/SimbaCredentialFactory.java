package com.simba.support.security;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.ObjectWrapper;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSManager;

public class SimbaCredentialFactory
  implements ICredentialFactory
{
  private GSSManager m_gssManager = GSSManager.getInstance();
  private final ILogger m_logger;
  private SimbaLocalCredentials m_acceptorCredentials = null;
  private GSSContext m_remoteContext = null;
  
  public SimbaCredentialFactory(ILogger paramILogger)
  {
    this.m_logger = paramILogger;
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
  }
  
  public ICredentials getLocalCredentials()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    return new SimbaLocalCredentials(this.m_logger, this.m_gssManager, false);
  }
  
  public ICredentials getAuthenticatedCredentials(byte[] paramArrayOfByte, ObjectWrapper<byte[]> paramObjectWrapper)
    throws Exception
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null == paramArrayOfByte) {
      paramArrayOfByte = new byte[0];
    }
    if (null == this.m_acceptorCredentials)
    {
      this.m_acceptorCredentials = new SimbaLocalCredentials(this.m_logger, this.m_gssManager, true);
      this.m_remoteContext = this.m_gssManager.createContext(this.m_acceptorCredentials.getCredentialHandle());
      this.m_remoteContext.requestCredDeleg(true);
    }
    paramObjectWrapper.setValue(this.m_remoteContext.acceptSecContext(paramArrayOfByte, 0, paramArrayOfByte.length));
    if (this.m_remoteContext.isEstablished()) {
      return new SimbaAuthenticatedCredentials(this.m_logger, this.m_gssManager, this.m_remoteContext);
    }
    return null;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/SimbaCredentialFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
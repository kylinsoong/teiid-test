package com.simba.support.security;

import java.security.PrivilegedAction;

public class CppPrivilegedAction
  implements PrivilegedAction<Long>
{
  private final long m_actionPointer;
  
  private static final native long executeAction(long paramLong);
  
  CppPrivilegedAction(long paramLong)
  {
    this.m_actionPointer = paramLong;
  }
  
  public Long run()
  {
    return Long.valueOf(executeAction(this.m_actionPointer));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/security/CppPrivilegedAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
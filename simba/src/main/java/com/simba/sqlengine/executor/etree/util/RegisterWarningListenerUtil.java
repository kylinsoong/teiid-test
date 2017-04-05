package com.simba.sqlengine.executor.etree.util;

import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.support.IWarningListener;

public class RegisterWarningListenerUtil
{
  public static void registerWarningListener(IWarningListener paramIWarningListener, IETNode paramIETNode)
  {
    ETWalker.walk(paramIETNode, new ETWalker.Action()
    {
      public void act(IETNode paramAnonymousIETNode)
      {
        if ((paramAnonymousIETNode instanceof IWarningSource)) {
          ((IWarningSource)paramAnonymousIETNode).registerWarningListener(this.val$warningListener);
        }
      }
      
      public Void getResult()
      {
        return null;
      }
    });
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/util/RegisterWarningListenerUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
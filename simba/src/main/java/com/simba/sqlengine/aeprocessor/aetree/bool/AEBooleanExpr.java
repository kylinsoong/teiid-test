package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.support.exceptions.ErrorException;

public abstract class AEBooleanExpr
  implements IAENode
{
  private boolean m_isOptimized = false;
  
  public abstract AEBooleanExpr copy();
  
  public abstract AEBooleanType getType();
  
  public boolean isOptimized()
  {
    return this.m_isOptimized;
  }
  
  public void setIsOptimized(boolean paramBoolean)
  {
    this.m_isOptimized = paramBoolean;
  }
  
  public void updateCoercion()
    throws ErrorException
  {}
  
  public static enum AEBooleanType
  {
    AND,  OR,  NOT,  COMPARISON,  IN_PRED,  LIKE_PRED,  NULL_PRED,  BOOLEAN_TRUE,  EXISTS_PRED,  QUANITIFIED_COMPARISON;
    
    private AEBooleanType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEBooleanExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
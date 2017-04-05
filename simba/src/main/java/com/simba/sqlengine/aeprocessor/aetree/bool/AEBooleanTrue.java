package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AEBooleanTrue
  extends AEBooleanExpr
{
  private IAENode m_parent;
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return Collections.emptyList().iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public IAENode getParent()
  {
    return this.m_parent;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    if (null == paramIAENode) {
      throw new NullPointerException("Parent cannot be null.");
    }
    this.m_parent = paramIAENode;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    return (this == paramIAENode) || ((paramIAENode instanceof AEBooleanTrue));
  }
  
  public AEBooleanExpr copy()
  {
    return new AEBooleanTrue();
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.BOOLEAN_TRUE;
  }
  
  public boolean isOptimized()
  {
    return true;
  }
  
  public void setIsOptimized(boolean paramBoolean) {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEBooleanTrue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
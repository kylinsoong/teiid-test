package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AEDistinct
  extends AEUnaryRelationalExpr
{
  private static final int NUM_CHILDREN = 1;
  
  public AEDistinct(AERelationalExpr paramAERelationalExpr)
  {
    super(paramAERelationalExpr);
  }
  
  private AEDistinct(AEDistinct paramAEDistinct)
  {
    super(paramAEDistinct);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AEDistinct)) {
      return false;
    }
    return getOperand().isEquivalent(((AEDistinct)paramIAENode).getOperand());
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public AEDistinct copy()
  {
    return new AEDistinct(this);
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 != paramInt) {
      throw new IndexOutOfBoundsException("There is no child at index: " + paramInt);
    }
    return getOperand();
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    getOperand().setDataNeededOnChild();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEDistinct.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
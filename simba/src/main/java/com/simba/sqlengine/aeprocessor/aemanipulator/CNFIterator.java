package com.simba.sqlengine.aeprocessor.aemanipulator;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import java.util.Iterator;

public class CNFIterator
  implements Iterator<AEBooleanExpr>
{
  AEBooleanExpr m_curNode;
  AEBooleanExpr m_root;
  AEBooleanExpr m_prev;
  
  public CNFIterator(AEBooleanExpr paramAEBooleanExpr)
  {
    assert (paramAEBooleanExpr != null);
    this.m_root = paramAEBooleanExpr;
    this.m_curNode = leftMost(paramAEBooleanExpr);
    this.m_prev = null;
  }
  
  public boolean hasNext()
  {
    return this.m_curNode != null;
  }
  
  public AEBooleanExpr next()
  {
    this.m_prev = this.m_curNode;
    findNext();
    return this.m_prev;
  }
  
  public void remove()
  {
    if (this.m_prev == this.m_root)
    {
      this.m_prev = null;
      this.m_root = null;
      return;
    }
    if (this.m_prev == null) {
      throw new IllegalStateException("Iterator call sequence error.");
    }
    AEAnd localAEAnd1 = (AEAnd)this.m_prev.getParent();
    IAENode localIAENode = localAEAnd1.getParent();
    if (localIAENode == this.m_root.getParent())
    {
      assert (localAEAnd1 == this.m_root);
      if (isLeftChild(localAEAnd1, this.m_prev)) {
        this.m_root = localAEAnd1.getRightOperand();
      } else {
        this.m_root = localAEAnd1.getLeftOperand();
      }
      return;
    }
    AEAnd localAEAnd2 = (AEAnd)localIAENode;
    AEBooleanExpr localAEBooleanExpr = isLeftChild(localAEAnd1, this.m_prev) ? localAEAnd1.getRightOperand() : localAEAnd1.getLeftOperand();
    if (isLeftChild(localAEAnd2, localAEAnd1)) {
      localAEAnd2.setLeftOperand(localAEBooleanExpr);
    } else {
      localAEAnd2.setRightOperand(localAEBooleanExpr);
    }
    this.m_prev = null;
  }
  
  public AEBooleanExpr getExpr()
  {
    return this.m_root;
  }
  
  private AEBooleanExpr leftMost(AEBooleanExpr paramAEBooleanExpr)
  {
    while ((paramAEBooleanExpr instanceof AEAnd)) {
      paramAEBooleanExpr = ((AEAnd)paramAEBooleanExpr).getLeftOperand();
    }
    return paramAEBooleanExpr;
  }
  
  private void findNext()
  {
    IAENode localIAENode = this.m_curNode.getParent();
    Object localObject = this.m_curNode;
    this.m_curNode = null;
    while (this.m_root.getParent() != localIAENode)
    {
      assert ((localIAENode instanceof AEAnd));
      AEAnd localAEAnd = (AEAnd)localIAENode;
      if (isLeftChild(localAEAnd, (AEBooleanExpr)localObject))
      {
        this.m_curNode = leftMost(localAEAnd.getRightOperand());
        return;
      }
      localObject = localAEAnd;
      localIAENode = localAEAnd.getParent();
    }
  }
  
  private boolean isLeftChild(AEAnd paramAEAnd, AEBooleanExpr paramAEBooleanExpr)
  {
    return paramAEAnd.getLeftOperand() == paramAEBooleanExpr;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aemanipulator/CNFIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.sqlengine.aeprocessor.aetree.AbstractAENodeList;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public class AEValueExprList
  extends AbstractAENodeList<AEValueExpr>
{
  public AEValueExprList(AEValueExprList paramAEValueExprList)
  {
    super(paramAEValueExprList);
  }
  
  public AEValueExprList() {}
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    if (paramIAENodeVisitor == null) {
      throw new NullPointerException("null visitor is not acceptable.");
    }
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEValueExprList copy()
  {
    return new AEValueExprList(this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEValueExprList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
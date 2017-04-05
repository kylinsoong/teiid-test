package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.InvalidOperationException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class NullRejectVisitor
  extends AEDefaultVisitor<Set<AERelationalExpr>>
{
  private boolean m_isInNot = false;
  
  public Set<AERelationalExpr> visit(AEAnd paramAEAnd)
    throws ErrorException
  {
    Set localSet = (Set)paramAEAnd.getLeftOperand().acceptVisitor(this);
    localSet.addAll((Collection)paramAEAnd.getRightOperand().acceptVisitor(this));
    return localSet;
  }
  
  public Set<AERelationalExpr> visit(AEOr paramAEOr)
    throws ErrorException
  {
    Set localSet = (Set)paramAEOr.getLeftOperand().acceptVisitor(this);
    localSet.retainAll((Collection)paramAEOr.getRightOperand().acceptVisitor(this));
    return localSet;
  }
  
  public Set<AERelationalExpr> visit(AEComparison paramAEComparison)
    throws ErrorException
  {
    Set localSet = (Set)paramAEComparison.getLeftOperand().acceptVisitor(NullExpressionVisitor.instance());
    localSet.addAll((Collection)paramAEComparison.getRightOperand().acceptVisitor(NullExpressionVisitor.instance()));
    return localSet;
  }
  
  public Set<AERelationalExpr> visit(AEInPredicate paramAEInPredicate)
    throws ErrorException
  {
    return (Set)paramAEInPredicate.getLeftOperand().acceptVisitor(NullExpressionVisitor.instance());
  }
  
  public Set<AERelationalExpr> visit(AELikePredicate paramAELikePredicate)
    throws ErrorException
  {
    return (Set)paramAELikePredicate.getLeftOperand().acceptVisitor(NullExpressionVisitor.instance());
  }
  
  public Set<AERelationalExpr> visit(AEQuantifiedComparison paramAEQuantifiedComparison)
    throws ErrorException
  {
    return (Set)paramAEQuantifiedComparison.getLeftOperand().acceptVisitor(NullExpressionVisitor.instance());
  }
  
  public Set<AERelationalExpr> visit(AENot paramAENot)
    throws ErrorException
  {
    if (!this.m_isInNot)
    {
      this.m_isInNot = true;
      try
      {
        AEBooleanExpr localAEBooleanExpr = paramAENot.getOperand();
        if ((localAEBooleanExpr instanceof AENullPredicate))
        {
          localSet = (Set)((AENullPredicate)localAEBooleanExpr).getOperand().acceptVisitor(NullExpressionVisitor.instance());
          return localSet;
        }
        Set localSet = (Set)localAEBooleanExpr.acceptVisitor(this);
        return localSet;
      }
      finally
      {
        this.m_isInNot = false;
      }
    }
    return new HashSet();
  }
  
  protected Set<AERelationalExpr> defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    if ((paramIAENode instanceof AEBooleanExpr)) {
      return new HashSet();
    }
    throw new InvalidOperationException(7, SQLEngineMessageKey.INVALID_OPERATION.name(), new String[] { paramIAENode.toString() });
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/NullRejectVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEBinaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEUnaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.support.exceptions.ErrorException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class NullExpressionVisitor
  extends AEDefaultVisitor<Set<AERelationalExpr>>
{
  private static final NullExpressionVisitor INSTANCE = new NullExpressionVisitor();
  
  public static NullExpressionVisitor instance()
  {
    return INSTANCE;
  }
  
  public Set<AERelationalExpr> visit(AEColumnReference paramAEColumnReference)
    throws ErrorException
  {
    return new HashSet(Arrays.asList(new AENamedRelationalExpr[] { paramAEColumnReference.getNamedRelationalExpr() }));
  }
  
  public Set<AERelationalExpr> visit(AEValueExprList paramAEValueExprList)
    throws ErrorException
  {
    return (Set)((AEValueExpr)paramAEValueExprList.getChild(0)).acceptVisitor(this);
  }
  
  public Set<AERelationalExpr> visit(AEAdd paramAEAdd)
    throws ErrorException
  {
    return visitBinary(paramAEAdd);
  }
  
  public Set<AERelationalExpr> visit(AESubtract paramAESubtract)
    throws ErrorException
  {
    return visitBinary(paramAESubtract);
  }
  
  public Set<AERelationalExpr> visit(AEMultiply paramAEMultiply)
    throws ErrorException
  {
    return visitBinary(paramAEMultiply);
  }
  
  public Set<AERelationalExpr> visit(AEDivide paramAEDivide)
    throws ErrorException
  {
    return visitBinary(paramAEDivide);
  }
  
  public Set<AERelationalExpr> visit(AEConcat paramAEConcat)
    throws ErrorException
  {
    return visitBinary(paramAEConcat);
  }
  
  public Set<AERelationalExpr> visit(AENegate paramAENegate)
    throws ErrorException
  {
    return visitUnary(paramAENegate);
  }
  
  public Set<AERelationalExpr> visit(AERename paramAERename)
    throws ErrorException
  {
    return visitUnary(paramAERename);
  }
  
  protected Set<AERelationalExpr> defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    return new HashSet();
  }
  
  private Set<AERelationalExpr> visitUnary(AEUnaryValueExpr paramAEUnaryValueExpr)
    throws ErrorException
  {
    return (Set)paramAEUnaryValueExpr.getOperand().acceptVisitor(this);
  }
  
  private Set<AERelationalExpr> visitBinary(AEBinaryValueExpr paramAEBinaryValueExpr)
    throws ErrorException
  {
    Set localSet = (Set)paramAEBinaryValueExpr.getLeftOperand().acceptVisitor(this);
    localSet.addAll((Collection)paramAEBinaryValueExpr.getRightOperand().acceptVisitor(this));
    return localSet;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/NullExpressionVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.bool.ETAnd;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.bool.ETComparison;
import com.simba.sqlengine.executor.etree.bool.ETLike;
import com.simba.sqlengine.executor.etree.bool.ETNot;
import com.simba.sqlengine.executor.etree.bool.ETNullPredicate;
import com.simba.sqlengine.executor.etree.bool.ETOr;
import com.simba.sqlengine.executor.etree.bool.ETTrue;
import com.simba.sqlengine.executor.etree.bool.functor.comp.BooleanFunctorFactory;
import com.simba.sqlengine.executor.etree.bool.functor.comp.IBooleanCompFunctor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ETBoolExprMaterializer
  extends MaterializerBase<ETBooleanExpr>
{
  public ETBoolExprMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    super(paramIQueryPlan, paramMaterializerContext);
  }
  
  public ETBooleanExpr visit(AEAnd paramAEAnd)
    throws ErrorException
  {
    return new ETAnd((ETBooleanExpr)paramAEAnd.getLeftOperand().acceptVisitor(this), (ETBooleanExpr)paramAEAnd.getRightOperand().acceptVisitor(this));
  }
  
  public ETBooleanExpr visit(AEOr paramAEOr)
    throws ErrorException
  {
    return new ETOr((ETBooleanExpr)paramAEOr.getLeftOperand().acceptVisitor(this), (ETBooleanExpr)paramAEOr.getRightOperand().acceptVisitor(this));
  }
  
  public ETBooleanExpr visit(AENot paramAENot)
    throws ErrorException
  {
    return new ETNot((ETBooleanExpr)paramAENot.getOperand().acceptVisitor(this));
  }
  
  public ETBooleanExpr visit(AEComparison paramAEComparison)
    throws ErrorException
  {
    if ((paramAEComparison.getLeftOperand().getNumChildren() != 1) || (paramAEComparison.getRightOperand().getNumChildren() != 1)) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Compare value list");
    }
    AEValueExpr localAEValueExpr1 = (AEValueExpr)paramAEComparison.getLeftOperand().getChild(0);
    AEValueExpr localAEValueExpr2 = (AEValueExpr)paramAEComparison.getRightOperand().getChild(0);
    IColumn localIColumn = paramAEComparison.getCoercedColumnMetadata();
    ETValueExpr localETValueExpr1 = materializeExpr(localAEValueExpr1, localIColumn);
    ETValueExpr localETValueExpr2 = materializeExpr(localAEValueExpr2, localIColumn);
    IBooleanCompFunctor localIBooleanCompFunctor = BooleanFunctorFactory.getBoolCompFunctor(paramAEComparison.getComparisonOp(), localIColumn.getTypeMetadata());
    return new ETComparison(localIColumn, localETValueExpr1, localETValueExpr2, localIBooleanCompFunctor);
  }
  
  public ETBooleanExpr visit(AELikePredicate paramAELikePredicate)
    throws ErrorException
  {
    AEValueExpr localAEValueExpr1 = paramAELikePredicate.getEscapeChar();
    AEValueExpr localAEValueExpr2 = paramAELikePredicate.getLeftOperand();
    AEValueExpr localAEValueExpr3 = paramAELikePredicate.getRightOperand();
    IColumn localIColumn = paramAELikePredicate.getCoercedColumnMetadata();
    ETValueExpr localETValueExpr1 = null;
    if (localAEValueExpr1 != null) {
      localETValueExpr1 = materializeExpr(localAEValueExpr1, localIColumn);
    }
    ETValueExpr localETValueExpr2 = materializeExpr(localAEValueExpr2, localIColumn);
    ETValueExpr localETValueExpr3 = materializeExpr(localAEValueExpr3, localIColumn);
    return new ETLike(localIColumn, localETValueExpr2, localETValueExpr3, localETValueExpr1);
  }
  
  public ETBooleanExpr visit(AEInPredicate paramAEInPredicate)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = paramAEInPredicate.getLeftOperand();
    if (localAEValueExprList.getNumChildren() != 1) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("IN predicate with multiple columns on as left operand.");
    }
    IColumn localIColumn = paramAEInPredicate.getCoercedColumnMetadata();
    ETValueExpr localETValueExpr = materializeExpr((AEValueExpr)localAEValueExprList.getChild(0), localIColumn);
    IAENode localIAENode = paramAEInPredicate.getRightOperand();
    if ((localIAENode instanceof AERelationalExpr)) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Subquery in IN clause");
    }
    if (!(localIAENode instanceof AEValueExprList)) {
      throw SQLEngineExceptionFactory.invalidAETreeException();
    }
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator = ((AEValueExprList)localIAENode).getChildItr();
    while (localIterator.hasNext())
    {
      localObject1 = materializeExpr((AEValueExpr)localIterator.next(), localIColumn);
      localObject2 = BooleanFunctorFactory.getBoolCompFunctor(AEComparisonType.EQUAL, localIColumn.getTypeMetadata());
      localLinkedList.add(new ETComparison(localIColumn, localETValueExpr, (ETValueExpr)localObject1, (IBooleanCompFunctor)localObject2));
    }
    if (localLinkedList.size() == 0) {
      throw SQLEngineExceptionFactory.invalidAETreeException();
    }
    Object localObject1 = localLinkedList.iterator();
    for (Object localObject2 = (ETBooleanExpr)((Iterator)localObject1).next(); ((Iterator)localObject1).hasNext(); localObject2 = new ETOr((ETBooleanExpr)localObject2, (ETBooleanExpr)((Iterator)localObject1).next())) {}
    return (ETBooleanExpr)localObject2;
  }
  
  public ETBooleanExpr visit(AENullPredicate paramAENullPredicate)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = paramAENullPredicate.getOperand();
    if (localAEValueExprList.getNumChildren() != 1) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("IS NULL predicate on multiple column.");
    }
    AEValueExpr localAEValueExpr = (AEValueExpr)localAEValueExprList.getChild(0);
    ETValueExpr localETValueExpr = materializeExpr(localAEValueExpr, null);
    return new ETNullPredicate(localETValueExpr, localAEValueExpr.getColumn());
  }
  
  public ETBooleanExpr visit(AEBooleanTrue paramAEBooleanTrue)
    throws ErrorException
  {
    return new ETTrue();
  }
  
  public ETBooleanExpr visit(AEExistsPredicate paramAEExistsPredicate)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("EXIST");
  }
  
  public ETBooleanExpr visit(AEQuantifiedComparison paramAEQuantifiedComparison)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("QUANTIFIED COMPARISON");
  }
  
  private ETValueExpr materializeExpr(AEValueExpr paramAEValueExpr, IColumn paramIColumn)
    throws ErrorException
  {
    ETValueExprMaterializer localETValueExprMaterializer = createValueExprMaterializer();
    ETValueExpr localETValueExpr = (ETValueExpr)paramAEValueExpr.acceptVisitor(localETValueExprMaterializer);
    if (paramIColumn == null) {
      return localETValueExpr;
    }
    return ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr, paramAEValueExpr.getColumn(), paramIColumn, getContext());
  }
  
  protected ETValueExprMaterializer createValueExprMaterializer()
  {
    return new ETValueExprMaterializer(getQueryPlan(), getContext());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETBoolExprMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
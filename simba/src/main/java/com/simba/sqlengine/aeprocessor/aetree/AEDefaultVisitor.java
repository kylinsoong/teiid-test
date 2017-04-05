package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEAggregate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEDistinct;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEExcept;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESort;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETableConstructor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETop;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnion;
import com.simba.sqlengine.aeprocessor.aetree.statement.AECreateTable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDelete;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDropTable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsert;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsertDefaults;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEQuery;
import com.simba.sqlengine.aeprocessor.aetree.statement.AESetClause;
import com.simba.sqlengine.aeprocessor.aetree.statement.AESetClauseList;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEUpdate;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AECustomScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDefault;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.support.exceptions.ErrorException;

public abstract class AEDefaultVisitor<T>
  implements IAENodeVisitor<T>
{
  public T visit(AEValueExprList paramAEValueExprList)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEValueExprList);
  }
  
  public T visit(AEProject paramAEProject)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEProject);
  }
  
  public T visit(AEColumnReference paramAEColumnReference)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEColumnReference);
  }
  
  public T visit(AERename paramAERename)
    throws ErrorException
  {
    return (T)defaultVisit(paramAERename);
  }
  
  public T visit(AETable paramAETable)
    throws ErrorException
  {
    return (T)defaultVisit(paramAETable);
  }
  
  public T visit(AEQuery paramAEQuery)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEQuery);
  }
  
  public T visit(AEScalarFn paramAEScalarFn)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEScalarFn);
  }
  
  public T visit(AESelect paramAESelect)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESelect);
  }
  
  public T visit(AEAnd paramAEAnd)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEAnd);
  }
  
  public T visit(AEOr paramAEOr)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEOr);
  }
  
  public T visit(AENot paramAENot)
    throws ErrorException
  {
    return (T)defaultVisit(paramAENot);
  }
  
  public T visit(AEComparison paramAEComparison)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEComparison);
  }
  
  public T visit(AELiteral paramAELiteral)
    throws ErrorException
  {
    return (T)defaultVisit(paramAELiteral);
  }
  
  public T visit(AENull paramAENull)
    throws ErrorException
  {
    return (T)defaultVisit(paramAENull);
  }
  
  public T visit(AENegate paramAENegate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAENegate);
  }
  
  public T visit(AETop paramAETop)
    throws ErrorException
  {
    return (T)defaultVisit(paramAETop);
  }
  
  public T visit(AELikePredicate paramAELikePredicate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAELikePredicate);
  }
  
  public T visit(AESort paramAESort)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESort);
  }
  
  public T visit(AEInPredicate paramAEInPredicate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEInPredicate);
  }
  
  public T visit(AENullPredicate paramAENullPredicate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAENullPredicate);
  }
  
  public T visit(AECrossJoin paramAECrossJoin)
    throws ErrorException
  {
    return (T)defaultVisit(paramAECrossJoin);
  }
  
  public T visit(AEJoin paramAEJoin)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEJoin);
  }
  
  public T visit(AEBooleanTrue paramAEBooleanTrue)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEBooleanTrue);
  }
  
  public T visit(AEAdd paramAEAdd)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEAdd);
  }
  
  public T visit(AEConcat paramAEConcat)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEConcat);
  }
  
  public T visit(AESubtract paramAESubtract)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESubtract);
  }
  
  public T visit(AEDivide paramAEDivide)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEDivide);
  }
  
  public T visit(AEMultiply paramAEMultiply)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEMultiply);
  }
  
  public T visit(AEParameter paramAEParameter)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEParameter);
  }
  
  public T visit(AEDistinct paramAEDistinct)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEDistinct);
  }
  
  public T visit(AEProxyColumn paramAEProxyColumn)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEProxyColumn);
  }
  
  public T visit(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEAggregate);
  }
  
  public T visit(AECountStarAggrFn paramAECountStarAggrFn)
    throws ErrorException
  {
    return (T)defaultVisit(paramAECountStarAggrFn);
  }
  
  public T visit(AEGeneralAggrFn paramAEGeneralAggrFn)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEGeneralAggrFn);
  }
  
  public T visit(AENodeList<? extends IAENode> paramAENodeList)
    throws ErrorException
  {
    return (T)defaultVisit(paramAENodeList);
  }
  
  public T visit(AESearchedCase paramAESearchedCase)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESearchedCase);
  }
  
  public T visit(AESearchedWhenClause paramAESearchedWhenClause)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESearchedWhenClause);
  }
  
  public T visit(AESimpleCase paramAESimpleCase)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESimpleCase);
  }
  
  public T visit(AESimpleWhenClause paramAESimpleWhenClause)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESimpleWhenClause);
  }
  
  public T visit(AESubQuery paramAESubQuery)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESubQuery);
  }
  
  public T visit(AEExistsPredicate paramAEExistsPredicate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEExistsPredicate);
  }
  
  public T visit(AEQuantifiedComparison paramAEQuantifiedComparison)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEQuantifiedComparison);
  }
  
  public T visit(AEValueSubQuery paramAEValueSubQuery)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEValueSubQuery);
  }
  
  public T visit(AEUpdate paramAEUpdate)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEUpdate);
  }
  
  public T visit(AEInsert paramAEInsert)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEInsert);
  }
  
  public T visit(AEInsertDefaults paramAEInsertDefaults)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEInsertDefaults);
  }
  
  public T visit(AEDelete paramAEDelete)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEDelete);
  }
  
  public T visit(AESetClause paramAESetClause)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESetClause);
  }
  
  public T visit(AESetClauseList paramAESetClauseList)
    throws ErrorException
  {
    return (T)defaultVisit(paramAESetClauseList);
  }
  
  public T visit(AETableConstructor paramAETableConstructor)
    throws ErrorException
  {
    return (T)defaultVisit(paramAETableConstructor);
  }
  
  public T visit(AEDefault paramAEDefault)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEDefault);
  }
  
  public T visit(AEUnion paramAEUnion)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEUnion);
  }
  
  public T visit(AECreateTable paramAECreateTable)
    throws ErrorException
  {
    return (T)defaultVisit(paramAECreateTable);
  }
  
  public T visit(AEDropTable paramAEDropTable)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEDropTable);
  }
  
  public T visit(AECustomScalarFn paramAECustomScalarFn)
    throws ErrorException
  {
    return (T)defaultVisit(paramAECustomScalarFn);
  }
  
  public T visit(AEExcept paramAEExcept)
    throws ErrorException
  {
    return (T)defaultVisit(paramAEExcept);
  }
  
  protected abstract T defaultVisit(IAENode paramIAENode)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AEDefaultVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
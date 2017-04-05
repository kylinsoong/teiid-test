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

public abstract interface IAENodeVisitor<T>
{
  public abstract T visit(AEValueExprList paramAEValueExprList)
    throws ErrorException;
  
  public abstract T visit(AEProject paramAEProject)
    throws ErrorException;
  
  public abstract T visit(AEColumnReference paramAEColumnReference)
    throws ErrorException;
  
  public abstract T visit(AERename paramAERename)
    throws ErrorException;
  
  public abstract T visit(AETable paramAETable)
    throws ErrorException;
  
  public abstract T visit(AEQuery paramAEQuery)
    throws ErrorException;
  
  public abstract T visit(AEScalarFn paramAEScalarFn)
    throws ErrorException;
  
  public abstract T visit(AESelect paramAESelect)
    throws ErrorException;
  
  public abstract T visit(AEAnd paramAEAnd)
    throws ErrorException;
  
  public abstract T visit(AEOr paramAEOr)
    throws ErrorException;
  
  public abstract T visit(AENot paramAENot)
    throws ErrorException;
  
  public abstract T visit(AEComparison paramAEComparison)
    throws ErrorException;
  
  public abstract T visit(AELiteral paramAELiteral)
    throws ErrorException;
  
  public abstract T visit(AENull paramAENull)
    throws ErrorException;
  
  public abstract T visit(AENegate paramAENegate)
    throws ErrorException;
  
  public abstract T visit(AETop paramAETop)
    throws ErrorException;
  
  public abstract T visit(AELikePredicate paramAELikePredicate)
    throws ErrorException;
  
  public abstract T visit(AESort paramAESort)
    throws ErrorException;
  
  public abstract T visit(AEInPredicate paramAEInPredicate)
    throws ErrorException;
  
  public abstract T visit(AENullPredicate paramAENullPredicate)
    throws ErrorException;
  
  public abstract T visit(AECrossJoin paramAECrossJoin)
    throws ErrorException;
  
  public abstract T visit(AEJoin paramAEJoin)
    throws ErrorException;
  
  public abstract T visit(AEBooleanTrue paramAEBooleanTrue)
    throws ErrorException;
  
  public abstract T visit(AEAdd paramAEAdd)
    throws ErrorException;
  
  public abstract T visit(AEConcat paramAEConcat)
    throws ErrorException;
  
  public abstract T visit(AESubtract paramAESubtract)
    throws ErrorException;
  
  public abstract T visit(AEDivide paramAEDivide)
    throws ErrorException;
  
  public abstract T visit(AEMultiply paramAEMultiply)
    throws ErrorException;
  
  public abstract T visit(AEParameter paramAEParameter)
    throws ErrorException;
  
  public abstract T visit(AEDistinct paramAEDistinct)
    throws ErrorException;
  
  public abstract T visit(AEProxyColumn paramAEProxyColumn)
    throws ErrorException;
  
  public abstract T visit(AEAggregate paramAEAggregate)
    throws ErrorException;
  
  public abstract T visit(AECountStarAggrFn paramAECountStarAggrFn)
    throws ErrorException;
  
  public abstract T visit(AEGeneralAggrFn paramAEGeneralAggrFn)
    throws ErrorException;
  
  public abstract T visit(AENodeList<? extends IAENode> paramAENodeList)
    throws ErrorException;
  
  public abstract T visit(AESearchedCase paramAESearchedCase)
    throws ErrorException;
  
  public abstract T visit(AESearchedWhenClause paramAESearchedWhenClause)
    throws ErrorException;
  
  public abstract T visit(AESimpleCase paramAESimpleCase)
    throws ErrorException;
  
  public abstract T visit(AESimpleWhenClause paramAESimpleWhenClause)
    throws ErrorException;
  
  public abstract T visit(AESubQuery paramAESubQuery)
    throws ErrorException;
  
  public abstract T visit(AEExistsPredicate paramAEExistsPredicate)
    throws ErrorException;
  
  public abstract T visit(AEQuantifiedComparison paramAEQuantifiedComparison)
    throws ErrorException;
  
  public abstract T visit(AEValueSubQuery paramAEValueSubQuery)
    throws ErrorException;
  
  public abstract T visit(AEUpdate paramAEUpdate)
    throws ErrorException;
  
  public abstract T visit(AEInsert paramAEInsert)
    throws ErrorException;
  
  public abstract T visit(AEInsertDefaults paramAEInsertDefaults)
    throws ErrorException;
  
  public abstract T visit(AEDelete paramAEDelete)
    throws ErrorException;
  
  public abstract T visit(AESetClause paramAESetClause)
    throws ErrorException;
  
  public abstract T visit(AESetClauseList paramAESetClauseList)
    throws ErrorException;
  
  public abstract T visit(AETableConstructor paramAETableConstructor)
    throws ErrorException;
  
  public abstract T visit(AEDefault paramAEDefault)
    throws ErrorException;
  
  public abstract T visit(AEUnion paramAEUnion)
    throws ErrorException;
  
  public abstract T visit(AECreateTable paramAECreateTable)
    throws ErrorException;
  
  public abstract T visit(AEDropTable paramAEDropTable)
    throws ErrorException;
  
  public abstract T visit(AECustomScalarFn paramAECustomScalarFn)
    throws ErrorException;
  
  public abstract T visit(AEExcept paramAEExcept)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/IAENodeVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
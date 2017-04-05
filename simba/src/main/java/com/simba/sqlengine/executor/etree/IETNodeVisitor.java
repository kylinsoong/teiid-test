package com.simba.sqlengine.executor.etree;

import com.simba.sqlengine.executor.etree.bool.ETAnd;
import com.simba.sqlengine.executor.etree.bool.ETComparison;
import com.simba.sqlengine.executor.etree.bool.ETLike;
import com.simba.sqlengine.executor.etree.bool.ETNot;
import com.simba.sqlengine.executor.etree.bool.ETNullPredicate;
import com.simba.sqlengine.executor.etree.bool.ETOr;
import com.simba.sqlengine.executor.etree.bool.ETTrue;
import com.simba.sqlengine.executor.etree.relation.ETDistinctMove;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate;
import com.simba.sqlengine.executor.etree.relation.ETProject;
import com.simba.sqlengine.executor.etree.relation.ETRelationalCache;
import com.simba.sqlengine.executor.etree.relation.ETRelationalConvert;
import com.simba.sqlengine.executor.etree.relation.ETSelect;
import com.simba.sqlengine.executor.etree.relation.ETSort;
import com.simba.sqlengine.executor.etree.relation.ETStreamAggregate;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.relation.ETTableConstructor;
import com.simba.sqlengine.executor.etree.relation.ETTop;
import com.simba.sqlengine.executor.etree.relation.ETUnionAll;
import com.simba.sqlengine.executor.etree.relation.join.ETConditionedJoin;
import com.simba.sqlengine.executor.etree.statement.ETInsert;
import com.simba.sqlengine.executor.etree.statement.ETQuery;
import com.simba.sqlengine.executor.etree.statement.ETSearchedDelete;
import com.simba.sqlengine.executor.etree.statement.ETSearchedUpdate;
import com.simba.sqlengine.executor.etree.statement.ETSetClause;
import com.simba.sqlengine.executor.etree.statement.ETSetClauseList;
import com.simba.sqlengine.executor.etree.value.ETAggregateFn;
import com.simba.sqlengine.executor.etree.value.ETBinaryArithValueExpr;
import com.simba.sqlengine.executor.etree.value.ETColumnRef;
import com.simba.sqlengine.executor.etree.value.ETConstant;
import com.simba.sqlengine.executor.etree.value.ETConvert;
import com.simba.sqlengine.executor.etree.value.ETCustomScalarFn;
import com.simba.sqlengine.executor.etree.value.ETDefault;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.sqlengine.executor.etree.value.ETSearchedCase;
import com.simba.sqlengine.executor.etree.value.ETSearchedWhenClause;
import com.simba.sqlengine.executor.etree.value.ETSimpleCase;
import com.simba.sqlengine.executor.etree.value.ETSimpleWhenClause;
import com.simba.sqlengine.executor.etree.value.ETUnaryArithValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.sqlengine.executor.etree.value.scalar.ETScalarFn;
import com.simba.support.exceptions.ErrorException;

public abstract interface IETNodeVisitor<T>
{
  public abstract T visit(ETQuery paramETQuery)
    throws ErrorException;
  
  public abstract T visit(ETProject paramETProject)
    throws ErrorException;
  
  public abstract T visit(ETValueExprList paramETValueExprList)
    throws ErrorException;
  
  public abstract T visit(ETColumnRef paramETColumnRef)
    throws ErrorException;
  
  public abstract T visit(ETTable paramETTable)
    throws ErrorException;
  
  public abstract T visit(ETBinaryArithValueExpr paramETBinaryArithValueExpr)
    throws ErrorException;
  
  public abstract T visit(ETUnaryArithValueExpr paramETUnaryArithValueExpr)
    throws ErrorException;
  
  public abstract T visit(ETConvert paramETConvert)
    throws ErrorException;
  
  public abstract T visit(ETConstant paramETConstant)
    throws ErrorException;
  
  public abstract T visit(ETCustomScalarFn paramETCustomScalarFn)
    throws ErrorException;
  
  public abstract T visit(ETTop paramETTop)
    throws ErrorException;
  
  public abstract T visit(ETParameter paramETParameter)
    throws ErrorException;
  
  public abstract T visit(ETTrue paramETTrue)
    throws ErrorException;
  
  public abstract T visit(ETNot paramETNot)
    throws ErrorException;
  
  public abstract T visit(ETAnd paramETAnd)
    throws ErrorException;
  
  public abstract T visit(ETOr paramETOr)
    throws ErrorException;
  
  public abstract T visit(ETComparison paramETComparison)
    throws ErrorException;
  
  public abstract T visit(ETSelect paramETSelect)
    throws ErrorException;
  
  public abstract T visit(ETLike paramETLike)
    throws ErrorException;
  
  public abstract T visit(ETNullPredicate paramETNullPredicate)
    throws ErrorException;
  
  public abstract T visit(ETRelationalConvert paramETRelationalConvert)
    throws ErrorException;
  
  public abstract T visit(ETInsert paramETInsert)
    throws ErrorException;
  
  public abstract T visit(ETSearchedDelete paramETSearchedDelete)
    throws ErrorException;
  
  public abstract T visit(ETTableConstructor paramETTableConstructor)
    throws ErrorException;
  
  public abstract T visit(ETScalarFn paramETScalarFn)
    throws ErrorException;
  
  public abstract T visit(ETSimpleCase paramETSimpleCase)
    throws ErrorException;
  
  public abstract T visit(ETSimpleWhenClause paramETSimpleWhenClause)
    throws ErrorException;
  
  public abstract T visit(ETSort paramETSort)
    throws ErrorException;
  
  public abstract T visit(ETSearchedCase paramETSearchedCase)
    throws ErrorException;
  
  public abstract T visit(ETSearchedWhenClause paramETSearchedWhenClause)
    throws ErrorException;
  
  public abstract T visit(ETConditionedJoin paramETConditionedJoin)
    throws ErrorException;
  
  public abstract T visit(ETHashAggregate paramETHashAggregate)
    throws ErrorException;
  
  public abstract T visit(ETStreamAggregate paramETStreamAggregate)
    throws ErrorException;
  
  public abstract T visit(ETDistinctMove paramETDistinctMove)
    throws ErrorException;
  
  public abstract T visit(ETAggregateFn paramETAggregateFn)
    throws ErrorException;
  
  public abstract T visit(ETRelationalCache paramETRelationalCache)
    throws ErrorException;
  
  public abstract T visit(ETSearchedUpdate paramETSearchedUpdate)
    throws ErrorException;
  
  public abstract T visit(ETSetClauseList paramETSetClauseList)
    throws ErrorException;
  
  public abstract T visit(ETSetClause paramETSetClause)
    throws ErrorException;
  
  public abstract T visit(ETUnionAll paramETUnionAll)
    throws ErrorException;
  
  public abstract T visit(ETDefault paramETDefault)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/IETNodeVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
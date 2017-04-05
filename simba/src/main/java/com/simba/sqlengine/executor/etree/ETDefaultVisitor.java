package com.simba.sqlengine.executor.etree;

import com.simba.sqlengine.executor.etree.bool.ETAnd;
import com.simba.sqlengine.executor.etree.bool.ETComparison;
import com.simba.sqlengine.executor.etree.bool.ETLike;
import com.simba.sqlengine.executor.etree.bool.ETNot;
import com.simba.sqlengine.executor.etree.bool.ETNullPredicate;
import com.simba.sqlengine.executor.etree.bool.ETOr;
import com.simba.sqlengine.executor.etree.bool.ETTrue;
import com.simba.sqlengine.executor.etree.relation.ETProject;
import com.simba.sqlengine.executor.etree.relation.ETRelationalConvert;
import com.simba.sqlengine.executor.etree.relation.ETSelect;
import com.simba.sqlengine.executor.etree.relation.ETSort;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.relation.ETTableConstructor;
import com.simba.sqlengine.executor.etree.relation.ETTop;
import com.simba.sqlengine.executor.etree.relation.join.ETConditionedJoin;
import com.simba.sqlengine.executor.etree.statement.ETInsert;
import com.simba.sqlengine.executor.etree.statement.ETQuery;
import com.simba.sqlengine.executor.etree.statement.ETSearchedDelete;
import com.simba.sqlengine.executor.etree.value.ETBinaryArithValueExpr;
import com.simba.sqlengine.executor.etree.value.ETColumnRef;
import com.simba.sqlengine.executor.etree.value.ETConstant;
import com.simba.sqlengine.executor.etree.value.ETConvert;
import com.simba.sqlengine.executor.etree.value.ETCustomScalarFn;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.sqlengine.executor.etree.value.ETSearchedCase;
import com.simba.sqlengine.executor.etree.value.ETSearchedWhenClause;
import com.simba.sqlengine.executor.etree.value.ETSimpleCase;
import com.simba.sqlengine.executor.etree.value.ETSimpleWhenClause;
import com.simba.sqlengine.executor.etree.value.ETUnaryArithValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.sqlengine.executor.etree.value.scalar.ETScalarFn;
import com.simba.support.exceptions.ErrorException;

public abstract class ETDefaultVisitor<T>
  implements IETNodeVisitor<T>
{
  public T visit(ETQuery paramETQuery)
    throws ErrorException
  {
    return (T)defaultVisit(paramETQuery);
  }
  
  public T visit(ETProject paramETProject)
    throws ErrorException
  {
    return (T)defaultVisit(paramETProject);
  }
  
  public T visit(ETValueExprList paramETValueExprList)
    throws ErrorException
  {
    return (T)defaultVisit(paramETValueExprList);
  }
  
  public T visit(ETColumnRef paramETColumnRef)
    throws ErrorException
  {
    return (T)defaultVisit(paramETColumnRef);
  }
  
  public T visit(ETTable paramETTable)
    throws ErrorException
  {
    return (T)defaultVisit(paramETTable);
  }
  
  public T visit(ETBinaryArithValueExpr paramETBinaryArithValueExpr)
    throws ErrorException
  {
    return (T)defaultVisit(paramETBinaryArithValueExpr);
  }
  
  public T visit(ETUnaryArithValueExpr paramETUnaryArithValueExpr)
    throws ErrorException
  {
    return (T)defaultVisit(paramETUnaryArithValueExpr);
  }
  
  public T visit(ETConvert paramETConvert)
    throws ErrorException
  {
    return (T)defaultVisit(paramETConvert);
  }
  
  public T visit(ETConstant paramETConstant)
    throws ErrorException
  {
    return (T)defaultVisit(paramETConstant);
  }
  
  public T visit(ETCustomScalarFn paramETCustomScalarFn)
    throws ErrorException
  {
    return (T)defaultVisit(paramETCustomScalarFn);
  }
  
  public T visit(ETTop paramETTop)
    throws ErrorException
  {
    return (T)defaultVisit(paramETTop);
  }
  
  public T visit(ETParameter paramETParameter)
    throws ErrorException
  {
    return (T)defaultVisit(paramETParameter);
  }
  
  public T visit(ETTrue paramETTrue)
    throws ErrorException
  {
    return (T)defaultVisit(paramETTrue);
  }
  
  public T visit(ETNot paramETNot)
    throws ErrorException
  {
    return (T)defaultVisit(paramETNot);
  }
  
  public T visit(ETAnd paramETAnd)
    throws ErrorException
  {
    return (T)defaultVisit(paramETAnd);
  }
  
  public T visit(ETOr paramETOr)
    throws ErrorException
  {
    return (T)defaultVisit(paramETOr);
  }
  
  public T visit(ETComparison paramETComparison)
    throws ErrorException
  {
    return (T)defaultVisit(paramETComparison);
  }
  
  public T visit(ETSelect paramETSelect)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSelect);
  }
  
  public T visit(ETLike paramETLike)
    throws ErrorException
  {
    return (T)defaultVisit(paramETLike);
  }
  
  public T visit(ETNullPredicate paramETNullPredicate)
    throws ErrorException
  {
    return (T)defaultVisit(paramETNullPredicate);
  }
  
  public T visit(ETRelationalConvert paramETRelationalConvert)
    throws ErrorException
  {
    return (T)defaultVisit(paramETRelationalConvert);
  }
  
  public T visit(ETInsert paramETInsert)
    throws ErrorException
  {
    return (T)defaultVisit(paramETInsert);
  }
  
  public T visit(ETSearchedDelete paramETSearchedDelete)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSearchedDelete);
  }
  
  public T visit(ETTableConstructor paramETTableConstructor)
    throws ErrorException
  {
    return (T)defaultVisit(paramETTableConstructor);
  }
  
  public T visit(ETScalarFn paramETScalarFn)
    throws ErrorException
  {
    return (T)defaultVisit(paramETScalarFn);
  }
  
  public T visit(ETSimpleCase paramETSimpleCase)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSimpleCase);
  }
  
  public T visit(ETSimpleWhenClause paramETSimpleWhenClause)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSimpleWhenClause);
  }
  
  public T visit(ETSort paramETSort)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSort);
  }
  
  public T visit(ETSearchedCase paramETSearchedCase)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSearchedCase);
  }
  
  public T visit(ETSearchedWhenClause paramETSearchedWhenClause)
    throws ErrorException
  {
    return (T)defaultVisit(paramETSearchedWhenClause);
  }
  
  public T visit(ETConditionedJoin paramETConditionedJoin)
    throws ErrorException
  {
    return (T)defaultVisit(paramETConditionedJoin);
  }
  
  protected abstract T defaultVisit(IETNode paramIETNode)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETDefaultVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
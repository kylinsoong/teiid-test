package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aebuilder.relation.AERelationalExprBuilder;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDefault;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTDynamicParameterNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTLiteralNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public class AEValueExprBuilder
  extends AEBuilderBase<AEValueExpr>
{
  public AEValueExprBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEValueExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case COLUMN_REFERENCE: 
      return new AEColumnReferenceBuilder(getQueryScope()).visit(paramPTNonterminalNode);
    case BINARY_PLUS_SIGN: 
    case BINARY_MINUS_SIGN: 
    case MULTIPLICATION_SIGN: 
    case DIVISION_SIGN: 
    case CONCAT_SIGN: 
      return buildBinaryArithmetic(paramPTNonterminalNode);
    case UNARY_MINUS_SIGN: 
      return buildUnaryMinusSign(paramPTNonterminalNode);
    case FUNC: 
      return new AEScalarFnBuilder(getQueryScope()).visit(paramPTNonterminalNode);
    case COUNT: 
    case AVG: 
    case SUM: 
    case MIN: 
    case MAX: 
      return new AEAggrFnBuilder(getQueryScope()).visit(paramPTNonterminalNode);
    case SEARCHED_CASE: 
    case SIMPLE_CASE: 
    case COALESCE: 
    case NULLIF: 
      return new AECaseExprBuilder(getQueryScope()).visit(paramPTNonterminalNode);
    case SUBQUERY: 
      return buildValueSubQuery(paramPTNonterminalNode);
    }
    throw SQLEngineExceptionFactory.featureNotImplementedException("Unsupported value expression.");
  }
  
  public AEValueExpr visit(PTLiteralNode paramPTLiteralNode)
    throws ErrorException
  {
    if (null == paramPTLiteralNode) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    if (paramPTLiteralNode.getLiteralType() == PTLiteralType.NULL) {
      return new AENull();
    }
    return new AELiteral(paramPTLiteralNode.getStringValue(), paramPTLiteralNode.getLiteralType(), getQueryScope().getDataEngine().getContext());
  }
  
  public AEValueExpr visit(PTDynamicParameterNode paramPTDynamicParameterNode)
    throws ErrorException
  {
    SqlDataEngineContext localSqlDataEngineContext = getQueryScope().getDataEngine().getContext();
    AEParameter localAEParameter = new AEParameter(paramPTDynamicParameterNode.getIndex(), localSqlDataEngineContext);
    return localAEParameter;
  }
  
  public AEValueExpr visit(PTFlagNode paramPTFlagNode)
    throws ErrorException
  {
    switch (paramPTFlagNode.getFlagType())
    {
    case DEFAULT: 
      return new AEDefault();
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private AEValueSubQuery buildValueSubQuery(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    IPTNode localIPTNode = paramPTNonterminalNode.getChild(PTPositionalType.SINGLE_CHILD);
    if ((1 != paramPTNonterminalNode.numChildren()) || (null == localIPTNode)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AERelationalExprBuilder localAERelationalExprBuilder = new AERelationalExprBuilder(getQueryScope(), true);
    AERelationalExpr localAERelationalExpr = (AERelationalExpr)localAERelationalExprBuilder.build(localIPTNode);
    if (1 != localAERelationalExpr.getColumnCount()) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.MULTI_EXPR_IN_SUB_QUERY.name());
    }
    return new AEValueSubQuery(localAERelationalExpr, localAERelationalExprBuilder.isQueryCorrelated());
  }
  
  private AENegate buildUnaryMinusSign(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    return new AENegate((AEValueExpr)paramPTNonterminalNode.getChild(PTPositionalType.SINGLE_CHILD).acceptVisitor(this), getQueryScope().getDataEngine().getContext());
  }
  
  private AEValueExpr buildBinaryArithmetic(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.BINARY_LEFT, AEBuilderCheck.nonEmpty(), PTPositionalType.BINARY_RIGHT, AEBuilderCheck.nonEmpty()));
    AEValueExpr localAEValueExpr1 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(paramPTNonterminalNode.getChild(PTPositionalType.BINARY_LEFT));
    AEValueExpr localAEValueExpr2 = (AEValueExpr)new AEValueExprBuilder(getQueryScope()).build(paramPTNonterminalNode.getChild(PTPositionalType.BINARY_RIGHT));
    ICoercionHandler localICoercionHandler = getQueryScope().getDataEngine().createCoercionHandler();
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case BINARY_PLUS_SIGN: 
      return new AEAdd(localICoercionHandler, localAEValueExpr1, localAEValueExpr2);
    case CONCAT_SIGN: 
      return new AEConcat(localICoercionHandler, localAEValueExpr1, localAEValueExpr2);
    case BINARY_MINUS_SIGN: 
      return new AESubtract(localICoercionHandler, localAEValueExpr1, localAEValueExpr2);
    case MULTIPLICATION_SIGN: 
      return new AEMultiply(localICoercionHandler, localAEValueExpr1, localAEValueExpr2);
    case DIVISION_SIGN: 
      return new AEDivide(localICoercionHandler, localAEValueExpr1, localAEValueExpr2);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEValueExprBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
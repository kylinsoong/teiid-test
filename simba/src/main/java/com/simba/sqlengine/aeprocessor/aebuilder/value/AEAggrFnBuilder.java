package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.AESemantics;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn.AggrFnId;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn.AggrFnQuantifier;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Set;

public class AEAggrFnBuilder
  extends AEBuilderBase<AEAggrFn>
{
  protected AEAggrFnBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEAggrFn visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case COUNT: 
      return buildCountAggrFn(paramPTNonterminalNode);
    case AVG: 
    case SUM: 
    case MIN: 
    case MAX: 
      return buildGeneralAggrFn(paramPTNonterminalNode);
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private AEAggrFn buildGeneralAggrFn(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (paramPTNonterminalNode.getAllPositionalTypes().size() != 2) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    AEValueExpr localAEValueExpr = (AEValueExpr)paramPTNonterminalNode.getChild(PTPositionalType.VALUE_EXPRESSION).acceptVisitor(new AEValueExprBuilder(getQueryScope()));
    AEAggrFn.AggrFnQuantifier localAggrFnQuantifier = AEAggrFn.AggrFnQuantifier.ALL;
    IPTNode localIPTNode = paramPTNonterminalNode.getChild(PTPositionalType.SET_QUANTIFIER);
    if (!localIPTNode.isEmptyNode())
    {
      if (!(localIPTNode instanceof PTFlagNode)) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      localObject = (PTFlagNode)localIPTNode;
      if (((PTFlagNode)localObject).getFlagType() == PTFlagType.DISTINCT) {
        localAggrFnQuantifier = AEAggrFn.AggrFnQuantifier.DISTINCT;
      } else if (((PTFlagNode)localObject).getFlagType() != PTFlagType.ALL) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
    }
    Object localObject = null;
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case COUNT: 
      localObject = AEAggrFn.AggrFnId.COUNT;
      break;
    case MIN: 
      localObject = AEAggrFn.AggrFnId.MIN;
      break;
    case MAX: 
      localObject = AEAggrFn.AggrFnId.MAX;
      break;
    case SUM: 
      localObject = AEAggrFn.AggrFnId.SUM;
      break;
    case AVG: 
      localObject = AEAggrFn.AggrFnId.AVG;
      break;
    default: 
      throw new IllegalArgumentException("Unknown aggregate function.");
    }
    AESemantics.checkAggrFnSemantics(localAEValueExpr, getQueryScope());
    return new AEGeneralAggrFn((AEAggrFn.AggrFnId)localObject, localAggrFnQuantifier, localAEValueExpr, getQueryScope().getDataEngine().getContext().getCoercionHandler());
  }
  
  private AEAggrFn buildCountAggrFn(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    assert (paramPTNonterminalNode.getNonterminalType() == PTNonterminalType.COUNT);
    if (paramPTNonterminalNode.getChild(PTPositionalType.STAR) != null)
    {
      if (paramPTNonterminalNode.getAllPositionalTypes().size() != 1) {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
      return new AECountStarAggrFn();
    }
    return buildGeneralAggrFn(paramPTNonterminalNode);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEAggrFnBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
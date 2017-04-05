package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;
import java.util.List;

public class AERowValueListBuilder
  extends AEBuilderBase<AEValueExprList>
{
  public AERowValueListBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEValueExprList visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    if ((paramPTListNode == null) || (paramPTListNode.getListType() != PTListType.ROW_VALUE_LIST)) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    List localList = paramPTListNode.getImmutableChildList();
    AEValueExprList localAEValueExprList = new AEValueExprList();
    AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      localAEValueExprList.addNode(localAEValueExprBuilder.build(localIPTNode));
    }
    return localAEValueExprList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AERowValueListBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
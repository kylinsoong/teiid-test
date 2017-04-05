package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public class AETableRefListBuilder
  extends AEBuilderBase<AERelationalExpr>
{
  protected AETableRefListBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
    if (null == paramAEQueryScope) {
      throw new NullPointerException("Query scope cannot be null.");
    }
  }
  
  public AERelationalExpr visit(PTListNode paramPTListNode)
    throws ErrorException
  {
    Object localObject = null;
    if (PTListType.TABLE_REFERENCE_LIST == paramPTListNode.getListType())
    {
      Iterator localIterator = paramPTListNode.getChildItr();
      AETableRefBuilder localAETableRefBuilder = new AETableRefBuilder(getQueryScope());
      while (localIterator.hasNext())
      {
        AERelationalExpr localAERelationalExpr = (AERelationalExpr)localAETableRefBuilder.build((IPTNode)localIterator.next());
        if (null == localAERelationalExpr) {
          throw new IllegalStateException("Table reference cannot be null.");
        }
        if (null == localObject) {
          localObject = localAERelationalExpr;
        } else {
          localObject = new AECrossJoin((AERelationalExpr)localObject, localAERelationalExpr);
        }
      }
      return (AERelationalExpr)localObject;
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AETableRefListBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
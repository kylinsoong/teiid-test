package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETableConstructor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.Iterator;

public final class StatementMetadataProcessor
  extends AEDefaultVisitor<Void>
{
  private static final StatementMetadataProcessor INSTANCE = new StatementMetadataProcessor();
  
  public Void visit(AETableConstructor paramAETableConstructor)
    throws ErrorException
  {
    super.visit(paramAETableConstructor);
    paramAETableConstructor.reprocessMetadata();
    return null;
  }
  
  protected Void defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    Iterator localIterator = paramIAENode.getChildItr();
    while (localIterator.hasNext())
    {
      IAENode localIAENode = (IAENode)localIterator.next();
      localIAENode.acceptVisitor(this);
    }
    if ((paramIAENode instanceof AEValueExpr)) {
      ((AEValueExpr)paramIAENode).updateColumn();
    } else if ((paramIAENode instanceof AEBooleanExpr)) {
      ((AEBooleanExpr)paramIAENode).updateCoercion();
    }
    return null;
  }
  
  static StatementMetadataProcessor getInstance()
  {
    return INSTANCE;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/StatementMetadataProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
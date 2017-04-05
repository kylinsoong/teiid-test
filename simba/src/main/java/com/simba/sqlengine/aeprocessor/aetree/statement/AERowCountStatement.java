package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;

public abstract class AERowCountStatement
  implements IAEStatement
{
  public abstract AERowCountStatement copy();
  
  public IAENode getParent()
  {
    return null;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    throw new UnsupportedOperationException(getLogString() + " node does not have a parent.");
  }
  
  protected static IAENodeVisitor<Void> getMetadataProcessor()
  {
    return StatementMetadataProcessor.getInstance();
  }
  
  public void notifyDataNeeded()
    throws ErrorException
  {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AERowCountStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public abstract interface IAEStatement
  extends IAENode
{
  public abstract void reprocessMetadata()
    throws ErrorException;
  
  public abstract void notifyDataNeeded()
    throws ErrorException;
  
  public abstract List<AEParameter> getDynamicParameters();
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/IAEStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
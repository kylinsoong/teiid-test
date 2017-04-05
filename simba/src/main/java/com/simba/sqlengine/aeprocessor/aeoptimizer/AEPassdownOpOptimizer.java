package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDelete;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEQuery;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEUpdate;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.sqlengine.dsiext.dataengine.DSIExtOperationHandlerFactory;
import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

public class AEPassdownOpOptimizer
  implements IAEOptimizer
{
  DSIExtOperationHandlerFactory m_opHandlerFactory;
  
  public AEPassdownOpOptimizer(DSIExtOperationHandlerFactory paramDSIExtOperationHandlerFactory)
  {
    this.m_opHandlerFactory = paramDSIExtOperationHandlerFactory;
  }
  
  public void optimize(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    Object localObject;
    if ((paramIAEStatement instanceof AEQuery))
    {
      localObject = (AEQuery)paramIAEStatement;
      ((AEQuery)localObject).setOperand(new AERelationalExprHandler(this.m_opHandlerFactory).passdown(((AEQuery)localObject).getOperand()));
    }
    else
    {
      AEBooleanExpr localAEBooleanExpr;
      AETable localAETable;
      Pair localPair;
      if ((paramIAEStatement instanceof AEUpdate))
      {
        localObject = (AEUpdate)paramIAEStatement;
        localAEBooleanExpr = ((AEUpdate)localObject).getUpdateCondition();
        localAETable = ((AEUpdate)localObject).getTable();
        localPair = doPassdown(localAETable, localAEBooleanExpr);
        ((AEUpdate)localObject).setTable((AETable)localPair.key());
        ((AEUpdate)localObject).setUpdateCondition((AEBooleanExpr)localPair.value());
      }
      else if ((paramIAEStatement instanceof AEDelete))
      {
        localObject = (AEDelete)paramIAEStatement;
        localAEBooleanExpr = ((AEDelete)localObject).getCondition();
        localAETable = ((AEDelete)localObject).getTable();
        localPair = doPassdown(localAETable, localAEBooleanExpr);
        ((AEDelete)localObject).setTable((AETable)localPair.key());
        ((AEDelete)localObject).setCondition((AEBooleanExpr)localPair.value());
      }
    }
  }
  
  private Pair<AETable, AEBooleanExpr> doPassdown(AETable paramAETable, AEBooleanExpr paramAEBooleanExpr)
    throws ErrorException
  {
    assert (paramAETable != null);
    assert (paramAEBooleanExpr != null);
    if ((paramAEBooleanExpr instanceof AEBooleanTrue)) {
      return new Pair(paramAETable, paramAEBooleanExpr);
    }
    IBooleanExprHandler localIBooleanExprHandler = this.m_opHandlerFactory.createFilterHandler(paramAETable.getTable());
    if (localIBooleanExprHandler == null) {
      return new Pair(paramAETable, paramAEBooleanExpr);
    }
    Pair localPair = new AEPassdownFilter(localIBooleanExprHandler).passdown(paramAEBooleanExpr);
    if (localPair.key() != null) {
      paramAETable.setTable((DSIExtJResultSet)localPair.key());
    }
    if (localPair.value() == null) {
      localPair.setValue(new AEBooleanTrue());
    }
    return new Pair(paramAETable, localPair.value());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/AEPassdownOpOptimizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
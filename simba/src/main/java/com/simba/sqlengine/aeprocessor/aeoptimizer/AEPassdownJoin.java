package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.sqlengine.dsiext.dataengine.DSIExtOperationHandlerFactory;
import com.simba.sqlengine.dsiext.dataengine.IBooleanExprHandler;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

public class AEPassdownJoin
{
  private DSIExtOperationHandlerFactory m_opHandlerFactory;
  
  public AEPassdownJoin(DSIExtOperationHandlerFactory paramDSIExtOperationHandlerFactory)
  {
    assert (null != paramDSIExtOperationHandlerFactory);
    this.m_opHandlerFactory = paramDSIExtOperationHandlerFactory;
  }
  
  public AERelationalExpr passdown(AEJoin paramAEJoin)
  {
    assert (null != paramAEJoin);
    assert ((paramAEJoin.getLeftOperand() instanceof AETable));
    assert ((paramAEJoin.getRightOperand() instanceof AETable));
    AETable localAETable1 = (AETable)paramAEJoin.getLeftOperand();
    AETable localAETable2 = (AETable)paramAEJoin.getRightOperand();
    IBooleanExprHandler localIBooleanExprHandler = this.m_opHandlerFactory.createJoinHandler(localAETable1.getTable(), localAETable2.getTable(), paramAEJoin.getJoinType());
    if (null == localIBooleanExprHandler) {
      return null;
    }
    AEBooleanExpr localAEBooleanExpr = paramAEJoin.getJoinCondition();
    DSIExtJResultSet localDSIExtJResultSet = null;
    Object localObject;
    if (AEJoin.AEJoinType.INNER_JOIN == paramAEJoin.getJoinType())
    {
      localObject = new AEPassdownFilter(localIBooleanExprHandler).passdown(localAEBooleanExpr);
      localDSIExtJResultSet = (DSIExtJResultSet)((Pair)localObject).key();
      localAEBooleanExpr = (AEBooleanExpr)((Pair)localObject).value();
    }
    else if (localIBooleanExprHandler.passdown(paramAEJoin.getJoinCondition()))
    {
      localDSIExtJResultSet = localIBooleanExprHandler.takeResult();
      localAEBooleanExpr = null;
    }
    if (localDSIExtJResultSet == null) {
      return null;
    }
    try
    {
      localObject = new AETable(localDSIExtJResultSet);
      resolveColumns(localAETable1, (AETable)localObject);
      resolveColumns(localAETable2, (AETable)localObject);
      if (null == localAEBooleanExpr) {
        return (AERelationalExpr)localObject;
      }
      return passdownJoinCond(localAEBooleanExpr, (AETable)localObject);
    }
    catch (ErrorException localErrorException) {}
    return null;
  }
  
  private AERelationalExpr passdownJoinCond(AEBooleanExpr paramAEBooleanExpr, AETable paramAETable)
  {
    assert (null != paramAETable);
    assert (null != paramAETable.getTable());
    IBooleanExprHandler localIBooleanExprHandler = this.m_opHandlerFactory.createFilterHandler(paramAETable.getTable());
    if (null == localIBooleanExprHandler) {
      return new AESelect(paramAETable, paramAEBooleanExpr);
    }
    Pair localPair = new AEPassdownFilter(localIBooleanExprHandler).passdown(paramAEBooleanExpr);
    if (null != localPair.key()) {
      paramAETable.setTable((DSIExtJResultSet)localPair.key());
    }
    if (localPair.value() == null) {
      return paramAETable;
    }
    return new AESelect(paramAETable, paramAEBooleanExpr);
  }
  
  private void resolveColumns(AETable paramAETable1, AETable paramAETable2)
    throws ErrorException
  {
    for (IAENode localIAENode1 = paramAETable1.getParent(); (localIAENode1.getParent() != null) && (!(localIAENode1 instanceof AESubQuery)); localIAENode1 = localIAENode1.getParent()) {}
    DSIExtJResultSet localDSIExtJResultSet1 = paramAETable2.getTable();
    DSIExtJResultSet localDSIExtJResultSet2 = paramAETable1.getTable();
    AETreeWalker localAETreeWalker = new AETreeWalker(localIAENode1);
    while (localAETreeWalker.hasNext())
    {
      IAENode localIAENode2 = localAETreeWalker.next();
      if ((localIAENode2 instanceof AEColumnReference))
      {
        AEColumnReference localAEColumnReference = (AEColumnReference)localIAENode2;
        if (localAEColumnReference.getNamedRelationalExpr() == paramAETable1)
        {
          localAEColumnReference.setNamedRelationalExpr(paramAETable2);
          int i = localDSIExtJResultSet1.resolveColumn(localDSIExtJResultSet2, localAEColumnReference.getColumnNum());
          localAEColumnReference.setColumnNum(i);
        }
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/AEPassdownJoin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
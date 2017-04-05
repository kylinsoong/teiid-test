package com.simba.sqlengine.executor.materializer;

import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.queryplan.IMaterializationInfo;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.support.exceptions.ErrorException;

public class ETTableMaterializer
  extends MaterializerBase<ETTable>
{
  public ETTableMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    super(paramIQueryPlan, paramMaterializerContext);
  }
  
  public ETTable visit(AETable paramAETable)
    throws ErrorException
  {
    boolean[] arrayOfBoolean = new boolean[paramAETable.getColumnCount()];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      if (paramAETable.getDataNeeded(i)) {
        arrayOfBoolean[i] = true;
      }
    }
    ETTable localETTable = null;
    if (getQueryPlan().getMaterializationInfo(paramAETable).isCached()) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Table caching");
    }
    localETTable = new ETTable(arrayOfBoolean, paramAETable.getTable());
    getContext().setMaterializedRelation(paramAETable, localETTable);
    return localETTable;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETTableMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
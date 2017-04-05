package com.simba.sqlengine.aeprocessor.aebuilder.relation;

import com.simba.dsi.core.utilities.Variant;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public class AETableNameBuilder
  extends AEBuilderBase<AENamedRelationalExpr>
{
  private SqlDataEngine m_dataEngine;
  
  public AETableNameBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
    if (null == paramAEQueryScope) {
      throw new NullPointerException("Query scope cannot be null.");
    }
    this.m_dataEngine = paramAEQueryScope.getDataEngine();
  }
  
  public AETable visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEQTableName localAEQTableName1 = AEQTableName.fromPTNode(paramPTNonterminalNode);
    Variant localVariant = this.m_dataEngine.getProperty(15);
    String str = localVariant.getString();
    AEQTableName localAEQTableName2;
    if (str.equalsIgnoreCase("Y")) {
      localAEQTableName2 = AEUtils.adjustCatalogAndSchema(localAEQTableName1, this.m_dataEngine.getContext(), true);
    } else {
      localAEQTableName2 = AEUtils.adjustCatalogAndSchema(localAEQTableName1, this.m_dataEngine.getContext(), false);
    }
    AEQueryScope localAEQueryScope = getQueryScope();
    DSIExtJResultSet localDSIExtJResultSet = this.m_dataEngine.openTable(localAEQTableName2.getCatalogName(), localAEQTableName2.getSchemaName(), localAEQTableName2.getTableName(), localAEQueryScope.getOpenTableType());
    if (null == localDSIExtJResultSet) {
      throw new SQLEngineException(DiagState.DIAG_BASE_TABLE_OR_VIEW_MISSING, SQLEngineMessageKey.TABLE_NOT_FOUND.name(), new String[] { localAEQTableName1.toString() });
    }
    AETable localAETable = new AETable(localDSIExtJResultSet);
    getQueryScope().getDataEngine().getContext().addReferencedTable(localAETable);
    return localAETable;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/relation/AETableNameBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
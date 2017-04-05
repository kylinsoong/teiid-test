package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SqlDataEngineContext
{
  private IStatement m_statement;
  private SqlDataEngine m_dataEngine;
  private Variant m_currentCatalog;
  private boolean m_isCaseSensitive;
  private ICoercionHandler m_coercionHandler;
  private HashSet<AETable> m_tables = new HashSet();
  private Map<String, Integer> m_stringToTypeMap = null;
  private SqlCustomBehaviourProvider m_customBehaviourProvider = null;
  
  public SqlDataEngineContext(IStatement paramIStatement, SqlDataEngine paramSqlDataEngine)
    throws ErrorException
  {
    this.m_statement = paramIStatement;
    this.m_dataEngine = paramSqlDataEngine;
    IConnection localIConnection = this.m_statement.getParentConnection();
    this.m_currentCatalog = localIConnection.getProperty(22);
    this.m_coercionHandler = this.m_dataEngine.createCoercionHandler();
    this.m_isCaseSensitive = false;
    try
    {
      this.m_isCaseSensitive = (getConnProperty(57).getChar() == '\003');
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}catch (NumericOverflowException localNumericOverflowException) {}
    this.m_customBehaviourProvider = paramSqlDataEngine.createCustomBehaviorProvider();
    this.m_customBehaviourProvider.initColumnFactory(paramSqlDataEngine);
  }
  
  public Variant getConnProperty(int paramInt)
    throws ErrorException
  {
    if (22 == paramInt) {
      return this.m_currentCatalog;
    }
    return this.m_statement.getParentConnection().getProperty(paramInt);
  }
  
  public ICoercionHandler getCoercionHandler()
  {
    return this.m_coercionHandler;
  }
  
  public Variant getStmtProperty(int paramInt)
    throws ErrorException
  {
    return this.m_statement.getProperty(paramInt);
  }
  
  public Variant getDataEngineProperty(int paramInt)
    throws ErrorException
  {
    return this.m_dataEngine.getProperty(paramInt);
  }
  
  public SqlDataEngine getDataEngine()
  {
    return this.m_dataEngine;
  }
  
  public boolean isSqlCaseSensitive()
  {
    return this.m_isCaseSensitive;
  }
  
  public int getSqlTypeForTypeName(String paramString)
    throws ErrorException
  {
    assert (null != paramString);
    Object localObject = this.m_stringToTypeMap;
    if (null == localObject) {
      this.m_stringToTypeMap = (localObject = initializeStringToTypeMap());
    }
    Integer localInteger = (Integer)((Map)localObject).get(paramString);
    return null == localInteger ? 0 : localInteger.intValue();
  }
  
  public void addReferencedTable(AETable paramAETable)
    throws ErrorException
  {
    if (!this.m_tables.add(paramAETable)) {
      throw new UnsupportedOperationException("Attempt to add duplicate table to set of referenced tables.");
    }
  }
  
  public Set<AETable> getReferencedTables()
  {
    return this.m_tables;
  }
  
  private TreeMap<String, Integer> initializeStringToTypeMap()
    throws ErrorException
  {
    TreeMap localTreeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(String.valueOf(0));
    IResultSet localIResultSet = this.m_dataEngine.makeNewMetadataResult(MetadataSourceID.TYPE_INFO, localArrayList, "", "", false);
    DataWrapper localDataWrapper = new DataWrapper();
    while (localIResultSet.moveToNextRow()) {
      try
      {
        localIResultSet.getData(0, 0L, -1L, localDataWrapper);
        String str = localDataWrapper.getVarChar();
        localIResultSet.getData(1, 0L, -1L, localDataWrapper);
        int i = localDataWrapper.getSmallInt().intValue();
        localTreeMap.put(str, Integer.valueOf(i));
      }
      catch (IncorrectTypeException localIncorrectTypeException)
      {
        throw new AssertionError(localIncorrectTypeException);
      }
    }
    return localTreeMap;
  }
  
  public SqlCustomBehaviourProvider getCustomBehaviourProvider()
  {
    return this.m_customBehaviourProvider;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/SqlDataEngineContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.dsi.dataengine.impl;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.filters.DSIMetadataFilterFactory;
import com.simba.dsi.dataengine.filters.IFilter;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.dsi.dataengine.interfaces.IMetadataSource;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.dataengine.utilities.OrderType;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.exceptions.ParsingException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DSIDataEngine
  implements IDataEngine
{
  private IStatement m_statement;
  private ILogger m_logger;
  
  protected DSIDataEngine(IStatement paramIStatement)
  {
    LogUtilities.logFunctionEntrance(paramIStatement.getLog(), new Object[] { paramIStatement });
    this.m_statement = paramIStatement;
    this.m_logger = paramIStatement.getLog();
  }
  
  public void close()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
  }
  
  public IResultSet makeNewMetadataResult(MetadataSourceID paramMetadataSourceID, ArrayList<String> paramArrayList, String paramString1, String paramString2, boolean paramBoolean)
    throws ErrorException
  {
    return makeNewMetadataResult(paramMetadataSourceID, paramArrayList, paramString1, paramString2, paramBoolean, OrderType.NONE);
  }
  
  public IResultSet makeNewMetadataResult(MetadataSourceID paramMetadataSourceID, ArrayList<String> paramArrayList, String paramString1, String paramString2, boolean paramBoolean, OrderType paramOrderType)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramMetadataSourceID, paramArrayList, paramString1, paramString2, Boolean.valueOf(paramBoolean) });
    ArrayList localArrayList1 = new ArrayList(paramArrayList);
    if (MetadataSourceID.TYPE_INFO == paramMetadataSourceID) {
      mapDatetimeTypes(localArrayList1);
    }
    ArrayList localArrayList2 = new ArrayList();
    HashMap localHashMap = new HashMap();
    DSIMetadataFilterFactory localDSIMetadataFilterFactory = new DSIMetadataFilterFactory(shouldPerformFiltering());
    localDSIMetadataFilterFactory.createFilters(paramMetadataSourceID, localArrayList1, paramString1, paramString2, paramBoolean, localArrayList2, localHashMap);
    IMetadataSource localIMetadataSource = makeNewMetadataSource(paramMetadataSourceID, localHashMap, paramString1, paramString2, paramBoolean);
    return getMetadataResultImplementation(paramMetadataSourceID, localHashMap, localIMetadataSource, localArrayList2, paramOrderType);
  }
  
  public abstract IQueryExecutor prepare(String paramString)
    throws ParsingException, ErrorException;
  
  public IQueryExecutor prepareBatch(List<String> paramList)
    throws ParsingException, ErrorException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramList });
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.NOT_IMPLEMENTED.name(), ExceptionType.FEATURE_NOT_IMPLEMENTED);
  }
  
  public void setDirectExecute() {}
  
  public void setMetadataNeeded(boolean paramBoolean) {}
  
  protected ILogger getLog()
  {
    return this.m_logger;
  }
  
  protected IStatement getParentStatement()
  {
    return this.m_statement;
  }
  
  protected IWarningListener getWarningListener()
  {
    return this.m_statement.getWarningListener();
  }
  
  protected abstract IMetadataSource makeNewMetadataSource(MetadataSourceID paramMetadataSourceID, Map<MetadataSourceColumnTag, String> paramMap, String paramString1, String paramString2, boolean paramBoolean)
    throws ErrorException;
  
  private IResultSet getMetadataResultImplementation(MetadataSourceID paramMetadataSourceID, Map<MetadataSourceColumnTag, String> paramMap, IMetadataSource paramIMetadataSource, List<IFilter> paramList, OrderType paramOrderType)
    throws ErrorException
  {
    boolean bool = false;
    try
    {
      bool = 2 == this.m_statement.getParentConnection().getParentEnvironment().getProperty(3).getInt();
    }
    catch (NumericOverflowException localNumericOverflowException) {}catch (IncorrectTypeException localIncorrectTypeException) {}
    switch (paramMetadataSourceID)
    {
    case TABLES: 
    case PROCEDURES: 
    case COLUMNS: 
    case COLUMN_PRIVILEGES: 
    case PROCEDURE_COLUMNS: 
    case PRIMARY_KEYS: 
    case FOREIGN_KEYS: 
    case FUNCTION_COLUMNS_JDBC4: 
    case FUNCTIONS_JDBC4: 
    case PSEUDO_COLUMNS_JDBC41: 
    case STATISTICS: 
    case TABLE_PRIVILEGES: 
    case TYPE_INFO: 
    case SPECIAL_COLUMNS: 
    case CATALOG_ONLY: 
    case SCHEMA_ONLY: 
    case TABLETYPE_ONLY: 
    case CATALOG_SCHEMA_ONLY: 
      return new DSIMetadataResultSet(this.m_statement, paramMetadataSourceID, paramIMetadataSource, paramList, paramOrderType, bool);
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_METADATA_ID.name(), paramMetadataSourceID.toString(), ExceptionType.DEFAULT);
  }
  
  private void mapDatetimeTypes(List<String> paramList)
  {
    for (int i = 0; i < paramList.size(); i++)
    {
      String str = (String)paramList.get(i);
      if (null != str) {
        switch (Short.valueOf(str).shortValue())
        {
        case 9: 
          paramList.set(i, String.valueOf(91));
          break;
        case 10: 
          paramList.set(i, String.valueOf(92));
          break;
        case 11: 
          paramList.set(i, String.valueOf(93));
        }
      }
    }
  }
  
  private boolean shouldPerformFiltering()
  {
    try
    {
      Variant localVariant = DSIDriverSingleton.getInstance().getProperty(22);
      return 1L == localVariant.getLong();
    }
    catch (Exception localException)
    {
      LogUtilities.logError(localException, this.m_logger);
    }
    return true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/impl/DSIDataEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
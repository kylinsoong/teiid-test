package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.AttributeDataMap;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.impl.DSIDataEngine;
import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.InvalidOperationException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.exceptions.ParsingException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.sqlengine.aeprocessor.aebuilder.AETreeBuilder;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEStatements;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.aeprocessor.metadatautil.AEMetadataCoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.ddl.TableSpecification;
import com.simba.sqlengine.dsiext.dataengine.utils.Variants;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.executor.conversions.SqlConverterGenerator;
import com.simba.sqlengine.parser.DefaultLimitChecker;
import com.simba.sqlengine.parser.IPTLimitChecker;
import com.simba.sqlengine.parser.PTParser;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.utilities.AEStringLogger;
import com.simba.sqlengine.utilities.PTStringLogger;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.ILogger;
import com.simba.support.SettingReader;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public abstract class SqlDataEngine
  extends DSIDataEngine
{
  private AttributeDataMap m_engineProperties = new AttributeDataMap();
  private SqlDataEngineContext m_context = null;
  
  protected SqlDataEngine(IStatement paramIStatement)
    throws ErrorException
  {
    super(paramIStatement);
    loadProperties();
  }
  
  public ICoercionHandler createCoercionHandler()
  {
    return new AEMetadataCoercionHandler();
  }
  
  protected SqlQueryExecutor createQueryExecutor(AEStatements paramAEStatements)
    throws ErrorException
  {
    return new SqlQueryExecutor(paramAEStatements, getContext(), getLog());
  }
  
  public IMetadataHelper createMetadataHelper()
  {
    return null;
  }
  
  public DSIExtOperationHandlerFactory createOperationHandlerFactory()
  {
    return null;
  }
  
  public SqlCustomBehaviourProvider createCustomBehaviorProvider()
  {
    return new SqlCustomBehaviourProvider();
  }
  
  public SqlConverterGenerator createSqlConverterGenerator()
  {
    return Holder.INSTANCE;
  }
  
  public void createTable(TableSpecification paramTableSpecification)
    throws ErrorException
  {
    throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.CREATE_TABLE_NOT_SUPPORTED.name());
  }
  
  public boolean doesTableExist(String paramString1, String paramString2, String paramString3)
    throws ErrorException
  {
    return null != openTable(paramString1, paramString2, paramString3, OpenTableType.READ_ONLY);
  }
  
  public void dropTable(String paramString1, String paramString2, String paramString3, DropTableOption paramDropTableOption)
    throws ErrorException
  {
    throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.DROP_TABLE_NOT_SUPPORTED.name());
  }
  
  public SqlDataEngineContext getContext()
  {
    if (null == this.m_context) {
      throw new InvalidOperationException();
    }
    return this.m_context;
  }
  
  public Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException
  {
    if (null == this.m_engineProperties) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name());
    }
    if (this.m_engineProperties.isProperty(paramInt)) {
      return this.m_engineProperties.getProperty(paramInt);
    }
    throw new BadPropertyKeyException(2, DSIMessageKey.INVALID_PROPKEY.name(), String.valueOf(paramInt));
  }
  
  public AttributeDataMap getPropertyMap()
  {
    return this.m_engineProperties;
  }
  
  public abstract StoredProcedure openProcedure(String paramString1, String paramString2, String paramString3)
    throws ErrorException;
  
  public CustomScalarFunction openScalarFunction(String paramString, int paramInt)
    throws ErrorException
  {
    return null;
  }
  
  public abstract DSIExtJResultSet openTable(String paramString1, String paramString2, String paramString3, OpenTableType paramOpenTableType)
    throws ErrorException;
  
  public IQueryExecutor prepare(String paramString)
    throws ParsingException, ErrorException
  {
    IPTNode localIPTNode = null;
    if (getProperty(6).getString().equalsIgnoreCase("Y"))
    {
      localIPTNode = PTParser.parse(paramString);
    }
    else
    {
      localObject1 = new DefaultLimitChecker();
      ((DefaultLimitChecker)localObject1).initLimitCheckValues(getParentStatement().getParentConnection());
      localIPTNode = PTParser.parse(paramString, (IPTLimitChecker)localObject1);
    }
    Object localObject2;
    if (getProperty(7).getString().equalsIgnoreCase("Y"))
    {
      localObject1 = SettingReader.readSetting("LogPath");
      localObject2 = new File((String)localObject1, "ParseTree.log");
      try
      {
        PTStringLogger.writePTLogString(localIPTNode, new FileOutputStream((File)localObject2));
      }
      catch (Exception localException)
      {
        getLog().logError(getClass().getPackage().getName(), getClass().getName(), "prepare", "Could not create or write to ParseTree.log");
      }
    }
    this.m_context = new SqlDataEngineContext(getParentStatement(), this);
    Object localObject1 = AETreeBuilder.build(localIPTNode, this);
    try
    {
      if (0L != (getProperty(4).getLong() & 1L))
      {
        localObject2 = SettingReader.readSetting("LogPath");
        File localFile = new File((String)localObject2, "AETree.log");
        AEStringLogger.logAETree((IAENode)localObject1, localFile.getPath(), new String[] { new Timestamp(System.currentTimeMillis()).toString() + " :: Pre-Optimize ==============================================================" });
      }
    }
    catch (IOException localIOException)
    {
      getLog().logError(getClass().getPackage().getName(), getClass().getName(), "prepare", "Could not create or write to AETree.log");
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new AssertionError(localIncorrectTypeException);
    }
    AEStatements localAEStatements = new AEStatements();
    localAEStatements.addStatement((IAEStatement)localObject1);
    return createQueryExecutor(localAEStatements);
  }
  
  public IQueryExecutor prepareBatch(List<String> paramList)
    throws ParsingException, ErrorException
  {
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_ACTION_SQLENGINE.name());
  }
  
  public void setDirectExecute() {}
  
  public void setMetadataNeeded(boolean paramBoolean) {}
  
  public void setProperty(int paramInt, Variant paramVariant)
    throws BadAttrValException, ErrorException
  {
    if (null == this.m_engineProperties) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name());
    }
    this.m_engineProperties.setProperty(paramInt, paramVariant);
  }
  
  private void loadProperties()
  {
    this.m_engineProperties.setProperty(0, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(1, Variants.makeWString("Y"));
    this.m_engineProperties.setProperty(2, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(3, Variants.makeWString("Y"));
    try
    {
      this.m_engineProperties.setProperty(4, Variants.makeUInt32(0L));
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
    this.m_engineProperties.setProperty(5, Variants.makeWString("Y"));
    this.m_engineProperties.setProperty(6, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(7, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(8, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(9, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(10, Variants.makeWString("N"));
    this.m_engineProperties.setProperty(12, Variants.makeInt64(1000000L));
    this.m_engineProperties.setProperty(13, Variants.makeInt32(1000000));
    int i = 50;
    try
    {
      String str = System.getProperty("os.name").toLowerCase();
      if (str.contains("windows")) {
        i = 100;
      } else if ((str.contains("mac")) || (str.contains("darwin")) || (str.contains("sunos")) || (str.contains("solaris"))) {
        i = 20;
      }
    }
    catch (Throwable localThrowable) {}
    this.m_engineProperties.setProperty(16, Variants.makeInt32(i));
    this.m_engineProperties.setProperty(15, Variants.makeWString("Y"));
  }
  
  private static class Holder
  {
    public static final SqlConverterGenerator INSTANCE = new SqlConverterGenerator();
  }
  
  public static enum DropTableOption
  {
    DROP_TABLE_CASCADE,  DROP_TABLE_RESTRICT,  DROP_TABLE_UNSPECIFIED;
    
    private DropTableOption() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/SqlDataEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
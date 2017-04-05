package com.simba.jdbc.common;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.PropertyUtilities;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public abstract class SDatabaseMetaData
  implements DatabaseMetaData
{
  private static final String FUNCTION_LIST_SEPARATOR = ",";
  static final Map<Long, String> m_systemFunctionNameMap = new LinkedHashMap()
  {
    private static final long serialVersionUID = -1124807681731600000L;
  };
  static final Map<Long, String> m_numericFunctionNameMap = new LinkedHashMap()
  {
    private static final long serialVersionUID = -3392302998382835758L;
  };
  private static final int SQL_ALL_TYPES = 0;
  private static final int SQL_NO_NULLS = 0;
  private static final int SQL_NULLABLE = 1;
  private static final int SQL_BEST_ROWID = 1;
  private static final int SQL_ROWVER = 2;
  private static final int SQL_INDEX_UNIQUE = 0;
  private static final int SQL_INDEX_ALL = 1;
  private static final int SQL_QUICK = 0;
  private static final int SQL_ENSURE = 1;
  protected static final String RETURN_YES = "Y";
  protected static final String RETURN_NO = "N";
  private static final String VERSION_SEPARATOR = ".";
  private static final int JDBC_MAJOR_VERSION = 4;
  protected ArrayList<ResultSet> m_resultSets = new ArrayList();
  protected IDataEngine m_dataEngine = null;
  protected IStatement m_parentStatement = null;
  protected SConnection m_parentConnection = null;
  protected SWarningListener m_warningListener = null;
  protected ILogger m_logger = null;
  
  protected SDatabaseMetaData(SConnection paramSConnection, ILogger paramILogger)
    throws SQLException
  {
    try
    {
      this.m_logger = paramILogger;
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSConnection, paramILogger });
      this.m_parentConnection = paramSConnection;
      this.m_parentStatement = paramSConnection.getConnection().createStatement();
      this.m_warningListener = new SWarningListener(paramSConnection.getDSIConnection().getMessageSource(), null);
      this.m_parentStatement.registerWarningListener(this.m_warningListener);
      this.m_dataEngine = this.m_parentStatement.createDataEngine();
    }
    catch (Exception localException1)
    {
      if (null != this.m_parentStatement) {
        try
        {
          this.m_parentStatement.close();
        }
        catch (Exception localException2)
        {
          LogUtilities.logError(localException2, this.m_logger);
        }
      }
      SWarningListener localSWarningListener = null == this.m_warningListener ? this.m_parentConnection.getWarningListener() : this.m_warningListener;
      throw ExceptionConverter.getInstance().toSQLException(localException1, localSWarningListener, this.m_logger);
    }
  }
  
  public boolean allProceduresAreCallable()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(1);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean allTablesAreSelectable()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(2);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean dataDefinitionCausesTransactionCommit()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(136);
      return 3 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean dataDefinitionIgnoredInTransactions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(136);
      return 4 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean deletesAreDetected(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      long l = getConnectionPropertyLong(1002);
      switch (paramInt)
      {
      case 1003: 
        return 0L != (1L & l);
      case 1004: 
        return 0L != (0x8 & l);
      case 1005: 
        return 0L != (0x40 & l);
      }
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean doesMaxRowSizeIncludeBlobs()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(80);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramString4 });
      checkParentConnection();
      return createNullMetaDataResult(MetadataSourceID.ATTRIBUTES);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(String.valueOf(1));
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      switch (paramInt)
      {
      case 0: 
      case 1: 
      case 2: 
        localArrayList.add(String.valueOf(paramInt));
        break;
      default: 
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_SCOPE, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
      }
      if (paramBoolean) {
        localArrayList.add(String.valueOf(1));
      } else {
        localArrayList.add(String.valueOf(0));
      }
      return createMetaDataResult(MetadataSourceID.SPECIAL_COLUMNS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getCatalogs()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      return createMetaDataResult(MetadataSourceID.CATALOG_ONLY, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getCatalogSeparator()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(10);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getCatalogTerm()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(11);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramString4 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(paramString4);
      return createMetaDataResult(MetadataSourceID.COLUMN_PRIVILEGES, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramString4 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(paramString4);
      return createMetaDataResult(MetadataSourceID.COLUMNS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public Connection getConnection()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return this.m_parentConnection;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramString4, paramString5, paramString6 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(paramString4);
      localArrayList.add(paramString5);
      localArrayList.add(paramString6);
      return createMetaDataResult(MetadataSourceID.FOREIGN_KEYS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getDatabaseMajorVersion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(42);
      if (null == localVariant.getString()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_VERSION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { localVariant.getString() });
      }
      StringTokenizer localStringTokenizer = new StringTokenizer(localVariant.getString(), ".");
      String str = localStringTokenizer.nextToken();
      return Integer.parseInt(str);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getDatabaseMinorVersion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(42);
      if (null == localVariant.getString()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_VERSION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { localVariant.getString() });
      }
      StringTokenizer localStringTokenizer = new StringTokenizer(localVariant.getString(), ".");
      localStringTokenizer.nextToken();
      String str = localStringTokenizer.nextToken();
      return Integer.parseInt(str);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getDatabaseProductName()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(41);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getDatabaseProductVersion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(42);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getDefaultTransactionIsolation()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(45);
      if (0L == l) {
        return 0;
      }
      if (2L == l) {
        return 2;
      }
      if (1L == l) {
        return 1;
      }
      if (4L == l) {
        return 4;
      }
      if (8L == l) {
        return 8;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_ISOLATION, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(l) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getDriverMajorVersion()
  {
    int i = 1;
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(5);
      if (null == localVariant.getString()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_VERSION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { localVariant.getString() });
      }
      StringTokenizer localStringTokenizer = new StringTokenizer(localVariant.getString(), ".");
      String str = localStringTokenizer.nextToken();
      i = Integer.parseInt(str);
    }
    catch (Exception localException)
    {
      ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
    return i;
  }
  
  public int getDriverMinorVersion()
  {
    int i = 0;
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(5);
      if (null == localVariant.getString()) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_VERSION, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { localVariant.getString() });
      }
      StringTokenizer localStringTokenizer = new StringTokenizer(localVariant.getString(), ".");
      localStringTokenizer.nextToken();
      String str = localStringTokenizer.nextToken();
      i = Integer.parseInt(str);
    }
    catch (Exception localException)
    {
      ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
    return i;
  }
  
  public String getDriverName()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(3);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getDriverVersion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(5);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      return createMetaDataResult(MetadataSourceID.FOREIGN_KEYS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getExtraNameCharacters()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(102);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getIdentifierQuoteString()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(58);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      return createMetaDataResult(MetadataSourceID.FOREIGN_KEYS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2) });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      if (paramBoolean1) {
        localArrayList.add(String.valueOf(0));
      } else {
        localArrayList.add(String.valueOf(1));
      }
      if (paramBoolean2) {
        localArrayList.add(String.valueOf(0));
      } else {
        localArrayList.add(String.valueOf(1));
      }
      return createMetaDataResult(MetadataSourceID.STATISTICS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxBinaryLiteralLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(65);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxCatalogNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(66);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxCharLiteralLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(67);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxColumnNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(68);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxColumnsInGroupBy()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(69);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxColumnsInIndex()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(70);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxColumnsInOrderBy()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(71);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxColumnsInSelect()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(72);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxColumnsInTable()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(73);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxConnections()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getEnvironmentPropertyVariant(2);
      return localVariant.getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxCursorNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(75);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxIndexLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(77);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxProcedureNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(78);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxRowSize()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(79);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxSchemaNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(81);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxStatementLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(82);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxStatements()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(74);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxTableNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(83);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxTablesInSelect()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(84);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getMaxUserNameLength()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return getConnectionPropertyInt(85);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getNumericFunctions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(90);
      return createListFromMap(m_numericFunctionNameMap, l);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      return createMetaDataResult(MetadataSourceID.PRIMARY_KEYS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramString4 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(paramString4);
      return createMetaDataResult(MetadataSourceID.PROCEDURE_COLUMNS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getProcedures(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      return createMetaDataResult(MetadataSourceID.PROCEDURES, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getProcedureTerm()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(96);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getResultSetHoldability()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(37);
      if ((1L == l) || (0L == l)) {
        return 2;
      }
      if (2L == l) {
        return 1;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_HOLDABILITY, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(l) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getSchemas()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      if (supportsCatalogs()) {
        return createMetaDataResult(MetadataSourceID.CATALOG_SCHEMA_ONLY, localArrayList);
      }
      return createMetaDataResult(MetadataSourceID.SCHEMA_ONLY, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getSchemas(String paramString1, String paramString2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(null);
      localArrayList.add(null);
      if (supportsCatalogs()) {
        return createMetaDataResult(MetadataSourceID.CATALOG_SCHEMA_ONLY, localArrayList);
      }
      return createMetaDataResult(MetadataSourceID.SCHEMA_ONLY, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getSchemaTerm()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(99);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getSearchStringEscape()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(151);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getSQLKeywords()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(62);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getSQLStateType()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(13);
      long l = localVariant.getLong();
      if (2L == l) {
        return 2;
      }
      if (1L == l) {
        return 1;
      }
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_SQLSTATE_TYPE, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(l) });
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public abstract String getStringFunctions()
    throws SQLException;
  
  public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      return createNullMetaDataResult(MetadataSourceID.SUPERTABLES);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      return createNullMetaDataResult(MetadataSourceID.SUPERTYPES);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getSystemFunctions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(131);
      return createListFromMap(m_systemFunctionNameMap, l);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      return createMetaDataResult(MetadataSourceID.TABLE_PRIVILEGES, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramArrayOfString });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      if (null != paramArrayOfString)
      {
        if (paramArrayOfString.length > 0)
        {
          StringBuffer localStringBuffer = new StringBuffer();
          for (int i = 0; i < paramArrayOfString.length; i++)
          {
            localStringBuffer.append(",");
            localStringBuffer.append(paramArrayOfString[i]);
          }
          localArrayList.add(localStringBuffer.substring(1));
        }
        else
        {
          localArrayList.add("");
        }
      }
      else {
        localArrayList.add(null);
      }
      return createMetaDataResult(MetadataSourceID.TABLES, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getTableTypes()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      localArrayList.add(null);
      return createMetaDataResult(MetadataSourceID.TABLETYPE_ONLY, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public abstract String getTimeDateFunctions()
    throws SQLException;
  
  public ResultSet getTypeInfo()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(String.valueOf(0));
      return createMetaDataResult(MetadataSourceID.TYPE_INFO, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramArrayOfInt });
      checkParentConnection();
      return createNullMetaDataResult(MetadataSourceID.UDT);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getURL()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return this.m_parentConnection.getURL();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public String getUserName()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(139);
      return localVariant.getString();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(String.valueOf(2));
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(String.valueOf(0));
      localArrayList.add(String.valueOf(1));
      return createUnorderedMetaDataResult(MetadataSourceID.SPECIAL_COLUMNS, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean insertsAreDetected(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      long l = getConnectionPropertyLong(1002);
      switch (paramInt)
      {
      case 1003: 
        return 0L != (0x2 & l);
      case 1004: 
        return 0L != (0x10 & l);
      case 1005: 
        return 0L != (0x80 & l);
      }
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isCatalogAtStart()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(8);
      return 1L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isReadOnly()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(40);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean locatorsUpdateCopy()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean nullPlusNonNullIsNull()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(15);
      return 0L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean nullsAreSortedAtEnd()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(89);
      return 4L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean nullsAreSortedAtStart()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(89);
      return 2L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean nullsAreSortedHigh()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(89);
      return 0L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean nullsAreSortedLow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(89);
      return 1L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean othersDeletesAreVisible(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean othersInsertsAreVisible(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean othersUpdatesAreVisible(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean ownDeletesAreVisible(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean ownInsertsAreVisible(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean ownUpdatesAreVisible(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean storesLowerCaseIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(57);
      return 2 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean storesLowerCaseQuotedIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(98);
      return 2 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean storesMixedCaseIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(57);
      return 4 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean storesMixedCaseQuotedIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(98);
      return 4 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean storesUpperCaseIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(57);
      return 1 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean storesUpperCaseQuotedIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(98);
      return 1 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsAlterTableWithAddColumn()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(6);
      return (0x40 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsAlterTableWithDropColumn()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(6);
      return (0x200 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsANSI92EntryLevelSQL()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(103);
      return 1L <= i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsANSI92FullSQL()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(103);
      return 8L <= i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsANSI92IntermediateSQL()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(103);
      return 4L <= i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsBatchUpdates()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(1002);
      return 1 == localVariant.getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCatalogsInDataManipulation()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(12);
      return (1L & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCatalogsInIndexDefinitions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(12);
      return (0x8 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCatalogsInPrivilegeDefinitions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(12);
      return (0x10 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCatalogsInProcedureCalls()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(12);
      return (0x2 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCatalogsInTableDefinitions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(12);
      return (0x4 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsColumnAliasing()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(14);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsConvert()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(27);
      return (1L & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsConvert(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      checkParentConnection();
      int i = 0;
      long l1 = 0L;
      switch (paramInt1)
      {
      case -5: 
        i = 106;
        break;
      case -2: 
        i = 107;
        break;
      case -7: 
      case 16: 
        i = 108;
        break;
      case 1: 
        i = 109;
        break;
      case 91: 
        i = 111;
        break;
      case 3: 
        i = 112;
        break;
      case 8: 
        i = 113;
        break;
      case 6: 
        i = 114;
        break;
      case 4: 
        i = 115;
        break;
      case -4: 
        i = 118;
        break;
      case -1: 
        i = 119;
        break;
      case 2: 
        i = 120;
        break;
      case 7: 
        i = 121;
        break;
      case 5: 
        i = 122;
        break;
      case 92: 
        i = 123;
        break;
      case 93: 
        i = 124;
        break;
      case -6: 
        i = 125;
        break;
      case -3: 
        i = 126;
        break;
      case 12: 
        i = 127;
        break;
      case 103: 
      case 104: 
      case 105: 
      case 106: 
      case 108: 
      case 109: 
      case 110: 
      case 111: 
      case 112: 
      case 113: 
        i = 117;
        break;
      case 101: 
      case 102: 
      case 107: 
        i = 116;
        break;
      case -8: 
        i = 128;
        break;
      case -10: 
        i = 129;
        break;
      case -9: 
        i = 130;
        break;
      case -11: 
        i = 110;
        break;
      case 0: 
      case 9: 
      case 10: 
      case 11: 
      case 13: 
      case 14: 
      case 15: 
      case 17: 
      case 18: 
      case 19: 
      case 20: 
      case 21: 
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 26: 
      case 27: 
      case 28: 
      case 29: 
      case 30: 
      case 31: 
      case 32: 
      case 33: 
      case 34: 
      case 35: 
      case 36: 
      case 37: 
      case 38: 
      case 39: 
      case 40: 
      case 41: 
      case 42: 
      case 43: 
      case 44: 
      case 45: 
      case 46: 
      case 47: 
      case 48: 
      case 49: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 54: 
      case 55: 
      case 56: 
      case 57: 
      case 58: 
      case 59: 
      case 60: 
      case 61: 
      case 62: 
      case 63: 
      case 64: 
      case 65: 
      case 66: 
      case 67: 
      case 68: 
      case 69: 
      case 70: 
      case 71: 
      case 72: 
      case 73: 
      case 74: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
      case 87: 
      case 88: 
      case 89: 
      case 90: 
      case 94: 
      case 95: 
      case 96: 
      case 97: 
      case 98: 
      case 99: 
      case 100: 
      default: 
        return false;
      }
      switch (paramInt2)
      {
      case -5: 
        l1 = 16384L;
        break;
      case -2: 
        l1 = 1024L;
        break;
      case -7: 
        l1 = 4096L;
        break;
      case 1: 
        l1 = 1L;
        break;
      case 91: 
        l1 = 32768L;
        break;
      case 3: 
        l1 = 4L;
        break;
      case 8: 
        l1 = 128L;
        break;
      case 6: 
        l1 = 32L;
        break;
      case 4: 
        l1 = 8L;
        break;
      case -4: 
        l1 = 262144L;
        break;
      case -1: 
        l1 = 512L;
        break;
      case 2: 
        l1 = 2L;
        break;
      case 7: 
        l1 = 64L;
        break;
      case 5: 
        l1 = 16L;
        break;
      case 92: 
        l1 = 65536L;
        break;
      case 93: 
        l1 = 131072L;
        break;
      case -6: 
        l1 = 8192L;
        break;
      case -3: 
        l1 = 2048L;
        break;
      case 12: 
        l1 = 256L;
        break;
      case 103: 
      case 104: 
      case 105: 
      case 106: 
      case 108: 
      case 109: 
      case 110: 
      case 111: 
      case 112: 
      case 113: 
        l1 = 1048576L;
        break;
      case 101: 
      case 102: 
      case 107: 
        l1 = 524288L;
        break;
      case -8: 
        l1 = 2097152L;
        break;
      case -10: 
        l1 = 4194304L;
        break;
      case -9: 
        l1 = 8388608L;
        break;
      case -11: 
        l1 = 16777216L;
        break;
      case 0: 
      case 9: 
      case 10: 
      case 11: 
      case 13: 
      case 14: 
      case 15: 
      case 16: 
      case 17: 
      case 18: 
      case 19: 
      case 20: 
      case 21: 
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 26: 
      case 27: 
      case 28: 
      case 29: 
      case 30: 
      case 31: 
      case 32: 
      case 33: 
      case 34: 
      case 35: 
      case 36: 
      case 37: 
      case 38: 
      case 39: 
      case 40: 
      case 41: 
      case 42: 
      case 43: 
      case 44: 
      case 45: 
      case 46: 
      case 47: 
      case 48: 
      case 49: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 54: 
      case 55: 
      case 56: 
      case 57: 
      case 58: 
      case 59: 
      case 60: 
      case 61: 
      case 62: 
      case 63: 
      case 64: 
      case 65: 
      case 66: 
      case 67: 
      case 68: 
      case 69: 
      case 70: 
      case 71: 
      case 72: 
      case 73: 
      case 74: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
      case 87: 
      case 88: 
      case 89: 
      case 90: 
      case 94: 
      case 95: 
      case 96: 
      case 97: 
      case 98: 
      case 99: 
      case 100: 
      default: 
        return false;
      }
      long l2 = getConnectionPropertyLong(i);
      return (l1 & l2) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCoreSQLGrammar()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(91);
      return 1 <= i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsCorrelatedSubqueries()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(105);
      return (0x10 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsDataDefinitionAndDataManipulationTransactions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(136);
      return 2 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsDataManipulationTransactionsOnly()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(136);
      return 1 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsDifferentTableCorrelationNames()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(28);
      return 1L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsExpressionsInOrderBy()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(55);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsExtendedSQLGrammar()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(91);
      return 2 <= i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsFullOuterJoins()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(92);
      int i = (0x4 & l) != 0L ? 1 : 0;
      int j = (0x8 & l) != 0L ? 1 : 0;
      return (i != 0) && (j != 0);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsGetGeneratedKeys()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsGroupBy()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(56);
      return 0L != i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsGroupByBeyondSelect()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(56);
      return 2L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsGroupByUnrelated()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(56);
      return 3L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsIntegrityEnhancementFacility()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(61);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsLikeEscapeClause()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(63);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsLimitedOuterJoins()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(94);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsMinimumSQLGrammar()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(91);
      return 0 <= i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsMixedCaseIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(57);
      return 3 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsMixedCaseQuotedIdentifiers()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(98);
      return 3 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsMultipleOpenResults()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsMultipleResultSets()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(86);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsMultipleTransactions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(87);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsNamedParameters()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(1003);
      return 1 == localVariant.getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsNonNullableColumns()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(88);
      return 1L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsOpenCursorsAcrossCommit()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(37);
      return 2L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsOpenCursorsAcrossRollback()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(38);
      return 2L == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsOpenStatementsAcrossCommit()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(37);
      return 0L != i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsOpenStatementsAcrossRollback()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(38);
      return 0L != i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsOrderByUnrelated()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getConnectionPropertyVariant(93);
      return localVariant.getString().equals("Y");
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsOuterJoins()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return supportsLimitedOuterJoins();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsPositionedDelete()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsPositionedUpdate()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      checkParentConnection();
      if (1003 != paramInt1) {
        return false;
      }
      if (1007 == paramInt2) {
        return true;
      }
      long l = getConnectionPropertyLong(1001);
      return 0L != l;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsResultSetHoldability(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      int i = getConnectionPropertyInt(37);
      boolean bool = false;
      switch (paramInt)
      {
      case 1: 
        bool = 2L == i;
        break;
      case 2: 
        bool = (1L == i) || (0L == i);
      }
      return bool;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsResultSetType(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      return 1003 == paramInt;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSavepoints()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return PropertyUtilities.hasSavepointSupport(this.m_parentConnection.getConnection());
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSchemasInDataManipulation()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(100);
      return (1L & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSchemasInIndexDefinitions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(100);
      return (0x8 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSchemasInPrivilegeDefinitions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(100);
      return (0x10 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSchemasInProcedureCalls()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(100);
      return (0x2 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSchemasInTableDefinitions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(100);
      return (0x4 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSelectForUpdate()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(1004);
      return 1 == localVariant.getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsStatementPooling()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsStoredProcedures()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return PropertyUtilities.hasStoredProcedureSupport(this.m_parentConnection.getConnection());
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSubqueriesInComparisons()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(105);
      return (1L & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSubqueriesInExists()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(105);
      return (0x2 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSubqueriesInIns()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(105);
      return (0x4 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsSubqueriesInQuantifieds()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(105);
      return (0x8 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsTableCorrelationNames()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(28);
      return 0L != i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsTransactionIsolationLevel(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      long l = getConnectionPropertyLong(137);
      switch (paramInt)
      {
      case 0: 
        return 0L == l;
      case 2: 
        return (0x2 & l) != 0L;
      case 1: 
        return (1L & l) != 0L;
      case 4: 
        return (0x4 & l) != 0L;
      case 8: 
        return (0x8 & l) != 0L;
      }
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsTransactions()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(136);
      return 0 != i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsUnion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(138);
      return (1L & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean supportsUnionAll()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      long l = getConnectionPropertyLong(138);
      return (0x2 & l) != 0L;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean updatesAreDetected(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkParentConnection();
      long l = getConnectionPropertyLong(1002);
      switch (paramInt)
      {
      case 1003: 
        return 0L != (0x4 & l);
      case 1004: 
        return 0L != (0x20 & l);
      case 1005: 
        return 0L != (0x100 & l);
      }
      return false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean usesLocalFilePerTable()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(6);
      int i = localVariant.getInt();
      return 1 == i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean usesLocalFiles()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      Variant localVariant = getDriverPropertyVariant(6);
      int i = localVariant.getInt();
      return 0 != i;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean autoCommitFailureClosesAllResultSets()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      int i = getConnectionPropertyInt(37);
      return (1L == i) || (0L == i);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3, paramString4 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      localArrayList.add(paramString4);
      return createMetaDataResult(MetadataSourceID.FUNCTION_COLUMNS_JDBC4, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public ResultSet getFunctions(String paramString1, String paramString2, String paramString3)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString1, paramString2, paramString3 });
      checkParentConnection();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString1);
      localArrayList.add(paramString2);
      localArrayList.add(paramString3);
      return createMetaDataResult(MetadataSourceID.FUNCTIONS_JDBC4, localArrayList);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public RowIdLifetime getRowIdLifetime()
    throws SQLException
  {
    return RowIdLifetime.ROWID_UNSUPPORTED;
  }
  
  public int getJDBCMajorVersion()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return 4;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public boolean supportsStoredFunctionsUsingCallSyntax()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkParentConnection();
      return PropertyUtilities.hasStoredFunctionsCallsSupport(this.m_parentConnection.getConnection());
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  public static String getReturnNo()
  {
    return "N";
  }
  
  protected synchronized void closeMetaData()
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null != this.m_dataEngine)
    {
      this.m_dataEngine.close();
      try
      {
        this.m_parentStatement.close();
      }
      catch (ErrorException localErrorException)
      {
        LogUtilities.logError(localErrorException, this.m_logger);
      }
      this.m_dataEngine = null;
      this.m_parentStatement = null;
    }
    this.m_parentConnection = null;
    if (null != this.m_resultSets)
    {
      for (int i = this.m_resultSets.size() - 1; i >= 0; i--) {
        try
        {
          ((ResultSet)this.m_resultSets.get(i)).close();
        }
        catch (Exception localException) {}
      }
      this.m_resultSets.clear();
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    closeMetaData();
  }
  
  protected IConnection getDSIConnection()
  {
    return this.m_parentConnection.getConnection();
  }
  
  protected IStatement getDSIStatement()
  {
    return this.m_parentStatement;
  }
  
  protected synchronized void removeResultSet(ResultSet paramResultSet)
  {
    this.m_resultSets.remove(paramResultSet);
  }
  
  protected synchronized void checkParentConnection()
    throws SQLException
  {
    if (null == this.m_parentConnection) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DBMETA_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
    }
    if (this.m_parentConnection.isClosed())
    {
      closeMetaData();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARENT_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT_CONNECTION, new Object[0]);
    }
  }
  
  protected String createListFromMap(Map<Long, String> paramMap, long paramLong)
    throws SQLException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      long l = ((Long)localIterator.next()).longValue();
      String str = (String)paramMap.get(Long.valueOf(l & paramLong));
      if (null != str) {
        localStringBuffer.append(",").append(str);
      }
    }
    if (localStringBuffer.length() > 0) {
      return localStringBuffer.toString().substring(1);
    }
    return "";
  }
  
  protected abstract ResultSet createMetaDataResult(MetadataSourceID paramMetadataSourceID, List<String> paramList)
    throws SQLException;
  
  protected abstract ResultSet createNullMetaDataResult(MetadataSourceID paramMetadataSourceID)
    throws SQLException;
  
  protected abstract ResultSet createUnorderedMetaDataResult(MetadataSourceID paramMetadataSourceID, List<String> paramList)
    throws SQLException;
  
  protected int getConnectionPropertyInt(int paramInt)
    throws SQLException
  {
    Variant localVariant = getConnectionPropertyVariant(paramInt);
    try
    {
      return localVariant.getInt();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_parentStatement.getWarningListener());
    }
  }
  
  protected long getConnectionPropertyLong(int paramInt)
    throws SQLException
  {
    Variant localVariant = getConnectionPropertyVariant(paramInt);
    try
    {
      return localVariant.getLong();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_parentStatement.getWarningListener());
    }
  }
  
  private Variant getConnectionPropertyVariant(int paramInt)
    throws SQLException
  {
    try
    {
      IConnection localIConnection = this.m_parentConnection.getConnection();
      return localIConnection.getProperty(paramInt);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_parentStatement.getWarningListener());
    }
  }
  
  private Variant getDriverPropertyVariant(int paramInt)
    throws SQLException
  {
    try
    {
      IDriver localIDriver = DSIDriverSingleton.getInstance();
      return localIDriver.getProperty(paramInt);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_parentStatement.getWarningListener());
    }
  }
  
  private Variant getEnvironmentPropertyVariant(int paramInt)
    throws SQLException
  {
    try
    {
      IEnvironment localIEnvironment = this.m_parentConnection.getConnection().getParentEnvironment();
      return localIEnvironment.getProperty(paramInt);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_parentStatement.getWarningListener());
    }
  }
  
  private boolean supportsCatalogs()
    throws SQLException
  {
    if ("N".equals(getConnectionPropertyVariant(9).getString())) {
      return false;
    }
    if ("".equals(getConnectionPropertyVariant(11).getString())) {
      return false;
    }
    return 0L != getConnectionPropertyLong(12);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SDatabaseMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.JDBCVersion;
import com.simba.utilities.MetaDataFactory;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public abstract class SMetaDataProxy
  extends SForwardResultSet
{
  private MetadataSourceID m_metadataType = null;
  private int[] m_columnMap = null;
  private int m_dataTypeColIndex = -1;
  private DatabaseMetaData m_parentMetaData = null;
  protected IWarningListener m_warningListener = null;
  
  protected SMetaDataProxy(DatabaseMetaData paramDatabaseMetaData, IResultSet paramIResultSet, MetadataSourceID paramMetadataSourceID, ILogger paramILogger, JDBCVersion paramJDBCVersion)
    throws SQLException
  {
    super(null, paramIResultSet, paramILogger);
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramDatabaseMetaData, paramIResultSet, paramMetadataSourceID, paramILogger });
      this.m_parentMetaData = paramDatabaseMetaData;
      this.m_metadataType = paramMetadataSourceID;
      this.m_warningListener = super.getWarningListener();
      this.m_warningListener.setLocale(getParentConnection().getLocale());
      this.m_jdbcVersion = paramJDBCVersion;
      this.m_streamBufferSize = getStreamBufferSize();
      initializeColumnMap(paramMetadataSourceID);
      initializeDataTypeColIndex(paramMetadataSourceID);
      initializeColumnNameMap();
      if ((MetadataSourceID.CATALOG_ONLY == this.m_metadataType) || (MetadataSourceID.CATALOG_SCHEMA_ONLY == this.m_metadataType) || (MetadataSourceID.SCHEMA_ONLY == this.m_metadataType) || (MetadataSourceID.TABLETYPE_ONLY == this.m_metadataType)) {
        while (this.m_cachedDataWrappers.size() < 4) {
          this.m_cachedDataWrappers.add(new DataWrapper());
        }
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public synchronized void close()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (null != this.m_parentMetaData)
    {
      ((SDatabaseMetaData)this.m_parentMetaData).removeResultSet(this);
      this.m_parentMetaData = null;
    }
    super.close();
  }
  
  public Array getArray(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getArray(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public InputStream getAsciiStream(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getAsciiStream(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public BigDecimal getBigDecimal(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return BigDecimal.valueOf(TypeUtilities.mapDataTypes(super.getInt(i)));
      }
      return super.getBigDecimal(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  /**
   * @deprecated
   */
  public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    try
    {
      if (shouldReturnNull(paramInt1))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      int i = getMappedColumnIndex(paramInt1);
      if (i == this.m_dataTypeColIndex) {
        return BigDecimal.valueOf(TypeUtilities.mapDataTypes(super.getInt(i)), paramInt2);
      }
      return super.getBigDecimal(i, paramInt2);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public InputStream getBinaryStream(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getBinaryStream(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public Blob getBlob(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getBlob(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public boolean getBoolean(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return false;
      }
      return super.getBoolean(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public byte getByte(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return 0;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return (byte)TypeUtilities.mapDataTypes(super.getInt(i));
      }
      return super.getByte(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public byte[] getBytes(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getBytes(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public Reader getCharacterStream(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getCharacterStream(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public Clob getClob(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getClob(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public Date getDate(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getDate(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public Date getDate(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramCalendar });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getDate(getMappedColumnIndex(paramInt), paramCalendar);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public double getDouble(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return 0.0D;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return TypeUtilities.mapDataTypes(super.getInt(i));
      }
      return super.getDouble(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public float getFloat(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return 0.0F;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return TypeUtilities.mapDataTypes(super.getInt(i));
      }
      return super.getFloat(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public int getHoldability()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      return this.m_parentMetaData.getConnection().getHoldability();
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public int getInt(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return 0;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return TypeUtilities.mapDataTypes(super.getInt(i));
      }
      return super.getInt(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public long getLong(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return 0L;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return TypeUtilities.mapDataTypes(super.getInt(i));
      }
      return super.getLong(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  protected void generateMetadataList()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      List localList = null;
      switch (this.m_metadataType)
      {
      case ATTRIBUTES: 
        localList = MetaDataFactory.createAttributeMetaData();
        break;
      case CATALOG_ONLY: 
        localList = MetaDataFactory.createCatalogOnlyMetaData();
        break;
      case COLUMN_PRIVILEGES: 
        localList = MetaDataFactory.createColumnPrivilegesMetaData();
        break;
      case COLUMNS: 
        localList = MetaDataFactory.createColumnsMetaData(this.m_jdbcVersion);
        break;
      case FOREIGN_KEYS: 
        localList = MetaDataFactory.createForeignKeysMetaData();
        break;
      case FUNCTION_COLUMNS_JDBC4: 
        localList = MetaDataFactory.createFunctionColumnsMetaData();
        break;
      case FUNCTIONS_JDBC4: 
        localList = MetaDataFactory.createFunctionsMetaData();
        break;
      case STATISTICS: 
        localList = MetaDataFactory.createIndexInfoMetaData();
        break;
      case PRIMARY_KEYS: 
        localList = MetaDataFactory.createPrimaryKeysMetaData();
        break;
      case PROCEDURE_COLUMNS: 
        localList = MetaDataFactory.createProcedureColumnsMetaData(this.m_jdbcVersion);
        break;
      case PROCEDURES: 
        localList = MetaDataFactory.createProceduresMetaData(this.m_jdbcVersion);
        break;
      case PSEUDO_COLUMNS_JDBC41: 
        if (JDBCVersion.JDBC4 == this.m_jdbcVersion) {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_METADATA_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(this.m_metadataType) });
        }
        localList = MetaDataFactory.createPseudoColumnsMetaData();
        break;
      case SCHEMA_ONLY: 
      case CATALOG_SCHEMA_ONLY: 
        localList = MetaDataFactory.createSchemasOnlyMetaData();
        break;
      case SPECIAL_COLUMNS: 
        localList = MetaDataFactory.createSpecialColumnsMetaData();
        break;
      case SUPERTABLES: 
        localList = MetaDataFactory.createSuperTablesMetaData();
        break;
      case SUPERTYPES: 
        localList = MetaDataFactory.createSuperTypesMetaData();
        break;
      case TABLE_PRIVILEGES: 
        localList = MetaDataFactory.createTablePrivilegesMetaData();
        break;
      case TABLETYPE_ONLY: 
        localList = MetaDataFactory.createTableTypesMetaData();
        break;
      case TABLES: 
        localList = MetaDataFactory.createTablesMetaData();
        break;
      case TYPE_INFO: 
        localList = MetaDataFactory.createTypeInfoMetaData();
        break;
      case UDT: 
        localList = MetaDataFactory.createUDTMetaData();
        break;
      default: 
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.UNKNOWN_METADATA_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(this.m_metadataType) });
      }
      this.m_resultSetColumns = localList;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  /* Error */
  public Object getObject(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 8	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 35	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 9	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 61	com/simba/jdbc/common/SMetaDataProxy:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 94	com/simba/jdbc/common/SMetaDataProxy:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: invokespecial 36	com/simba/jdbc/common/SMetaDataProxy:shouldReturnNull	(I)Z
    //   31: ifeq +10 -> 41
    //   34: aload_0
    //   35: iconst_1
    //   36: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   39: aconst_null
    //   40: areturn
    //   41: aload_0
    //   42: iload_1
    //   43: invokespecial 38	com/simba/jdbc/common/SMetaDataProxy:getMappedColumnIndex	(I)I
    //   46: istore_2
    //   47: iload_2
    //   48: aload_0
    //   49: getfield 4	com/simba/jdbc/common/SMetaDataProxy:m_dataTypeColIndex	I
    //   52: if_icmpne +15 -> 67
    //   55: aload_0
    //   56: iload_2
    //   57: invokespecial 43	com/simba/jdbc/common/SForwardResultSet:getInt	(I)I
    //   60: invokestatic 44	com/simba/dsi/dataengine/utilities/TypeUtilities:mapDataTypes	(I)I
    //   63: invokestatic 35	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   66: areturn
    //   67: aload_0
    //   68: iload_2
    //   69: ldc2_w 95
    //   72: invokevirtual 97	com/simba/jdbc/common/SMetaDataProxy:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   75: astore_3
    //   76: aload_0
    //   77: getfield 93	com/simba/jdbc/common/SMetaDataProxy:m_resultSetColumns	Ljava/util/List;
    //   80: iload_1
    //   81: iconst_1
    //   82: isub
    //   83: invokeinterface 98 2 0
    //   88: checkcast 99	com/simba/dsi/dataengine/interfaces/IColumn
    //   91: astore 4
    //   93: aload_3
    //   94: aload 4
    //   96: invokeinterface 100 1 0
    //   101: aload_0
    //   102: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   105: invokestatic 101	com/simba/utilities/conversion/TypeConverter:toObject	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/lang/Object;
    //   108: astore 5
    //   110: aload_0
    //   111: aload_3
    //   112: invokevirtual 102	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   115: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   118: aload 5
    //   120: areturn
    //   121: astore 4
    //   123: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   126: getstatic 104	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   129: aload_0
    //   130: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   133: getstatic 80	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   136: iconst_2
    //   137: anewarray 8	java/lang/Object
    //   140: dup
    //   141: iconst_0
    //   142: iload_1
    //   143: invokestatic 105	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   146: aastore
    //   147: dup
    //   148: iconst_1
    //   149: sipush 2000
    //   152: invokestatic 106	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   155: aastore
    //   156: invokevirtual 82	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   159: athrow
    //   160: astore_2
    //   161: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   164: aload_2
    //   165: aload_0
    //   166: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   169: aload_0
    //   170: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   173: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   176: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	177	0	this	SMetaDataProxy
    //   0	177	1	paramInt	int
    //   46	23	2	i	int
    //   160	5	2	localException	Exception
    //   75	37	3	localDataWrapper	DataWrapper
    //   91	4	4	localIColumn	com.simba.dsi.dataengine.interfaces.IColumn
    //   121	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    //   108	11	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   76	120	121	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	40	160	java/lang/Exception
    //   41	66	160	java/lang/Exception
    //   67	120	160	java/lang/Exception
    //   121	160	160	java/lang/Exception
  }
  
  public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramMap });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getObject(getMappedColumnIndex(paramInt), paramMap);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public Ref getRef(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getRef(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public short getShort(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return 0;
      }
      int i = getMappedColumnIndex(paramInt);
      if (i == this.m_dataTypeColIndex) {
        return (short)TypeUtilities.mapDataTypes(super.getInt(i));
      }
      return super.getShort(i);
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  /* Error */
  public String getString(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 8	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 35	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 9	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 61	com/simba/jdbc/common/SMetaDataProxy:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 94	com/simba/jdbc/common/SMetaDataProxy:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: invokespecial 36	com/simba/jdbc/common/SMetaDataProxy:shouldReturnNull	(I)Z
    //   31: ifeq +10 -> 41
    //   34: aload_0
    //   35: iconst_1
    //   36: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   39: aconst_null
    //   40: areturn
    //   41: aload_0
    //   42: iload_1
    //   43: invokespecial 38	com/simba/jdbc/common/SMetaDataProxy:getMappedColumnIndex	(I)I
    //   46: istore_2
    //   47: iload_2
    //   48: aload_0
    //   49: getfield 4	com/simba/jdbc/common/SMetaDataProxy:m_dataTypeColIndex	I
    //   52: if_icmpne +15 -> 67
    //   55: aload_0
    //   56: iload_2
    //   57: invokespecial 43	com/simba/jdbc/common/SForwardResultSet:getInt	(I)I
    //   60: invokestatic 44	com/simba/dsi/dataengine/utilities/TypeUtilities:mapDataTypes	(I)I
    //   63: invokestatic 105	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   66: areturn
    //   67: aload_0
    //   68: iload_2
    //   69: ldc2_w 95
    //   72: invokevirtual 97	com/simba/jdbc/common/SMetaDataProxy:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   75: astore_3
    //   76: aload_0
    //   77: aload_3
    //   78: invokevirtual 102	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   81: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   84: aload_0
    //   85: getfield 93	com/simba/jdbc/common/SMetaDataProxy:m_resultSetColumns	Ljava/util/List;
    //   88: iload_1
    //   89: iconst_1
    //   90: isub
    //   91: invokeinterface 98 2 0
    //   96: checkcast 99	com/simba/dsi/dataengine/interfaces/IColumn
    //   99: astore 4
    //   101: aload_3
    //   102: aload 4
    //   104: invokeinterface 100 1 0
    //   109: invokestatic 110	com/simba/utilities/conversion/TypeConverter:toString	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;)Ljava/lang/String;
    //   112: areturn
    //   113: astore 4
    //   115: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   118: getstatic 104	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   121: aload_0
    //   122: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   125: getstatic 80	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   128: iconst_2
    //   129: anewarray 8	java/lang/Object
    //   132: dup
    //   133: iconst_0
    //   134: iload_1
    //   135: invokestatic 105	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   138: aastore
    //   139: dup
    //   140: iconst_1
    //   141: sipush 2000
    //   144: invokestatic 106	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   147: aastore
    //   148: invokevirtual 82	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   151: athrow
    //   152: astore_2
    //   153: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   156: aload_2
    //   157: aload_0
    //   158: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   161: aload_0
    //   162: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   165: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   168: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	169	0	this	SMetaDataProxy
    //   0	169	1	paramInt	int
    //   46	23	2	i	int
    //   152	5	2	localException	Exception
    //   75	27	3	localDataWrapper	DataWrapper
    //   99	4	4	localIColumn	com.simba.dsi.dataengine.interfaces.IColumn
    //   113	1	4	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   76	112	113	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	40	152	java/lang/Exception
    //   41	66	152	java/lang/Exception
    //   67	112	152	java/lang/Exception
    //   113	152	152	java/lang/Exception
  }
  
  /* Error */
  public java.sql.Time getTime(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 8	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 35	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 9	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 61	com/simba/jdbc/common/SMetaDataProxy:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 94	com/simba/jdbc/common/SMetaDataProxy:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: invokespecial 36	com/simba/jdbc/common/SMetaDataProxy:shouldReturnNull	(I)Z
    //   31: ifeq +10 -> 41
    //   34: aload_0
    //   35: iconst_1
    //   36: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   39: aconst_null
    //   40: areturn
    //   41: aload_0
    //   42: aload_0
    //   43: iload_1
    //   44: invokespecial 38	com/simba/jdbc/common/SMetaDataProxy:getMappedColumnIndex	(I)I
    //   47: ldc2_w 95
    //   50: invokevirtual 97	com/simba/jdbc/common/SMetaDataProxy:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   53: astore_2
    //   54: aload_0
    //   55: aload_2
    //   56: invokevirtual 102	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   59: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   62: aload_0
    //   63: getfield 93	com/simba/jdbc/common/SMetaDataProxy:m_resultSetColumns	Ljava/util/List;
    //   66: iload_1
    //   67: iconst_1
    //   68: isub
    //   69: invokeinterface 98 2 0
    //   74: checkcast 99	com/simba/dsi/dataengine/interfaces/IColumn
    //   77: astore_3
    //   78: aload_2
    //   79: aload_3
    //   80: invokeinterface 100 1 0
    //   85: aload_0
    //   86: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   89: invokestatic 111	com/simba/utilities/conversion/TypeConverter:toTime	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Time;
    //   92: areturn
    //   93: astore_3
    //   94: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   97: getstatic 104	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   100: aload_0
    //   101: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   104: getstatic 80	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   107: iconst_2
    //   108: anewarray 8	java/lang/Object
    //   111: dup
    //   112: iconst_0
    //   113: iload_1
    //   114: invokestatic 105	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   117: aastore
    //   118: dup
    //   119: iconst_1
    //   120: bipush 92
    //   122: invokestatic 106	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   125: aastore
    //   126: invokevirtual 82	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   129: athrow
    //   130: astore_2
    //   131: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   134: aload_2
    //   135: aload_0
    //   136: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   139: aload_0
    //   140: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   143: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   146: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	147	0	this	SMetaDataProxy
    //   0	147	1	paramInt	int
    //   53	26	2	localDataWrapper	DataWrapper
    //   130	5	2	localException	Exception
    //   77	3	3	localIColumn	com.simba.dsi.dataengine.interfaces.IColumn
    //   93	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   54	92	93	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	40	130	java/lang/Exception
    //   41	92	130	java/lang/Exception
    //   93	130	130	java/lang/Exception
  }
  
  /* Error */
  public java.sql.Timestamp getTimestamp(int paramInt)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   4: iconst_1
    //   5: anewarray 8	java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic 35	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   14: aastore
    //   15: invokestatic 9	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual 61	com/simba/jdbc/common/SMetaDataProxy:checkIfOpen	()V
    //   22: aload_0
    //   23: invokevirtual 94	com/simba/jdbc/common/SMetaDataProxy:closeCurrentStream	()V
    //   26: aload_0
    //   27: iload_1
    //   28: invokespecial 36	com/simba/jdbc/common/SMetaDataProxy:shouldReturnNull	(I)Z
    //   31: ifeq +10 -> 41
    //   34: aload_0
    //   35: iconst_1
    //   36: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   39: aconst_null
    //   40: areturn
    //   41: aload_0
    //   42: aload_0
    //   43: iload_1
    //   44: invokespecial 38	com/simba/jdbc/common/SMetaDataProxy:getMappedColumnIndex	(I)I
    //   47: ldc2_w 95
    //   50: invokevirtual 97	com/simba/jdbc/common/SMetaDataProxy:getData	(IJ)Lcom/simba/dsi/dataengine/utilities/DataWrapper;
    //   53: astore_2
    //   54: aload_0
    //   55: aload_2
    //   56: invokevirtual 102	com/simba/dsi/dataengine/utilities/DataWrapper:isNull	()Z
    //   59: putfield 37	com/simba/jdbc/common/SMetaDataProxy:m_wasLastValueNull	Z
    //   62: aload_0
    //   63: getfield 93	com/simba/jdbc/common/SMetaDataProxy:m_resultSetColumns	Ljava/util/List;
    //   66: iload_1
    //   67: iconst_1
    //   68: isub
    //   69: invokeinterface 98 2 0
    //   74: checkcast 99	com/simba/dsi/dataengine/interfaces/IColumn
    //   77: astore_3
    //   78: aload_2
    //   79: aload_3
    //   80: invokeinterface 100 1 0
    //   85: aload_0
    //   86: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   89: invokestatic 112	com/simba/utilities/conversion/TypeConverter:toTimestamp	(Lcom/simba/dsi/dataengine/utilities/DataWrapper;Lcom/simba/dsi/dataengine/utilities/TypeMetadata;Lcom/simba/support/IWarningListener;)Ljava/sql/Timestamp;
    //   92: areturn
    //   93: astore_3
    //   94: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   97: getstatic 104	com/simba/exceptions/JDBCMessageKey:INVALID_COLUMN_TYPE	Lcom/simba/exceptions/JDBCMessageKey;
    //   100: aload_0
    //   101: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   104: getstatic 80	com/simba/support/exceptions/ExceptionType:DATA	Lcom/simba/support/exceptions/ExceptionType;
    //   107: iconst_2
    //   108: anewarray 8	java/lang/Object
    //   111: dup
    //   112: iconst_0
    //   113: iload_1
    //   114: invokestatic 105	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   117: aastore
    //   118: dup
    //   119: iconst_1
    //   120: bipush 93
    //   122: invokestatic 106	com/simba/utilities/TypeNames:getTypeName	(I)Ljava/lang/String;
    //   125: aastore
    //   126: invokevirtual 82	com/simba/exceptions/ExceptionConverter:toSQLException	(Lcom/simba/exceptions/JDBCMessageKey;Lcom/simba/support/IWarningListener;Lcom/simba/support/exceptions/ExceptionType;[Ljava/lang/Object;)Ljava/sql/SQLException;
    //   129: athrow
    //   130: astore_2
    //   131: invokestatic 30	com/simba/exceptions/ExceptionConverter:getInstance	()Lcom/simba/exceptions/ExceptionConverter;
    //   134: aload_2
    //   135: aload_0
    //   136: getfield 6	com/simba/jdbc/common/SMetaDataProxy:m_warningListener	Lcom/simba/support/IWarningListener;
    //   139: aload_0
    //   140: getfield 7	com/simba/jdbc/common/SMetaDataProxy:m_logger	Lcom/simba/support/ILogger;
    //   143: invokevirtual 31	com/simba/exceptions/ExceptionConverter:toSQLException	(Ljava/lang/Exception;Lcom/simba/support/IWarningListener;Lcom/simba/support/ILogger;)Ljava/sql/SQLException;
    //   146: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	147	0	this	SMetaDataProxy
    //   0	147	1	paramInt	int
    //   53	26	2	localDataWrapper	DataWrapper
    //   130	5	2	localException	Exception
    //   77	3	3	localIColumn	com.simba.dsi.dataengine.interfaces.IColumn
    //   93	1	3	localIncorrectTypeException	com.simba.dsi.exceptions.IncorrectTypeException
    // Exception table:
    //   from	to	target	type
    //   54	92	93	com/simba/dsi/exceptions/IncorrectTypeException
    //   0	40	130	java/lang/Exception
    //   41	92	130	java/lang/Exception
    //   93	130	130	java/lang/Exception
  }
  
  /**
   * @deprecated
   */
  public InputStream getUnicodeStream(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getUnicodeStream(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public URL getURL(int paramInt)
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    try
    {
      if (shouldReturnNull(paramInt))
      {
        this.m_wasLastValueNull = true;
        return null;
      }
      return super.getURL(getMappedColumnIndex(paramInt));
    }
    catch (SQLException localSQLException)
    {
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
  }
  
  public synchronized void checkIfOpen()
    throws SQLException
  {
    if (!this.m_isOpen) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULTSET_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
    if (null == this.m_parentMetaData)
    {
      close();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARENT_CLOSED, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[0]);
    }
  }
  
  public void checkIfValidColumnNumber(int paramInt)
    throws SQLException
  {
    for (int i = 0; i < this.m_columnMap.length; i++) {
      if (this.m_columnMap[i] == paramInt) {
        return;
      }
    }
    throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
  }
  
  public boolean isClosed()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    return !this.m_isOpen;
  }
  
  private int getMappedColumnIndex(int paramInt)
    throws SQLException
  {
    if (null == this.m_columnMap) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.NULL_METADATA_COLUMNMAP, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    try
    {
      return this.m_columnMap[(paramInt - 1)];
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_INDEX, this.m_warningListener, ExceptionType.DEFAULT, new Object[] { String.valueOf(paramInt) });
    }
  }
  
  private void initializeColumnMap(MetadataSourceID paramMetadataSourceID)
  {
    switch (paramMetadataSourceID)
    {
    case CATALOG_ONLY: 
      this.m_columnMap = new int[1];
      this.m_columnMap[0] = 1;
      break;
    case SCHEMA_ONLY: 
    case CATALOG_SCHEMA_ONLY: 
      this.m_columnMap = new int[2];
      this.m_columnMap[0] = 2;
      this.m_columnMap[1] = 1;
      break;
    case TABLETYPE_ONLY: 
      this.m_columnMap = new int[1];
      this.m_columnMap[0] = 4;
      break;
    case COLUMN_PRIVILEGES: 
      this.m_columnMap = new int[8];
      break;
    case COLUMNS: 
      if (JDBCVersion.JDBC4 == this.m_jdbcVersion) {
        this.m_columnMap = new int[23];
      } else {
        this.m_columnMap = new int[24];
      }
      break;
    case FOREIGN_KEYS: 
      this.m_columnMap = new int[14];
      break;
    case FUNCTION_COLUMNS_JDBC4: 
      this.m_columnMap = new int[17];
      break;
    case PSEUDO_COLUMNS_JDBC41: 
      if (JDBCVersion.JDBC41 == this.m_jdbcVersion) {
        this.m_columnMap = new int[12];
      } else {
        return;
      }
      break;
    case FUNCTIONS_JDBC4: 
      this.m_columnMap = new int[6];
      break;
    case STATISTICS: 
      this.m_columnMap = new int[13];
      break;
    case PRIMARY_KEYS: 
      this.m_columnMap = new int[6];
      break;
    case PROCEDURE_COLUMNS: 
      this.m_columnMap = new int[20];
      break;
    case PROCEDURES: 
      this.m_columnMap = new int[9];
      break;
    case SPECIAL_COLUMNS: 
      this.m_columnMap = new int[8];
      break;
    case SUPERTABLES: 
      this.m_columnMap = new int[4];
      break;
    case SUPERTYPES: 
      this.m_columnMap = new int[6];
      break;
    case TABLE_PRIVILEGES: 
      this.m_columnMap = new int[7];
      break;
    case TABLES: 
      this.m_columnMap = new int[10];
      break;
    case TYPE_INFO: 
      this.m_columnMap = new int[18];
      break;
    case UDT: 
      this.m_columnMap = new int[7];
      break;
    case ATTRIBUTES: 
      this.m_columnMap = new int[21];
      break;
    default: 
      return;
    }
    int i;
    if ((MetadataSourceID.CATALOG_ONLY != this.m_metadataType) && (MetadataSourceID.CATALOG_SCHEMA_ONLY != this.m_metadataType) && (MetadataSourceID.SCHEMA_ONLY != this.m_metadataType) && (MetadataSourceID.TABLETYPE_ONLY != this.m_metadataType)) {
      for (i = 0; i < this.m_columnMap.length; i++) {
        this.m_columnMap[i] = (i + 1);
      }
    }
    if (MetadataSourceID.COLUMNS == this.m_metadataType)
    {
      for (i = 18; i < 22; i++) {
        this.m_columnMap[i] = (i + 2);
      }
      this.m_columnMap[22] = 19;
      if (JDBCVersion.JDBC41 == this.m_jdbcVersion) {
        this.m_columnMap[23] = 21;
      }
    }
  }
  
  private void initializeDataTypeColIndex(MetadataSourceID paramMetadataSourceID)
  {
    switch (paramMetadataSourceID)
    {
    case ATTRIBUTES: 
    case COLUMNS: 
    case UDT: 
      this.m_dataTypeColIndex = 5;
      break;
    case SPECIAL_COLUMNS: 
      this.m_dataTypeColIndex = 3;
      break;
    case PROCEDURE_COLUMNS: 
      this.m_dataTypeColIndex = 6;
      break;
    case TYPE_INFO: 
      this.m_dataTypeColIndex = 2;
      break;
    }
  }
  
  private boolean shouldReturnNull(int paramInt)
  {
    boolean bool = false;
    if (MetadataSourceID.COLUMNS.equals(this.m_metadataType)) {
      bool = (18 < paramInt) && (23 > paramInt);
    } else if (MetadataSourceID.TABLES.equals(this.m_metadataType)) {
      bool = (5 < paramInt) && (11 > paramInt);
    } else if ((MetadataSourceID.ATTRIBUTES.equals(this.m_metadataType)) || (MetadataSourceID.SUPERTABLES.equals(this.m_metadataType)) || (MetadataSourceID.SUPERTYPES.equals(this.m_metadataType)) || (MetadataSourceID.UDT.equals(this.m_metadataType))) {
      bool = true;
    }
    return bool;
  }
  
  protected final IConnection getParentConnection()
    throws SQLException
  {
    return ((SConnection)this.m_parentMetaData.getConnection()).getConnection();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SMetaDataProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
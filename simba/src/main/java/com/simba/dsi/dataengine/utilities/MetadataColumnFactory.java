package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.filters.IFilter;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import com.simba.support.exceptions.GeneralException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetadataColumnFactory
{
  public static ArrayList<MetadataColumn> createMetadataColumns(IStatement paramIStatement, MetadataSourceID paramMetadataSourceID)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(paramIStatement.getLog(), new Object[] { paramIStatement, paramMetadataSourceID });
    switch (paramMetadataSourceID)
    {
    case COLUMN_PRIVILEGES: 
      return createColumnPrivilegesMetadata(paramIStatement);
    case COLUMNS: 
      return createColumnsMetadata(paramIStatement);
    case FOREIGN_KEYS: 
      return createForeignKeysMetadata(paramIStatement);
    case FUNCTIONS_JDBC4: 
      return createFunctionMetadata(paramIStatement);
    case FUNCTION_COLUMNS_JDBC4: 
      return createFunctionColumnsMetadata(paramIStatement);
    case PRIMARY_KEYS: 
      return createPrimaryKeysMetadata(paramIStatement);
    case PROCEDURE_COLUMNS: 
      return createProcedureColumnsMetadata(paramIStatement);
    case PROCEDURES: 
      return createProceduresMetadata(paramIStatement);
    case PSEUDO_COLUMNS_JDBC41: 
      return createPseudoColumnsMetadata(paramIStatement);
    case SPECIAL_COLUMNS: 
      return createSpecialColumnsMetadata(paramIStatement);
    case STATISTICS: 
      return createStatisticsMetadata(paramIStatement);
    case TABLE_PRIVILEGES: 
      return createTablePrivilegesMetadata(paramIStatement);
    case CATALOG_ONLY: 
    case CATALOG_SCHEMA_ONLY: 
    case SCHEMA_ONLY: 
    case TABLES: 
    case TABLETYPE_ONLY: 
      return createTablesMetadata(paramIStatement, paramMetadataSourceID);
    case TYPE_INFO: 
      return createTypeInfoMetadata();
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_METADATA_ID.name(), paramMetadataSourceID.toString(), ExceptionType.DEFAULT);
  }
  
  public static List<MetadataSourceColumnTag> getSortOrder(ILogger paramILogger, MetadataSourceID paramMetadataSourceID, List<IFilter> paramList, OrderType paramOrderType)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(paramILogger, new Object[] { paramMetadataSourceID });
    switch (paramMetadataSourceID)
    {
    case COLUMN_PRIVILEGES: 
      return getColumnPrivilegesMetadataSortOrder(paramOrderType);
    case COLUMNS: 
      return getColumnsMetadataSortOrder(paramOrderType);
    case FOREIGN_KEYS: 
      return getForeignKeysMetadataSortOrder(paramList);
    case FUNCTIONS_JDBC4: 
      return getFunctionMetadataSortOrder();
    case FUNCTION_COLUMNS_JDBC4: 
      return getFunctionColumnsMetadataSortOrder();
    case PSEUDO_COLUMNS_JDBC41: 
      return getPseudoColumnsMetadataSortOrder();
    case PRIMARY_KEYS: 
      return getPrimaryKeysMetadataSortOrder(paramOrderType);
    case PROCEDURE_COLUMNS: 
      return getProcedureColumnsMetadataSortOrder(paramOrderType);
    case PROCEDURES: 
      return getProceduresMetadataSortOrder(paramOrderType);
    case SPECIAL_COLUMNS: 
      return getBestRowIdentifierMetadataSortOrder(paramList);
    case STATISTICS: 
      return getStatisticsMetadataSortOrder(paramOrderType);
    case TABLE_PRIVILEGES: 
      return getTablePrivilegesMetadataSortOrder(paramOrderType);
    case CATALOG_SCHEMA_ONLY: 
      return getCatalogSchemaOnlyMetadataSortOrder(paramOrderType);
    case CATALOG_ONLY: 
      return getCatalogOnlyMetadataSortOrder();
    case TABLES: 
    case TABLETYPE_ONLY: 
      return getTablesMetadataSortOrder(paramOrderType);
    case SCHEMA_ONLY: 
      return getSchemasMetadataSortOrder(paramOrderType);
    case TYPE_INFO: 
      return getTypeInfoMetadataSortOrder();
    }
    throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_METADATA_ID.name(), paramMetadataSourceID.toString(), ExceptionType.DEFAULT);
  }
  
  private static ArrayList<MetadataColumn> createColumnPrivilegesMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 68);
      int n = getMaxLength(localIConnection, 85);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.GRANTOR);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("GRANTOR");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(n);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.GRANTEE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("GRANTEE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(n);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIVILEGE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PRIVILEGE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(32L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.ISGRANTABLE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_GRANTABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(3L);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createColumnsMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.DATA_TYPE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TYPE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(128L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.COLUMN_SIZE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_SIZE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.BUFFER_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("BUFFER_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DECIMAL_DIGITS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DECIMAL_DIGITS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NUM_PREC_RADIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_PREC_RADIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NULLABLE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_DEF);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_DEF");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(4000L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SQL_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SQL_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SQL_DATETIME_SUB);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SQL_DATETIME_SUB");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.CHAR_OCTET_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CHAR_OCTET_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.ORDINAL_POSITION);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("ORDINAL_POSITION");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.IS_NULLABLE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.IS_AUTOINCREMENT);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_AUTOINCREMENT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, false), MetadataSourceColumnTag.USER_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("USER_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.IS_GENERATEDCOLUMN);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_GENERATEDCOLUMN");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createFunctionMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 78);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FUNCTION_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FUNCTION_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PROCEDURE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FUNCTION_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.PROCEDURE_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SPECIFIC_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SPECIFIC_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createFunctionColumnsMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 78);
      int m = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FUNCTION_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FUNCTION_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PROCEDURE_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FUNCTION_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.PROCEDURE_COLUMN_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.DATA_TYPE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TYPE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(128L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.COLUMN_SIZE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_SIZE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.BUFFER_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("BUFFER_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DECIMAL_DIGITS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DECIMAL_DIGITS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NUM_PREC_RADIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_PREC_RADIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NULLABLE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.CHAR_OCTET_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CHAR_OCTET_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.ORDINAL_POSITION);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("ORDINAL_POSITION");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.IS_NULLABLE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SPECIFIC_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SPECIFIC_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, false), MetadataSourceColumnTag.USER_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("USER_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createForeignKeysMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PKTABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PKTABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PKTABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PKCOLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.FOREIGN_KEY_CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FKTABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.FOREIGN_KEY_SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FKTABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.FOREIGN_KEY_TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FKTABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.FOREIGN_KEY_COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FKCOLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.KEY_SEQ);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("KEY_SEQ");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.FOREIGN_KEY_UPDATE_RULE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("UPDATE_RULE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.FOREIGN_KEY_DELETE_RULE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DELETE_RULE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.FOREIGN_KEY_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FK_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PK_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DEFERRABILITY);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DEFERRABILITY");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createPrimaryKeysMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.KEY_SEQ);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("KEY_SEQ");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIMARY_KEY_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PK_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createProcedureColumnsMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 78);
      int m = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PROCEDURE_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.PROCEDURE_COLUMN_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.DATA_TYPE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TYPE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(128L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.COLUMN_SIZE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_SIZE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.BUFFER_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("BUFFER_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DECIMAL_DIGITS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DECIMAL_DIGITS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NUM_PREC_RADIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_PREC_RADIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NULLABLE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_DEF);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_DEF");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(4000L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SQL_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SQL_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SQL_DATETIME_SUB);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SQL_DATETIME_SUB");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.CHAR_OCTET_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CHAR_OCTET_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.ORDINAL_POSITION);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("ORDINAL_POSITION");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.IS_NULLABLE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SPECIFIC_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SPECIFIC_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, false), MetadataSourceColumnTag.IS_RESULT_SET);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_RESULT_SET_COLUMN");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, false), MetadataSourceColumnTag.USER_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("USER_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createProceduresMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 78);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PROCEDURE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.NUM_INPUT_PARAMS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_INPUT_PARAMS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.NUM_OUTPUT_PARAMS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_OUTPUT_PARAMS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.NUM_RESULT_SETS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_RESULT_SETS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.PROCEDURE_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PROCEDURE_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SPECIFIC_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SPECIFIC_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createPseudoColumnsMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.COLUMN_SIZE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_SIZE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DECIMAL_DIGITS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DECIMAL_DIGITS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NUM_PREC_RADIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_PREC_RADIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_USAGE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_USAGE");
      localMetadataColumn.setColumnLength(254L);
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.CHAR_OCTET_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CHAR_OCTET_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.IS_NULLABLE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createSpecialColumnsMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 68);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SCOPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SCOPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.DATA_TYPE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TYPE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(128L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.COLUMN_SIZE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_SIZE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.BUFFER_LENGTH);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("BUFFER_LENGTH");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DECIMAL_DIGITS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DECIMAL_DIGITS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.PSEUDO);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PSEUDO_COLUMN");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, false), MetadataSourceColumnTag.USER_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("USER_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createStatisticsMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 68);
      int n = getMaxLength(localIConnection, 76);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NON_UNIQUE_INDEX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NON_UNIQUE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.INDEX_QUALIFIER);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("INDEX_QUALIFIER");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(n);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.INDEX_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("INDEX_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.INDEX_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.ORDINAL_POSITION);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("ORDINAL_POSITION");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.COLUMN_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(1), MetadataSourceColumnTag.INDEX_SORT_SEQUENCE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("ASC_OR_DESC");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(1L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.INDEX_CARDINALITY);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CARDINALITY");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.INDEX_PAGES);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PAGES");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.INDEX_FILTER_CONDITION);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FILTER_CONDITION");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createTablePrivilegesMetadata(IStatement paramIStatement)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      int m = getMaxLength(localIConnection, 85);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.GRANTOR);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("GRANTOR");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.GRANTEE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("GRANTEE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(m);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.PRIVILEGE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("PRIVILEGE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(32L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.ISGRANTABLE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("IS_GRANTABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(3L);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createTablesMetadata(IStatement paramIStatement, MetadataSourceID paramMetadataSourceID)
    throws ErrorException
  {
    IConnection localIConnection = paramIStatement.getParentConnection();
    try
    {
      int i = getMaxLength(localIConnection, 66);
      int j = getMaxLength(localIConnection, 81);
      int k = getMaxLength(localIConnection, 83);
      ArrayList localArrayList = new ArrayList();
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CATALOG_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_CAT");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(i);
      localMetadataColumn.setNullColumn((MetadataSourceID.TABLES != paramMetadataSourceID) && (MetadataSourceID.CATALOG_ONLY != paramMetadataSourceID) && (MetadataSourceID.CATALOG_SCHEMA_ONLY != paramMetadataSourceID));
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.SCHEMA_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_SCHEM");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(j);
      localMetadataColumn.setNullColumn((MetadataSourceID.TABLES != paramMetadataSourceID) && (MetadataSourceID.SCHEMA_ONLY != paramMetadataSourceID) && (MetadataSourceID.CATALOG_SCHEMA_ONLY != paramMetadataSourceID));
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(k);
      localMetadataColumn.setNullColumn(MetadataSourceID.TABLES != paramMetadataSourceID);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.TABLE_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TABLE_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(32L);
      localMetadataColumn.setNullColumn((MetadataSourceID.TABLES != paramMetadataSourceID) && (MetadataSourceID.TABLETYPE_ONLY != paramMetadataSourceID));
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.REMARKS);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("REMARKS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(254L);
      localMetadataColumn.setNullColumn(MetadataSourceID.TABLES != paramMetadataSourceID);
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static ArrayList<MetadataColumn> createTypeInfoMetadata()
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      MetadataColumn localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.DATA_TYPE_NAME);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("TYPE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(128L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.COLUMN_SIZE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("COLUMN_SIZE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.LITERAL_PREFIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("LITERAL_PREFIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(32L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.LITERAL_SUFFIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("LITERAL_SUFFIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(32L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.CREATE_PARAM);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CREATE_PARAMS");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(32L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.NULLABLE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NULLABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.CASE_SENSITIVE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("CASE_SENSITIVE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SEARCHABLE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SEARCHABLE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.UNSIGNED_ATTRIBUTE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("UNSIGNED_ATTRIBUTE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.FIXED_PREC_SCALE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("FIXED_PREC_SCALE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.AUTO_UNIQUE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("AUTO_UNIQUE_VALUE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(12), MetadataSourceColumnTag.LOCAL_TYPE_NAME);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("LOCAL_TYPE_NAME");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localMetadataColumn.setColumnLength(128L);
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.MINIMUM_SCALE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("MINIMUM_SCALE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.MAXIMUM_SCALE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("MAXIMUM_SCALE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SQL_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NO_NULLS);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SQL_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.SQL_DATETIME_SUB);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("SQL_DATETIME_SUB");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(4, true), MetadataSourceColumnTag.NUM_PREC_RADIX);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("NUM_PREC_RADIX");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, true), MetadataSourceColumnTag.INTERVAL_PRECISION);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("INTERVAL_PRECISION");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      localMetadataColumn = new MetadataColumn(TypeMetadata.createTypeMetadata(5, false), MetadataSourceColumnTag.USER_DATA_TYPE);
      localMetadataColumn.setNullable(Nullable.NULLABLE);
      localMetadataColumn.setSearchable(Searchable.PREDICATE_NONE);
      localMetadataColumn.setUpdatable(Updatable.READ_ONLY);
      localMetadataColumn.setLabel("USER_DATA_TYPE");
      localMetadataColumn.setName(localMetadataColumn.getLabel());
      localArrayList.add(localMetadataColumn);
      return localArrayList;
    }
    catch (ErrorException localErrorException)
    {
      throw localErrorException;
    }
    catch (Exception localException)
    {
      throw new GeneralException(localException.getLocalizedMessage(), 0);
    }
  }
  
  private static List<MetadataSourceColumnTag> getCatalogOnlyMetadataSortOrder()
  {
    return Collections.singletonList(MetadataSourceColumnTag.CATALOG_NAME);
  }
  
  private static List<MetadataSourceColumnTag> getCatalogSchemaOnlyMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
    localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getColumnsMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
    localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
    localArrayList.add(MetadataSourceColumnTag.TABLE_NAME);
    localArrayList.add(MetadataSourceColumnTag.ORDINAL_POSITION);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getColumnPrivilegesMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    if (OrderType.ODBC == paramOrderType)
    {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.TABLE_NAME);
    }
    localArrayList.add(MetadataSourceColumnTag.COLUMN_NAME);
    localArrayList.add(MetadataSourceColumnTag.PRIVILEGE);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getForeignKeysMetadataSortOrder(List<IFilter> paramList)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    if (null != paramList.get(0))
    {
      if (null != paramList.get(3))
      {
        localArrayList.add(MetadataSourceColumnTag.FOREIGN_KEY_CATALOG_NAME);
        localArrayList.add(MetadataSourceColumnTag.FOREIGN_KEY_SCHEMA_NAME);
        localArrayList.add(MetadataSourceColumnTag.FOREIGN_KEY_TABLE_NAME);
        localArrayList.add(MetadataSourceColumnTag.KEY_SEQ);
      }
      else
      {
        localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME);
        localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME);
        localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME);
        localArrayList.add(MetadataSourceColumnTag.KEY_SEQ);
      }
    }
    else if (null != paramList.get(3))
    {
      localArrayList.add(MetadataSourceColumnTag.FOREIGN_KEY_CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.FOREIGN_KEY_SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.FOREIGN_KEY_TABLE_NAME);
      localArrayList.add(MetadataSourceColumnTag.KEY_SEQ);
    }
    else
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_METADATA_ID.name(), MetadataSourceID.FOREIGN_KEYS.toString(), ExceptionType.DEFAULT);
    }
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getFunctionMetadataSortOrder()
  {
    return getProceduresMetadataSortOrder(OrderType.JDBC_4);
  }
  
  private static List<MetadataSourceColumnTag> getFunctionColumnsMetadataSortOrder()
  {
    return getProcedureColumnsMetadataSortOrder(OrderType.JDBC_4);
  }
  
  private static int getMaxLength(IConnection paramIConnection, int paramInt)
    throws NumericOverflowException, IncorrectTypeException, BadPropertyKeyException, ErrorException
  {
    int i = paramIConnection.getProperty(paramInt).getInt();
    if (0 == i) {
      return 1024;
    }
    return i;
  }
  
  private static List<MetadataSourceColumnTag> getPrimaryKeysMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    if (OrderType.ODBC == paramOrderType)
    {
      localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME);
      localArrayList.add(MetadataSourceColumnTag.KEY_SEQ);
    }
    localArrayList.add(MetadataSourceColumnTag.PRIMARY_KEY_COLUMN_NAME);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getProcedureColumnsMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    if ((OrderType.JDBC_4 == paramOrderType) || (OrderType.JDBC_41 == paramOrderType))
    {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.PROCEDURE_NAME);
      localArrayList.add(MetadataSourceColumnTag.SPECIFIC_NAME);
      localArrayList.add(MetadataSourceColumnTag.PROCEDURE_COLUMN_TYPE);
      localArrayList.add(MetadataSourceColumnTag.ORDINAL_POSITION);
    }
    else
    {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.PROCEDURE_NAME);
      localArrayList.add(MetadataSourceColumnTag.IS_RESULT_SET);
      localArrayList.add(MetadataSourceColumnTag.ORDINAL_POSITION);
    }
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getProceduresMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    if ((OrderType.JDBC_4 == paramOrderType) || (OrderType.JDBC_41 == paramOrderType))
    {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.PROCEDURE_NAME);
      localArrayList.add(MetadataSourceColumnTag.SPECIFIC_NAME);
    }
    else
    {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.PROCEDURE_NAME);
    }
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getPseudoColumnsMetadataSortOrder()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
    localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
    localArrayList.add(MetadataSourceColumnTag.TABLE_NAME);
    localArrayList.add(MetadataSourceColumnTag.COLUMN_NAME);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getSchemasMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    if ((OrderType.JDBC_4 == paramOrderType) || (OrderType.JDBC_41 == paramOrderType)) {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
    }
    localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getBestRowIdentifierMetadataSortOrder(List<IFilter> paramList)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.SCOPE);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getStatisticsMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    if (OrderType.ODBC == paramOrderType)
    {
      localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
      localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
      localArrayList.add(MetadataSourceColumnTag.TABLE_NAME);
      localArrayList.add(MetadataSourceColumnTag.NON_UNIQUE_INDEX);
      localArrayList.add(MetadataSourceColumnTag.INDEX_TYPE);
      localArrayList.add(MetadataSourceColumnTag.INDEX_QUALIFIER);
      localArrayList.add(MetadataSourceColumnTag.INDEX_NAME);
      localArrayList.add(MetadataSourceColumnTag.ORDINAL_POSITION);
    }
    else
    {
      localArrayList.add(MetadataSourceColumnTag.NON_UNIQUE_INDEX);
      localArrayList.add(MetadataSourceColumnTag.INDEX_TYPE);
      localArrayList.add(MetadataSourceColumnTag.INDEX_NAME);
      localArrayList.add(MetadataSourceColumnTag.ORDINAL_POSITION);
    }
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getTablesMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.TABLE_TYPE);
    localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
    localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
    localArrayList.add(MetadataSourceColumnTag.TABLE_NAME);
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getTablePrivilegesMetadataSortOrder(OrderType paramOrderType)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.CATALOG_NAME);
    localArrayList.add(MetadataSourceColumnTag.SCHEMA_NAME);
    localArrayList.add(MetadataSourceColumnTag.TABLE_NAME);
    localArrayList.add(MetadataSourceColumnTag.PRIVILEGE);
    if (OrderType.ODBC == paramOrderType) {
      localArrayList.add(MetadataSourceColumnTag.GRANTEE);
    }
    return localArrayList;
  }
  
  private static List<MetadataSourceColumnTag> getTypeInfoMetadataSortOrder()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(MetadataSourceColumnTag.DATA_TYPE);
    localArrayList.add(MetadataSourceColumnTag.DATA_TYPE_NAME);
    return localArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/MetadataColumnFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
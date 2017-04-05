package com.simba.utilities;

import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.dsi.exceptions.NumericOverflowException;
import java.util.ArrayList;
import java.util.List;

public final class MetaDataFactory
{
  private static int COLUMN_STRING_LENGTH = 128;
  private static int COLUMN_REMARKS_LENGTH = 254;
  private static int COLUMN_INTEGER_LENGTH = 10;
  private static int COLUMN_SMALLINT_LENGTH = 5;
  private static int NUM_COMMON_COLUMNS = 4;
  private static int NUM_COMMON_TABLE_COLUMNS = NUM_COMMON_COLUMNS - 1;
  private static int LENGTH_COMMON_TABLE_HEADER = 5;
  
  public static List<ColumnMetadata> createAttributeMetaData()
  {
    ArrayList localArrayList = new ArrayList(21);
    addCommonMetaData(localArrayList);
    localArrayList.remove(3);
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      ColumnMetadata localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      String str = localColumnMetadata2.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "TYPE" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createStringMetaDataColumn("ATTR_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("ATTR_TYPE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("ATTR_SIZE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("DECIMAL_DIGITS", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("NUM_PREC_RADIX", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("NULLABLE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("ATTR_DEF", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("SQL_DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("SQL_DATETIME_SUB", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("CHAR_OCTET_LENGTH", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("ORDINAL_POSITION", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("IS_NULLABLE", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SCOPE_CATALOG", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SCOPE_SCHEMA", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SCOPE_TABLE", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SOURCE_DATA_TYPE", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createClientInfoPropertiesMetadata()
  {
    ArrayList localArrayList = new ArrayList(4);
    try
    {
      ColumnMetadata localColumnMetadata1 = createStringMetaDataColumn("NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localColumnMetadata1.setSearchable(Searchable.PREDICATE_NONE);
      localColumnMetadata1.setUpdatable(Updatable.READ_ONLY);
      localArrayList.add(localColumnMetadata1);
      ColumnMetadata localColumnMetadata2 = createIntegerMetaDataColumn("MAX_LEN", Nullable.NO_NULLS);
      localColumnMetadata2.setSearchable(Searchable.PREDICATE_NONE);
      localColumnMetadata2.setUpdatable(Updatable.READ_ONLY);
      localArrayList.add(localColumnMetadata2);
      ColumnMetadata localColumnMetadata3 = createStringMetaDataColumn("DEFAULT_VALUE", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localColumnMetadata3.setSearchable(Searchable.PREDICATE_NONE);
      localColumnMetadata3.setUpdatable(Updatable.READ_ONLY);
      localArrayList.add(localColumnMetadata3);
      ColumnMetadata localColumnMetadata4 = createStringMetaDataColumn("DESCRIPTION", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localColumnMetadata4.setSearchable(Searchable.PREDICATE_NONE);
      localColumnMetadata4.setUpdatable(Updatable.READ_ONLY);
      localArrayList.add(localColumnMetadata4);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createCatalogOnlyMetaData()
  {
    ArrayList localArrayList = new ArrayList(1);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("TABLE_CAT", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createColumnPrivilegesMetaData()
  {
    ArrayList localArrayList = new ArrayList(8);
    addCommonMetaData(localArrayList);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("GRANTOR", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("GRANTEE", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("PRIVILEGE", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("IS_GRANTABLE", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createColumnsMetaData(JDBCVersion paramJDBCVersion)
  {
    ArrayList localArrayList = new ArrayList(24);
    addCommonMetaData(localArrayList);
    try
    {
      ColumnMetadata localColumnMetadata = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TYPE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("COLUMN_SIZE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("BUFFER_LENGTH", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("DECIMAL_DIGITS", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("NUM_PREC_RADIX", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("NULLABLE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("COLUMN_DEF", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("SQL_DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("SQL_DATETIME_SUB", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("CHAR_OCTET_LENGTH", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("ORDINAL_POSITION", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("IS_NULLABLE", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("SCOPE_CATALOG", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("SCOPE_SCHEMA", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("SCOPE_TABLE", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("SOURCE_DATA_TYPE", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("IS_AUTOINCREMENT", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      if (JDBCVersion.JDBC41 == paramJDBCVersion)
      {
        localColumnMetadata = createStringMetaDataColumn("IS_GENERATEDCOLUMN", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
        localArrayList.add(localColumnMetadata);
      }
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createForeignKeysMetaData()
  {
    ArrayList localArrayList = new ArrayList(14);
    addCommonMetaData(localArrayList);
    ColumnMetadata localColumnMetadata2;
    String str;
    for (int i = 0; i < NUM_COMMON_COLUMNS; i++)
    {
      localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      str = localColumnMetadata2.getName();
      str = "PK" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    addCommonMetaData(localArrayList);
    for (i = NUM_COMMON_COLUMNS; i < 2 * NUM_COMMON_COLUMNS; i++)
    {
      localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      str = localColumnMetadata2.getName();
      str = "FK" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createShortMetaDataColumn("KEY_SEQ", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("UPDATE_RULE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("DELETE_RULE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("FK_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("PK_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("DEFERRABILITY", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createFunctionsMetaData()
  {
    ArrayList localArrayList = new ArrayList(8);
    addCommonMetaData(localArrayList);
    localArrayList.remove(NUM_COMMON_TABLE_COLUMNS);
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      ColumnMetadata localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      String str = localColumnMetadata2.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "FUNCTION" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("FUNCTION_TYPE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SPECIFIC_NAME", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
    }
    catch (Exception localException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createFunctionColumnsMetaData()
  {
    ArrayList localArrayList = new ArrayList(13);
    addCommonMetaData(localArrayList);
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      ColumnMetadata localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      String str = localColumnMetadata2.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "FUNCTION" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createShortMetaDataColumn("COLUMN_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("TYPE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("COLUMN_SIZE", Nullable.NULLABLE);
      localColumnMetadata1.setName("PRECISION");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("BUFFER_LENGTH", Nullable.NULLABLE);
      localColumnMetadata1.setName("LENGTH");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("DECIMAL_DIGITS", Nullable.NULLABLE);
      localColumnMetadata1.setName("SCALE");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("NUM_PREC_RADIX", Nullable.NULLABLE);
      localColumnMetadata1.setName("RADIX");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("NULLABLE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("CHAR_OCTET_LENGTH", Nullable.NULLABLE);
      localColumnMetadata1.setName("CHAR_OCTET_LENGTH");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("ORDINAL_POSITION", Nullable.NO_NULLS);
      localColumnMetadata1.setName("ORDINAL_POSITION");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("IS_NULLABLE", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SPECIFIC_NAME", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
    }
    catch (Exception localException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createIndexInfoMetaData()
  {
    ArrayList localArrayList = new ArrayList(13);
    addCommonMetaData(localArrayList);
    localArrayList.remove(NUM_COMMON_TABLE_COLUMNS);
    try
    {
      ColumnMetadata localColumnMetadata = createBooleanMetaDataColumn("NON_UNIQUE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("INDEX_QUALIFIER", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("INDEX_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("ORDINAL_POSITION", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("COLUMN_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("ASC_OR_DESC", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("CARDINALITY", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("PAGES", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("FILTER_CONDITION", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createPrimaryKeysMetaData()
  {
    ArrayList localArrayList = new ArrayList(6);
    addCommonMetaData(localArrayList);
    try
    {
      ColumnMetadata localColumnMetadata = createShortMetaDataColumn("KEY_SEQ", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("PK_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createProcedureColumnsMetaData(JDBCVersion paramJDBCVersion)
  {
    ArrayList localArrayList = new ArrayList(13);
    addCommonMetaData(localArrayList);
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      ColumnMetadata localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      String str = localColumnMetadata2.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "PROCEDURE" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createShortMetaDataColumn("COLUMN_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("TYPE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("COLUMN_SIZE", Nullable.NULLABLE);
      localColumnMetadata1.setName("PRECISION");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("BUFFER_LENGTH", Nullable.NULLABLE);
      localColumnMetadata1.setName("LENGTH");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("DECIMAL_DIGITS", Nullable.NULLABLE);
      localColumnMetadata1.setName("SCALE");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("NUM_PREC_RADIX", Nullable.NULLABLE);
      localColumnMetadata1.setName("RADIX");
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("NULLABLE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("COLUMN_DEF", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("SQL_DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("SQL_DATETIME_SUB", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("CHAR_OCTET_LENGTH", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("ORDINAL_POSITION", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("IS_NULLABLE", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SPECIFIC_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
    }
    catch (Exception localException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createProceduresMetaData(JDBCVersion paramJDBCVersion)
  {
    ArrayList localArrayList = new ArrayList(8);
    addCommonMetaData(localArrayList);
    localArrayList.remove(NUM_COMMON_TABLE_COLUMNS);
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      ColumnMetadata localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      String str = localColumnMetadata2.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "PROCEDURE" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createIntegerMetaDataColumn("NUM_INPUT_PARAMS", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("NUM_OUTPUT_PARAMS", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("NUM_RESULT_SETS", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("PROCEDURE_TYPE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("SPECIFIC_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
    }
    catch (Exception localException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createPseudoColumnsMetaData()
  {
    ArrayList localArrayList = new ArrayList(13);
    addCommonMetaData(localArrayList);
    try
    {
      ColumnMetadata localColumnMetadata = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("COLUMN_SIZE", Nullable.NULLABLE);
      localColumnMetadata.setName("PRECISION");
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("DECIMAL_DIGITS", Nullable.NULLABLE);
      localColumnMetadata.setName("SCALE");
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("NUM_PREC_RADIX", Nullable.NULLABLE);
      localColumnMetadata.setName("RADIX");
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("COLUMN_USAGE", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("CHAR_OCTET_LENGTH", Nullable.NULLABLE);
      localColumnMetadata.setName("CHAR_OCTET_LENGTH");
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("IS_NULLABLE", Nullable.NO_NULLS, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (Exception localException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createSchemasOnlyMetaData()
  {
    ArrayList localArrayList = new ArrayList(2);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("TABLE_SCHEM", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TABLE_CATALOG", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createSpecialColumnsMetaData()
  {
    ArrayList localArrayList = new ArrayList(8);
    try
    {
      ColumnMetadata localColumnMetadata = createShortMetaDataColumn("SCOPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("COLUMN_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TYPE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("COLUMN_SIZE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("BUFFER_LENGTH", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("DECIMAL_DIGITS", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("PSEUDO_COLUMN", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createSuperTablesMetaData()
  {
    ArrayList localArrayList = new ArrayList(4);
    addCommonMetaData(localArrayList);
    localArrayList.remove(3);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("SUPERTABLE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createSuperTypesMetaData()
  {
    ArrayList localArrayList = new ArrayList(6);
    addCommonMetaData(localArrayList);
    localArrayList.remove(NUM_COMMON_TABLE_COLUMNS);
    ColumnMetadata localColumnMetadata;
    String str;
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      localColumnMetadata = (ColumnMetadata)localArrayList.get(i);
      str = localColumnMetadata.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "TYPE" + str;
      localColumnMetadata.setLabel(str);
      localColumnMetadata.setName(str);
    }
    addCommonMetaData(localArrayList);
    localArrayList.remove(6);
    for (i = NUM_COMMON_TABLE_COLUMNS; i < 2 * NUM_COMMON_TABLE_COLUMNS; i++)
    {
      localColumnMetadata = (ColumnMetadata)localArrayList.get(i);
      str = localColumnMetadata.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "SUPERTYPE" + str;
      localColumnMetadata.setLabel(str);
      localColumnMetadata.setName(str);
    }
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createTablePrivilegesMetaData()
  {
    ArrayList localArrayList = new ArrayList(7);
    addCommonMetaData(localArrayList);
    localArrayList.remove(3);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("GRANTOR", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("GRANTEE", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("PRIVILEGE", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("IS_GRANTABLE", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createTablesMetaData()
  {
    ArrayList localArrayList = new ArrayList(10);
    addCommonMetaData(localArrayList);
    localArrayList.remove(3);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("TABLE_TYPE", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TYPE_CAT", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TYPE_SCHEM", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TYPE_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("SELF_REFERENCING_COL_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("REF_GENERATION", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createTableTypesMetaData()
  {
    ArrayList localArrayList = new ArrayList(1);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("TABLE_TYPE", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createTypeInfoMetaData()
  {
    ArrayList localArrayList = new ArrayList(18);
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("TYPE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("PRECISION", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("LITERAL_PREFIX", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("LITERAL_SUFFIX", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("CREATE_PARAMS", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("NULLABLE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createBooleanMetaDataColumn("CASE_SENSITIVE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("SEARCHABLE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createBooleanMetaDataColumn("UNSIGNED_ATTRIBUTE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createBooleanMetaDataColumn("FIXED_PREC_SCALE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createBooleanMetaDataColumn("AUTO_INCREMENT", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("LOCAL_TYPE_NAME", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("MINIMUM_SCALE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createShortMetaDataColumn("MAXIMUM_SCALE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("SQL_DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("SQL_DATETIME_SUB", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
      localColumnMetadata = createIntegerMetaDataColumn("NUM_PREC_RADIX", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  public static List<ColumnMetadata> createUDTMetaData()
  {
    ArrayList localArrayList = new ArrayList(7);
    addCommonMetaData(localArrayList);
    localArrayList.remove(NUM_COMMON_TABLE_COLUMNS);
    for (int i = 0; i < NUM_COMMON_TABLE_COLUMNS; i++)
    {
      ColumnMetadata localColumnMetadata2 = (ColumnMetadata)localArrayList.get(i);
      String str = localColumnMetadata2.getName().substring(LENGTH_COMMON_TABLE_HEADER);
      str = "TYPE" + str;
      localColumnMetadata2.setLabel(str);
      localColumnMetadata2.setName(str);
    }
    try
    {
      ColumnMetadata localColumnMetadata1 = createStringMetaDataColumn("CLASS_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createIntegerMetaDataColumn("DATA_TYPE", Nullable.NO_NULLS);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createStringMetaDataColumn("REMARKS", Nullable.NULLABLE, COLUMN_REMARKS_LENGTH);
      localArrayList.add(localColumnMetadata1);
      localColumnMetadata1 = createShortMetaDataColumn("BASE_TYPE", Nullable.NULLABLE);
      localArrayList.add(localColumnMetadata1);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
    return localArrayList;
  }
  
  private static void addCommonMetaData(List<ColumnMetadata> paramList)
  {
    try
    {
      ColumnMetadata localColumnMetadata = createStringMetaDataColumn("TABLE_CAT", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      paramList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TABLE_SCHEM", Nullable.NULLABLE, COLUMN_STRING_LENGTH);
      paramList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("TABLE_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      paramList.add(localColumnMetadata);
      localColumnMetadata = createStringMetaDataColumn("COLUMN_NAME", Nullable.NO_NULLS, COLUMN_STRING_LENGTH);
      paramList.add(localColumnMetadata);
    }
    catch (NumericOverflowException localNumericOverflowException) {}
  }
  
  private static ColumnMetadata createBooleanMetaDataColumn(String paramString, Nullable paramNullable)
    throws NumericOverflowException
  {
    TypeMetadata localTypeMetadata = new TypeMetadata((short)-7, "BIT", (short)0, (short)0, 0, false);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
    localColumnMetadata.setLabel(paramString);
    localColumnMetadata.setName(paramString);
    localColumnMetadata.setNullable(paramNullable);
    localColumnMetadata.setSearchable(Searchable.PREDICATE_NONE);
    localColumnMetadata.setUpdatable(Updatable.READ_ONLY);
    localColumnMetadata.setColumnLength(COLUMN_SMALLINT_LENGTH);
    return localColumnMetadata;
  }
  
  private static ColumnMetadata createIntegerMetaDataColumn(String paramString, Nullable paramNullable)
    throws NumericOverflowException
  {
    TypeMetadata localTypeMetadata = new TypeMetadata((short)4, "INTEGER", (short)1, (short)1, 0, true);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
    localColumnMetadata.setLabel(paramString);
    localColumnMetadata.setName(paramString);
    localColumnMetadata.setNullable(paramNullable);
    localColumnMetadata.setSearchable(Searchable.PREDICATE_NONE);
    localColumnMetadata.setUpdatable(Updatable.READ_ONLY);
    localColumnMetadata.setColumnLength(COLUMN_INTEGER_LENGTH);
    return localColumnMetadata;
  }
  
  private static ColumnMetadata createShortMetaDataColumn(String paramString, Nullable paramNullable)
    throws NumericOverflowException
  {
    TypeMetadata localTypeMetadata = new TypeMetadata((short)5, "SMALLINT", (short)0, (short)0, 0, true);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
    localColumnMetadata.setLabel(paramString);
    localColumnMetadata.setName(paramString);
    localColumnMetadata.setNullable(paramNullable);
    localColumnMetadata.setSearchable(Searchable.PREDICATE_NONE);
    localColumnMetadata.setUpdatable(Updatable.READ_ONLY);
    localColumnMetadata.setColumnLength(COLUMN_SMALLINT_LENGTH);
    return localColumnMetadata;
  }
  
  private static ColumnMetadata createStringMetaDataColumn(String paramString, Nullable paramNullable, int paramInt)
    throws NumericOverflowException
  {
    TypeMetadata localTypeMetadata = new TypeMetadata((short)12, "VARCHAR", (short)1, (short)0, 1, false);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
    localColumnMetadata.setLabel(paramString);
    localColumnMetadata.setName(paramString);
    localColumnMetadata.setNullable(paramNullable);
    localColumnMetadata.setSearchable(Searchable.PREDICATE_NONE);
    localColumnMetadata.setUpdatable(Updatable.READ_ONLY);
    localColumnMetadata.setColumnLength(paramInt);
    return localColumnMetadata;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/utilities/MetaDataFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
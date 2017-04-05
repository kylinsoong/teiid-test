package com.simba.dsi.dataengine.utilities;

public enum MetadataSourceID
{
  TABLES,  CATALOG_ONLY,  SCHEMA_ONLY,  TABLETYPE_ONLY,  TABLE_PRIVILEGES,  COLUMNS,  COLUMN_PRIVILEGES,  FOREIGN_KEYS,  PRIMARY_KEYS,  SPECIAL_COLUMNS,  STATISTICS,  PROCEDURES,  PROCEDURE_COLUMNS,  TYPE_INFO,  CATALOG_SCHEMA_ONLY,  ATTRIBUTES,  SUPERTABLES,  SUPERTYPES,  UDT,  FUNCTIONS_JDBC4,  FUNCTION_COLUMNS_JDBC4,  PSEUDO_COLUMNS_JDBC41;
  
  private MetadataSourceID() {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/MetadataSourceID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
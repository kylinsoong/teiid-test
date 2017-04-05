package com.simba.dsi.exceptions;

public enum ParsingErrorID
{
  INSERT_VAL_LIST_COL_LIST_MISMATCH,  DEGREE_DERIVED_TABLE_COL_LIST_MISMATCH,  INVALID_CHAR_VAL_FOR_CAST,  INVALID_ESC_CHAR,  INVALID_ESC_SQNCE,  INVALID_CURSOR_NAME,  INVALID_CATALOG_NAME,  INVALID_SCHEMA_NAME,  SYNTAX_ERR_OR_ACCESS_VIOLATION,  BASE_TABLE_OR_VIEW_EXISTS,  BASE_TABLE_OR_VIEW_MISSING,  INDEX_EXISTS,  INDEX_MISSING,  COLUMN_EXISTS,  COLUMN_MISSING;
  
  private ParsingErrorID() {}
  
  public int getIdentifier()
  {
    switch (this)
    {
    case INSERT_VAL_LIST_COL_LIST_MISMATCH: 
      return 112;
    case DEGREE_DERIVED_TABLE_COL_LIST_MISMATCH: 
      return 113;
    case INVALID_CHAR_VAL_FOR_CAST: 
      return 121;
    case INVALID_ESC_CHAR: 
      return 122;
    case INVALID_ESC_SQNCE: 
      return 123;
    case INVALID_CURSOR_NAME: 
      return 132;
    case INVALID_CATALOG_NAME: 
      return 134;
    case INVALID_SCHEMA_NAME: 
      return 135;
    case SYNTAX_ERR_OR_ACCESS_VIOLATION: 
      return 139;
    case BASE_TABLE_OR_VIEW_EXISTS: 
      return 140;
    case BASE_TABLE_OR_VIEW_MISSING: 
      return 141;
    case INDEX_EXISTS: 
      return 142;
    case INDEX_MISSING: 
      return 143;
    case COLUMN_EXISTS: 
      return 144;
    case COLUMN_MISSING: 
      return 145;
    }
    return -1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/ParsingErrorID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
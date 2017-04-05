package com.simba.sqlengine.parser;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.type.PTCountLimit;
import com.simba.sqlengine.parser.type.PTStringConstraint;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.HashSet;

public class DefaultLimitChecker
  implements IPTLimitChecker
{
  private static final short DEFAULT_COUNT_VALUE = 0;
  private ParserLimits m_pLimits = new ParserLimits(null);
  
  public void checkString(PTStringConstraint paramPTStringConstraint, String paramString)
    throws SQLEngineException
  {
    switch (paramPTStringConstraint)
    {
    case BINARY_LITERAL_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxBinaryLiteralLen)) {
        throw createException(SQLEngineMessageKey.MAX_BINARY_LITERAL_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxBinaryLiteralLen) });
      }
      break;
    case CATALOG_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxCatalogNameLen)) {
        throw createException(SQLEngineMessageKey.MAX_CATALOG_NAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxCatalogNameLen) });
      }
      break;
    case CHAR_LITERAL_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxCharLiteralLen)) {
        throw createException(SQLEngineMessageKey.MAX_CHAR_LITERAL_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxCharLiteralLen) });
      }
      break;
    case COLUMN_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxColumnNameLen)) {
        throw createException(SQLEngineMessageKey.MAX_COLUMN_NAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxColumnNameLen) });
      }
      break;
    case CURSOR_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxCursorNameLen)) {
        throw createException(SQLEngineMessageKey.MAX_CURSOR_NAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxCursorNameLen) });
      }
      break;
    case IDENTIFIER_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxIdentifierLen)) {
        throw createException(SQLEngineMessageKey.MAX_IDENTIFIER_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxIdentifierLen) });
      }
      break;
    case PROCEDURE_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxProcedureNameLen)) {
        throw createException(SQLEngineMessageKey.MAX_PROCEDURE_NAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxProcedureNameLen) });
      }
      break;
    case RESERVED_KEYWORD: 
      if (this.m_pLimits.m_reservedKeywords.contains(paramString.toLowerCase())) {
        throw createException(SQLEngineMessageKey.RESERVED_KEYWORD_USED.name(), new String[] { paramString });
      }
      break;
    case SCHEMA_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxSchemaNameLen)) {
        throw createException(SQLEngineMessageKey.MAX_SCHEMA_NAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxSchemaNameLen) });
      }
      break;
    case STATEMENT_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxStatementLen)) {
        throw createException(SQLEngineMessageKey.MAX_STATEMENT_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxStatementLen) });
      }
      break;
    case TABLE_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxTableNameLen)) {
        throw createException(SQLEngineMessageKey.MAX_TABLE_NAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxTableNameLen) });
      }
      break;
    case USER_NAME_LEN: 
      if (!check(paramString.length(), this.m_pLimits.m_maxUsernameLen)) {
        throw createException(SQLEngineMessageKey.MAX_USERNAME_LEN_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxUsernameLen) });
      }
      break;
    }
  }
  
  public void checkCount(PTCountLimit paramPTCountLimit, int paramInt)
    throws SQLEngineException
  {
    switch (paramPTCountLimit)
    {
    case COLUMNS_IN_GROUP_BY: 
      if (!check(paramInt, this.m_pLimits.m_maxColumnsInGroupBy)) {
        throw createException(SQLEngineMessageKey.MAX_COLUMNS_IN_GROUP_BY_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxColumnsInGroupBy) });
      }
      break;
    case COLUMNS_IN_INDEX: 
      if (!check(paramInt, this.m_pLimits.m_maxColumnsInIndex)) {
        throw createException(SQLEngineMessageKey.MAX_COLUMNS_IN_INDEX_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxColumnsInIndex) });
      }
      break;
    case COLUMNS_IN_ORDER_BY: 
      if (!check(paramInt, this.m_pLimits.m_maxColumnsInOrderBy)) {
        throw createException(SQLEngineMessageKey.MAX_COLUMNS_IN_ORDER_BY_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxColumnsInOrderBy) });
      }
      break;
    case COLUMNS_IN_SELECT: 
      if (!check(paramInt, this.m_pLimits.m_maxColumnsInSelect)) {
        throw createException(SQLEngineMessageKey.MAX_COLUMNS_IN_SELECT_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxColumnsInSelect) });
      }
      break;
    case COLUMNS_IN_TABLE: 
      if (!check(paramInt, this.m_pLimits.m_maxColumnsInTable)) {
        throw createException(SQLEngineMessageKey.MAX_COLUMNS_IN_TABLE_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxColumnsInTable) });
      }
      break;
    case INDEX_SIZE: 
      if (!check(paramInt, this.m_pLimits.m_maxIndexSize)) {
        throw createException(SQLEngineMessageKey.MAX_INDEX_SIZE_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxIndexSize) });
      }
      break;
    case ROW_SIZE: 
      if (!check(paramInt, this.m_pLimits.m_maxRowSize)) {
        throw createException(SQLEngineMessageKey.MAX_ROW_SIZE_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxRowSize) });
      }
      break;
    case TABLES_IN_SELECT: 
      if (!check(paramInt, this.m_pLimits.m_maxTablesInSelect)) {
        throw createException(SQLEngineMessageKey.MAX_TABLES_IN_SELECT_EXCEEDED.name(), new String[] { String.valueOf(this.m_pLimits.m_maxTablesInSelect) });
      }
      break;
    }
  }
  
  public DefaultLimitChecker initLimitCheckValues(IConnection paramIConnection)
    throws SQLEngineException
  {
    try
    {
      String[] arrayOfString1 = paramIConnection.getProperty(62).getString().split(",");
      for (String str : arrayOfString1) {
        this.m_pLimits.m_reservedKeywords.add(str.toLowerCase());
      }
      this.m_pLimits.m_maxBinaryLiteralLen = paramIConnection.getProperty(65).getLong();
      this.m_pLimits.m_maxCharLiteralLen = paramIConnection.getProperty(67).getLong();
      this.m_pLimits.m_maxIndexSize = paramIConnection.getProperty(77).getLong();
      this.m_pLimits.m_maxRowSize = paramIConnection.getProperty(79).getLong();
      this.m_pLimits.m_maxStatementLen = paramIConnection.getProperty(82).getLong();
      this.m_pLimits.m_maxCatalogNameLen = paramIConnection.getProperty(66).getInt();
      this.m_pLimits.m_maxColumnNameLen = paramIConnection.getProperty(68).getInt();
      this.m_pLimits.m_maxColumnsInGroupBy = paramIConnection.getProperty(69).getInt();
      this.m_pLimits.m_maxColumnsInIndex = paramIConnection.getProperty(70).getInt();
      this.m_pLimits.m_maxColumnsInOrderBy = paramIConnection.getProperty(71).getInt();
      this.m_pLimits.m_maxColumnsInSelect = paramIConnection.getProperty(72).getInt();
      this.m_pLimits.m_maxColumnsInTable = paramIConnection.getProperty(73).getInt();
      this.m_pLimits.m_maxCursorNameLen = paramIConnection.getProperty(75).getInt();
      this.m_pLimits.m_maxIdentifierLen = paramIConnection.getProperty(76).getInt();
      this.m_pLimits.m_maxProcedureNameLen = paramIConnection.getProperty(78).getInt();
      this.m_pLimits.m_maxSchemaNameLen = paramIConnection.getProperty(81).getInt();
      this.m_pLimits.m_maxTableNameLen = paramIConnection.getProperty(83).getInt();
      this.m_pLimits.m_maxTablesInSelect = paramIConnection.getProperty(84).getInt();
      this.m_pLimits.m_maxUsernameLen = paramIConnection.getProperty(85).getInt();
      return this;
    }
    catch (BadPropertyKeyException localBadPropertyKeyException)
    {
      throw new SQLEngineException(SQLEngineMessageKey.INVALID_OPERATION.name(), localBadPropertyKeyException);
    }
    catch (ErrorException localErrorException)
    {
      throw new SQLEngineException(SQLEngineMessageKey.INVALID_OPERATION.name(), localErrorException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new SQLEngineException(SQLEngineMessageKey.INVALID_OPERATION.name(), localNumericOverflowException);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new SQLEngineException(SQLEngineMessageKey.INVALID_OPERATION.name(), localIncorrectTypeException);
    }
  }
  
  private boolean check(int paramInt, long paramLong)
    throws SQLEngineException
  {
    return (0L == paramLong) || (paramInt <= paramLong);
  }
  
  private SQLEngineException createException(String paramString, String[] paramArrayOfString)
  {
    return new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramString, paramArrayOfString);
  }
  
  private static class ParserLimits
  {
    HashSet<String> m_reservedKeywords = new HashSet();
    long m_maxBinaryLiteralLen;
    long m_maxCharLiteralLen;
    long m_maxIndexSize;
    long m_maxRowSize;
    long m_maxStatementLen;
    int m_maxCatalogNameLen;
    int m_maxColumnNameLen;
    int m_maxColumnsInGroupBy;
    int m_maxColumnsInIndex;
    int m_maxColumnsInOrderBy;
    int m_maxColumnsInSelect;
    int m_maxColumnsInTable;
    int m_maxCursorNameLen;
    int m_maxIdentifierLen;
    int m_maxProcedureNameLen;
    int m_maxSchemaNameLen;
    int m_maxTableNameLen;
    int m_maxTablesInSelect;
    int m_maxUsernameLen;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/DefaultLimitChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
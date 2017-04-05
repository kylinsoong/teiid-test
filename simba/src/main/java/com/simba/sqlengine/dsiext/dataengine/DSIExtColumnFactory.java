package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class DSIExtColumnFactory
  implements IColumnFactory
{
  private SqlDataEngine m_dataEngine;
  
  public DSIExtColumnFactory(SqlDataEngine paramSqlDataEngine)
  {
    this.m_dataEngine = paramSqlDataEngine;
  }
  
  public IColumn createColumn(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, List<String> paramList, Nullable paramNullable)
    throws ErrorException
  {
    int i = this.m_dataEngine.getContext().getSqlTypeForTypeName(paramString5);
    if (i == 0) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_SQL_TYPE_SPECIFIED.name(), new String[] { paramString5 });
    }
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(i);
    ColumnMetadata localColumnMetadata = new ColumnMetadata(localTypeMetadata);
    localColumnMetadata.setName(paramString4);
    localColumnMetadata.setSchemaName(paramString2);
    localColumnMetadata.setCatalogName(paramString1);
    localColumnMetadata.setLabel(paramString4);
    localColumnMetadata.setNullable(paramNullable);
    if (localTypeMetadata.isCharacterOrBinaryType())
    {
      if (paramList.size() > 1) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_TYPE_PARAMS.name(), new String[] { paramString5 });
      }
      if (paramList.size() == 1) {
        try
        {
          long l = Long.parseLong((String)paramList.get(0));
          localColumnMetadata.setColumnLength(l);
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          throw SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
        }
        catch (NumericOverflowException localNumericOverflowException)
        {
          throw SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
        }
      }
    }
    else if ((i == 92) || (i == 93))
    {
      if (paramList.size() > 1) {
        SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
      }
      if (paramList.size() == 1) {
        try
        {
          short s1 = Short.parseShort((String)paramList.get(0));
          localTypeMetadata.setPrecision(s1);
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          throw SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
        }
      }
    }
    else if (localTypeMetadata.isExactNumericType())
    {
      if (paramList.size() > 2) {
        throw SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
      }
      try
      {
        short s2;
        if (paramList.size() > 1)
        {
          s2 = Short.parseShort((String)paramList.get(0));
          localTypeMetadata.setPrecision(s2);
          short s3 = Short.parseShort((String)paramList.get(1));
          localTypeMetadata.setScale(s3);
        }
        else if (paramList.size() > 0)
        {
          s2 = Short.parseShort((String)paramList.get(0));
          localTypeMetadata.setPrecision(s2);
        }
      }
      catch (NumberFormatException localNumberFormatException3)
      {
        throw SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
      }
    }
    else if (paramList.size() != 0)
    {
      throw SQLEngineExceptionFactory.invalidTypeParameterException(paramString5);
    }
    return localColumnMetadata;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtColumnFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
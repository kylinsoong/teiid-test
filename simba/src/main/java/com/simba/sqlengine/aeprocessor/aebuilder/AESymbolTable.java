package com.simba.sqlengine.aeprocessor.aebuilder;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class AESymbolTable
{
  private final boolean m_isCaseSensitive;
  private final Map<String, AENamedRelationalExpr> m_symbols = new LinkedHashMap();
  
  AESymbolTable(boolean paramBoolean)
  {
    this.m_isCaseSensitive = paramBoolean;
  }
  
  public boolean isCaseSensitive()
  {
    return this.m_isCaseSensitive;
  }
  
  public AEColumnInfo findColumn(AEQColumnName paramAEQColumnName)
    throws ErrorException
  {
    if (paramAEQColumnName.hasQualifier()) {
      return findUniqueQualifiedColumn(paramAEQColumnName);
    }
    Object localObject = null;
    Iterator localIterator = this.m_symbols.values().iterator();
    while (localIterator.hasNext())
    {
      AENamedRelationalExpr localAENamedRelationalExpr = (AENamedRelationalExpr)localIterator.next();
      AEColumnInfo localAEColumnInfo = findColumn(localAENamedRelationalExpr, paramAEQColumnName.getColName());
      if (null != localAEColumnInfo)
      {
        if (null != localObject) {
          throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.COLUMN_REF_NOT_UNIQUE.name(), new String[] { paramAEQColumnName.toString() });
        }
        localObject = localAEColumnInfo;
      }
    }
    return (AEColumnInfo)localObject;
  }
  
  public void addTable(AENamedRelationalExpr paramAENamedRelationalExpr)
    throws ErrorException
  {
    String str = paramAENamedRelationalExpr.getTableName();
    if (hasSymbol(paramAENamedRelationalExpr.getTableName())) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.TABLE_NAME_NOT_UNIQUE.name(), new String[] { str });
    }
    HashSet localHashSet = new HashSet(paramAENamedRelationalExpr.getColumnCount());
    for (int i = 0; i < paramAENamedRelationalExpr.getColumnCount(); i++)
    {
      IColumn localIColumn = paramAENamedRelationalExpr.getColumn(i);
      if (0 == localIColumn.getName().length()) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.COLUMN_NAME_NOT_SPECIFIED.name(), new String[] { String.valueOf(i + 1), paramAENamedRelationalExpr.getTableName() });
      }
      if (!localHashSet.add(canonical(localIColumn.getName()))) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.COLUMN_NAME_DUPLICATED.name(), new String[] { localIColumn.getName(), paramAENamedRelationalExpr.getTableName() });
      }
    }
    this.m_symbols.put(canonical(str), paramAENamedRelationalExpr);
  }
  
  public boolean hasSymbol(String paramString)
  {
    return this.m_symbols.containsKey(canonical(paramString));
  }
  
  public int size()
  {
    return this.m_symbols.size();
  }
  
  public Iterator<AEColumnInfo> getColumnItr()
  {
    final Iterator localIterator = this.m_symbols.values().iterator();
    new Iterator()
    {
      private final Iterator<AENamedRelationalExpr> m_source = localIterator;
      private Iterator<AEColumnInfo> m_current = this.m_finished ? null : AESymbolTable.iteratorFromNamedRelationalExpr((AENamedRelationalExpr)this.m_source.next());
      private boolean m_finished = !this.m_source.hasNext();
      
      public boolean hasNext()
      {
        if (this.m_finished) {
          return false;
        }
        while (!this.m_current.hasNext())
        {
          if (!this.m_source.hasNext())
          {
            this.m_finished = true;
            return false;
          }
          this.m_current = AESymbolTable.iteratorFromNamedRelationalExpr((AENamedRelationalExpr)this.m_source.next());
        }
        return true;
      }
      
      public AEColumnInfo next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return (AEColumnInfo)this.m_current.next();
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException("remove() not supported.");
      }
    };
  }
  
  public Iterator<AEColumnInfo> getColumnItr(AEQTableName paramAEQTableName)
    throws SQLEngineException
  {
    AENamedRelationalExpr localAENamedRelationalExpr = findTable(paramAEQTableName);
    if (null != localAENamedRelationalExpr) {
      return iteratorFromNamedRelationalExpr(localAENamedRelationalExpr);
    }
    throw new SQLEngineException(DiagState.DIAG_BASE_TABLE_OR_VIEW_MISSING, SQLEngineMessageKey.TABLE_NOT_FOUND.name(), new String[] { paramAEQTableName.toString() });
  }
  
  private String canonical(String paramString)
  {
    return isCaseSensitive() ? paramString : paramString.toUpperCase();
  }
  
  private AEColumnInfo findColumn(AENamedRelationalExpr paramAENamedRelationalExpr, String paramString)
  {
    int i = paramAENamedRelationalExpr.findColumn(paramString, isCaseSensitive());
    if (0 <= i) {
      return new AEColumnInfo(paramAENamedRelationalExpr, i, false);
    }
    return null;
  }
  
  private AEColumnInfo findUniqueQualifiedColumn(AEQColumnName paramAEQColumnName)
    throws ErrorException
  {
    assert (paramAEQColumnName.hasQualifier());
    AEColumnInfo localAEColumnInfo = null;
    AENamedRelationalExpr localAENamedRelationalExpr = findTable(paramAEQColumnName.getQTable());
    if (null != localAENamedRelationalExpr)
    {
      localAEColumnInfo = findColumn(localAENamedRelationalExpr, paramAEQColumnName.getColName());
      if (null == localAEColumnInfo) {
        throw new SQLEngineException(DiagState.DIAG_COLUMN_MISSING, SQLEngineMessageKey.COLUMN_NOT_FOUND.name(), new String[] { paramAEQColumnName.toString() });
      }
    }
    return localAEColumnInfo;
  }
  
  private AENamedRelationalExpr findTable(AEQTableName paramAEQTableName)
  {
    AENamedRelationalExpr localAENamedRelationalExpr = (AENamedRelationalExpr)this.m_symbols.get(canonical(paramAEQTableName.getTableName()));
    if ((null != localAENamedRelationalExpr) && (localAENamedRelationalExpr.matchesName(paramAEQTableName, isCaseSensitive()))) {
      return localAENamedRelationalExpr;
    }
    return null;
  }
  
  private static Iterator<AEColumnInfo> iteratorFromNamedRelationalExpr(AENamedRelationalExpr paramAENamedRelationalExpr)
  {
    new AbstractList()
    {
      public AEColumnInfo get(int paramAnonymousInt)
      {
        return new AEColumnInfo(this.val$namedRelationalExpr, paramAnonymousInt, false);
      }
      
      public int size()
      {
        return this.val$namedRelationalExpr.getColumnCount();
      }
    }.iterator();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/AESymbolTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
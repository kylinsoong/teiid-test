package com.simba.sqlengine.aeprocessor;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.EnumSet;

public final class AEQTableName
{
  private static final AEQTableName EMPTY = new AEQTableName("", "", "");
  private final String m_catalogName;
  private final String m_schemaName;
  private final String m_tableName;
  
  public AEQTableName(String paramString1, String paramString2, String paramString3)
  {
    this.m_catalogName = (paramString1 == null ? "" : paramString1);
    this.m_schemaName = (paramString2 == null ? "" : paramString2);
    this.m_tableName = (paramString3 == null ? "" : paramString3);
    if (("".equals(this.m_tableName)) && ((!"".equals(this.m_catalogName)) || (!"".equals(this.m_schemaName)))) {
      throw new IllegalArgumentException("Table name was not specified: cannot specify catalog or schema name.");
    }
  }
  
  public boolean hasCatalogName()
  {
    return this.m_catalogName.length() > 0;
  }
  
  public boolean hasSchemaName()
  {
    return this.m_schemaName.length() > 0;
  }
  
  public boolean hasTableName()
  {
    return this.m_tableName.length() > 0;
  }
  
  public String getCatalogName()
  {
    return this.m_catalogName;
  }
  
  public String getSchemaName()
  {
    return this.m_schemaName;
  }
  
  public String getTableName()
  {
    return this.m_tableName;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(30);
    if (hasCatalogName())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_catalogName)).append(".");
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_schemaName)).append(".");
    }
    else if (hasSchemaName())
    {
      localStringBuilder.append(AEUtils.sqlQuoted(this.m_schemaName)).append(".");
    }
    localStringBuilder.append(AEUtils.sqlQuoted(this.m_tableName));
    return localStringBuilder.toString();
  }
  
  public static AEQTableName fromPTNode(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    if (PTNonterminalType.TABLE_NAME != paramPTNonterminalNode.getNonterminalType()) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    EnumSet localEnumSet = EnumSet.of(PTPositionalType.CATALOG_IDENT, PTPositionalType.SCHEMA_IDENT, PTPositionalType.TABLE_IDENT);
    if (!localEnumSet.equals(paramPTNonterminalNode.getAllPositionalTypes())) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    IPTNode localIPTNode1 = paramPTNonterminalNode.getChild(PTPositionalType.CATALOG_IDENT);
    IPTNode localIPTNode2 = paramPTNonterminalNode.getChild(PTPositionalType.SCHEMA_IDENT);
    IPTNode localIPTNode3 = paramPTNonterminalNode.getChild(PTPositionalType.TABLE_IDENT);
    if (((!localIPTNode1.isEmptyNode()) && (!(localIPTNode1 instanceof PTIdentifierNode))) || ((!localIPTNode2.isEmptyNode()) && (!(localIPTNode2 instanceof PTIdentifierNode))) || (!(localIPTNode3 instanceof PTIdentifierNode))) {
      throw SQLEngineExceptionFactory.invalidParseTreeException();
    }
    String str1 = ((PTIdentifierNode)localIPTNode3).getIdentifier();
    String str2 = AEUtils.getIdentifierString(localIPTNode1);
    String str3 = AEUtils.getIdentifierString(localIPTNode2);
    return new AEQTableName(str2, str3, str1);
  }
  
  public static AEQTableName empty()
  {
    return EMPTY;
  }
  
  public static class AEQTableNameBuilder
  {
    private String m_catalogName = null;
    private String m_schemaName = null;
    private String m_tableName = null;
    private AEQTableName m_source;
    
    public AEQTableNameBuilder()
    {
      this(AEQTableName.empty());
    }
    
    public AEQTableNameBuilder(AEQTableName paramAEQTableName)
    {
      if (null == paramAEQTableName) {
        throw new NullPointerException("Cannot build from a null AEQTableName.");
      }
      this.m_source = paramAEQTableName;
    }
    
    public void setCatalogName(String paramString)
    {
      this.m_catalogName = (null == paramString ? "" : paramString);
    }
    
    public void setSchemaName(String paramString)
    {
      this.m_schemaName = (null == paramString ? "" : paramString);
    }
    
    public void setTableName(String paramString)
    {
      this.m_tableName = (null == paramString ? "" : paramString);
    }
    
    public AEQTableName build()
    {
      if ((null == this.m_catalogName) && (null == this.m_schemaName) && (null == this.m_tableName)) {
        return this.m_source;
      }
      return new AEQTableName(null == this.m_catalogName ? this.m_source.getCatalogName() : this.m_catalogName, null == this.m_schemaName ? this.m_source.getSchemaName() : this.m_schemaName, null == this.m_tableName ? this.m_source.getTableName() : this.m_tableName);
    }
    
    public void reset()
    {
      this.m_catalogName = (this.m_schemaName = this.m_tableName = null);
      this.m_source = AEQTableName.empty();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/AEQTableName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
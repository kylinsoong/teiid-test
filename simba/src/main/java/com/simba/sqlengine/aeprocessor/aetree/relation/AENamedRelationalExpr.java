package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.dsi.exceptions.InvalidOperationException;
import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class AENamedRelationalExpr
  extends AERelationalExpr
{
  private ArrayList<RenameColumn> m_renamedColumns;
  private String m_correlationSpecName;
  
  protected AENamedRelationalExpr()
  {
    this.m_renamedColumns = new ArrayList();
    this.m_correlationSpecName = "";
  }
  
  protected AENamedRelationalExpr(AENamedRelationalExpr paramAENamedRelationalExpr)
  {
    super(paramAENamedRelationalExpr);
    this.m_renamedColumns = new ArrayList(paramAENamedRelationalExpr.m_renamedColumns);
    this.m_correlationSpecName = paramAENamedRelationalExpr.m_correlationSpecName;
  }
  
  public abstract String getCatalogName();
  
  public IColumn getColumn(int paramInt)
  {
    if (!hasDerivedColumnList()) {
      return getBaseColumn(paramInt);
    }
    return (IColumn)this.m_renamedColumns.get(paramInt);
  }
  
  public String getCorrelationName()
  {
    return this.m_correlationSpecName;
  }
  
  public AEQTableName getQTableName()
  {
    return new AEQTableName(getCatalogName(), getSchemaName(), getTableName());
  }
  
  public abstract String getSchemaName();
  
  public abstract String getTableName();
  
  public boolean hasDerivedColumnList()
  {
    return !this.m_renamedColumns.isEmpty();
  }
  
  public boolean matchesName(AEQTableName paramAEQTableName, boolean paramBoolean)
  {
    if ("".equals(getTableName())) {
      throw new IllegalStateException("Named Relational Expression must have a name: " + getLogString());
    }
    if (!stringEquals(getTableName(), paramAEQTableName.getTableName(), paramBoolean)) {
      return false;
    }
    if ((!"".equals(paramAEQTableName.getSchemaName())) && (!stringEquals(getSchemaName(), paramAEQTableName.getSchemaName(), paramBoolean))) {
      return false;
    }
    return ("".equals(paramAEQTableName.getCatalogName())) || (stringEquals(getCatalogName(), paramAEQTableName.getCatalogName(), paramBoolean));
  }
  
  public boolean matchesNameCaseSensitive(AEQTableName paramAEQTableName)
  {
    return matchesName(paramAEQTableName, true);
  }
  
  public boolean matchesNameCaseInsensitive(AEQTableName paramAEQTableName)
  {
    return matchesName(paramAEQTableName, false);
  }
  
  public int findColumn(String paramString, boolean paramBoolean)
  {
    if ((null == paramString) || (paramString.length() == 0)) {
      throw new IllegalArgumentException("name cannot be null or empty.");
    }
    int i = getColumnCount();
    for (int j = 0; j < i; j++)
    {
      String str = getColumn(j).getName();
      if (paramBoolean ? paramString.equals(str) : paramString.equalsIgnoreCase(str)) {
        return j;
      }
    }
    return -1;
  }
  
  public AEColumnInfo findQColumn(AEQColumnName paramAEQColumnName, boolean paramBoolean)
    throws ErrorException
  {
    boolean bool = paramAEQColumnName.hasQualifier();
    if ((bool) && (!matchesName(paramAEQColumnName.getQTable(), paramBoolean))) {
      return null;
    }
    int i = findColumn(paramAEQColumnName.getColName(), paramBoolean);
    if (0 > i)
    {
      if (bool) {
        throw new SQLEngineException(DiagState.DIAG_COLUMN_MISSING, SQLEngineMessageKey.COLUMN_NOT_FOUND.name(), new String[] { paramAEQColumnName.toString() });
      }
      return null;
    }
    return new AEColumnInfo(this, i, false);
  }
  
  public void overrideColumnNames(List<String> paramList, boolean paramBoolean)
    throws ErrorException
  {
    if (null == paramList) {
      throw new IllegalArgumentException("names cannot be null or empty.");
    }
    if (this.m_correlationSpecName.length() == 0) {
      throw new InvalidOperationException();
    }
    if (paramList.size() != getColumnCount())
    {
      String[] arrayOfString = { this.m_correlationSpecName, Integer.toString(getColumnCount()), Integer.toString(paramList.size()) };
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.CORR_SPEC_INVALID_NUM_COLS.name(), arrayOfString);
    }
    checkNameUniqueness(paramList, paramBoolean);
    this.m_renamedColumns.clear();
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      IColumn localIColumn = getBaseColumn(i);
      if (null == localIColumn) {
        throw new NullPointerException("Could not retrieve column.");
      }
      this.m_renamedColumns.add(new RenameColumn(localIColumn, str));
      i++;
    }
  }
  
  public void setCorrelationName(String paramString)
  {
    if (null == paramString) {
      throw new NullPointerException("Correlation name cannot be null.");
    }
    this.m_correlationSpecName = paramString;
  }
  
  public abstract IColumn getBaseColumn(int paramInt);
  
  private void checkNameUniqueness(List<String> paramList, boolean paramBoolean)
    throws ErrorException
  {
    HashSet localHashSet = new HashSet(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (!paramBoolean) {
        str = str.toLowerCase();
      }
      if (!localHashSet.add(str))
      {
        String[] arrayOfString = { this.m_correlationSpecName.toUpperCase(), str.toUpperCase() };
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.CORR_SPEC_NON_UNIQUE_COLS.name(), arrayOfString);
      }
    }
  }
  
  private static boolean stringEquals(String paramString1, String paramString2, boolean paramBoolean)
  {
    return paramBoolean ? paramString1.equals(paramString2) : paramString1.equalsIgnoreCase(paramString2);
  }
  
  private static class RenameColumn
    implements IColumn
  {
    private final IColumn m_col;
    private final String m_name;
    
    public RenameColumn(IColumn paramIColumn, String paramString)
    {
      this.m_col = paramIColumn;
      this.m_name = paramString;
    }
    
    public String getCatalogName()
    {
      return this.m_col.getCatalogName();
    }
    
    public long getColumnLength()
    {
      return this.m_col.getColumnLength();
    }
    
    public long getDisplaySize()
      throws ErrorException
    {
      return this.m_col.getDisplaySize();
    }
    
    public String getLabel()
    {
      return this.m_name;
    }
    
    public String getName()
    {
      return this.m_name;
    }
    
    public Nullable getNullable()
    {
      return this.m_col.getNullable();
    }
    
    public String getSchemaName()
    {
      return this.m_col.getSchemaName();
    }
    
    public Searchable getSearchable()
    {
      return this.m_col.getSearchable();
    }
    
    public String getTableName()
    {
      return this.m_col.getTableName();
    }
    
    public TypeMetadata getTypeMetadata()
    {
      return this.m_col.getTypeMetadata();
    }
    
    public Updatable getUpdatable()
    {
      return this.m_col.getUpdatable();
    }
    
    public boolean isAutoUnique()
    {
      return this.m_col.isAutoUnique();
    }
    
    public boolean isCaseSensitive()
    {
      return this.m_col.isCaseSensitive();
    }
    
    public boolean isDefinitelyWritable()
    {
      return this.m_col.isDefinitelyWritable();
    }
    
    public boolean isUnnamed()
    {
      return this.m_col.isUnnamed();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AENamedRelationalExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
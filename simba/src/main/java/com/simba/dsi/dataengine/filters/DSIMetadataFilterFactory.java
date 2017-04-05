package com.simba.dsi.dataengine.filters;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.dataengine.utilities.MetadataSourceID;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.List;
import java.util.Map;

public class DSIMetadataFilterFactory
{
  private boolean m_performFiltering;
  
  public DSIMetadataFilterFactory(boolean paramBoolean)
  {
    this.m_performFiltering = paramBoolean;
  }
  
  public void createFilters(MetadataSourceID paramMetadataSourceID, List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
    throws ErrorException
  {
    switch (paramMetadataSourceID)
    {
    case CATALOG_SCHEMA_ONLY: 
      createCatalogSchemaOnlyFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case COLUMN_PRIVILEGES: 
      createColumnPrivilegesFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case COLUMNS: 
    case PSEUDO_COLUMNS_JDBC41: 
      createColumnsFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case FOREIGN_KEYS: 
      createForeignKeysFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case PRIMARY_KEYS: 
      createPrimaryKeysFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case PROCEDURE_COLUMNS: 
    case FUNCTION_COLUMNS_JDBC4: 
      createProcedureColumnsFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case PROCEDURES: 
    case FUNCTIONS_JDBC4: 
      createProceduresFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case SPECIAL_COLUMNS: 
      createSpecialColumnsFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case STATISTICS: 
      createStatisticsFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case TABLE_PRIVILEGES: 
      createTablePrivilegesFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case CATALOG_ONLY: 
    case SCHEMA_ONLY: 
    case TABLETYPE_ONLY: 
      break;
    case TABLES: 
      createTablesFilters(paramList, paramString1, paramString2, paramBoolean, paramList1, paramMap);
      break;
    case TYPE_INFO: 
      createTypeInfoFilters(paramList, paramList1, paramMap);
      break;
    default: 
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_METADATA_ID.name(), paramMetadataSourceID.toString(), ExceptionType.DEFAULT);
    }
  }
  
  private void addRestriction(String paramString, MetadataSourceColumnTag paramMetadataSourceColumnTag, Map<MetadataSourceColumnTag, String> paramMap)
  {
    if (null != paramString) {
      paramMap.put(paramMetadataSourceColumnTag, paramString);
    }
  }
  
  private void addRestriction(StringPatternFilter paramStringPatternFilter, String paramString, MetadataSourceColumnTag paramMetadataSourceColumnTag, Map<MetadataSourceColumnTag, String> paramMap)
  {
    if ((null != paramString) && ((!this.m_performFiltering) || (!paramStringPatternFilter.hasPatternFilter()))) {
      paramMap.put(paramMetadataSourceColumnTag, paramString);
    }
  }
  
  private void createCatalogSchemaOnlyFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (2 <= paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
    }
    else
    {
      createPatternFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString1, paramList1, paramMap);
    }
  }
  
  private void createColumnPrivilegesFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (4 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.COLUMN_NAME, (String)paramList.get(3), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.COLUMN_NAME, (String)paramList.get(3), paramString1, paramList1, paramMap);
    }
  }
  
  private void createColumnsFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (4 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.COLUMN_NAME, (String)paramList.get(3), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.COLUMN_NAME, (String)paramList.get(3), paramString1, paramList1, paramMap);
    }
  }
  
  private void createForeignKeysFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (6 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.FOREIGN_KEY_CATALOG_NAME, (String)paramList.get(3), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.FOREIGN_KEY_SCHEMA_NAME, (String)paramList.get(4), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.FOREIGN_KEY_TABLE_NAME, (String)paramList.get(5), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME, (String)paramList.get(1), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME, (String)paramList.get(2), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.FOREIGN_KEY_CATALOG_NAME, (String)paramList.get(3), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.FOREIGN_KEY_SCHEMA_NAME, (String)paramList.get(4), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.FOREIGN_KEY_TABLE_NAME, (String)paramList.get(5), paramList1, paramMap);
    }
  }
  
  private void createIdentifierFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString1, String paramString2, List<IFilter> paramList, Map<MetadataSourceColumnTag, String> paramMap)
  {
    paramList.add(new IdentifierFilter(paramMetadataSourceColumnTag, paramString1, paramString2));
    addRestriction(paramString1, paramMetadataSourceColumnTag, paramMap);
  }
  
  private void createPatternFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString1, String paramString2, List<IFilter> paramList, Map<MetadataSourceColumnTag, String> paramMap)
  {
    StringPatternFilter localStringPatternFilter = new StringPatternFilter(paramMetadataSourceColumnTag, paramString1, paramString2);
    paramList.add(localStringPatternFilter);
    addRestriction(localStringPatternFilter, paramString1, paramMetadataSourceColumnTag, paramMap);
  }
  
  private void createPrimaryKeysFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (3 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.PRIMARY_KEY_CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.PRIMARY_KEY_SCHEMA_NAME, (String)paramList.get(1), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.PRIMARY_KEY_TABLE_NAME, (String)paramList.get(2), paramList1, paramMap);
    }
  }
  
  private void createProcedureColumnsFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (4 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.PROCEDURE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.COLUMN_NAME, (String)paramList.get(3), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.PROCEDURE_NAME, (String)paramList.get(2), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.COLUMN_NAME, (String)paramList.get(3), paramString1, paramList1, paramMap);
    }
  }
  
  private void createProceduresFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (3 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.PROCEDURE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.PROCEDURE_NAME, (String)paramList.get(2), paramString1, paramList1, paramMap);
    }
  }
  
  private void createSpecialColumnsFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (4 <= paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(3), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(1), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(2), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(3), paramList1, paramMap);
    }
    paramList1.add(new SmallIntFilter(MetadataSourceColumnTag.SCOPE, (String)paramList.get(4)));
    addRestriction((String)paramList.get(4), MetadataSourceColumnTag.SCOPE, paramMap);
  }
  
  private void createStatisticsFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (3 <= paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramList1, paramMap);
      createStringFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramList1, paramMap);
    }
  }
  
  private void createStringFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString, List<IFilter> paramList, Map<MetadataSourceColumnTag, String> paramMap)
  {
    paramList.add(new StringFilter(paramMetadataSourceColumnTag, paramString));
    addRestriction(paramString, paramMetadataSourceColumnTag, paramMap);
  }
  
  private void createTablePrivilegesFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (3 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
    }
    else
    {
      createStringFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString1, paramList1, paramMap);
    }
  }
  
  private void createTablesFilters(List<String> paramList, String paramString1, String paramString2, boolean paramBoolean, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (4 == paramList.size());
    if (paramBoolean)
    {
      createIdentifierFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString2, paramList1, paramMap);
      createIdentifierFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString2, paramList1, paramMap);
    }
    else
    {
      createPatternFilter(MetadataSourceColumnTag.CATALOG_NAME, (String)paramList.get(0), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.SCHEMA_NAME, (String)paramList.get(1), paramString1, paramList1, paramMap);
      createPatternFilter(MetadataSourceColumnTag.TABLE_NAME, (String)paramList.get(2), paramString1, paramList1, paramMap);
    }
    if (this.m_performFiltering) {
      paramList1.add(new StringListFilter(MetadataSourceColumnTag.TABLE_TYPE, (String)paramList.get(3)));
    } else {
      createStringFilter(MetadataSourceColumnTag.TABLE_TYPE, (String)paramList.get(3), paramList1, paramMap);
    }
  }
  
  private void createTypeInfoFilters(List<String> paramList, List<IFilter> paramList1, Map<MetadataSourceColumnTag, String> paramMap)
  {
    assert (1 == paramList.size());
    String str = (String)paramList.get(0);
    if (null == str) {
      return;
    }
    if (!str.equals("0"))
    {
      paramList1.add(new SmallIntFilter(MetadataSourceColumnTag.DATA_TYPE, str));
      addRestriction((String)paramList.get(0), MetadataSourceColumnTag.DATA_TYPE, paramMap);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/DSIMetadataFilterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
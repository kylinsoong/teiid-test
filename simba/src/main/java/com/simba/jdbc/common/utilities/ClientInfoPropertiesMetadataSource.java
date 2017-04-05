package com.simba.jdbc.common.utilities;

import com.simba.dsi.core.utilities.ClientInfoData;
import com.simba.dsi.dataengine.impl.DSISimpleResultSet;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.DSITypeUtilities;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.support.exceptions.ErrorException;
import com.simba.utilities.MetaDataFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientInfoPropertiesMetadataSource
  extends DSISimpleResultSet
{
  private List<ClientInfoPropertyColumnInfo> m_columnData = null;
  private List<ColumnMetadata> m_columns = null;
  private int m_numRows;
  
  public ClientInfoPropertiesMetadataSource(Map<String, ClientInfoData> paramMap)
  {
    initializeData(paramMap);
    this.m_columns = MetaDataFactory.createClientInfoPropertiesMetadata();
  }
  
  public boolean getData(int paramInt, long paramLong1, long paramLong2, DataWrapper paramDataWrapper)
    throws ErrorException
  {
    switch (paramInt)
    {
    case 0: 
      return DSITypeUtilities.outputVarCharStringData(((ClientInfoPropertyColumnInfo)this.m_columnData.get(getCurrentRow())).m_name, paramDataWrapper, paramLong1, paramLong2);
    case 1: 
      paramDataWrapper.setInteger(((ClientInfoPropertyColumnInfo)this.m_columnData.get(getCurrentRow())).m_maxLength);
      return false;
    case 2: 
      return DSITypeUtilities.outputVarCharStringData(((ClientInfoPropertyColumnInfo)this.m_columnData.get(getCurrentRow())).m_defaultValue, paramDataWrapper, paramLong1, paramLong2);
    case 3: 
      return DSITypeUtilities.outputVarCharStringData(((ClientInfoPropertyColumnInfo)this.m_columnData.get(getCurrentRow())).m_description, paramDataWrapper, paramLong1, paramLong2);
    }
    return false;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_numRows;
  }
  
  public ArrayList<? extends IColumn> getSelectColumns()
    throws ErrorException
  {
    return (ArrayList)this.m_columns;
  }
  
  public boolean hasMoreRows()
    throws ErrorException
  {
    return getCurrentRow() + 1 < this.m_numRows;
  }
  
  public boolean hasRowCount()
  {
    return true;
  }
  
  protected void doCloseCursor()
    throws ErrorException
  {
    this.m_columnData = null;
  }
  
  protected boolean doMoveToNextRow()
    throws ErrorException
  {
    return getCurrentRow() < this.m_numRows;
  }
  
  private void initializeData(Map<String, ClientInfoData> paramMap)
  {
    this.m_columnData = new ArrayList();
    Set localSet = paramMap.keySet();
    String[] arrayOfString1 = (String[])localSet.toArray(new String[localSet.size()]);
    Arrays.sort(arrayOfString1, 0, arrayOfString1.length);
    ClientInfoPropertyColumnInfo localClientInfoPropertyColumnInfo = null;
    ClientInfoData localClientInfoData = null;
    for (String str : arrayOfString1)
    {
      localClientInfoData = (ClientInfoData)paramMap.get(str);
      localClientInfoPropertyColumnInfo = new ClientInfoPropertyColumnInfo(null);
      localClientInfoPropertyColumnInfo.m_name = localClientInfoData.getName();
      localClientInfoPropertyColumnInfo.m_defaultValue = localClientInfoData.getDefaultValue();
      localClientInfoPropertyColumnInfo.m_maxLength = localClientInfoData.getMaxLength();
      localClientInfoPropertyColumnInfo.m_description = localClientInfoData.getDescription();
      this.m_columnData.add(localClientInfoPropertyColumnInfo);
    }
    this.m_numRows = arrayOfString1.length;
  }
  
  private static class ClientInfoPropertyColumnInfo
  {
    public String m_name;
    public int m_maxLength;
    public String m_defaultValue;
    public String m_description;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/utilities/ClientInfoPropertiesMetadataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
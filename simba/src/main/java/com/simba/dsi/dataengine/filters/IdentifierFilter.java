package com.simba.dsi.dataengine.filters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.exceptions.IncorrectTypeException;

public class IdentifierFilter
  implements IFilter
{
  private MetadataSourceColumnTag m_columnTag;
  private String m_value = null;
  
  public IdentifierFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString1, String paramString2)
  {
    this.m_columnTag = paramMetadataSourceColumnTag;
    if (null != paramString1)
    {
      int i = 0;
      int j = -1;
      if ((null != paramString2) && (0 < paramString2.length())) {
        j = paramString1.indexOf(paramString2);
      }
      if (-1 != j)
      {
        int k = paramString1.indexOf(paramString2, j + 1);
        if (-1 != k)
        {
          i = 1;
          this.m_value = paramString1.substring(j, k);
        }
      }
      if (i == 0) {
        this.m_value = paramString1.trim();
      }
    }
  }
  
  public boolean filter(DataWrapper paramDataWrapper)
  {
    if (null == this.m_value) {
      return true;
    }
    try
    {
      switch (paramDataWrapper.getType())
      {
      case -8: 
      case 1: 
        return this.m_value.equalsIgnoreCase(paramDataWrapper.getChar());
      case -9: 
      case 12: 
        return this.m_value.equalsIgnoreCase(paramDataWrapper.getVarChar());
      case -10: 
      case -1: 
        return this.m_value.equalsIgnoreCase(paramDataWrapper.getLongVarChar());
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException) {}
    if (!$assertionsDisabled) {
      throw new AssertionError();
    }
    return false;
  }
  
  public MetadataSourceColumnTag getColumnTag()
  {
    return this.m_columnTag;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/IdentifierFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
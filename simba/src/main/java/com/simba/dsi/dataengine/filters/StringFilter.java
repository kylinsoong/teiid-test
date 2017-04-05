package com.simba.dsi.dataengine.filters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.exceptions.IncorrectTypeException;

public class StringFilter
  implements IFilter
{
  private MetadataSourceColumnTag m_columnTag;
  private final String m_value;
  
  public StringFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString)
  {
    this.m_columnTag = paramMetadataSourceColumnTag;
    if (null != paramString) {
      this.m_value = paramString.trim();
    } else {
      this.m_value = null;
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
      case 1: 
        return this.m_value.equals(paramDataWrapper.getChar());
      case 12: 
        return this.m_value.equals(paramDataWrapper.getVarChar());
      case -1: 
        return this.m_value.equals(paramDataWrapper.getLongVarChar());
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/StringFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
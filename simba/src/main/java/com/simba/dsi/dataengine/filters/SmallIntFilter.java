package com.simba.dsi.dataengine.filters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.exceptions.IncorrectTypeException;

public class SmallIntFilter
  implements IFilter
{
  private MetadataSourceColumnTag m_columnTag;
  private Integer m_value = null;
  
  public SmallIntFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString)
  {
    this.m_columnTag = paramMetadataSourceColumnTag;
    if (null != paramString) {
      this.m_value = Integer.valueOf(paramString.toString());
    }
  }
  
  public boolean filter(DataWrapper paramDataWrapper)
  {
    if (null == this.m_value) {
      return true;
    }
    try
    {
      if (MetadataSourceColumnTag.SCOPE == this.m_columnTag)
      {
        if (paramDataWrapper.isNull()) {
          return true;
        }
        return this.m_value.intValue() <= paramDataWrapper.getSmallInt().intValue();
      }
      return this.m_value.equals(paramDataWrapper.getSmallInt());
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      if (!$assertionsDisabled) {
        throw new AssertionError();
      }
    }
    return false;
  }
  
  public MetadataSourceColumnTag getColumnTag()
  {
    return this.m_columnTag;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/SmallIntFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
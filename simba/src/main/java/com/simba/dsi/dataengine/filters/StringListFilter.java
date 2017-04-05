package com.simba.dsi.dataengine.filters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.exceptions.IncorrectTypeException;
import java.util.ArrayList;
import java.util.Iterator;

public class StringListFilter
  implements IFilter
{
  private MetadataSourceColumnTag m_columnTag;
  private ArrayList<String> m_values = null;
  private final String m_originalValue;
  
  public StringListFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString)
  {
    this.m_columnTag = paramMetadataSourceColumnTag;
    this.m_originalValue = paramString;
  }
  
  public boolean filter(DataWrapper paramDataWrapper)
  {
    if (null == this.m_originalValue) {
      return true;
    }
    if (null == this.m_values)
    {
      localObject1 = this.m_originalValue.split(",");
      this.m_values = new ArrayList(localObject1.length);
      for (String str2 : localObject1)
      {
        str2 = str2.trim();
        if ((1 < str2.length()) && (str2.startsWith("'")) && (str2.endsWith("'"))) {
          this.m_values.add(str2.substring(1, str2.length() - 1));
        } else if (0 < str2.length()) {
          this.m_values.add(str2);
        }
      }
    }
    Object localObject1 = null;
    try
    {
      switch (paramDataWrapper.getType())
      {
      case -8: 
      case 1: 
        localObject1 = paramDataWrapper.getChar();
        break;
      case -9: 
      case 12: 
        localObject1 = paramDataWrapper.getVarChar();
        break;
      case -10: 
      case -1: 
        localObject1 = paramDataWrapper.getLongVarChar();
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      if (!$assertionsDisabled) {
        throw new AssertionError();
      }
      return false;
    }
    Iterator localIterator = this.m_values.iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      if (str1.equalsIgnoreCase((String)localObject1)) {
        return true;
      }
    }
    return false;
  }
  
  public MetadataSourceColumnTag getColumnTag()
  {
    return this.m_columnTag;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/StringListFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
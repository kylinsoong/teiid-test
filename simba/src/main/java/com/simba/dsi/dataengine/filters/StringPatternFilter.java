package com.simba.dsi.dataengine.filters;

import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.MetadataSourceColumnTag;
import com.simba.dsi.exceptions.IncorrectTypeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringPatternFilter
  implements IFilter
{
  private MetadataSourceColumnTag m_columnTag;
  private Pattern m_regEx = null;
  private String m_valueAsRegexStr = null;
  boolean m_hasPatternFilter = false;
  
  public StringPatternFilter(MetadataSourceColumnTag paramMetadataSourceColumnTag, String paramString1, String paramString2)
  {
    this.m_columnTag = paramMetadataSourceColumnTag;
    if (null != paramString1) {
      this.m_valueAsRegexStr = convertToRegexString(paramString1, paramString2);
    }
  }
  
  public boolean filter(DataWrapper paramDataWrapper)
  {
    if (null == this.m_valueAsRegexStr) {
      return true;
    }
    if (paramDataWrapper.isNull()) {
      return false;
    }
    if (null == this.m_regEx) {
      this.m_regEx = Pattern.compile(this.m_valueAsRegexStr);
    }
    try
    {
      switch (paramDataWrapper.getType())
      {
      case -8: 
      case 1: 
        return this.m_regEx.matcher(paramDataWrapper.getChar()).matches();
      case -9: 
      case 12: 
        return this.m_regEx.matcher(paramDataWrapper.getVarChar()).matches();
      case -10: 
      case -1: 
        return this.m_regEx.matcher(paramDataWrapper.getLongVarChar()).matches();
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
  
  public boolean hasPatternFilter()
  {
    return this.m_hasPatternFilter;
  }
  
  private String convertToRegexString(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString1.length());
    int i = 0;
    int j = 0;
    int k = 0;
    while (j < paramString1.length()) {
      if (paramString1.regionMatches(j, paramString2, 0, paramString2.length()))
      {
        if (j > i) {
          localStringBuilder.append(Pattern.quote(paramString1.substring(i, j)));
        }
        if (k != 0)
        {
          localStringBuilder.append(Pattern.quote(paramString2));
          k = 0;
        }
        else
        {
          k = 1;
        }
        j += paramString2.length();
        i = j;
      }
      else
      {
        char c = paramString1.charAt(j);
        if (('_' == c) || ('%' == c))
        {
          if (j > i) {
            localStringBuilder.append(Pattern.quote(paramString1.substring(i, j)));
          }
          if (k != 0)
          {
            localStringBuilder.append(c);
          }
          else
          {
            if ('_' == c) {
              localStringBuilder.append('.');
            } else {
              localStringBuilder.append(".*");
            }
            this.m_hasPatternFilter = true;
          }
          j++;
          i = j;
        }
        else
        {
          j++;
        }
        k = 0;
      }
    }
    if ((k == 0) && (j > i)) {
      localStringBuilder.append(Pattern.quote(paramString1.substring(i, j)));
    }
    return "\\A" + localStringBuilder.toString() + "\\z";
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/filters/StringPatternFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
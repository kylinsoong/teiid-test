package com.simba.jdbc.common;

import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.SettingReader;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ExceptionUtilities;
import com.simba.utilities.FunctionID;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SWarningListener
  implements IWarningListener
{
  private static final HashMap<WarningCode, String> WARNING_MAP = new HashMap()
  {
    private static final long serialVersionUID = -4253295231118746567L;
  };
  private List<Warning> m_warnings = new ArrayList();
  private IMessageSource m_messageSource = null;
  private Locale m_locale = null;
  private SQLWarning m_sqlWarning = null;
  private SQLWarning m_tailSqlWarning = null;
  private FunctionID m_currFunction = null;
  
  protected SWarningListener(IMessageSource paramIMessageSource, FunctionID paramFunctionID)
  {
    this.m_messageSource = paramIMessageSource;
    this.m_currFunction = paramFunctionID;
    String str = SettingReader.readSetting("DriverLocale");
    this.m_locale = Locale.ENGLISH;
    if (null != str) {
      this.m_locale = ExceptionUtilities.createLocale(str);
    }
  }
  
  public synchronized void clear()
  {
    if (null != this.m_sqlWarning)
    {
      this.m_warnings.clear();
      this.m_sqlWarning = null;
      this.m_tailSqlWarning = null;
    }
  }
  
  public void clearAndSetFunction(FunctionID paramFunctionID)
  {
    clear();
    this.m_currFunction = paramFunctionID;
  }
  
  public Locale getLocale()
  {
    return this.m_locale;
  }
  
  public IMessageSource getMessageSource()
  {
    return this.m_messageSource;
  }
  
  public SQLWarning getSQLWarnings()
  {
    return this.m_sqlWarning;
  }
  
  public List<Warning> getWarnings()
  {
    return this.m_warnings;
  }
  
  public synchronized void postWarning(Warning paramWarning)
  {
    if (null == paramWarning) {
      return;
    }
    paramWarning.setLocalizedWarningMessage(this.m_messageSource, this.m_locale);
    this.m_warnings.add(paramWarning);
    SQLWarning localSQLWarning = createSQLWarning(paramWarning);
    if (null == this.m_sqlWarning)
    {
      this.m_sqlWarning = localSQLWarning;
      this.m_tailSqlWarning = localSQLWarning;
    }
    else
    {
      this.m_tailSqlWarning.setNextWarning(localSQLWarning);
      this.m_tailSqlWarning = localSQLWarning;
    }
  }
  
  public void setCurrentFunction(FunctionID paramFunctionID)
  {
    this.m_currFunction = paramFunctionID;
  }
  
  public void setLocale(Locale paramLocale)
  {
    this.m_locale = paramLocale;
  }
  
  private SQLWarning createSQLWarning(Warning paramWarning)
  {
    String str;
    if (paramWarning.hasCustomState()) {
      str = paramWarning.getCustomState();
    } else {
      str = getSQLState(this.m_currFunction, paramWarning.getWarningCode());
    }
    if (-1 != paramWarning.getNativeErrorCode()) {
      return new SQLWarning(paramWarning.getMessage(), str, paramWarning.getNativeErrorCode());
    }
    return new SQLWarning(paramWarning.getMessage(), str);
  }
  
  private String getSQLState(FunctionID paramFunctionID, WarningCode paramWarningCode)
  {
    if (null == paramFunctionID) {
      return (String)WARNING_MAP.get(WarningCode.GENERAL_WARNING);
    }
    switch (paramFunctionID)
    {
    case CONNECTION_UPDATE_SETTINGS: 
      switch (paramWarningCode)
      {
      case STRING_RIGHT_TRUNCATION_WARNING: 
      case INVALID_CONNECTION_STRING_ATTRIBUTE: 
      case OPTIONAL_VALUE_CHANGED: 
        return (String)WARNING_MAP.get(paramWarningCode);
      }
      break;
    case CONNECTION_SET_PROPERTY: 
    case STATEMENT_SET_PROPERTY: 
      switch (paramWarningCode)
      {
      case OPTIONAL_VALUE_CHANGED: 
        return (String)WARNING_MAP.get(paramWarningCode);
      }
      break;
    case STATEMENT_PREPARE: 
      switch (paramWarningCode)
      {
      case OPTIONAL_VALUE_CHANGED: 
        return (String)WARNING_MAP.get(paramWarningCode);
      case FRACTIONAL_TRUNCATION: 
        return (String)WARNING_MAP.get(paramWarningCode);
      }
      break;
    case STATEMENT_EXECUTE: 
      switch (paramWarningCode)
      {
      case OPTIONAL_VALUE_CHANGED: 
      case FRACTIONAL_TRUNCATION: 
      case CURSOR_OPERATION_CONFLICT: 
      case NULL_VALUE_ELIMINATED_IN_SET_FUNCTION: 
      case PRIVILEGE_NOT_GRANTED: 
      case PRIVILEGE_NOT_REVOKED: 
        return (String)WARNING_MAP.get(paramWarningCode);
      }
      break;
    }
    return (String)WARNING_MAP.get(WarningCode.GENERAL_WARNING);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SWarningListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
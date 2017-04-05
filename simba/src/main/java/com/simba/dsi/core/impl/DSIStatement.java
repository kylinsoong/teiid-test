package com.simba.dsi.core.impl;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.AttributeDataMap;
import com.simba.dsi.core.utilities.PropertyLimitKeys;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.Hashtable;
import java.util.Map;

public abstract class DSIStatement
  implements IStatement
{
  private AttributeDataMap m_settings = new AttributeDataMap();
  private Map<Integer, PropertyLimitKeys> m_propertyLimitKeyMap = new Hashtable();
  private IConnection m_parentConnection = null;
  private String m_cursorName = "";
  private IWarningListener m_warningListener = null;
  
  protected DSIStatement(IConnection paramIConnection)
    throws ErrorException
  {
    this.m_parentConnection = paramIConnection;
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { paramIConnection });
    try
    {
      loadProperties();
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.DEFAULT);
    }
    loadPropertyKeyToPropertyLimitKeyMapping();
  }
  
  public String getCursorName()
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[0]);
    return this.m_cursorName;
  }
  
  public Variant getCustomProperty(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt) });
    return null;
  }
  
  public int getCustomPropertyType(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt) });
    return 0;
  }
  
  public ILogger getLog()
  {
    return this.m_parentConnection.getConnectionLog();
  }
  
  public IConnection getParentConnection()
  {
    return this.m_parentConnection;
  }
  
  public AttributeDataMap getAllProperties()
  {
    return this.m_settings;
  }
  
  public Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt) });
    if (null == this.m_settings) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    if (this.m_settings.isProperty(paramInt)) {
      return this.m_settings.getProperty(paramInt);
    }
    throw new BadPropertyKeyException(2, DSIMessageKey.INVALID_PROPKEY.name(), String.valueOf(paramInt));
  }
  
  public Variant getSimilarValue(int paramInt, Variant paramVariant)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
    if (null == this.m_propertyLimitKeyMap) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROP_LIMITMAP.name(), ExceptionType.DEFAULT);
    }
    try
    {
      PropertyLimitKeys localPropertyLimitKeys = (PropertyLimitKeys)this.m_propertyLimitKeyMap.get(Integer.valueOf(paramInt));
      if (null == localPropertyLimitKeys) {
        throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROP_LIMITKEY.name(), ExceptionType.DEFAULT);
      }
      int i = (int)paramVariant.getLong();
      Integer localInteger1 = localPropertyLimitKeys.getMaxLimitkey();
      Variant localVariant1 = this.m_settings.getProperty(localInteger1.intValue());
      long l1 = i;
      if (null != localVariant1) {
        l1 = localVariant1.getLong();
      }
      Integer localInteger2 = localPropertyLimitKeys.getMinLimitkey();
      Variant localVariant2 = this.m_settings.getProperty(localInteger2.intValue());
      long l2 = i;
      if (null != localVariant2) {
        l2 = localVariant2.getLong();
      }
      if ((null != localVariant1) && (null != localVariant2) && (l1 == l2)) {
        return null;
      }
      if ((null != localVariant1) && (l1 < i)) {
        return localVariant1;
      }
      if ((null != localVariant2) && (l2 > i)) {
        return localVariant2;
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.DEFAULT);
    }
    return null;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public boolean isCustomProperty(int paramInt)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt) });
    return false;
  }
  
  public boolean isValueSupported(int paramInt, Variant paramVariant)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
    if (null == this.m_propertyLimitKeyMap) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROP_LIMITMAP.name(), ExceptionType.DEFAULT);
    }
    PropertyLimitKeys localPropertyLimitKeys = (PropertyLimitKeys)this.m_propertyLimitKeyMap.get(Integer.valueOf(paramInt));
    if (null == localPropertyLimitKeys) {
      return true;
    }
    try
    {
      long l1 = paramVariant.getLong();
      int i = 0;
      Integer localInteger1 = localPropertyLimitKeys.getMaxLimitkey();
      Variant localVariant1 = this.m_settings.getProperty(localInteger1.intValue());
      long l2 = l1;
      if (null != localVariant1)
      {
        l2 = localVariant1.getLong();
        i = 0L != l2 ? 1 : 0;
      }
      Integer localInteger2 = localPropertyLimitKeys.getMinLimitkey();
      Variant localVariant2 = this.m_settings.getProperty(localInteger2.intValue());
      long l3 = l1;
      if (null != localVariant2) {
        l3 = localVariant2.getLong();
      }
      if ((i != 0) && (null != localVariant2) && (l2 == l3)) {
        return l2 == l1;
      }
      if ((i != 0) && (l2 < l1)) {
        return false;
      }
      if ((null != localVariant2) && (l3 > l1)) {
        return false;
      }
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), ExceptionType.DEFAULT);
    }
    return true;
  }
  
  public void notifyCursorNameChange(String paramString)
    throws ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { paramString });
    this.m_cursorName = paramString;
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { paramIWarningListener });
    this.m_warningListener = paramIWarningListener;
  }
  
  public void setCustomProperty(int paramInt, Variant paramVariant)
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
  }
  
  public void setProperty(int paramInt, Variant paramVariant)
    throws BadAttrValException, ErrorException
  {
    LogUtilities.logFunctionEntrance(getLog(), new Object[] { Integer.valueOf(paramInt), paramVariant });
    if (null == this.m_settings) {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.INVALID_PROPMAP.name(), ExceptionType.DEFAULT);
    }
    if (isValueSupported(paramInt, paramVariant))
    {
      this.m_settings.setProperty(paramInt, paramVariant);
    }
    else
    {
      Variant localVariant = getSimilarValue(paramInt, paramVariant);
      if (null == localVariant) {
        throw new BadAttrValException(2, DSIMessageKey.BAD_ATTR_VAL.name(), new String[] { String.valueOf(paramInt), paramVariant.getString() });
      }
      this.m_settings.setProperty(paramInt, localVariant);
      if (null != this.m_warningListener) {
        this.m_warningListener.postWarning(new Warning(WarningCode.OPTIONAL_VALUE_CHANGED, 2, DSIMessageKey.OPTIONAL_VAL_CHANGED.name(), new String[] { String.valueOf(paramInt), paramVariant.getString() }));
      }
    }
  }
  
  private void loadProperties()
    throws IncorrectTypeException, NumericOverflowException
  {
    this.m_settings.setProperty(100, 4, Long.valueOf(4294967295L));
    this.m_settings.setProperty(101, 4, Long.valueOf(0L));
    this.m_settings.setProperty(106, 4, Long.valueOf(2147483647L));
    this.m_settings.setProperty(107, 4, Long.valueOf(0L));
    this.m_settings.setProperty(104, 4, Long.valueOf(0L));
    this.m_settings.setProperty(105, 4, Long.valueOf(0L));
    this.m_settings.setProperty(103, 4, Long.valueOf(0L));
    this.m_settings.setProperty(1, 4, Long.valueOf(0L));
    this.m_settings.setProperty(2, 4, Long.valueOf(0L));
    this.m_settings.setProperty(3, 4, Long.valueOf(0L));
    this.m_settings.setProperty(4, 5, Short.valueOf((short)0));
    this.m_settings.setProperty(5, 4, Long.valueOf(1L));
    this.m_settings.setProperty(6, 4, Long.valueOf(0L));
  }
  
  private void loadPropertyKeyToPropertyLimitKeyMapping()
  {
    PropertyLimitKeys localPropertyLimitKeys1 = new PropertyLimitKeys(100, 101);
    this.m_propertyLimitKeyMap.put(Integer.valueOf(1), localPropertyLimitKeys1);
    PropertyLimitKeys localPropertyLimitKeys2 = new PropertyLimitKeys(102, 103);
    this.m_propertyLimitKeyMap.put(Integer.valueOf(2), localPropertyLimitKeys2);
    PropertyLimitKeys localPropertyLimitKeys3 = new PropertyLimitKeys(104, 105);
    this.m_propertyLimitKeyMap.put(Integer.valueOf(3), localPropertyLimitKeys3);
    PropertyLimitKeys localPropertyLimitKeys4 = new PropertyLimitKeys(106, 107);
    this.m_propertyLimitKeyMap.put(Integer.valueOf(6), localPropertyLimitKeys4);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/impl/DSIStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
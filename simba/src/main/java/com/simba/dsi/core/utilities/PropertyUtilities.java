package com.simba.dsi.core.utilities;

import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.dsi.utilities.DSIMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.LogUtilities;
import com.simba.support.Warning;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.Iterator;
import java.util.List;

public class PropertyUtilities
{
  public static boolean hasCatalogSupport(IConnection paramIConnection)
  {
    assert (null != paramIConnection);
    try
    {
      if (0 == paramIConnection.getProperty(9).getString().length()) {
        return false;
      }
      if (0 == paramIConnection.getProperty(11).getString().length()) {
        return false;
      }
      return 0L != paramIConnection.getProperty(12).getLong();
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean hasSavepointSupport(IConnection paramIConnection)
  {
    assert (null != paramIConnection);
    try
    {
      Variant localVariant = paramIConnection.getProperty(1000);
      return 1 == localVariant.getShort();
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean hasSchemaSupport(IConnection paramIConnection)
  {
    assert (null != paramIConnection);
    try
    {
      if (0 == paramIConnection.getProperty(99).getString().length()) {
        return false;
      }
      return 0L != paramIConnection.getProperty(100).getLong();
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean hasStoredFunctionsCallsSupport(IConnection paramIConnection)
  {
    assert (null != paramIConnection);
    try
    {
      Variant localVariant = paramIConnection.getProperty(1003);
      Short localShort = Short.valueOf(localVariant.getShort());
      return 1 == localShort.shortValue();
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean hasStoredProcedureSupport(IConnection paramIConnection)
  {
    assert (null != paramIConnection);
    try
    {
      if (0 == paramIConnection.getProperty(96).getString().length()) {
        return false;
      }
      return "Y".equals(paramIConnection.getProperty(97).getString());
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static boolean isReadOnly(IConnection paramIConnection)
  {
    assert (null != paramIConnection);
    try
    {
      long l = paramIConnection.getProperty(16).getLong();
      if (1L != l) {
        return false;
      }
      return "Y".equals(paramIConnection.getProperty(40).getString());
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static void setCatalogSupport(IConnection paramIConnection, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramIConnection);
    IWarningListener localIWarningListener = paramIConnection.getWarningListener();
    if (null == localIWarningListener) {
      localIWarningListener = paramIConnection.getParentEnvironment().getWarningListener();
    }
    try
    {
      if (paramBoolean)
      {
        paramIConnection.setProperty(9, new Variant(0, "Y"));
        paramIConnection.setProperty(11, new Variant(0, "catalog"));
        paramIConnection.setProperty(12, new Variant(3, Long.valueOf(31L)));
      }
      else
      {
        paramIConnection.setProperty(9, new Variant(0, "N"));
        paramIConnection.setProperty(11, new Variant(0, ""));
        paramIConnection.setProperty(12, new Variant(3, Long.valueOf(0L)));
      }
    }
    catch (BadAttrValException localBadAttrValException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.BAD_ATTR_VAL.name(), localBadAttrValException, ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), localNumericOverflowException, ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), localIncorrectTypeException, ExceptionType.DEFAULT);
    }
    finally
    {
      logWarnings(paramIConnection, localIWarningListener);
    }
  }
  
  public static void setReadOnly(IConnection paramIConnection, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramIConnection);
    IWarningListener localIWarningListener = paramIConnection.getWarningListener();
    if (null == localIWarningListener) {
      localIWarningListener = paramIConnection.getParentEnvironment().getWarningListener();
    }
    try
    {
      if (paramBoolean)
      {
        paramIConnection.setProperty(16, new Variant(3, Long.valueOf(1L)));
        paramIConnection.setProperty(40, new Variant(0, "Y"));
      }
      else
      {
        paramIConnection.setProperty(16, new Variant(3, Long.valueOf(0L)));
        paramIConnection.setProperty(40, new Variant(0, "N"));
      }
    }
    catch (BadAttrValException localBadAttrValException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.BAD_ATTR_VAL.name(), localBadAttrValException, ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), localNumericOverflowException, ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), localIncorrectTypeException, ExceptionType.DEFAULT);
    }
    finally
    {
      logWarnings(paramIConnection, localIWarningListener);
    }
  }
  
  public static void setSavepointSupport(IConnection paramIConnection, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramIConnection);
    IWarningListener localIWarningListener = paramIConnection.getWarningListener();
    if (null == localIWarningListener) {
      localIWarningListener = paramIConnection.getParentEnvironment().getWarningListener();
    }
    try
    {
      if (paramBoolean) {
        paramIConnection.setProperty(1000, new Variant(5, Short.valueOf((short)1)));
      } else {
        paramIConnection.setProperty(1000, new Variant(5, Short.valueOf((short)0)));
      }
    }
    catch (BadAttrValException localBadAttrValException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.BAD_ATTR_VAL.name(), localBadAttrValException, ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), localNumericOverflowException, ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), localIncorrectTypeException, ExceptionType.DEFAULT);
    }
    finally
    {
      logWarnings(paramIConnection, localIWarningListener);
    }
  }
  
  public static void setSchemaSupport(IConnection paramIConnection, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramIConnection);
    IWarningListener localIWarningListener = paramIConnection.getWarningListener();
    if (null == localIWarningListener) {
      localIWarningListener = paramIConnection.getParentEnvironment().getWarningListener();
    }
    try
    {
      if (paramBoolean)
      {
        paramIConnection.setProperty(99, new Variant(0, "schema"));
        paramIConnection.setProperty(100, new Variant(3, Long.valueOf(31L)));
        paramIConnection.setProperty(81, new Variant(2, Character.valueOf('Ā')));
      }
      else
      {
        paramIConnection.setProperty(99, new Variant(0, ""));
        paramIConnection.setProperty(100, new Variant(3, Long.valueOf(0L)));
        paramIConnection.setProperty(81, new Variant(2, Character.valueOf('\000')));
      }
    }
    catch (BadAttrValException localBadAttrValException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.BAD_ATTR_VAL.name(), localBadAttrValException, ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), localNumericOverflowException, ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), localIncorrectTypeException, ExceptionType.DEFAULT);
    }
    finally
    {
      logWarnings(paramIConnection, localIWarningListener);
    }
  }
  
  public static void setStoredFunctionsCallsSupport(IConnection paramIConnection, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramIConnection);
    IWarningListener localIWarningListener = paramIConnection.getWarningListener();
    if (null == localIWarningListener) {
      localIWarningListener = paramIConnection.getParentEnvironment().getWarningListener();
    }
    try
    {
      if (paramBoolean) {
        paramIConnection.setProperty(1003, new Variant(5, Short.valueOf((short)1)));
      } else {
        paramIConnection.setProperty(1003, new Variant(5, Short.valueOf((short)0)));
      }
    }
    catch (BadAttrValException localBadAttrValException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.BAD_ATTR_VAL.name(), localBadAttrValException, ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), localNumericOverflowException, ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), localIncorrectTypeException, ExceptionType.DEFAULT);
    }
    finally
    {
      logWarnings(paramIConnection, localIWarningListener);
    }
  }
  
  public static void setStoredProcedureSupport(IConnection paramIConnection, boolean paramBoolean)
    throws ErrorException
  {
    assert (null != paramIConnection);
    IWarningListener localIWarningListener = paramIConnection.getWarningListener();
    if (null == localIWarningListener) {
      localIWarningListener = paramIConnection.getParentEnvironment().getWarningListener();
    }
    try
    {
      if (paramBoolean)
      {
        paramIConnection.setProperty(96, new Variant(0, "procedure"));
        paramIConnection.setProperty(97, new Variant(0, "Y"));
      }
      else
      {
        paramIConnection.setProperty(96, new Variant(0, ""));
        paramIConnection.setProperty(97, new Variant(0, "N"));
      }
    }
    catch (BadAttrValException localBadAttrValException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.BAD_ATTR_VAL.name(), localBadAttrValException, ExceptionType.DEFAULT);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_NUMERIC_OVERFLOW.name(), localNumericOverflowException, ExceptionType.DEFAULT);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw DSIDriver.s_DSIMessages.createGeneralException(DSIMessageKey.PROP_VALTYPE_MISMATCH.name(), localIncorrectTypeException, ExceptionType.DEFAULT);
    }
    finally
    {
      logWarnings(paramIConnection, localIWarningListener);
    }
  }
  
  private static void logWarnings(IConnection paramIConnection, IWarningListener paramIWarningListener)
  {
    Iterator localIterator = paramIWarningListener.getWarnings().iterator();
    while (localIterator.hasNext())
    {
      Warning localWarning = (Warning)localIterator.next();
      LogUtilities.logWarning(localWarning.getMessage(), paramIConnection.getConnectionLog());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/utilities/PropertyUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
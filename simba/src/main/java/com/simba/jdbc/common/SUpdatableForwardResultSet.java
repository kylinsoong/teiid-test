package com.simba.jdbc.common;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.exceptions.ExceptionConverter;
import com.simba.exceptions.JDBCMessageKey;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.streams.resultsetinput.AbstractDataStream;
import com.simba.streams.resultsetinput.AsciiDataStream;
import com.simba.streams.resultsetinput.BinaryDataStream;
import com.simba.streams.resultsetinput.CharacterDataStream;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionType;
import com.simba.utilities.TypeNames;
import com.simba.utilities.conversion.TypeConverter;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class SUpdatableForwardResultSet
  extends SForwardResultSet
{
  private boolean m_isOnInsertRow = false;
  private boolean m_hasUpdateValues = false;
  protected List<DataWrapper> m_updateValues = new ArrayList();
  protected List<AbstractDataStream> m_updateStreamValues = new ArrayList();
  private long m_updatableSupport;
  
  protected SUpdatableForwardResultSet(SStatement paramSStatement, IResultSet paramIResultSet, ILogger paramILogger)
    throws SQLException
  {
    super(paramSStatement, paramIResultSet, paramILogger);
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramSStatement, paramIResultSet, paramILogger });
      Variant localVariant = paramSStatement.getStatement().getParentConnection().getProperty(1001);
      this.m_updatableSupport = localVariant.getLong();
      for (int i = getMetaData().getColumnCount(); i > 0; i--)
      {
        this.m_updateValues.add(null);
        this.m_updateStreamValues.add(null);
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean absolute(int paramInt)
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
    return super.absolute(paramInt);
  }
  
  public void afterLast()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    super.afterLast();
  }
  
  public void beforeFirst()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    super.beforeFirst();
  }
  
  public void cancelRowUpdates()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    checkIfOpen();
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
  }
  
  public void clearWarnings()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    super.clearWarnings();
  }
  
  public void deleteRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (this.m_isOnInsertRow) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      if (0L == (this.m_updatableSupport & 1L)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULT_DELETE_NOT_SUPPORTED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      this.m_resultSet.deleteRow();
      resetUpdateValues();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean first()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
    return super.first();
  }
  
  public int getConcurrency()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      return 1008;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public int getRow()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    if (this.m_isOnInsertRow) {
      return 0;
    }
    return super.getRow();
  }
  
  public void insertRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (0L == (this.m_updatableSupport & 0x2)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULT_INSERT_NOT_SUPPORTED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      checkForInvalidNulls(JDBCMessageKey.INVALID_NULL_INSERT, true);
      this.m_resultSet.appendRow();
      writeRow();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean isAfterLast()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return super.isAfterLast();
  }
  
  public boolean isBeforeFirst()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return super.isBeforeFirst();
  }
  
  public boolean isFirst()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return super.isFirst();
  }
  
  public boolean isLast()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    return super.isLast();
  }
  
  public boolean last()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
    return super.last();
  }
  
  public void moveToCurrentRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      resetUpdateValues();
      this.m_isOnInsertRow = false;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void moveToInsertRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      resetUpdateValues();
      this.m_isOnInsertRow = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean next()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
    return super.next();
  }
  
  public boolean previous()
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
    return super.previous();
  }
  
  public void refreshRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public boolean relative(int paramInt)
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    resetUpdateValues();
    return super.relative(paramInt);
  }
  
  public boolean rowDeleted()
    throws SQLException
  {
    if (this.m_isOnInsertRow) {
      return false;
    }
    return super.rowDeleted();
  }
  
  public boolean rowInserted()
    throws SQLException
  {
    if (this.m_isOnInsertRow) {
      return false;
    }
    return super.rowInserted();
  }
  
  public boolean rowUpdated()
    throws SQLException
  {
    if (this.m_isOnInsertRow) {
      return false;
    }
    return super.rowUpdated();
  }
  
  public void setFetchDirection(int paramInt)
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    super.setFetchDirection(paramInt);
  }
  
  public void setFetchSize(int paramInt)
    throws SQLException
  {
    if (this.m_isOnInsertRow)
    {
      SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_ACTION_ON_INSERT_ROW, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      LogUtilities.logError(localSQLException, this.m_logger);
      throw localSQLException;
    }
    super.setFetchSize(paramInt);
  }
  
  public void updateArray(int paramInt, Array paramArray)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramArray });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    updateAsciiStreamImpl(paramInt1, paramInputStream, paramInt2);
  }
  
  public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramBigDecimal });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setNumeric(paramBigDecimal);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException
  {
    updateBinaryStreamImpl(paramInt1, paramInputStream, paramInt2);
  }
  
  public void updateBlob(int paramInt, Blob paramBlob)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramBlob });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBoolean(int paramInt, boolean paramBoolean)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setBoolean(paramBoolean);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateByte(int paramInt, byte paramByte)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setTinyInt((short)paramByte);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateBytes(int paramInt, byte[] paramArrayOfByte)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramArrayOfByte });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setVarBinary(paramArrayOfByte);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException
  {
    updateCharacterStreamImpl(paramInt1, paramReader, paramInt2);
  }
  
  public void updateClob(int paramInt, Clob paramClob)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramClob });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateDate(int paramInt, Date paramDate)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramDate });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setDate(paramDate);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateDouble(int paramInt, double paramDouble)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Double.valueOf(paramDouble) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setDouble(paramDouble);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateFloat(int paramInt, float paramFloat)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Float.valueOf(paramFloat) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setReal(paramFloat);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateInt(int paramInt1, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setInteger(paramInt2);
      setValue(paramInt1, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateLong(int paramInt, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Long.valueOf(paramLong) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setBigInt(paramLong);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateNull(int paramInt)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      checkIfValidRowNumber();
      clearStream(paramInt);
      TypeMetadata localTypeMetadata = ((IColumn)this.m_resultSet.getSelectColumns().get(paramInt - 1)).getTypeMetadata();
      try
      {
        DataWrapper localDataWrapper = new DataWrapper();
        localDataWrapper.setNull(localTypeMetadata.getType());
        this.m_updateValues.set(paramInt - 1, TypeConverter.toType(localDataWrapper, localTypeMetadata, this.m_warningListener));
        this.m_hasUpdateValues = true;
      }
      catch (IncorrectTypeException localIncorrectTypeException)
      {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), TypeNames.getTypeName(localTypeMetadata.getType()) });
      }
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateObject(int paramInt, Object paramObject)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramObject });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      clearStream(paramInt);
      TypeMetadata localTypeMetadata = ((IColumn)this.m_resultSet.getSelectColumns().get(paramInt - 1)).getTypeMetadata();
      DataWrapper localDataWrapper1 = new DataWrapper();
      if (null == paramObject) {
        localDataWrapper1.setNull(localTypeMetadata.getType());
      } else {
        try
        {
          DataWrapper localDataWrapper2 = new DataWrapper();
          localDataWrapper2.setData(TypeConverter.getSqlType(paramObject), paramObject);
          localDataWrapper1 = TypeConverter.toType(localDataWrapper2, localTypeMetadata, this.m_warningListener);
        }
        catch (IncorrectTypeException localIncorrectTypeException)
        {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARAM_OBJECT_MISMATCH, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramObject) });
        }
      }
      this.m_updateValues.set(paramInt - 1, localDataWrapper1);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateObject(int paramInt1, Object paramObject, int paramInt2)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt1), paramObject, Integer.valueOf(paramInt2) });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt1);
      clearStream(paramInt1);
      TypeMetadata localTypeMetadata = ((IColumn)this.m_resultSet.getSelectColumns().get(paramInt1 - 1)).getTypeMetadata();
      DataWrapper localDataWrapper1 = new DataWrapper();
      if (null == paramObject) {
        localDataWrapper1.setNull(localTypeMetadata.getType());
      } else {
        try
        {
          DataWrapper localDataWrapper2 = new DataWrapper();
          localDataWrapper2.setData(TypeConverter.getSqlType(paramObject), paramObject);
          localDataWrapper1 = TypeConverter.toType(localDataWrapper2, localTypeMetadata, this.m_warningListener);
        }
        catch (IncorrectTypeException localIncorrectTypeException)
        {
          throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.PARAM_OBJECT_MISMATCH, this.m_warningListener, ExceptionType.NON_TRANSIENT, new Object[] { String.valueOf(paramObject) });
        }
      }
      if ((2 == localDataWrapper1.getType()) || (3 == localDataWrapper1.getType()))
      {
        BigDecimal localBigDecimal = (BigDecimal)localDataWrapper1.getObject();
        localDataWrapper1.setData(localDataWrapper1.getType(), localBigDecimal.setScale(paramInt2, 4));
      }
      this.m_updateValues.set(paramInt1 - 1, localDataWrapper1);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateRef(int paramInt, Ref paramRef)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramRef });
      checkIfOpen();
      checkIfValidColumnNumber(paramInt);
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, this.m_warningListener, ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateRow()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
      checkIfOpen();
      if (0L == (this.m_updatableSupport & 0x4)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.RESULT_UPDATE_NOT_SUPPORTED, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
      }
      checkForInvalidNulls(JDBCMessageKey.INVALID_NULL_UPDATE, false);
      this.m_resultSet.onStartRowUpdate();
      writeRow();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateShort(int paramInt, short paramShort)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), Short.valueOf(paramShort) });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setSmallInt(paramShort);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateString(int paramInt, String paramString)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramString });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setVarChar(paramString);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateTime(int paramInt, Time paramTime)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTime });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setTime(paramTime);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramTimestamp });
      DataWrapper localDataWrapper = new DataWrapper();
      localDataWrapper.setTimestamp(paramTimestamp);
      setValue(paramInt, localDataWrapper);
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  protected DataWrapper getData(int paramInt, long paramLong)
    throws SQLException
  {
    if (!this.m_isOnInsertRow) {
      return super.getData(paramInt, paramLong);
    }
    checkIfValidColumnNumber(paramInt);
    if (0 == this.m_currentRow) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.CURSOR_BEFORE_FIRST_ROW, this.m_warningListener, ExceptionType.DATA, new Object[0]);
    }
    DataWrapper localDataWrapper = (DataWrapper)this.m_updateValues.get(paramInt - 1);
    if (null != localDataWrapper) {
      return localDataWrapper;
    }
    if (null != this.m_updateStreamValues.get(paramInt - 1)) {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_GET_UPDATE_STREAM, this.m_warningListener, ExceptionType.DEFAULT, new Object[0]);
    }
    try
    {
      localDataWrapper = new DataWrapper();
      this.m_resultSet.getData(paramInt - 1, 0L, paramLong, localDataWrapper);
      return localDataWrapper;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener);
    }
  }
  
  private void checkForInvalidNulls(JDBCMessageKey paramJDBCMessageKey, boolean paramBoolean)
    throws SQLException
  {
    ResultSetMetaData localResultSetMetaData = getMetaData();
    for (int i = 0; i < this.m_updateValues.size(); i++)
    {
      DataWrapper localDataWrapper = (DataWrapper)this.m_updateValues.get(i);
      if ((null == localDataWrapper) || (localDataWrapper.isNull()))
      {
        AbstractDataStream localAbstractDataStream = (AbstractDataStream)this.m_updateStreamValues.get(i);
        if ((null == localAbstractDataStream) && (0 == localResultSetMetaData.isNullable(i + 1))) {
          throw ExceptionConverter.getInstance().toSQLException(paramJDBCMessageKey, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(i + 1) });
        }
        if ((paramBoolean) && (null == localDataWrapper) && (null == localAbstractDataStream)) {
          this.m_updateValues.set(i, new DataWrapper());
        }
      }
    }
  }
  
  private void clearStream(int paramInt)
  {
    AbstractDataStream localAbstractDataStream = (AbstractDataStream)this.m_updateStreamValues.get(paramInt - 1);
    if (null != localAbstractDataStream) {
      localAbstractDataStream.close();
    }
    this.m_updateStreamValues.set(paramInt - 1, null);
  }
  
  private void resetUpdateValues()
  {
    if (this.m_hasUpdateValues)
    {
      for (int i = 0; i < this.m_updateValues.size(); i++)
      {
        this.m_updateValues.set(i, null);
        clearStream(i + 1);
      }
      this.m_hasUpdateValues = false;
    }
  }
  
  private void setStream(int paramInt, AbstractDataStream paramAbstractDataStream)
    throws SQLException, ErrorException
  {
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    checkIfValidRowNumber();
    clearStream(paramInt);
    this.m_updateStreamValues.set(paramInt - 1, paramAbstractDataStream);
  }
  
  private void setValue(int paramInt, DataWrapper paramDataWrapper)
    throws SQLException, ErrorException
  {
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    if (!this.m_isOnInsertRow) {
      checkIfValidRowNumber();
    }
    TypeMetadata localTypeMetadata = ((IColumn)this.m_resultSet.getSelectColumns().get(paramInt - 1)).getTypeMetadata();
    clearStream(paramInt);
    try
    {
      this.m_updateValues.set(paramInt - 1, TypeConverter.toType(paramDataWrapper, localTypeMetadata, this.m_warningListener));
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt), TypeNames.getTypeName(localTypeMetadata.getType()) });
    }
  }
  
  protected void updateAsciiStreamImpl(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
      checkIfOpen();
      int i = this.m_resultMetaData.getColumnType(paramInt);
      if (!TypeConverter.canConvertStreamTo(-1, i)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_SET_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
      }
      if (null == paramInputStream) {
        updateNull(paramInt);
      } else {
        setStream(paramInt, new AsciiDataStream(paramInputStream, paramLong, i, this.m_warningListener));
      }
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  protected void updateBinaryStreamImpl(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
      checkIfOpen();
      int i = this.m_resultMetaData.getColumnType(paramInt);
      if (!TypeConverter.canConvertStreamTo(-4, i)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_SET_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
      }
      if (null == paramInputStream) {
        updateNull(paramInt);
      } else {
        setStream(paramInt, new BinaryDataStream(paramInputStream, paramLong, i, this.m_warningListener));
      }
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  protected void updateCharacterStreamImpl(int paramInt, Reader paramReader, long paramLong)
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
      checkIfOpen();
      int i = this.m_resultMetaData.getColumnType(paramInt);
      if (!TypeConverter.canConvertStreamTo(-1, i)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.INVALID_COLUMN_SET_TYPE, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt) });
      }
      if (null == paramReader) {
        updateNull(paramInt);
      } else {
        setStream(paramInt, new CharacterDataStream(paramReader, paramLong, i, this.m_warningListener));
      }
      this.m_hasUpdateValues = true;
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, this.m_warningListener, this.m_logger);
    }
  }
  
  private void writeRow()
    throws SQLException
  {
    try
    {
      for (int i = 0; i < this.m_updateValues.size(); i++) {
        if (null != this.m_updateValues.get(i))
        {
          if (this.m_resultSet.writeData(i, (DataWrapper)this.m_updateValues.get(i), 0L, false)) {
            throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DATA_TRUNCATED_ERR, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(i) });
          }
        }
        else if (null != this.m_updateStreamValues.get(i)) {
          writeStream(i);
        }
      }
      this.m_resultSet.onFinishRowUpdate();
    }
    catch (ErrorException localErrorException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localErrorException, this.m_warningListener);
    }
    resetUpdateValues();
  }
  
  private void writeStream(int paramInt)
    throws ErrorException, SQLException
  {
    AbstractDataStream localAbstractDataStream = (AbstractDataStream)this.m_updateStreamValues.get(paramInt);
    Pair localPair;
    for (long l = 0L; localAbstractDataStream.hasMoreData(); l += ((Long)localPair.value()).longValue())
    {
      localPair = localAbstractDataStream.getNextValue();
      if (this.m_resultSet.writeData(paramInt, (DataWrapper)localPair.key(), l, false)) {
        throw ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DATA_TRUNCATED_ERR, this.m_warningListener, ExceptionType.DATA, new Object[] { String.valueOf(paramInt + 1) });
      }
    }
  }
  
  public int getHoldability()
    throws SQLException
  {
    try
    {
      LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
      checkIfOpen();
      return this.m_parentStatement.getResultSetHoldability();
    }
    catch (Exception localException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localException, getWarningListener(), getLogger());
    }
  }
  
  public Reader getNCharacterStream(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public Reader getNCharacterStream(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getNCharacterStream(findColumn(paramString));
  }
  
  public NClob getNClob(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public NClob getNClob(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getNClob(findColumn(paramString));
  }
  
  public String getNString(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public String getNString(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getNString(findColumn(paramString));
  }
  
  public RowId getRowId(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public RowId getRowId(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getRowId(findColumn(paramString));
  }
  
  public SQLXML getSQLXML(int paramInt)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt) });
    checkIfOpen();
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public SQLXML getSQLXML(String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString });
    return getSQLXML(findColumn(paramString));
  }
  
  public boolean isClosed()
    throws SQLException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[0]);
    return (!this.m_isOpen) || (null == this.m_parentStatement);
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
  
  public void updateAsciiStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateAsciiStream(paramInt, paramInputStream, -1);
  }
  
  public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateAsciiStreamImpl(paramInt, paramInputStream, paramLong);
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream });
    updateAsciiStream(findColumn(paramString), paramInputStream);
  }
  
  public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramString, paramInputStream, Long.valueOf(paramLong) });
    updateAsciiStream(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateBinaryStream(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateBinaryStream(paramInt, paramInputStream, -1);
  }
  
  public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateBinaryStreamImpl(paramInt, paramInputStream, paramLong);
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateBinaryStream(findColumn(paramString), paramInputStream, -1);
  }
  
  public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateBinaryStreamImpl(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateBlob(int paramInt, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramInputStream, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateBlob(String paramString, InputStream paramInputStream)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramInputStream });
    updateBlob(findColumn(paramString), paramInputStream);
  }
  
  public void updateBlob(String paramString, InputStream paramInputStream, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramInputStream, Long.valueOf(paramLong) });
    updateBlob(findColumn(paramString), paramInputStream, paramLong);
  }
  
  public void updateCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateCharacterStream(paramInt, paramReader, -1);
  }
  
  public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateCharacterStreamImpl(paramInt, paramReader, paramLong);
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateCharacterStream(findColumn(paramString), paramReader, -1);
  }
  
  public void updateCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    updateCharacterStreamImpl(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateClob(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader });
    updateClob(findColumn(paramString), paramReader);
  }
  
  public void updateClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateClob(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNCharacterStream(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader });
    updateNCharacterStream(findColumn(paramString), paramReader);
  }
  
  public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateNCharacterStream(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNClob(int paramInt, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramNClob });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNClob(int paramInt, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNClob(int paramInt, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramReader, Long.valueOf(paramLong) });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNClob(String paramString, NClob paramNClob)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramNClob });
    updateNClob(findColumn(paramString), paramNClob);
  }
  
  public void updateNClob(String paramString, Reader paramReader)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader });
    updateNClob(findColumn(paramString), paramReader);
  }
  
  public void updateNClob(String paramString, Reader paramReader, long paramLong)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramReader, Long.valueOf(paramLong) });
    updateNClob(findColumn(paramString), paramReader, paramLong);
  }
  
  public void updateNString(int paramInt, String paramString)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramString });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateNString(String paramString1, String paramString2)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString1, paramString2 });
    updateNString(findColumn(paramString1), paramString2);
  }
  
  public void updateRowId(int paramInt, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramRowId });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateRowId(String paramString, RowId paramRowId)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramRowId });
    updateRowId(findColumn(paramString), paramRowId);
  }
  
  public void updateSQLXML(int paramInt, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { Integer.valueOf(paramInt), paramSQLXML });
    checkIfOpen();
    checkIfValidColumnNumber(paramInt);
    SQLException localSQLException = ExceptionConverter.getInstance().toSQLException(JDBCMessageKey.DRIVER_NOT_CAPABLE, getWarningListener(), ExceptionType.FEATURE_NOT_IMPLEMENTED, new Object[0]);
    LogUtilities.logError(localSQLException, getLogger());
    throw localSQLException;
  }
  
  public void updateSQLXML(String paramString, SQLXML paramSQLXML)
    throws SQLException, SQLFeatureNotSupportedException
  {
    LogUtilities.logFunctionEntrance(getLogger(), new Object[] { paramString, paramSQLXML });
    updateSQLXML(findColumn(paramString), paramSQLXML);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/SUpdatableForwardResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
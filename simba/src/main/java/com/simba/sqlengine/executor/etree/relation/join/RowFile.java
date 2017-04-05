package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.temptable.IRowBlock;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.executor.etree.temptable.column.BitsUtil;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceArray;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceBuilder;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class RowFile
  implements IRowBlock
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree.relation.join";
  private static final String CLASS_NAME = "RowFile";
  private File m_tempFile;
  private FileOutputStream m_fileOut;
  private ResizeableByteArrayOS m_bos;
  private ObjectOutputStream m_objectOut;
  private ObjectInputStream m_objectIn;
  private ILogger m_logger;
  private final IColumn[] m_metadata;
  private int m_longDataBorder;
  private long m_rowCount;
  private long m_currentRow;
  private ColumnSliceArray m_row;
  private boolean m_isWriting;
  private boolean[] m_dataNeeded;
  
  public RowFile(IColumn[] paramArrayOfIColumn, File paramFile, ILogger paramILogger, int paramInt, boolean[] paramArrayOfBoolean)
    throws ErrorException
  {
    this.m_logger = paramILogger;
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    this.m_metadata = ((IColumn[])paramArrayOfIColumn.clone());
    this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
    this.m_longDataBorder = paramInt;
    this.m_rowCount = 0L;
    this.m_currentRow = -1L;
    this.m_row = ColumnSliceBuilder.buildColumnSliceArray(paramArrayOfIColumn, paramArrayOfBoolean, 1, paramInt);
    this.m_isWriting = true;
    this.m_tempFile = ExternalAlgorithmUtil.createTempFile(paramFile, this.m_logger);
  }
  
  public void close()
  {
    closeStreams();
    if (null != this.m_tempFile)
    {
      if ((this.m_tempFile.exists()) && (!this.m_tempFile.delete()) && (null != this.m_logger)) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "close", "Cannot delete temprary file: " + this.m_tempFile.getAbsolutePath());
      }
      this.m_tempFile = null;
    }
  }
  
  public IColumn[] getMetadata()
  {
    return (IColumn[])this.m_metadata.clone();
  }
  
  public boolean isNull(int paramInt)
  {
    return this.m_row.get(paramInt).isNull(0);
  }
  
  public long getRowCount()
  {
    if (this.m_isWriting) {
      return -1L;
    }
    return this.m_rowCount;
  }
  
  public long getBigInt(int paramInt)
  {
    return this.m_row.get(paramInt).getBigInt(0);
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    return this.m_row.get(paramInt).getExactNum(0);
  }
  
  public double getDouble(int paramInt)
  {
    return this.m_row.get(paramInt).getDouble(0);
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    return this.m_row.get(paramInt).getFileMarker(0);
  }
  
  public float getReal(int paramInt)
  {
    return this.m_row.get(paramInt).getReal(0);
  }
  
  public boolean getBoolean(int paramInt)
  {
    return this.m_row.get(paramInt).getBoolean(0);
  }
  
  public String getString(int paramInt)
  {
    return this.m_row.get(paramInt).getString(0);
  }
  
  public Date getDate(int paramInt)
  {
    return this.m_row.get(paramInt).getDate(0);
  }
  
  public Time getTime(int paramInt)
  {
    return this.m_row.get(paramInt).getTime(0);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    return this.m_row.get(paramInt).getTimestamp(0);
  }
  
  public UUID getGuid(int paramInt)
  {
    return this.m_row.get(paramInt).getGuid(0);
  }
  
  public int getInteger(int paramInt)
  {
    return this.m_row.get(paramInt).getInteger(0);
  }
  
  public short getSmallInt(int paramInt)
  {
    return this.m_row.get(paramInt).getSmallInt(0);
  }
  
  public byte getTinyInt(int paramInt)
  {
    return this.m_row.get(paramInt).getTinyInt(0);
  }
  
  public byte[] getBytes(int paramInt)
  {
    return this.m_row.get(paramInt).getBytes(0);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_metadata[paramInt];
  }
  
  public boolean moveToNextRow()
    throws ErrorException
  {
    if (this.m_isWriting) {
      this.m_isWriting = false;
    }
    if (null == this.m_objectIn) {
      prepareToRead();
    }
    if (this.m_currentRow + 1L >= this.m_rowCount) {
      return false;
    }
    this.m_currentRow += 1L;
    try
    {
      readRow();
      return true;
    }
    catch (Exception localException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "moveToNextRow", localException.getMessage());
      }
      throw SQLEngineExceptionFactory.failedToReadData(localException);
    }
  }
  
  public void reset()
  {
    this.m_isWriting = false;
    this.m_currentRow = -1L;
    closeStreams();
  }
  
  public void writeRows(InMemTable paramInMemTable, List<Integer> paramList, int paramInt)
    throws ErrorException
  {
    assert (this.m_isWriting);
    resetOutStreams(paramInt);
    try
    {
      long l = 0L;
      int i = 0;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        int j = ((Integer)localIterator.next()).intValue();
        byte[] arrayOfByte = new byte[(this.m_metadata.length + 7) / 8];
        for (int k = 0; k < this.m_metadata.length; k++) {
          if ((this.m_dataNeeded[k] != 0) && (paramInMemTable.isNull(j, k))) {
            BitsUtil.setBit(arrayOfByte, k);
          }
        }
        this.m_objectOut.writeObject(arrayOfByte);
        for (k = 0; k < this.m_metadata.length; k++) {
          if ((this.m_dataNeeded[k] != 0) && (!BitsUtil.isSet(arrayOfByte, k))) {
            writeColumn(paramInMemTable, this.m_objectOut, j, k);
          }
        }
        k = this.m_bos.size() - i;
        if (k > l) {
          l = k;
        }
        if (this.m_bos.size() + l >= paramInt)
        {
          this.m_objectOut.reset();
          this.m_objectOut.flush();
          this.m_bos.writeToOutputStream(this.m_fileOut);
          this.m_bos.reset();
        }
        i = this.m_bos.size();
      }
      this.m_objectOut.reset();
      this.m_objectOut.flush();
      this.m_bos.writeToOutputStream(this.m_fileOut);
      this.m_rowCount += paramList.size();
    }
    catch (IOException localIOException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "writeRow", "Could not create input stream.");
      }
      throw SQLEngineExceptionFactory.failedToWriteData(localIOException);
    }
    finally
    {
      this.m_bos.destroyBuffer(0);
    }
  }
  
  private void resetOutStreams(int paramInt)
    throws ErrorException
  {
    if (null == this.m_objectOut) {
      try
      {
        this.m_fileOut = new FileOutputStream(this.m_tempFile);
        this.m_bos = new ResizeableByteArrayOS(paramInt);
        this.m_objectOut = new ObjectOutputStream(this.m_bos);
      }
      catch (Exception localException)
      {
        if (null != this.m_logger) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "RowFile", "Could not create temporary file.");
        }
        throw SQLEngineExceptionFactory.failedToCreateFile(this.m_tempFile.getAbsolutePath(), localException.getLocalizedMessage());
      }
    } else {
      this.m_bos.destroyBuffer(paramInt);
    }
  }
  
  private void prepareToRead()
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    this.m_currentRow = -1L;
    closeStreams();
    try
    {
      this.m_objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.m_tempFile)));
    }
    catch (Exception localException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "prepareToRead", "Could not create input stream.");
      }
      throw SQLEngineExceptionFactory.failedToReadData(localException);
    }
  }
  
  private void writeColumn(InMemTable paramInMemTable, ObjectOutputStream paramObjectOutputStream, int paramInt1, int paramInt2)
    throws IOException
  {
    TemporaryFile.FileMarker localFileMarker;
    switch (this.m_metadata[paramInt2].getTypeMetadata().getType())
    {
    case -5: 
      paramObjectOutputStream.writeLong(paramInMemTable.getBigInt(paramInt1, paramInt2));
      break;
    case -4: 
    case -3: 
    case -2: 
      if (ColumnSizeCalculator.isLongData(this.m_metadata[paramInt2], this.m_longDataBorder))
      {
        localFileMarker = paramInMemTable.getFileMarker(paramInt1, paramInt2);
        paramObjectOutputStream.writeLong(localFileMarker.m_pos);
        paramObjectOutputStream.writeLong(localFileMarker.m_length);
      }
      else
      {
        paramObjectOutputStream.writeObject(paramInMemTable.getBytes(paramInt1, paramInt2));
      }
      break;
    case -7: 
    case 16: 
      paramObjectOutputStream.writeBoolean(paramInMemTable.getBoolean(paramInt1, paramInt2));
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      if (ColumnSizeCalculator.isLongData(this.m_metadata[paramInt2], this.m_longDataBorder))
      {
        localFileMarker = paramInMemTable.getFileMarker(paramInt1, paramInt2);
        paramObjectOutputStream.writeLong(localFileMarker.m_pos);
        paramObjectOutputStream.writeLong(localFileMarker.m_length);
      }
      else
      {
        paramObjectOutputStream.writeObject(paramInMemTable.getString(paramInt1, paramInt2));
      }
      break;
    case 91: 
      paramObjectOutputStream.writeObject(paramInMemTable.getDate(paramInt1, paramInt2));
      break;
    case 6: 
    case 8: 
      paramObjectOutputStream.writeDouble(paramInMemTable.getDouble(paramInt1, paramInt2));
      break;
    case 2: 
    case 3: 
      paramObjectOutputStream.writeObject(paramInMemTable.getExactNum(paramInt1, paramInt2));
      break;
    case -11: 
      paramObjectOutputStream.writeObject(paramInMemTable.getGuid(paramInt1, paramInt2));
      break;
    case 4: 
      paramObjectOutputStream.writeInt(paramInMemTable.getInteger(paramInt1, paramInt2));
      break;
    case 7: 
      paramObjectOutputStream.writeFloat(paramInMemTable.getReal(paramInt1, paramInt2));
      break;
    case 5: 
      paramObjectOutputStream.writeShort(paramInMemTable.getSmallInt(paramInt1, paramInt2));
      break;
    case 92: 
      paramObjectOutputStream.writeObject(paramInMemTable.getTime(paramInt1, paramInt2));
      break;
    case 93: 
      paramObjectOutputStream.writeObject(paramInMemTable.getTimestamp(paramInt1, paramInt2));
      break;
    case -6: 
      paramObjectOutputStream.writeByte(paramInMemTable.getTinyInt(paramInt1, paramInt2));
      break;
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    default: 
      throw new IllegalStateException(String.format("Unknown data type: %d.", new Object[] { Short.valueOf(this.m_metadata[paramInt2].getTypeMetadata().getType()) }));
    }
  }
  
  private void closeStreams()
  {
    try
    {
      if (null != this.m_objectIn) {
        this.m_objectIn.close();
      }
    }
    catch (IOException localIOException1)
    {
      this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "closeStreams", "Cannot close ObjectInputStream");
    }
    try
    {
      if (null != this.m_fileOut) {
        this.m_fileOut.close();
      }
    }
    catch (IOException localIOException2)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "RowFile", "closeStreams", "Cannot close ObjectOutputStream");
      }
    }
    finally
    {
      this.m_objectIn = null;
      this.m_fileOut = null;
      this.m_bos = null;
      this.m_objectOut = null;
    }
  }
  
  private void readRow()
    throws IOException, ClassNotFoundException
  {
    byte[] arrayOfByte = (byte[])this.m_objectIn.readObject();
    for (int i = 0; i < this.m_metadata.length; i++) {
      if (this.m_dataNeeded[i] != 0) {
        if (BitsUtil.isSet(arrayOfByte, i))
        {
          this.m_row.get(i).setNull(0);
        }
        else
        {
          long l1;
          long l2;
          switch (this.m_metadata[i].getTypeMetadata().getType())
          {
          case -7: 
          case 16: 
            this.m_row.get(i).setBoolean(0, this.m_objectIn.readBoolean());
            break;
          case -5: 
            this.m_row.get(i).setBigInt(0, this.m_objectIn.readLong());
            break;
          case 6: 
          case 8: 
            this.m_row.get(i).setDouble(0, this.m_objectIn.readDouble());
            break;
          case 4: 
            this.m_row.get(i).setInteger(0, this.m_objectIn.readInt());
            break;
          case 7: 
            this.m_row.get(i).setReal(0, this.m_objectIn.readFloat());
            break;
          case 5: 
            this.m_row.get(i).setSmallInt(0, this.m_objectIn.readShort());
            break;
          case -6: 
            this.m_row.get(i).setTinyInt(0, this.m_objectIn.readByte());
            break;
          case -10: 
          case -9: 
          case -8: 
          case -1: 
          case 1: 
          case 12: 
            if (ColumnSizeCalculator.isLongData(this.m_metadata[i], this.m_longDataBorder))
            {
              l1 = this.m_objectIn.readLong();
              l2 = this.m_objectIn.readLong();
              this.m_row.get(i).setFileMarker(0, new TemporaryFile.FileMarker(l1, l2));
            }
            else
            {
              this.m_row.get(i).setString(0, (String)this.m_objectIn.readObject());
            }
            break;
          case -4: 
          case -3: 
          case -2: 
            if (ColumnSizeCalculator.isLongData(this.m_metadata[i], this.m_longDataBorder))
            {
              l1 = this.m_objectIn.readLong();
              l2 = this.m_objectIn.readLong();
              this.m_row.get(i).setFileMarker(0, new TemporaryFile.FileMarker(l1, l2));
            }
            else
            {
              this.m_row.get(i).setBytes(0, (byte[])this.m_objectIn.readObject());
            }
            break;
          case 2: 
          case 3: 
            this.m_row.get(i).setExactNum(0, (BigDecimal)this.m_objectIn.readObject());
            break;
          case -11: 
            this.m_row.get(i).setGuid(0, (UUID)this.m_objectIn.readObject());
            break;
          case 91: 
            this.m_row.get(i).setDate(0, (Date)this.m_objectIn.readObject());
            break;
          case 92: 
            this.m_row.get(i).setTime(0, (Time)this.m_objectIn.readObject());
            break;
          case 93: 
            this.m_row.get(i).setTimestamp(0, (Timestamp)this.m_objectIn.readObject());
            break;
          case 0: 
          case 9: 
          case 10: 
          case 11: 
          case 13: 
          case 14: 
          case 15: 
          case 17: 
          case 18: 
          case 19: 
          case 20: 
          case 21: 
          case 22: 
          case 23: 
          case 24: 
          case 25: 
          case 26: 
          case 27: 
          case 28: 
          case 29: 
          case 30: 
          case 31: 
          case 32: 
          case 33: 
          case 34: 
          case 35: 
          case 36: 
          case 37: 
          case 38: 
          case 39: 
          case 40: 
          case 41: 
          case 42: 
          case 43: 
          case 44: 
          case 45: 
          case 46: 
          case 47: 
          case 48: 
          case 49: 
          case 50: 
          case 51: 
          case 52: 
          case 53: 
          case 54: 
          case 55: 
          case 56: 
          case 57: 
          case 58: 
          case 59: 
          case 60: 
          case 61: 
          case 62: 
          case 63: 
          case 64: 
          case 65: 
          case 66: 
          case 67: 
          case 68: 
          case 69: 
          case 70: 
          case 71: 
          case 72: 
          case 73: 
          case 74: 
          case 75: 
          case 76: 
          case 77: 
          case 78: 
          case 79: 
          case 80: 
          case 81: 
          case 82: 
          case 83: 
          case 84: 
          case 85: 
          case 86: 
          case 87: 
          case 88: 
          case 89: 
          case 90: 
          default: 
            throw new IllegalStateException(String.format("Unknown data type: %d.", new Object[] { Short.valueOf(this.m_metadata[i].getTypeMetadata().getType()) }));
          }
        }
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/RowFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
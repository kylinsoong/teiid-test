package com.simba.sqlengine.executor.etree.temptable;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TemporaryFile
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree.temptable";
  private static final String CLASS_NAME = "TemporaryFile";
  private ILogger m_logger;
  private RandomAccessFile m_file;
  private File m_fileUnderneath;
  private long m_currentPosition;
  private boolean m_appending = false;
  private long m_appendStart = 0L;
  
  public TemporaryFile(File paramFile, ILogger paramILogger)
    throws ErrorException
  {
    this.m_logger = paramILogger;
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramFile });
    }
    this.m_fileUnderneath = ExternalAlgorithmUtil.createTempFile(paramFile, this.m_logger);
    try
    {
      this.m_file = new RandomAccessFile(this.m_fileUnderneath, "rw");
      this.m_currentPosition = this.m_file.getFilePointer();
    }
    catch (Exception localException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.temptable", "TemporaryFile", "TemporaryFile", "Could not create temporary file.");
      }
      throw SQLEngineExceptionFactory.failedToCreateFile(this.m_fileUnderneath.getAbsolutePath(), localException.getLocalizedMessage());
    }
  }
  
  public void append(byte[] paramArrayOfByte)
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    if (!this.m_appending)
    {
      this.m_appending = true;
      this.m_appendStart = this.m_currentPosition;
    }
    doPut(paramArrayOfByte);
  }
  
  public FileMarker generateFileMarker()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    if (!this.m_appending) {
      throw new IllegalStateException("Attempt to generate a file marker before calling append");
    }
    this.m_appending = false;
    long l = this.m_currentPosition - this.m_appendStart;
    return new FileMarker(this.m_appendStart, l);
  }
  
  public boolean isAppending()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    return this.m_appending;
  }
  
  public FileMarker put(byte[] paramArrayOfByte)
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    if (this.m_appending) {
      throw new IllegalStateException("Attempt to write data during an append operation.");
    }
    return doPut(paramArrayOfByte);
  }
  
  public byte[] get(FileMarker paramFileMarker)
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { paramFileMarker });
    }
    if (paramFileMarker.m_length > 2147483647L) {
      throw new IllegalStateException("Requesting data that is too big to load into memory");
    }
    int i = (int)paramFileMarker.m_length;
    byte[] arrayOfByte = new byte[i];
    try
    {
      this.m_file.seek(paramFileMarker.m_pos);
      int j = 0;
      int k = 0;
      while ((j < i) && ((k = this.m_file.read(arrayOfByte, j, i - j)) != -1)) {
        j += k;
      }
      if (k < i)
      {
        if (null != this.m_logger) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree.temptable", "TemporaryFile", "get", "Failed to read data from file.");
        }
        throw SQLEngineExceptionFactory.failedToReadData();
      }
    }
    catch (IOException localIOException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.temptable", "TemporaryFile", "destroy", localIOException.getLocalizedMessage());
      }
      throw SQLEngineExceptionFactory.failedToReadData(localIOException);
    }
    return arrayOfByte;
  }
  
  public void destroy()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    if (null != this.m_file) {
      try
      {
        this.m_file.close();
        this.m_file = null;
      }
      catch (IOException localIOException)
      {
        if (null != this.m_logger) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree.temptable", "TemporaryFile", "destroy", localIOException.getLocalizedMessage());
        }
      }
    }
    if (null != this.m_fileUnderneath)
    {
      if ((this.m_fileUnderneath.exists()) && (!this.m_fileUnderneath.delete()) && (null != this.m_logger)) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.temptable", "TemporaryFile", "destroy", "Cannot delete temprary file: " + this.m_fileUnderneath.getAbsolutePath());
      }
      this.m_fileUnderneath = null;
    }
  }
  
  private FileMarker doPut(byte[] paramArrayOfByte)
    throws ErrorException
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    try
    {
      this.m_file.seek(this.m_currentPosition);
      FileMarker localFileMarker = new FileMarker(this.m_currentPosition, paramArrayOfByte.length);
      this.m_file.write(paramArrayOfByte);
      this.m_currentPosition = this.m_file.getFilePointer();
      return localFileMarker;
    }
    catch (IOException localIOException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.temptable", "TemporaryFile", "put", localIOException.getLocalizedMessage());
      }
      throw SQLEngineExceptionFactory.failedToWriteData(localIOException);
    }
  }
  
  public static class FileMarker
  {
    public final long m_pos;
    public final long m_length;
    
    public FileMarker(long paramLong1, long paramLong2)
    {
      this.m_pos = paramLong1;
      this.m_length = paramLong2;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof FileMarker)) {
        return false;
      }
      FileMarker localFileMarker = (FileMarker)paramObject;
      return (this.m_pos == localFileMarker.m_pos) && (this.m_length == localFileMarker.m_length);
    }
    
    public int hashCode()
    {
      int i = 1;
      i = 31 * i + (int)(this.m_pos ^ this.m_pos >>> 32);
      i = 31 * i + (int)(this.m_length ^ this.m_length >>> 32);
      return i;
    }
    
    public String toString()
    {
      return String.format("FileMarker : [position %d], [length %d]", new Object[] { Long.valueOf(this.m_pos), Long.valueOf(this.m_length) });
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/TemporaryFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
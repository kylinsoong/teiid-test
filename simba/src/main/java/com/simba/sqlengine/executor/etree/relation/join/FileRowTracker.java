package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.temptable.column.BitsUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class FileRowTracker
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree.relation.join";
  private static final String CLASS_NAME = "FileRowTracker";
  private RandomAccessFile m_storage = null;
  private File m_fileUnderneath = null;
  private File m_sourceDirectory;
  private ILogger m_logger;
  private byte[] m_buffer;
  private long m_currentPage;
  private boolean m_dirty;
  private long m_maxPageNumber;
  
  public FileRowTracker(File paramFile, long paramLong, ILogger paramILogger)
  {
    this.m_logger = paramILogger;
    this.m_sourceDirectory = paramFile;
    if (paramLong > 268435455L) {
      paramLong = 268435455L;
    }
    this.m_buffer = new byte[(int)paramLong];
    this.m_currentPage = 0L;
    this.m_maxPageNumber = -1L;
    this.m_dirty = true;
  }
  
  public void close()
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "close");
    }
    try
    {
      if (this.m_storage != null) {
        this.m_storage.close();
      }
      if ((this.m_fileUnderneath != null) && (this.m_fileUnderneath.exists()) && (!this.m_fileUnderneath.delete()) && (null != this.m_logger)) {
        this.m_logger.logError(getClass().getPackage().getName(), getClass().getName(), "close", "Cannot delete temprary file: " + this.m_fileUnderneath.getAbsolutePath());
      }
    }
    catch (IOException localIOException)
    {
      if (null != this.m_logger) {
        this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "close", "Did not successfully close resources.");
      }
    }
    finally
    {
      this.m_storage = null;
      this.m_fileUnderneath = null;
    }
  }
  
  public void set(long paramLong)
    throws ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logTrace("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "set", "Row number " + paramLong);
    }
    long l = paramLong / 8L / this.m_buffer.length;
    try
    {
      load(l);
    }
    catch (IOException localIOException)
    {
      throw SQLEngineExceptionFactory.failedToWriteData(localIOException);
    }
    int i = (int)(paramLong - this.m_currentPage * this.m_buffer.length * 8L);
    BitsUtil.setBit(this.m_buffer, i);
    this.m_dirty = true;
  }
  
  public boolean isSet(long paramLong)
    throws ErrorException
  {
    long l = paramLong / 8L / this.m_buffer.length;
    if ((l != this.m_currentPage) && (l > this.m_maxPageNumber)) {
      return false;
    }
    try
    {
      load(l);
    }
    catch (IOException localIOException)
    {
      throw SQLEngineExceptionFactory.failedToWriteData(localIOException);
    }
    int i = (int)(paramLong - this.m_currentPage * this.m_buffer.length * 8L);
    boolean bool = BitsUtil.isSet(this.m_buffer, i);
    if (null != this.m_logger) {
      this.m_logger.logTrace("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "set", paramLong + (bool ? " is " : "is not") + "set");
    }
    return bool;
  }
  
  public void reset()
  {
    if (null != this.m_storage) {
      try
      {
        this.m_storage.close();
      }
      catch (IOException localIOException)
      {
        if (null != this.m_logger) {
          this.m_logger.logError("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "rest", "Could not delete temporary file.");
        }
      }
    }
    this.m_dirty = true;
    this.m_storage = null;
    Arrays.fill(this.m_buffer, (byte)0);
    this.m_currentPage = 0L;
    this.m_maxPageNumber = -1L;
  }
  
  private void load(long paramLong)
    throws IOException, ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logTrace("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "load", "page number: " + paramLong);
    }
    if (paramLong == this.m_currentPage) {
      return;
    }
    writeCurrentPage();
    this.m_currentPage = paramLong;
    if (paramLong <= this.m_maxPageNumber)
    {
      this.m_storage.seek(paramLong * this.m_buffer.length);
      int i = this.m_storage.read(this.m_buffer);
      this.m_dirty = false;
      assert (i == this.m_buffer.length);
    }
    else
    {
      Arrays.fill(this.m_buffer, (byte)0);
      this.m_dirty = true;
      while (this.m_maxPageNumber + 1L < this.m_currentPage)
      {
        this.m_maxPageNumber += 1L;
        this.m_storage.seek(this.m_maxPageNumber * this.m_buffer.length);
        this.m_storage.write(this.m_buffer);
      }
    }
  }
  
  private void writeCurrentPage()
    throws IOException, ErrorException
  {
    if (!this.m_dirty) {
      return;
    }
    if (this.m_maxPageNumber < 0L) {
      createStorage();
    }
    this.m_storage.seek(this.m_currentPage * this.m_buffer.length);
    this.m_storage.write(this.m_buffer);
    this.m_maxPageNumber = Math.max(this.m_currentPage, this.m_maxPageNumber);
  }
  
  private void createStorage()
    throws IOException, ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "FileRowTracker", "createStorage");
    }
    this.m_fileUnderneath = ExternalAlgorithmUtil.createTempFile(this.m_sourceDirectory, this.m_logger);
    this.m_storage = new RandomAccessFile(this.m_fileUnderneath, "rw");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/FileRowTracker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
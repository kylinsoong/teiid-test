package com.simba.sqlengine.utilities;

import com.simba.dsi.core.impl.DSIDriverSingleton;
import com.simba.dsi.core.interfaces.IDriver;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.mem.MemoryManager;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.ETResourceManager;
import com.simba.sqlengine.executor.etree.ETTempFolderResource;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ExternalAlgorithmUtil
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.utilities";
  private static final String CLASS_NAME = "ExternalAlgorithmUtil";
  
  public static ExternalAlgorithmProperties createProperties(SqlDataEngine paramSqlDataEngine, ILogger paramILogger, IWarningListener paramIWarningListener, ETResourceManager paramETResourceManager)
    throws ErrorException
  {
    int i = 0;
    long l1 = 0L;
    long l2 = MemoryManager.getInstance().getTotalMemory();
    int j = 0;
    try
    {
      i = paramSqlDataEngine.getProperty(13).getInt();
      l1 = paramSqlDataEngine.getProperty(12).getLong();
      j = paramSqlDataEngine.getProperty(16).getInt();
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw SQLEngineExceptionFactory.invalidConfiguration(localNumericOverflowException);
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw SQLEngineExceptionFactory.invalidConfiguration(localIncorrectTypeException);
    }
    if (l2 <= l1)
    {
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.GENERAL_WARNING, 7, SQLEngineMessageKey.INVALID_CONFIG.name()));
      }
      if (null != paramILogger) {
        paramILogger.logWarning("com.simba.sqlengine.utilities", "ExternalAlgorithmUtil", "createProperties", "Recommended block size is larger that total available memory.");
      }
      l1 = l2 / 5L;
      if (0L >= l1) {
        throw SQLEngineExceptionFactory.invalidConfiguration();
      }
    }
    if (i > l1)
    {
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.GENERAL_WARNING, 7, SQLEngineMessageKey.INVALID_CONFIG.name()));
      }
      if (null != paramILogger) {
        paramILogger.logWarning("com.simba.sqlengine.utilities", "ExternalAlgorithmUtil", "createProperties", "Max in-memory data size is larger than recommended block size.");
      }
      i = (int)(l1 - 1L);
      if (0 >= i) {
        throw SQLEngineExceptionFactory.invalidConfiguration();
      }
    }
    if (j < 4)
    {
      if (null != paramIWarningListener) {
        paramIWarningListener.postWarning(new Warning(WarningCode.GENERAL_WARNING, 7, SQLEngineMessageKey.INVALID_CONFIG.name()));
      }
      if (null != paramILogger) {
        paramILogger.logWarning("com.simba.sqlengine.utilities", "ExternalAlgorithmUtil", "createProperties", "Invalid value (" + j + ") for maximum number of open files per node.");
      }
      j = 4;
    }
    return new ExternalAlgorithmProperties(i, l1, j, paramILogger, paramETResourceManager, null);
  }
  
  public static long calculateRowSize(Iterable<IColumn> paramIterable, boolean[] paramArrayOfBoolean, int paramInt)
  {
    long l = 0L;
    int i = 0;
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      IColumn localIColumn = (IColumn)localIterator.next();
      l = (l + Math.ceil(ColumnSizeCalculator.getColumnSizePerRow(localIColumn, paramArrayOfBoolean[i], paramInt)));
      l = (l + Math.ceil(ColumnSizeCalculator.getOverHeadPerRow(localIColumn, paramArrayOfBoolean[i], paramInt)));
      i++;
    }
    return l;
  }
  
  public static File createTempFile(File paramFile, ILogger paramILogger)
    throws ErrorException
  {
    if ((null == paramFile) || (!paramFile.isDirectory())) {
      throw new IllegalArgumentException("The File passed in is not a direcotry.");
    }
    String str = "Unknown";
    try
    {
      File localFile = File.createTempFile("sql_", null, paramFile);
      str = localFile.getAbsolutePath();
      if ((!localFile.canRead()) || (!localFile.canWrite()))
      {
        if (null != paramILogger) {
          paramILogger.logError("com.simba.sqlengine.utilities", "ExternalAlgorithmUtil", "RowFile", "Could not create temporary file.");
        }
        throw SQLEngineExceptionFactory.failedToCreateFile(str);
      }
      return localFile;
    }
    catch (IOException localIOException)
    {
      if (null != paramILogger) {
        paramILogger.logError("com.simba.sqlengine.utilities", "ExternalAlgorithmUtil", "RowFile", "Could not create temporary file.");
      }
      throw SQLEngineExceptionFactory.failedToCreateFile(str, localIOException.getLocalizedMessage());
    }
  }
  
  private static void mkdir(File paramFile)
    throws ErrorException
  {
    if ((paramFile.exists()) && (!paramFile.isDirectory())) {
      paramFile.delete();
    }
    paramFile.mkdir();
    paramFile.deleteOnExit();
    if ((!paramFile.exists()) || (!paramFile.isDirectory()) || (!paramFile.canRead()) || (!paramFile.canWrite())) {
      throw SQLEngineExceptionFactory.failedToCreateFile(paramFile.getAbsolutePath());
    }
  }
  
  private static class TmpFileDirHolder
  {
    private static File TEMP_DIR = getTempFileDir();
    
    private static File getTempFileDir()
    {
      try
      {
        String str = null;
        IDriver localIDriver = DSIDriverSingleton.getInstance();
        if (null != localIDriver)
        {
          localObject = localIDriver.getProperty(1006);
          if (null != localObject) {
            str = ((Variant)localObject).getString();
          }
        }
        if ((str == null) || (str.equals(""))) {
          str = System.getProperty("java.io.tmpdir");
        }
        assert (str != null);
        Object localObject = new File(str);
        ExternalAlgorithmUtil.mkdir((File)localObject);
        return (File)localObject;
      }
      catch (Exception localException)
      {
        throw new ExceptionInInitializerError(localException);
      }
    }
  }
  
  public static class ExternalAlgorithmProperties
  {
    private final int m_cellMemoryLimit;
    private final long m_blockSize;
    private final int m_maxOpenFiles;
    private final ILogger m_logger;
    private File m_storageDir = null;
    private final ETResourceManager m_rManager;
    
    private ExternalAlgorithmProperties(int paramInt1, long paramLong, int paramInt2, ILogger paramILogger, ETResourceManager paramETResourceManager)
    {
      assert (paramInt1 > 0);
      assert (paramLong > 0L);
      this.m_cellMemoryLimit = paramInt1;
      this.m_blockSize = paramLong;
      this.m_maxOpenFiles = paramInt2;
      this.m_logger = paramILogger;
      this.m_rManager = paramETResourceManager;
    }
    
    public int getCellMemoryLimit()
    {
      return this.m_cellMemoryLimit;
    }
    
    public long getBlockSize()
    {
      return this.m_blockSize;
    }
    
    public File getStorageDir()
      throws ErrorException
    {
      if (null == this.m_storageDir)
      {
        File localFile1 = null;
        try
        {
          localFile1 = ExternalAlgorithmUtil.TmpFileDirHolder.TEMP_DIR;
        }
        catch (ExceptionInInitializerError localExceptionInInitializerError)
        {
          throw SQLEngineExceptionFactory.failedToCreateFile("SQLENGINE_TEMP_FILE_DIRECTORY", localExceptionInInitializerError.getLocalizedMessage());
        }
        catch (NoClassDefFoundError localNoClassDefFoundError)
        {
          throw SQLEngineExceptionFactory.failedToCreateFile("SQLENGINE_TEMP_FILE_DIRECTORY", localNoClassDefFoundError.getLocalizedMessage());
        }
        assert (localFile1 != null);
        File localFile2 = ExternalAlgorithmUtil.createTempFile(localFile1, null);
        ExternalAlgorithmUtil.mkdir(localFile2);
        this.m_storageDir = localFile2;
        ETTempFolderResource localETTempFolderResource = new ETTempFolderResource(this.m_storageDir, this.m_logger);
        this.m_rManager.registerResource(localETTempFolderResource);
      }
      return this.m_storageDir;
    }
    
    public ILogger getLogger()
    {
      return this.m_logger;
    }
    
    public int getMaxNumOpenFiles()
    {
      return this.m_maxOpenFiles;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/utilities/ExternalAlgorithmUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
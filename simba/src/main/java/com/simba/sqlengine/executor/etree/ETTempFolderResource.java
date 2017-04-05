package com.simba.sqlengine.executor.etree;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.ILogger;
import com.simba.support.exceptions.ErrorException;
import java.io.File;

public class ETTempFolderResource
  implements IETResource
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree";
  private static final String CLASS_NAME = "ETTempFolderResource";
  private File m_folder;
  private ILogger m_logger;
  
  public ETTempFolderResource(File paramFile, ILogger paramILogger)
  {
    this.m_folder = paramFile;
    this.m_logger = paramILogger;
  }
  
  public void allocate()
    throws ErrorException
  {
    if (!this.m_folder.exists()) {
      this.m_folder.mkdir();
    }
    if ((!this.m_folder.exists()) || (!this.m_folder.isDirectory()) || (!this.m_folder.canRead()) || (!this.m_folder.canWrite())) {
      throw SQLEngineExceptionFactory.failedToCreateFile(this.m_folder.getAbsolutePath());
    }
  }
  
  public void free()
    throws ErrorException
  {
    if (!this.m_folder.exists()) {
      return;
    }
    if (!this.m_folder.isDirectory()) {
      throw SQLEngineExceptionFactory.failedToDeleteFile(this.m_folder.getAbsolutePath());
    }
    File[] arrayOfFile1 = this.m_folder.listFiles();
    if (arrayOfFile1 == null) {
      throw SQLEngineExceptionFactory.failedToDeleteFile(this.m_folder.getAbsolutePath());
    }
    assert (arrayOfFile1.length == 0);
    if ((arrayOfFile1.length != 0) && (this.m_logger != null)) {
      this.m_logger.logWarning("com.simba.sqlengine.executor.etree", "ETTempFolderResource", "free", "Temporary files are not cleaned up after execution.");
    }
    for (File localFile : arrayOfFile1) {
      if ((localFile.isDirectory()) || (!localFile.delete())) {
        throw SQLEngineExceptionFactory.failedToDeleteFile(this.m_folder.getAbsolutePath());
      }
    }
    if (!this.m_folder.delete()) {
      throw SQLEngineExceptionFactory.failedToDeleteFile(this.m_folder.getAbsolutePath());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/ETTempFolderResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
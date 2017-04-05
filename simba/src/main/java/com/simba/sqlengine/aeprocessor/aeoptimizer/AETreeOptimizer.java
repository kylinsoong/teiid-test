package com.simba.sqlengine.aeprocessor.aeoptimizer;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.dsiext.dataengine.SqlQueryExecutorContext;
import com.simba.sqlengine.utilities.AEStringLogger;
import com.simba.support.SettingReader;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class AETreeOptimizer
  implements IAEOptimizer
{
  private SqlQueryExecutorContext m_executorContext;
  private SqlDataEngineContext m_dataEngineContext;
  private SqlDataEngine m_dataEngine;
  
  public AETreeOptimizer(SqlQueryExecutorContext paramSqlQueryExecutorContext)
  {
    this.m_executorContext = paramSqlQueryExecutorContext;
    this.m_dataEngineContext = paramSqlQueryExecutorContext.getDataEngineContext();
    this.m_dataEngine = this.m_dataEngineContext.getDataEngine();
  }
  
  public void optimize(IAEStatement paramIAEStatement)
    throws ErrorException
  {
    if (null == paramIAEStatement) {
      throw new NullPointerException("statement cannot be null");
    }
    if (this.m_dataEngine.getProperty(3).getString().equals("Y"))
    {
      try
      {
        if (0L != (this.m_dataEngine.getProperty(4).getLong() & 0x2))
        {
          String str1 = SettingReader.readSetting("LogPath");
          AEStringLogger.logAETree(paramIAEStatement, new File(str1, "AETree.log").getPath(), new String[] { new Timestamp(System.currentTimeMillis()).toString() + " :: Post-ReOrder ==========================================================" });
        }
      }
      catch (IOException localIOException1) {}catch (IncorrectTypeException localIncorrectTypeException1)
      {
        throw new AssertionError(localIncorrectTypeException1);
      }
      catch (NumericOverflowException localNumericOverflowException1)
      {
        throw new AssertionError(localNumericOverflowException1);
      }
      new AEFilterOptimizer(this.m_dataEngineContext, this.m_executorContext.getPassdownInformation()).optimize(paramIAEStatement);
      try
      {
        if (0L != (this.m_dataEngine.getProperty(4).getLong() & 0x4))
        {
          String str2 = SettingReader.readSetting("LogPath");
          AEStringLogger.logAETree(paramIAEStatement, new File(str2, "AETree.log").getPath(), new String[] { new Timestamp(System.currentTimeMillis()).toString() + " :: Post-Optimize ==========================================================" });
        }
      }
      catch (IOException localIOException2) {}catch (IncorrectTypeException localIncorrectTypeException2)
      {
        throw new AssertionError(localIncorrectTypeException2);
      }
      catch (NumericOverflowException localNumericOverflowException2)
      {
        throw new AssertionError(localNumericOverflowException2);
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aeoptimizer/AETreeOptimizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
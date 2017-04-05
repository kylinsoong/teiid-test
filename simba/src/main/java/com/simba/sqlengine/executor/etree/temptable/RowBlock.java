package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSliceArray;
import com.simba.sqlengine.executor.etree.temptable.column.IColumnSlice;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class RowBlock
  implements IRowView
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree.temptable";
  private static final String CLASS_NAME = "RowBlock";
  private ColumnSliceArray m_columns;
  private int m_currentRow = -1;
  private ILogger m_logger;
  private IColumn[] m_metadata;
  
  public RowBlock(ColumnSliceArray paramColumnSliceArray, IColumn[] paramArrayOfIColumn, ILogger paramILogger)
  {
    this.m_columns = paramColumnSliceArray;
    this.m_metadata = ((IColumn[])paramArrayOfIColumn.clone());
    this.m_logger = paramILogger;
  }
  
  /* Error */
  public static RowBlock loadData(byte[] paramArrayOfByte, ILogger paramILogger, IColumn[] paramArrayOfIColumn)
    throws com.simba.support.exceptions.ErrorException
  {
    // Byte code:
    //   0: aconst_null
    //   1: aload_1
    //   2: if_acmpeq +11 -> 13
    //   5: aload_1
    //   6: iconst_0
    //   7: anewarray 8	java/lang/Object
    //   10: invokestatic 9	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   13: aconst_null
    //   14: aload_1
    //   15: if_acmpeq +21 -> 36
    //   18: getstatic 10	com/simba/support/LogLevel:TRACE	Lcom/simba/support/LogLevel;
    //   21: aload_1
    //   22: invokeinterface 11 1 0
    //   27: if_acmpne +9 -> 36
    //   30: invokestatic 12	java/lang/System:nanoTime	()J
    //   33: goto +4 -> 37
    //   36: lconst_0
    //   37: lstore_3
    //   38: new 13	java/io/ObjectInputStream
    //   41: dup
    //   42: new 14	java/io/ByteArrayInputStream
    //   45: dup
    //   46: aload_0
    //   47: invokespecial 15	java/io/ByteArrayInputStream:<init>	([B)V
    //   50: invokespecial 16	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
    //   53: astore 5
    //   55: aload 5
    //   57: invokevirtual 17	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
    //   60: checkcast 18	com/simba/sqlengine/executor/etree/temptable/column/ColumnSliceArray
    //   63: astore 6
    //   65: new 19	com/simba/sqlengine/executor/etree/temptable/RowBlock
    //   68: dup
    //   69: aload 6
    //   71: aload_2
    //   72: aload_1
    //   73: invokespecial 20	com/simba/sqlengine/executor/etree/temptable/RowBlock:<init>	(Lcom/simba/sqlengine/executor/etree/temptable/column/ColumnSliceArray;[Lcom/simba/dsi/dataengine/interfaces/IColumn;Lcom/simba/support/ILogger;)V
    //   76: astore 7
    //   78: aconst_null
    //   79: aload_1
    //   80: if_acmpeq +82 -> 162
    //   83: getstatic 10	com/simba/support/LogLevel:TRACE	Lcom/simba/support/LogLevel;
    //   86: aload_1
    //   87: invokeinterface 11 1 0
    //   92: if_acmpne +70 -> 162
    //   95: aload_1
    //   96: ldc 21
    //   98: ldc 22
    //   100: ldc 23
    //   102: ldc 24
    //   104: iconst_4
    //   105: anewarray 8	java/lang/Object
    //   108: dup
    //   109: iconst_0
    //   110: aload_0
    //   111: arraylength
    //   112: invokestatic 25	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   115: aastore
    //   116: dup
    //   117: iconst_1
    //   118: aload 6
    //   120: invokevirtual 26	com/simba/sqlengine/executor/etree/temptable/column/ColumnSliceArray:trueNumColumns	()I
    //   123: invokestatic 25	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   126: aastore
    //   127: dup
    //   128: iconst_2
    //   129: aload 6
    //   131: invokevirtual 27	com/simba/sqlengine/executor/etree/temptable/column/ColumnSliceArray:numRows	()I
    //   134: invokestatic 25	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   137: aastore
    //   138: dup
    //   139: iconst_3
    //   140: invokestatic 12	java/lang/System:nanoTime	()J
    //   143: lload_3
    //   144: lsub
    //   145: l2d
    //   146: ldc2_w 28
    //   149: ddiv
    //   150: invokestatic 30	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   153: aastore
    //   154: invokestatic 31	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   157: invokeinterface 32 5 0
    //   162: aload 7
    //   164: astore 8
    //   166: aload 5
    //   168: invokevirtual 33	java/io/ObjectInputStream:close	()V
    //   171: goto +24 -> 195
    //   174: astore 9
    //   176: aconst_null
    //   177: aload_1
    //   178: if_acmpeq +17 -> 195
    //   181: aload_1
    //   182: ldc 21
    //   184: ldc 22
    //   186: ldc 23
    //   188: ldc 35
    //   190: invokeinterface 36 5 0
    //   195: aload 8
    //   197: areturn
    //   198: astore 10
    //   200: aload 5
    //   202: invokevirtual 33	java/io/ObjectInputStream:close	()V
    //   205: goto +24 -> 229
    //   208: astore 11
    //   210: aconst_null
    //   211: aload_1
    //   212: if_acmpeq +17 -> 229
    //   215: aload_1
    //   216: ldc 21
    //   218: ldc 22
    //   220: ldc 23
    //   222: ldc 35
    //   224: invokeinterface 36 5 0
    //   229: aload 10
    //   231: athrow
    //   232: astore_3
    //   233: aload_3
    //   234: invokestatic 38	com/simba/sqlengine/exceptions/SQLEngineExceptionFactory:failedToReadData	(Ljava/lang/Exception;)Lcom/simba/support/exceptions/ErrorException;
    //   237: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	238	0	paramArrayOfByte	byte[]
    //   0	238	1	paramILogger	ILogger
    //   0	238	2	paramArrayOfIColumn	IColumn[]
    //   37	107	3	l	long
    //   232	2	3	localException	Exception
    //   53	148	5	localObjectInputStream	java.io.ObjectInputStream
    //   63	67	6	localColumnSliceArray	ColumnSliceArray
    //   76	87	7	localRowBlock1	RowBlock
    //   164	32	8	localRowBlock2	RowBlock
    //   174	1	9	localIOException1	java.io.IOException
    //   198	32	10	localObject	Object
    //   208	1	11	localIOException2	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   166	171	174	java/io/IOException
    //   55	166	198	finally
    //   198	200	198	finally
    //   200	205	208	java/io/IOException
    //   13	195	232	java/lang/Exception
    //   198	232	232	java/lang/Exception
  }
  
  public boolean moveToNextRow()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    if (this.m_currentRow < this.m_columns.numRows()) {
      this.m_currentRow += 1;
    }
    return this.m_currentRow < this.m_columns.numRows();
  }
  
  public boolean moveToRow(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    if ((paramInt < this.m_columns.numRows()) && (paramInt > -1))
    {
      this.m_currentRow = paramInt;
      return true;
    }
    this.m_currentRow = this.m_columns.numRows();
    return false;
  }
  
  /* Error */
  public byte[] serialize()
    throws com.simba.support.exceptions.ErrorException
  {
    // Byte code:
    //   0: aconst_null
    //   1: aload_0
    //   2: getfield 7	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_logger	Lcom/simba/support/ILogger;
    //   5: if_acmpeq +14 -> 19
    //   8: aload_0
    //   9: getfield 7	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_logger	Lcom/simba/support/ILogger;
    //   12: iconst_0
    //   13: anewarray 8	java/lang/Object
    //   16: invokestatic 9	com/simba/support/LogUtilities:logFunctionEntrance	(Lcom/simba/support/ILogger;[Ljava/lang/Object;)V
    //   19: new 39	java/io/ByteArrayOutputStream
    //   22: dup
    //   23: sipush 1024
    //   26: invokespecial 40	java/io/ByteArrayOutputStream:<init>	(I)V
    //   29: astore_1
    //   30: new 41	java/io/ObjectOutputStream
    //   33: dup
    //   34: aload_1
    //   35: invokespecial 42	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   38: astore_2
    //   39: aload_2
    //   40: aload_0
    //   41: getfield 3	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_columns	Lcom/simba/sqlengine/executor/etree/temptable/column/ColumnSliceArray;
    //   44: invokevirtual 43	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
    //   47: aload_2
    //   48: invokevirtual 44	java/io/ObjectOutputStream:flush	()V
    //   51: aload_1
    //   52: invokevirtual 45	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   55: astore_3
    //   56: aload_2
    //   57: invokevirtual 46	java/io/ObjectOutputStream:close	()V
    //   60: goto +30 -> 90
    //   63: astore 4
    //   65: aconst_null
    //   66: aload_0
    //   67: getfield 7	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_logger	Lcom/simba/support/ILogger;
    //   70: if_acmpeq +20 -> 90
    //   73: aload_0
    //   74: getfield 7	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_logger	Lcom/simba/support/ILogger;
    //   77: ldc 21
    //   79: ldc 22
    //   81: ldc 47
    //   83: ldc 35
    //   85: invokeinterface 36 5 0
    //   90: aload_3
    //   91: areturn
    //   92: astore 5
    //   94: aload_2
    //   95: invokevirtual 46	java/io/ObjectOutputStream:close	()V
    //   98: goto +30 -> 128
    //   101: astore 6
    //   103: aconst_null
    //   104: aload_0
    //   105: getfield 7	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_logger	Lcom/simba/support/ILogger;
    //   108: if_acmpeq +20 -> 128
    //   111: aload_0
    //   112: getfield 7	com/simba/sqlengine/executor/etree/temptable/RowBlock:m_logger	Lcom/simba/support/ILogger;
    //   115: ldc 21
    //   117: ldc 22
    //   119: ldc 47
    //   121: ldc 35
    //   123: invokeinterface 36 5 0
    //   128: aload 5
    //   130: athrow
    //   131: astore_1
    //   132: aload_1
    //   133: invokestatic 48	com/simba/sqlengine/exceptions/SQLEngineExceptionFactory:failedToWriteData	(Ljava/lang/Exception;)Lcom/simba/support/exceptions/ErrorException;
    //   136: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	RowBlock
    //   29	23	1	localByteArrayOutputStream	java.io.ByteArrayOutputStream
    //   131	2	1	localIOException1	java.io.IOException
    //   38	57	2	localObjectOutputStream	java.io.ObjectOutputStream
    //   55	36	3	arrayOfByte	byte[]
    //   63	1	4	localIOException2	java.io.IOException
    //   92	37	5	localObject	Object
    //   101	1	6	localIOException3	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   56	60	63	java/io/IOException
    //   39	56	92	finally
    //   92	94	92	finally
    //   94	98	101	java/io/IOException
    //   19	90	131	java/io/IOException
    //   92	131	131	java/io/IOException
  }
  
  public int getNumRows()
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[0]);
    }
    return this.m_columns.numRows();
  }
  
  public BigDecimal getExactNumber(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getExactNum(this.m_currentRow);
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getFileMarker(this.m_currentRow);
  }
  
  public long getBigInt(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getBigInt(this.m_currentRow);
  }
  
  public boolean isNull(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).isNull(this.m_currentRow);
  }
  
  public double getDouble(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getDouble(this.m_currentRow);
  }
  
  public float getReal(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getReal(this.m_currentRow);
  }
  
  public boolean getBoolean(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getBoolean(this.m_currentRow);
  }
  
  public String getString(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getString(this.m_currentRow);
  }
  
  public Date getDate(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getDate(this.m_currentRow);
  }
  
  public Time getTime(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getTime(this.m_currentRow);
  }
  
  public Timestamp getTimestamp(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getTimestamp(this.m_currentRow);
  }
  
  public UUID getGuid(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getGuid(this.m_currentRow);
  }
  
  public int getInteger(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getInteger(this.m_currentRow);
  }
  
  public short getSmallInt(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getSmallInt(this.m_currentRow);
  }
  
  public byte getTinyInt(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getTinyInt(this.m_currentRow);
  }
  
  public byte[] getBytes(int paramInt)
  {
    if (null != this.m_logger) {
      LogUtilities.logFunctionEntrance(this.m_logger, new Object[] { Integer.valueOf(paramInt) });
    }
    return this.m_columns.get(paramInt).getBytes(this.m_currentRow);
  }
  
  public IColumn getColumn(int paramInt)
  {
    return this.m_metadata[paramInt];
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/RowBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
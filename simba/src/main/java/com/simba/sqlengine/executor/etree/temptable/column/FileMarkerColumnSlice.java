package com.simba.sqlengine.executor.etree.temptable.column;

import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileMarkerColumnSlice
  extends DefaultColumnSlice
{
  private static final long serialVersionUID = 8346899091480541118L;
  private transient TemporaryFile.FileMarker[] m_data;
  private final int m_columnNumber;
  
  public FileMarkerColumnSlice(int paramInt1, int paramInt2)
  {
    this.m_data = new TemporaryFile.FileMarker[paramInt1];
    this.m_columnNumber = paramInt2;
  }
  
  public int columnNumber()
  {
    return this.m_columnNumber;
  }
  
  public boolean isNull(int paramInt)
  {
    return null == this.m_data[paramInt];
  }
  
  public void setNull(int paramInt)
  {
    this.m_data[paramInt] = null;
  }
  
  public TemporaryFile.FileMarker getFileMarker(int paramInt)
  {
    return this.m_data[paramInt];
  }
  
  public IColumnSlice.ColumnSliceType getType()
  {
    return IColumnSlice.ColumnSliceType.FILE_MARKER;
  }
  
  public void setFileMarker(int paramInt, TemporaryFile.FileMarker paramFileMarker)
  {
    this.m_data[paramInt] = paramFileMarker;
  }
  
  public int size()
  {
    return this.m_data.length;
  }
  
  public void copy(IColumnSlice paramIColumnSlice, int paramInt1, int paramInt2, int paramInt3)
  {
    FileMarkerColumnSlice localFileMarkerColumnSlice = (FileMarkerColumnSlice)paramIColumnSlice;
    System.arraycopy(localFileMarkerColumnSlice.m_data, paramInt1, this.m_data, paramInt3, paramInt2);
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    int i = this.m_data.length;
    paramObjectOutputStream.writeInt(i);
    for (int j = 0; j < i; j++)
    {
      TemporaryFile.FileMarker localFileMarker = this.m_data[j];
      if (null == localFileMarker)
      {
        paramObjectOutputStream.writeBoolean(true);
      }
      else
      {
        paramObjectOutputStream.writeBoolean(false);
        paramObjectOutputStream.writeLong(localFileMarker.m_pos);
        paramObjectOutputStream.writeLong(localFileMarker.m_length);
      }
    }
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    int i = paramObjectInputStream.readInt();
    this.m_data = new TemporaryFile.FileMarker[i];
    for (int j = 0; j < i; j++) {
      this.m_data[j] = (paramObjectInputStream.readBoolean() ? null : new TemporaryFile.FileMarker(paramObjectInputStream.readLong(), paramObjectInputStream.readLong()));
    }
  }
  
  static double estimateRowSize(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectShellSize() + 2 * paramJavaSize.getLongSize();
  }
  
  static double estimateRowOverhead(ColumnSizeCalculator.JavaSize paramJavaSize)
  {
    return paramJavaSize.getObjectRefSize();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/column/FileMarkerColumnSlice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
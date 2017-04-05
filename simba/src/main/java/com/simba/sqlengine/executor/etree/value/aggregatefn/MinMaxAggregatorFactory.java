package com.simba.sqlengine.executor.etree.value.aggregatefn;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.temptable.InMemTable;
import com.simba.sqlengine.executor.etree.temptable.IndexRowView;
import com.simba.sqlengine.executor.etree.temptable.RowBlock;
import com.simba.sqlengine.executor.etree.temptable.RowComparator;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.exceptions.ErrorException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MinMaxAggregatorFactory
  extends AbstractAggregatorFactory
{
  private RowComparator m_rowCmp = null;
  private final boolean m_isMax;
  private final int m_maxColSize;
  
  public MinMaxAggregatorFactory(IColumn[] paramArrayOfIColumn, IColumn paramIColumn, boolean paramBoolean, int paramInt)
  {
    super(paramArrayOfIColumn, paramIColumn);
    this.m_isMax = paramBoolean;
    this.m_maxColSize = paramInt;
  }
  
  public boolean requiresDistinct()
  {
    return false;
  }
  
  public AbstractAggregator createAggregator()
    throws ErrorException
  {
    IColumn[] arrayOfIColumn = { getInputMetadata(0) };
    boolean[] arrayOfBoolean = { true };
    if (null == this.m_rowCmp) {
      this.m_rowCmp = RowComparator.createComparator(arrayOfIColumn, Arrays.asList(new AESortSpec[] { new AESortSpec(0, this.m_isMax) }), RowComparator.getDefaultNullCollation());
    }
    InMemTable localInMemTable = new InMemTable(arrayOfIColumn, this.m_maxColSize, 2, arrayOfBoolean, null);
    localInMemTable.setMemLimit(Long.MAX_VALUE);
    return new MinMaxAggregator(this, localInMemTable, localInMemTable.appendRow(), localInMemTable.appendRow());
  }
  
  protected final RowComparator getComparator()
  {
    return this.m_rowCmp;
  }
  
  private static class MinMaxAggregator
    extends AbstractAggregator
  {
    private final InMemTable m_inMemTable;
    private int m_currRow;
    private int m_nextRow;
    
    public MinMaxAggregator(MinMaxAggregatorFactory paramMinMaxAggregatorFactory, InMemTable paramInMemTable, int paramInt1, int paramInt2)
    {
      super();
      this.m_currRow = paramInt1;
      this.m_nextRow = paramInt2;
      this.m_inMemTable = paramInMemTable;
    }
    
    public void close()
    {
      this.m_inMemTable.clear();
    }
    
    public void load(byte[] paramArrayOfByte)
      throws ErrorException
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
      byte[] arrayOfByte = new byte[paramArrayOfByte.length];
      localByteBuffer.get(arrayOfByte);
      RowBlock localRowBlock = RowBlock.loadData(arrayOfByte, null, new IColumn[] { getInputMetadata(0) });
      localRowBlock.moveToNextRow();
      InMemTable.setColumn(this.m_inMemTable, localRowBlock, 0, this.m_currRow, Integer.MAX_VALUE);
      localRowBlock.moveToNextRow();
      InMemTable.setColumn(this.m_inMemTable, localRowBlock, 0, this.m_nextRow, Integer.MAX_VALUE);
      assert (!localRowBlock.moveToNextRow());
      int i;
      if ((this.m_inMemTable.isNull(this.m_currRow, 0)) && (!this.m_inMemTable.isNull(this.m_nextRow, 0)))
      {
        i = this.m_currRow;
        this.m_currRow = this.m_nextRow;
        this.m_nextRow = i;
      }
      else if ((!this.m_inMemTable.isNull(this.m_nextRow, 0)) && (0 > doCompare()))
      {
        i = this.m_currRow;
        this.m_currRow = this.m_nextRow;
        this.m_nextRow = i;
      }
      this.m_inMemTable.setNull(this.m_nextRow, 0);
    }
    
    public byte[] serialize()
      throws ErrorException
    {
      byte[] arrayOfByte = this.m_inMemTable.toRowBlock().serialize();
      ByteBuffer localByteBuffer = ByteBuffer.allocate(arrayOfByte.length);
      localByteBuffer.put(arrayOfByte);
      return localByteBuffer.array();
    }
    
    public boolean retrieveData(ETDataRequest paramETDataRequest)
      throws ErrorException
    {
      IndexRowView localIndexRowView = new IndexRowView(this.m_inMemTable);
      localIndexRowView.setRowNum(this.m_currRow);
      return DataRetrievalUtil.retrieveFromRowView(0, false, paramETDataRequest, localIndexRowView, (TemporaryFile)null);
    }
    
    public void reset()
    {
      this.m_inMemTable.setNull(this.m_currRow, 0);
      this.m_inMemTable.setNull(this.m_nextRow, 0);
    }
    
    protected long getMemoryUsage()
    {
      if (null != this.m_inMemTable) {
        return this.m_inMemTable.getMemUsage() + 4;
      }
      return 4L;
    }
    
    protected void update()
      throws ErrorException
    {
      if (this.m_inMemTable.isNull(this.m_currRow, 0))
      {
        InMemTable.setColumn(this.m_inMemTable, getArgumentData(0), getInputMetadata(0), 0, this.m_currRow);
      }
      else
      {
        InMemTable.setColumn(this.m_inMemTable, getArgumentData(0), getInputMetadata(0), 0, this.m_nextRow);
        if (!this.m_inMemTable.isNull(this.m_nextRow, 0))
        {
          if (doCompare() < 0)
          {
            int i = this.m_currRow;
            this.m_currRow = this.m_nextRow;
            this.m_nextRow = i;
          }
          this.m_inMemTable.setNull(this.m_nextRow, 0);
        }
      }
    }
    
    private int doCompare()
    {
      IndexRowView localIndexRowView1 = new IndexRowView(this.m_inMemTable);
      localIndexRowView1.setRowNum(this.m_currRow);
      IndexRowView localIndexRowView2 = new IndexRowView(this.m_inMemTable);
      localIndexRowView2.setRowNum(this.m_nextRow);
      return ((MinMaxAggregatorFactory)getFactory()).getComparator().compare(localIndexRowView1, localIndexRowView2);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/aggregatefn/MinMaxAggregatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
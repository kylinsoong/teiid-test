package com.simba.sqlengine.executor.etree.statement;

import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet.DMLType;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IETUnaryNode;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ETSearchedDelete
  extends RowCountStatement
  implements IETUnaryNode<ETTable>
{
  private ETTable m_operand;
  private ETBooleanExpr m_condition;
  private long m_rowCount = -1L;
  
  public ETSearchedDelete(ETTable paramETTable, ETBooleanExpr paramETBooleanExpr, Map<Integer, ETParameter> paramMap)
  {
    super(paramMap);
    this.m_operand = paramETTable;
    this.m_condition = paramETBooleanExpr;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    if (null != this.m_operand)
    {
      try
      {
        this.m_operand.close();
      }
      catch (Exception localException) {}
      this.m_operand = null;
    }
    this.m_condition = null;
  }
  
  public Iterator<? extends IETNode> getChildItr()
  {
    return asList().iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public ETTable getOperand()
  {
    return this.m_operand;
  }
  
  public long getRowCount()
    throws ErrorException
  {
    return this.m_rowCount;
  }
  
  public ETBooleanExpr getSearchCondition()
  {
    return this.m_condition;
  }
  
  public void startBatch()
    throws ErrorException
  {
    this.m_operand.open(CursorType.FORWARD_ONLY);
    this.m_operand.onStartDMLBatch(DSIExtJResultSet.DMLType.DELETE, -1L);
  }
  
  public void endBatch()
    throws ErrorException
  {
    this.m_operand.onFinishDMLBatch();
    this.m_operand.close();
  }
  
  public void execute()
    throws ErrorException
  {
    this.m_operand.reset();
    this.m_condition.open();
    this.m_rowCount = 0L;
    while (this.m_operand.move()) {
      if (ETBoolean.SQL_BOOLEAN_TRUE == this.m_condition.evaluate())
      {
        this.m_operand.deleteRow();
        this.m_rowCount += 1L;
      }
    }
    this.m_condition.close();
  }
  
  public boolean isResultSet()
  {
    return false;
  }
  
  private List<? extends IETNode> asList()
  {
    new AbstractList()
    {
      public IETNode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return ETSearchedDelete.this.m_operand;
        }
        if (1 == paramAnonymousInt) {
          return ETSearchedDelete.this.m_condition;
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int size()
      {
        return 2;
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/statement/ETSearchedDelete.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.AEColumnInfo;
import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public abstract class AEBinaryRelationalExpr
  extends AERelationalExpr
  implements IAEBinaryNode<AERelationalExpr, AERelationalExpr>
{
  private AERelationalExpr m_left;
  private AERelationalExpr m_right;
  protected boolean[] m_dataNeeded;
  protected boolean m_shouldUpdateDNTracker = true;
  
  protected AEBinaryRelationalExpr(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2)
  {
    this.m_left = paramAERelationalExpr1;
    this.m_left.setParent(this);
    this.m_right = paramAERelationalExpr2;
    this.m_right.setParent(this);
  }
  
  protected AEBinaryRelationalExpr(AEBinaryRelationalExpr paramAEBinaryRelationalExpr)
  {
    super(paramAEBinaryRelationalExpr);
    this.m_shouldUpdateDNTracker = true;
    this.m_left = paramAEBinaryRelationalExpr.m_left.copy();
    this.m_left.setParent(this);
    this.m_right = paramAEBinaryRelationalExpr.m_right.copy();
    this.m_right.setParent(this);
  }
  
  public abstract AEBinaryRelationalExpr copy();
  
  public AERelationalExpr getLeftOperand()
  {
    return this.m_left;
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  protected abstract IAENode getChild(int paramInt);
  
  public int findColumn(String paramString, boolean paramBoolean)
  {
    if ((null == paramString) || (paramString.length() == 0)) {
      throw new IllegalArgumentException("name cannot be null or empty.");
    }
    int i = getColumnCount();
    for (int j = 0; j < i; j++)
    {
      String str = getColumn(j).getName();
      if (paramBoolean ? paramString.equals(str) : paramString.equalsIgnoreCase(str)) {
        return j;
      }
    }
    return -1;
  }
  
  public AEColumnInfo findQColumn(AEQColumnName paramAEQColumnName, boolean paramBoolean)
    throws ErrorException
  {
    AEColumnInfo localAEColumnInfo = getLeftOperand().findQColumn(paramAEQColumnName, paramBoolean);
    if (null != localAEColumnInfo) {
      return localAEColumnInfo;
    }
    return getRightOperand().findQColumn(paramAEQColumnName, paramBoolean);
  }
  
  public void setLeftOperand(AERelationalExpr paramAERelationalExpr)
  {
    paramAERelationalExpr.setParent(this);
    this.m_left = paramAERelationalExpr;
  }
  
  public AERelationalExpr getRightOperand()
  {
    return this.m_right;
  }
  
  public void setRightOperand(AERelationalExpr paramAERelationalExpr)
  {
    paramAERelationalExpr.setParent(this);
    this.m_right = paramAERelationalExpr;
  }
  
  public IColumn getColumn(int paramInt)
  {
    int i = getLeftOperand().getColumnCount();
    if (paramInt < i) {
      return getLeftOperand().getColumn(paramInt);
    }
    return getRightOperand().getColumn(paramInt - i);
  }
  
  public int getColumnCount()
  {
    return getLeftOperand().getColumnCount() + getRightOperand().getColumnCount();
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[getLeftOperand().getColumnCount() + getRightOperand().getColumnCount()];
      this.m_shouldUpdateDNTracker = false;
    }
    return this.m_dataNeeded[paramInt];
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    if (this.m_shouldUpdateDNTracker)
    {
      this.m_dataNeeded = new boolean[getLeftOperand().getColumnCount() + getRightOperand().getColumnCount()];
      this.m_shouldUpdateDNTracker = false;
    }
    if (paramAERelationalExpr.equals(this))
    {
      this.m_dataNeeded[paramInt] = true;
      if (paramInt >= this.m_left.getColumnCount()) {
        this.m_right.setDataNeeded(this.m_right, paramInt - this.m_left.getColumnCount());
      } else {
        this.m_left.setDataNeeded(this.m_left, paramInt);
      }
      return paramInt;
    }
    int i = this.m_left.setDataNeeded(paramAERelationalExpr, paramInt);
    if (i == -1)
    {
      i = this.m_right.setDataNeeded(paramAERelationalExpr, paramInt);
      if (i != -1)
      {
        i += this.m_left.getColumnCount();
        this.m_dataNeeded[i] = true;
      }
    }
    else
    {
      this.m_dataNeeded[i] = true;
    }
    return i;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    this.m_left.setDataNeededOnChild();
    this.m_right.setDataNeededOnChild();
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        return AEBinaryRelationalExpr.this.getChild(paramAnonymousInt);
      }
      
      public int size()
      {
        return AEBinaryRelationalExpr.this.getNumChildren();
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AEBinaryRelationalExpr.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
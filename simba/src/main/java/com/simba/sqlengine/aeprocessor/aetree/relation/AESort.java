package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AESort
  extends AEUnaryRelationalExpr
{
  private static final int NUM_CHILDREN = 1;
  private List<AESortSpec> m_sortSpecs;
  private int m_accessibleColCount;
  
  public AESort(AERelationalExpr paramAERelationalExpr, List<AESortSpec> paramList, int paramInt)
  {
    super(paramAERelationalExpr);
    this.m_sortSpecs = new ArrayList(paramList);
    if ((paramInt < 0) || (paramInt > paramAERelationalExpr.getColumnCount())) {
      throw new IllegalArgumentException("Invalid restricted column count.");
    }
    this.m_accessibleColCount = paramInt;
  }
  
  private AESort(AESort paramAESort)
  {
    super(paramAESort);
    this.m_sortSpecs = new ArrayList(paramAESort.m_sortSpecs);
    this.m_accessibleColCount = paramAESort.m_accessibleColCount;
  }
  
  public List<AESortSpec> getSortSpecs()
  {
    return Collections.unmodifiableList(this.m_sortSpecs);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AESort)) {
      return false;
    }
    AESort localAESort = (AESort)paramIAENode;
    return (getOperand().isEquivalent(localAESort.getOperand())) && (this.m_sortSpecs.equals(localAESort.m_sortSpecs));
  }
  
  public AESort copy()
  {
    return new AESort(this);
  }
  
  public int getColumnCount()
  {
    return this.m_accessibleColCount;
  }
  
  public IColumn getColumn(int paramInt)
  {
    if ((0 > paramInt) || (paramInt >= this.m_accessibleColCount)) {
      throw new IndexOutOfBoundsException();
    }
    return super.getColumn(paramInt);
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {
    Iterator localIterator = this.m_sortSpecs.iterator();
    while (localIterator.hasNext())
    {
      AESortSpec localAESortSpec = (AESortSpec)localIterator.next();
      assert ((getOperand() instanceof AEProject));
      getOperand().setDataNeeded(getOperand(), localAESortSpec.getColumnNumber());
    }
    getOperand().setDataNeededOnChild();
  }
  
  protected IAENode getChild(int paramInt)
  {
    if (0 != paramInt) {
      throw new IndexOutOfBoundsException("There is no child at index: " + paramInt);
    }
    return getOperand();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AESort.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
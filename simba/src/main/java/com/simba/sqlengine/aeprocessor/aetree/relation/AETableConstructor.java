package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataColumnInfo;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataUtilities;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AETableConstructor
  extends AERelationalExpr
{
  private final ICoercionHandler m_coercionHandler;
  private final List<AEValueExprList> m_rowConstructors;
  private List<? extends IColumn> m_metadata;
  private final boolean m_externalMetadata;
  
  public AETableConstructor(List<AEValueExprList> paramList, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    this(paramList, null, paramICoercionHandler);
  }
  
  public AETableConstructor(List<AEValueExprList> paramList, List<? extends IColumn> paramList1, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    if (0 == paramList.size()) {
      throw new IllegalArgumentException();
    }
    this.m_metadata = paramList1;
    this.m_rowConstructors = paramList;
    this.m_coercionHandler = paramICoercionHandler;
    if (null != paramList1)
    {
      this.m_externalMetadata = true;
      this.m_metadata = paramList1;
      validateMetadata();
    }
    else
    {
      this.m_externalMetadata = false;
      this.m_metadata = calculateMetadata(paramList, paramICoercionHandler);
    }
  }
  
  private AETableConstructor(AETableConstructor paramAETableConstructor)
  {
    super(paramAETableConstructor);
    this.m_coercionHandler = paramAETableConstructor.m_coercionHandler;
    this.m_rowConstructors = new ArrayList(paramAETableConstructor.m_rowConstructors);
    this.m_metadata = new ArrayList(paramAETableConstructor.m_metadata);
    this.m_externalMetadata = paramAETableConstructor.m_externalMetadata;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AETableConstructor copy()
  {
    return new AETableConstructor(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return Collections.unmodifiableList(this.m_rowConstructors).iterator();
  }
  
  public IColumn getColumn(int paramInt)
  {
    return (IColumn)this.m_metadata.get(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_metadata.size();
  }
  
  public int getNumChildren()
  {
    return this.m_rowConstructors.size();
  }
  
  public AEValueExprList getRow(int paramInt)
  {
    return (AEValueExprList)this.m_rowConstructors.get(paramInt);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AETableConstructor)) {
      return false;
    }
    AETableConstructor localAETableConstructor = (AETableConstructor)paramIAENode;
    if (this.m_rowConstructors.size() != localAETableConstructor.m_rowConstructors.size()) {
      return false;
    }
    for (int i = 0; i < this.m_rowConstructors.size(); i++) {
      if (!((AEValueExprList)this.m_rowConstructors.get(i)).isEquivalent((IAENode)localAETableConstructor.m_rowConstructors.get(i))) {
        return false;
      }
    }
    return true;
  }
  
  public void reprocessMetadata()
    throws ErrorException
  {
    if (this.m_externalMetadata) {
      validateMetadata();
    } else {
      this.m_metadata = calculateMetadata(this.m_rowConstructors, this.m_coercionHandler);
    }
  }
  
  public boolean getDataNeeded(int paramInt)
  {
    return true;
  }
  
  public int setDataNeeded(AERelationalExpr paramAERelationalExpr, int paramInt)
    throws ErrorException
  {
    return paramInt;
  }
  
  public void setDataNeededOnChild()
    throws ErrorException
  {}
  
  private void validateMetadata()
    throws ErrorException
  {
    assert (this.m_externalMetadata);
    assert (!this.m_rowConstructors.isEmpty());
    int i = this.m_metadata.size();
    Iterator localIterator = this.m_rowConstructors.iterator();
    while (localIterator.hasNext())
    {
      AEValueExprList localAEValueExprList = (AEValueExprList)localIterator.next();
      if (i != localAEValueExprList.getNumChildren()) {
        throw SQLEngineExceptionFactory.invalidAETreeException();
      }
      for (int j = 0; j < i; j++)
      {
        TypeMetadata localTypeMetadata1 = ((AEValueExpr)localAEValueExprList.getChild(j)).getTypeMetadata();
        TypeMetadata localTypeMetadata2 = ((IColumn)this.m_metadata.get(j)).getTypeMetadata();
        boolean bool = AEUtils.isConversionLegal(localTypeMetadata1.getType(), localTypeMetadata2.getType());
        if (!bool) {
          throw SQLEngineExceptionFactory.conversionNotSupported(localTypeMetadata1.getTypeName(), localTypeMetadata2.getTypeName());
        }
      }
    }
  }
  
  private static List<ColumnMetadata> calculateMetadata(List<AEValueExprList> paramList, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    Iterator localIterator1 = paramList.iterator();
    ArrayList localArrayList = new ArrayList();
    AEValueExprList localAEValueExprList = (AEValueExprList)localIterator1.next();
    int i = localAEValueExprList.getNumChildren();
    for (int j = 0; j < i; j++) {
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localAEValueExprList.getChild(j)));
    }
    Object localObject2;
    while (localIterator1.hasNext())
    {
      localObject1 = (AEValueExprList)localIterator1.next();
      if (((AEValueExprList)localObject1).getNumChildren() != i) {
        throw SQLEngineExceptionFactory.invalidAETreeException();
      }
      for (int k = 0; k < i; k++)
      {
        localObject2 = paramICoercionHandler.coerceUnionColumns((IColumnInfo)localArrayList.get(k), new AECoercionColumnInfo((AEValueExpr)((AEValueExprList)localObject1).getChild(k)));
        localArrayList.set(k, new MetadataColumnInfo((IColumn)localObject2, IColumnInfo.ColumnType.COLUMN));
      }
    }
    Object localObject1 = new ArrayList(localArrayList.size());
    Iterator localIterator2 = localArrayList.iterator();
    while (localIterator2.hasNext())
    {
      localObject2 = (IColumnInfo)localIterator2.next();
      ((List)localObject1).add(MetadataUtilities.createColumnMetadata((IColumnInfo)localObject2));
    }
    return (List<ColumnMetadata>)localObject1;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AETableConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
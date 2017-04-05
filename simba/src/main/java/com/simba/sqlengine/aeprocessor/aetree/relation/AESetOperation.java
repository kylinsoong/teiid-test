package com.simba.sqlengine.aeprocessor.aetree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public abstract class AESetOperation
  extends AEBinaryRelationalExpr
{
  private final boolean m_isAllOptPresent;
  
  protected AESetOperation(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, boolean paramBoolean)
  {
    super(paramAERelationalExpr1, paramAERelationalExpr2);
    this.m_isAllOptPresent = paramBoolean;
  }
  
  protected AESetOperation(AESetOperation paramAESetOperation)
  {
    super(paramAESetOperation);
    this.m_isAllOptPresent = paramAESetOperation.isAllOptPresent();
  }
  
  public abstract AESetOperation copy();
  
  public final boolean isAllOptPresent()
  {
    return this.m_isAllOptPresent;
  }
  
  protected static List<ColumnMetadata> calculateMetadata(AERelationalExpr paramAERelationalExpr1, AERelationalExpr paramAERelationalExpr2, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    int i = paramAERelationalExpr1.getColumnCount();
    if ((i <= 0) || (i != paramAERelationalExpr2.getColumnCount())) {
      throw SQLEngineExceptionFactory.invalidAETreeException();
    }
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++)
    {
      IColumn localIColumn = paramICoercionHandler.coerceUnionColumns(new MetadataColumnInfo(paramAERelationalExpr1.getColumn(j), IColumnInfo.ColumnType.COLUMN), new MetadataColumnInfo(paramAERelationalExpr2.getColumn(j), IColumnInfo.ColumnType.COLUMN));
      ColumnMetadata localColumnMetadata = ColumnMetadata.copyOf(localIColumn);
      localColumnMetadata.setName(paramAERelationalExpr1.getColumn(j).getName());
      localArrayList.add(localColumnMetadata);
    }
    return localArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/relation/AESetOperation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
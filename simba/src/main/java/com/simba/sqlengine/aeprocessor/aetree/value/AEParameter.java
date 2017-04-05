package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESort;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETop;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AEParameter
  extends AEValueExpr
{
  private int m_index;
  private long m_maxBinaryLiteralLen = 0L;
  private long m_maxLiteralCharLen = 0L;
  private ColumnMetadata m_colMetadata;
  private boolean m_metaHasBeenSet = false;
  private DataWrapper m_inputData;
  
  public AEParameter(int paramInt, SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    this.m_index = paramInt;
    try
    {
      this.m_maxBinaryLiteralLen = paramSqlDataEngineContext.getConnProperty(65).getLong();
      this.m_maxLiteralCharLen = paramSqlDataEngineContext.getConnProperty(67).getLong();
    }
    catch (NumericOverflowException localNumericOverflowException) {}catch (IncorrectTypeException localIncorrectTypeException) {}
  }
  
  protected AEParameter(AEParameter paramAEParameter)
  {
    this.m_index = paramAEParameter.m_index;
    this.m_maxBinaryLiteralLen = paramAEParameter.m_maxBinaryLiteralLen;
    this.m_maxLiteralCharLen = paramAEParameter.m_maxLiteralCharLen;
    this.m_inputData = paramAEParameter.m_inputData;
    this.m_metaHasBeenSet = paramAEParameter.m_metaHasBeenSet;
    if (null != paramAEParameter.m_colMetadata) {
      this.m_colMetadata = AEValueExpr.createColumnMetadata(paramAEParameter.m_colMetadata);
    }
  }
  
  private void initMetadata()
    throws ErrorException
  {
    IAENode localIAENode = getParent();
    Object localObject1;
    if ((localIAENode instanceof AETop))
    {
      localObject1 = (AETop)getParent();
      if (((AETop)localObject1).isPercent()) {
        this.m_colMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(8, true));
      } else {
        this.m_colMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-5, true));
      }
      this.m_colMetadata.setNullable(Nullable.NO_NULLS);
    }
    else
    {
      Object localObject2;
      if (((localIAENode instanceof AEValueExprList)) && (((localIAENode.getParent() instanceof AEScalarFn)) || ((localIAENode.getParent() instanceof AECustomScalarFn))))
      {
        localObject1 = (AEValueExprList)localIAENode;
        localObject2 = ((AEValueExprList)localObject1).getParent();
        int i = ((AEValueExprList)localObject1).findNode(this);
        if (i < 0) {
          throw SQLEngineExceptionFactory.invalidOperationException("Invalid AETree has been created and a valid path cannot be found.");
        }
        Object localObject3;
        if ((localObject2 instanceof AEScalarFn))
        {
          localObject3 = (AEScalarFn)localObject2;
          List localList = ((AEScalarFn)localObject3).getExpectedArgMetadata();
          this.m_colMetadata = ((ColumnMetadata)localList.get(i));
        }
        else
        {
          localObject3 = (AECustomScalarFn)localObject2;
          this.m_colMetadata = ColumnMetadata.copyOf((IColumn)((AECustomScalarFn)localObject3).getInputMetadata().get(i));
        }
        setMetadataDefaults(this.m_colMetadata);
      }
      else
      {
        localObject1 = null;
        if (null != localIAENode) {
          localObject1 = findSiblingMetadata(localIAENode, this);
        }
        if (null == localObject1)
        {
          this.m_colMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(0));
        }
        else
        {
          this.m_colMetadata = AEValueExpr.createColumnMetadata((IColumn)localObject1);
          localObject2 = this.m_colMetadata.getTypeMetadata();
          if (TypeUtilities.isCharacterType(((TypeMetadata)localObject2).getType())) {
            ((TypeMetadata)localObject2).setPrecision((short)(int)Math.max(((TypeMetadata)localObject2).getPrecision(), this.m_maxLiteralCharLen));
          } else if (TypeUtilities.isBinaryType(((TypeMetadata)localObject2).getType())) {
            ((TypeMetadata)localObject2).setPrecision((short)(int)Math.max(((TypeMetadata)localObject2).getPrecision(), this.m_maxBinaryLiteralLen));
          }
        }
        setMetadataDefaults(this.m_colMetadata);
      }
    }
  }
  
  private void setMetadataDefaults(ColumnMetadata paramColumnMetadata)
  {
    paramColumnMetadata.setNullable(Nullable.UNKNOWN);
    paramColumnMetadata.setSearchable(Searchable.SEARCHABLE);
    paramColumnMetadata.setAutoUnique(false);
    if (TypeUtilities.isNumberType(paramColumnMetadata.getTypeMetadata().getType())) {
      paramColumnMetadata.getTypeMetadata().setSigned(true);
    }
  }
  
  private IColumn findSiblingMetadata(IAENode paramIAENode1, IAENode paramIAENode2)
    throws ErrorException
  {
    assert ((null != paramIAENode1) && (null != paramIAENode2));
    Object localObject1;
    Object localObject2;
    switch (ParamAncestorType.getAncestorType(paramIAENode1))
    {
    case NEGATE: 
    case RENAME: 
      if (null != paramIAENode1.getParent()) {
        return findSiblingMetadata(paramIAENode1.getParent(), paramIAENode1);
      }
      return null;
    case BINARY_VALUE_EXPR: 
    case BOOLEAN_EXPR: 
      localObject1 = (IAEBinaryNode)paramIAENode1;
      if (paramIAENode2 == ((IAEBinaryNode)localObject1).getLeftOperand()) {
        localObject2 = ((IAEBinaryNode)localObject1).getRightOperand();
      } else {
        localObject2 = ((IAEBinaryNode)localObject1).getLeftOperand();
      }
      if ((localObject2 instanceof AEParameter)) {
        return null;
      }
      if ((localObject2 instanceof AEValueExprList)) {
        return findSiblingMetadata((IAENode)localObject2, paramIAENode1);
      }
      assert ((localObject2 instanceof AEValueExpr));
      return ((AEValueExpr)localObject2).getColumn();
    case VALUE_LIST: 
      localObject1 = ParamAncestorType.getAncestorType(paramIAENode1.getParent());
      if (ParamAncestorType.RELATION != localObject1)
      {
        localObject2 = paramIAENode1.getChildItr();
        while (((Iterator)localObject2).hasNext())
        {
          IAENode localIAENode = (IAENode)((Iterator)localObject2).next();
          if (!isParameter(localIAENode)) {
            return ((AEValueExpr)localIAENode).getColumn();
          }
        }
      }
      if ((paramIAENode1.getParent() != paramIAENode2) && (ParamAncestorType.BOOLEAN_EXPR == localObject1)) {
        return findSiblingMetadata(paramIAENode1.getParent(), paramIAENode1);
      }
      return null;
    }
    return null;
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return Collections.emptyList().iterator();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public int getIndex()
  {
    return this.m_index;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEParameter)) {
      return false;
    }
    return ((AEParameter)paramIAENode).m_index == this.m_index;
  }
  
  public IColumn getColumn()
  {
    if (!this.m_metaHasBeenSet) {
      return NullMetadataInstance.INSTANCE;
    }
    return this.m_colMetadata;
  }
  
  public IColumn getInferredOrSetColumn()
    throws ErrorException
  {
    if (null == this.m_colMetadata) {
      initMetadata();
    }
    return this.m_colMetadata;
  }
  
  public void setColumn(IColumn paramIColumn)
  {
    assert (null != paramIColumn);
    this.m_metaHasBeenSet = true;
    this.m_colMetadata = AEValueExpr.createColumnMetadata(paramIColumn);
  }
  
  public boolean hasBeenSet()
  {
    return this.m_metaHasBeenSet;
  }
  
  public void updateColumn()
    throws ErrorException
  {}
  
  public DataWrapper getInputData()
  {
    return this.m_inputData;
  }
  
  public void setInputData(DataWrapper paramDataWrapper)
  {
    this.m_inputData = paramDataWrapper;
  }
  
  public AEValueExpr copy()
  {
    return new AEParameter(this);
  }
  
  private boolean isParameter(IAENode paramIAENode)
  {
    return ((paramIAENode instanceof AEParameter)) || (((paramIAENode instanceof AENegate)) && ((((AENegate)paramIAENode).getOperand() instanceof AEParameter)));
  }
  
  private static enum ParamAncestorType
  {
    NEGATE,  RENAME,  BINARY_VALUE_EXPR,  BOOLEAN_EXPR,  VALUE_LIST,  RELATION,  TOP,  SORT,  NULL,  AGGREGATE_FN,  CASE;
    
    private ParamAncestorType() {}
    
    public static ParamAncestorType getAncestorType(IAENode paramIAENode)
      throws ErrorException
    {
      if (null == paramIAENode) {
        return NULL;
      }
      if ((paramIAENode instanceof AEBinaryValueExpr)) {
        return BINARY_VALUE_EXPR;
      }
      if ((paramIAENode instanceof AENegate)) {
        return NEGATE;
      }
      if (((paramIAENode instanceof AEComparison)) || ((paramIAENode instanceof AELikePredicate)) || ((paramIAENode instanceof AEInPredicate))) {
        return BOOLEAN_EXPR;
      }
      if ((paramIAENode instanceof AERename)) {
        return RENAME;
      }
      if (((paramIAENode instanceof AEProject)) || ((paramIAENode instanceof AESelect))) {
        return RELATION;
      }
      if ((paramIAENode instanceof AETop)) {
        return TOP;
      }
      if ((paramIAENode instanceof AESort)) {
        return SORT;
      }
      if ((paramIAENode instanceof AEValueExprList)) {
        return VALUE_LIST;
      }
      if ((paramIAENode instanceof AEAggrFn)) {
        return AGGREGATE_FN;
      }
      if (((paramIAENode instanceof AESimpleWhenClause)) || ((paramIAENode instanceof AESimpleCase)) || ((paramIAENode instanceof AESearchedWhenClause)) || ((paramIAENode instanceof AESearchedCase))) {
        return CASE;
      }
      throw SQLEngineExceptionFactory.featureNotImplementedException("Unknown ancestor of dynamic parameter node: " + paramIAENode.getClass().getName());
    }
  }
  
  private static class NullMetadataInstance
  {
    private static final ColumnMetadata INSTANCE;
    
    static
    {
      ColumnMetadata localColumnMetadata = null;
      try
      {
        localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(0));
      }
      catch (ErrorException localErrorException) {}finally
      {
        INSTANCE = localColumnMetadata;
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
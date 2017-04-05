package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEScalarFnMetadataFactory;
import com.simba.sqlengine.aeprocessor.aebuilder.value.AEScalarFnMetadataFactory.ScalarFnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.ScalarFunctionID;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AEScalarFn
  extends AEValueExpr
{
  private String m_scalarFnName;
  private ScalarFunctionID m_scalarFnId;
  private AEValueExprList m_arguments;
  private List<IColumn> m_expectedArgMetadata;
  private ColumnMetadata m_columnMeta;
  private final AEScalarFnMetadataFactory m_metadataHandler;
  private SqlDataEngineContext m_context;
  
  public AEScalarFn(String paramString, ScalarFunctionID paramScalarFunctionID, IColumn paramIColumn, AEValueExprList paramAEValueExprList, List<IColumn> paramList, AEScalarFnMetadataFactory paramAEScalarFnMetadataFactory, SqlDataEngineContext paramSqlDataEngineContext)
  {
    this.m_scalarFnName = paramString;
    this.m_scalarFnId = paramScalarFunctionID;
    this.m_columnMeta = createColumnMetadata(paramIColumn);
    this.m_columnMeta.setName(null);
    this.m_arguments = paramAEValueExprList;
    this.m_arguments.setParent(this);
    this.m_expectedArgMetadata = new ArrayList(paramList);
    this.m_metadataHandler = paramAEScalarFnMetadataFactory;
    this.m_context = paramSqlDataEngineContext;
  }
  
  public AEScalarFn(AEScalarFn paramAEScalarFn)
  {
    super(paramAEScalarFn);
    this.m_scalarFnName = paramAEScalarFn.m_scalarFnName;
    this.m_scalarFnId = paramAEScalarFn.m_scalarFnId;
    this.m_columnMeta = createColumnMetadata(paramAEScalarFn.m_columnMeta);
    this.m_arguments = paramAEScalarFn.m_arguments.copy();
    this.m_arguments.setParent(this);
    this.m_expectedArgMetadata = new ArrayList(paramAEScalarFn.m_expectedArgMetadata);
    this.m_metadataHandler = paramAEScalarFn.m_metadataHandler;
    this.m_context = paramAEScalarFn.m_context;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEScalarFn copy()
  {
    return new AEScalarFn(this);
  }
  
  public AEValueExprList getArguments()
  {
    return this.m_arguments;
  }
  
  public List<IColumn> getExpectedArgMetadata()
  {
    return Collections.unmodifiableList(this.m_expectedArgMetadata);
  }
  
  public String getScalarFnName()
  {
    return this.m_scalarFnName;
  }
  
  public ScalarFunctionID getScalarFnId()
  {
    return this.m_scalarFnId;
  }
  
  public Iterator<IAENode> getChildItr()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEScalarFn.this.getArguments();
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int size()
      {
        return 1;
      }
    }.iterator();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEScalarFn)) {
      return false;
    }
    AEScalarFn localAEScalarFn = (AEScalarFn)paramIAENode;
    return (this.m_scalarFnId == localAEScalarFn.m_scalarFnId) && (this.m_scalarFnName.equalsIgnoreCase(localAEScalarFn.m_scalarFnName)) && (this.m_arguments.isEquivalent(localAEScalarFn.m_arguments));
  }
  
  public IColumn getColumn()
  {
    return this.m_columnMeta;
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName() + ": " + this.m_scalarFnName;
  }
  
  public void updateColumn()
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.m_arguments.getChildItr();
    while (localIterator.hasNext()) {
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localIterator.next()));
    }
    AEScalarFnMetadataFactory.ScalarFnMetadata localScalarFnMetadata = this.m_metadataHandler.createMetadata(this.m_context, this.m_scalarFnId, this.m_scalarFnName, localArrayList);
    this.m_columnMeta = createColumnMetadata(localScalarFnMetadata.getColumnMetadata());
    this.m_columnMeta.setName(null);
    this.m_expectedArgMetadata = new ArrayList(localScalarFnMetadata.getExpectedArgumentMetadata());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEScalarFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
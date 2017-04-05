package com.simba.sqlengine.executor.materializer;

import com.simba.sqlengine.aeprocessor.aetree.relation.AEBinaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.executor.conversions.SqlConverterGenerator;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETResourceManager;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MaterializerContext
{
  private Map<AERelationalExpr, ETRelationalExpr> m_materializedRelationMap;
  private final SqlConverterGenerator m_converterGenerator;
  private final IWarningListener m_warningListener;
  private final SqlDataEngineContext m_dataEngineContext;
  private long m_sharedBytesToReserve = 0L;
  private ILogger m_log;
  private final ETCancelState m_cancelState;
  private Map<AENamedRelationalExpr, Pair<AEBinaryRelationalExpr, Integer>> m_joinMapping;
  private Map<Integer, ETParameter> m_parameters;
  private ExternalAlgorithmUtil.ExternalAlgorithmProperties m_extAlgoProp = null;
  private final ETResourceManager m_rManager;
  
  public MaterializerContext(SqlConverterGenerator paramSqlConverterGenerator, IWarningListener paramIWarningListener, SqlDataEngineContext paramSqlDataEngineContext, ETResourceManager paramETResourceManager, ILogger paramILogger)
  {
    this.m_converterGenerator = paramSqlConverterGenerator;
    this.m_warningListener = paramIWarningListener;
    this.m_materializedRelationMap = new HashMap();
    this.m_dataEngineContext = paramSqlDataEngineContext;
    this.m_joinMapping = new HashMap();
    this.m_parameters = new HashMap();
    this.m_log = paramILogger;
    this.m_cancelState = new ETCancelState();
    this.m_rManager = paramETResourceManager;
  }
  
  public SqlConverterGenerator getSqlConverterGenerator()
  {
    return this.m_converterGenerator;
  }
  
  public ILogger getLog()
  {
    return this.m_log;
  }
  
  public ETRelationalExpr getMaterializedRelation(AERelationalExpr paramAERelationalExpr)
  {
    return (ETRelationalExpr)this.m_materializedRelationMap.get(paramAERelationalExpr);
  }
  
  public Pair<AEBinaryRelationalExpr, Integer> resolveJoinRelation(AENamedRelationalExpr paramAENamedRelationalExpr)
  {
    return (Pair)this.m_joinMapping.get(paramAENamedRelationalExpr);
  }
  
  public SqlDataEngineContext getDataEngineContext()
  {
    return this.m_dataEngineContext;
  }
  
  public IWarningListener getWarningListener()
  {
    return this.m_warningListener;
  }
  
  public ETCancelState getCancelState()
  {
    return this.m_cancelState;
  }
  
  public ETResourceManager getResourceManager()
  {
    return this.m_rManager;
  }
  
  public void setMaterializedRelation(AERelationalExpr paramAERelationalExpr, ETRelationalExpr paramETRelationalExpr)
  {
    if (this.m_materializedRelationMap.containsKey(paramAERelationalExpr)) {
      throw new UnsupportedOperationException("Logic error - the key: " + paramAERelationalExpr + " already has a materialized relation.");
    }
    this.m_materializedRelationMap.put(paramAERelationalExpr, paramETRelationalExpr);
  }
  
  public void requireSharedBytes(long paramLong)
  {
    if (0L > paramLong) {
      throw new IllegalArgumentException();
    }
    if (paramLong > this.m_sharedBytesToReserve) {
      this.m_sharedBytesToReserve = paramLong;
    }
  }
  
  public void setJoinMapping(AENamedRelationalExpr paramAENamedRelationalExpr, AEBinaryRelationalExpr paramAEBinaryRelationalExpr, int paramInt)
  {
    if ((!(paramAEBinaryRelationalExpr instanceof AEJoin)) && (!(paramAEBinaryRelationalExpr instanceof AECrossJoin))) {
      throw new IllegalArgumentException("Join mapping is set with none join node.");
    }
    this.m_joinMapping.put(paramAENamedRelationalExpr, new Pair(paramAEBinaryRelationalExpr, Integer.valueOf(paramInt)));
  }
  
  public void updateMapping(AEBinaryRelationalExpr paramAEBinaryRelationalExpr1, AEBinaryRelationalExpr paramAEBinaryRelationalExpr2, int paramInt)
  {
    Iterator localIterator = this.m_joinMapping.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      AEBinaryRelationalExpr localAEBinaryRelationalExpr = (AEBinaryRelationalExpr)((Pair)localEntry.getValue()).key();
      if (localAEBinaryRelationalExpr == paramAEBinaryRelationalExpr1)
      {
        ((Pair)localEntry.getValue()).setKey(paramAEBinaryRelationalExpr2);
        ((Pair)localEntry.getValue()).setValue(Integer.valueOf(((Integer)((Pair)localEntry.getValue()).value()).intValue() + paramInt));
      }
    }
  }
  
  public void registerParameter(int paramInt, ETParameter paramETParameter)
  {
    if (this.m_parameters.containsKey(Integer.valueOf(paramInt))) {
      throw new IllegalArgumentException("Duplicate parameter index.");
    }
    this.m_parameters.put(Integer.valueOf(paramInt), paramETParameter);
  }
  
  public Map<Integer, ETParameter> getParameters()
  {
    return this.m_parameters;
  }
  
  public ExternalAlgorithmUtil.ExternalAlgorithmProperties getExternalAlgorithmProperties()
    throws ErrorException
  {
    if (this.m_extAlgoProp == null) {
      this.m_extAlgoProp = ExternalAlgorithmUtil.createProperties(this.m_dataEngineContext.getDataEngine(), this.m_log, this.m_warningListener, this.m_rManager);
    }
    return this.m_extAlgoProp;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/MaterializerContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
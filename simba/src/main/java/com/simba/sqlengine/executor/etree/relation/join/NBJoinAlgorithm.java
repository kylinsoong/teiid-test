package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.ILogger;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

public class NBJoinAlgorithm
  extends AbstractJoinAlgorithmAdaper
{
  private static final String PACKAGE_NAME = "com.simba.sqlengine.executor.etree.relation.join";
  private static final String CLASS_NAME = "NBJoinAlgorithm";
  private ETRelationalExpr m_slaveRelation;
  private InMemJoinUnit m_slaveUnit;
  private RelationWrapperJoinUnit m_masterUnit;
  private final boolean m_isMasterOnLeft;
  private boolean m_haveMoreSlaveRows;
  private boolean[] m_slaveDataNeeded;
  private IMemManagerAgent m_memManagerAgent;
  private long m_memAvailable;
  private boolean m_firstUnitsLoaded;
  private ILogger m_logger;
  private final ETCancelState m_cancelState;
  
  public NBJoinAlgorithm(ETRelationalExpr paramETRelationalExpr1, ETRelationalExpr paramETRelationalExpr2, AEJoin.AEJoinType paramAEJoinType, ExternalAlgorithmUtil.ExternalAlgorithmProperties paramExternalAlgorithmProperties, ETCancelState paramETCancelState, ILogger paramILogger)
    throws ErrorException
  {
    super(paramAEJoinType);
    this.m_logger = paramILogger;
    ETRelationalExpr localETRelationalExpr = null;
    if (paramAEJoinType == AEJoin.AEJoinType.RIGHT_OUTER_JOIN)
    {
      this.m_isMasterOnLeft = true;
      localETRelationalExpr = paramETRelationalExpr1;
      this.m_slaveRelation = paramETRelationalExpr2;
    }
    else
    {
      this.m_isMasterOnLeft = false;
      localETRelationalExpr = paramETRelationalExpr2;
      this.m_slaveRelation = paramETRelationalExpr1;
    }
    IColumn[] arrayOfIColumn = new IColumn[this.m_slaveRelation.getColumnCount()];
    for (int i = 0; i < arrayOfIColumn.length; i++) {
      arrayOfIColumn[i] = this.m_slaveRelation.getColumn(i);
    }
    this.m_cancelState = paramETCancelState;
    this.m_slaveDataNeeded = new boolean[this.m_slaveRelation.getColumnCount()];
    for (i = 0; i < this.m_slaveRelation.getColumnCount(); i++) {
      if (this.m_slaveRelation.getDataNeeded(i)) {
        this.m_slaveDataNeeded[i] = true;
      }
    }
    this.m_slaveUnit = new InMemJoinUnit(arrayOfIColumn, paramExternalAlgorithmProperties, this.m_slaveDataNeeded);
    this.m_haveMoreSlaveRows = true;
    this.m_masterUnit = new RelationWrapperJoinUnit(localETRelationalExpr, paramExternalAlgorithmProperties, this.m_joinType == AEJoin.AEJoinType.FULL_OUTER_JOIN);
    this.m_firstUnitsLoaded = false;
  }
  
  public boolean moveMasterUnmatch()
    throws ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "moveMasterUnmatch");
    }
    if ((!this.m_haveMoreSlaveRows) && (this.m_masterUnit.isCurRowUnmatched())) {
      return super.moveMasterUnmatch();
    }
    return this.m_masterUnit.moveToNextRow();
  }
  
  private void loadSlaveUnit()
    throws ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "loadSlaveUnit");
    }
    if (!this.m_slaveUnit.canAppendRow()) {
      throw new IllegalStateException("Not enough memory assigned.");
    }
    this.m_cancelState.checkCancel();
    while ((this.m_slaveUnit.canAppendRow()) && (this.m_haveMoreSlaveRows))
    {
      this.m_slaveUnit.appendRow(this.m_slaveRelation);
      this.m_haveMoreSlaveRows = this.m_slaveRelation.move();
    }
  }
  
  public boolean isMasterJoinUnitOnLeft()
  {
    return this.m_isMasterOnLeft;
  }
  
  public void closeRelations()
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "closeRelations");
    }
    this.m_slaveUnit.close();
    this.m_slaveRelation.close();
    this.m_masterUnit.closeRelation();
    this.m_memManagerAgent.recycleMemory(this.m_memAvailable);
    this.m_memAvailable = 0L;
    this.m_memManagerAgent.unregisterConsumer();
  }
  
  public void reset()
    throws ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "reset");
    }
    this.m_slaveRelation.reset();
    this.m_haveMoreSlaveRows = this.m_slaveRelation.move();
    this.m_masterUnit.reset();
    this.m_slaveUnit.reset();
  }
  
  public Pair<ISlaveJoinUnit, IMasterJoinUnit> loadMasterSlave()
    throws ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "loadMasterSlave");
    }
    if (((this.m_firstUnitsLoaded) && (!this.m_haveMoreSlaveRows)) || ((!this.m_firstUnitsLoaded) && (this.m_joinType != AEJoin.AEJoinType.FULL_OUTER_JOIN) && (!this.m_haveMoreSlaveRows)))
    {
      if (null != this.m_logger) {
        this.m_logger.logTrace("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "loadMasterSlave", "No more join units.");
      }
      return null;
    }
    if (this.m_firstUnitsLoaded)
    {
      this.m_slaveUnit.reset();
      this.m_masterUnit.resetRelation();
    }
    else
    {
      this.m_firstUnitsLoaded = true;
    }
    loadSlaveUnit();
    if (!this.m_haveMoreSlaveRows)
    {
      long l = this.m_slaveUnit.reduceMemoryUsage();
      if (l > 0L)
      {
        this.m_memManagerAgent.recycleMemory(l);
        this.m_memAvailable -= l;
      }
    }
    return new Pair(this.m_slaveUnit, this.m_masterUnit);
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_memManagerAgent = paramIMemManagerAgent;
  }
  
  public long assign(long paramLong)
  {
    long l = this.m_masterUnit.assignMemory(paramLong);
    paramLong -= l;
    if (paramLong < 0L) {
      throw new IllegalArgumentException("Master unit take more memory than assigned.");
    }
    if ((paramLong > 0L) && (this.m_haveMoreSlaveRows))
    {
      this.m_slaveUnit.assignMemory(paramLong);
      l += paramLong;
    }
    this.m_memAvailable += l;
    if (null != this.m_logger) {
      this.m_logger.logTrace("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "assign", "Memory assigned: " + l);
    }
    return l;
  }
  
  public long getRequiredMemory()
  {
    return this.m_slaveUnit.getRequiredMemory() + this.m_masterUnit.getRequiredMemory();
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    if (null != this.m_logger) {
      this.m_logger.logFunctionEntrance("com.simba.sqlengine.executor.etree.relation.join", "NBJoinAlgorithm", "open");
    }
    this.m_slaveRelation.open(CursorType.FORWARD_ONLY);
    this.m_haveMoreSlaveRows = this.m_slaveRelation.move();
    this.m_masterUnit.openRelation();
    this.m_firstUnitsLoaded = false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/NBJoinAlgorithm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
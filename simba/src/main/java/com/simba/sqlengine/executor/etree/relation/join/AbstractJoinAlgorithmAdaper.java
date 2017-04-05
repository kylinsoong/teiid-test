package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

abstract class AbstractJoinAlgorithmAdaper
  implements IJoinAlgorithmAdapter
{
  protected boolean m_masterMatch;
  protected boolean m_outputMasterOuter;
  protected boolean m_outputSlaveOuter;
  protected AEJoin.AEJoinType m_joinType;
  private ISlaveJoinUnit m_slaveUnit;
  private IMasterJoinUnit m_masterJoinUnit;
  private boolean m_isMasterOnLeft;
  
  protected AbstractJoinAlgorithmAdaper(AEJoin.AEJoinType paramAEJoinType)
  {
    this.m_joinType = paramAEJoinType;
    this.m_outputMasterOuter = false;
    this.m_outputSlaveOuter = false;
    this.m_masterMatch = true;
  }
  
  public void match()
    throws ErrorException
  {
    this.m_masterMatch = true;
    this.m_slaveUnit.match();
    this.m_masterJoinUnit.match();
  }
  
  public boolean isOuterRow()
  {
    return (this.m_outputMasterOuter) || (this.m_outputSlaveOuter);
  }
  
  public boolean moveMaster()
    throws ErrorException
  {
    this.m_outputMasterOuter = false;
    boolean bool;
    if ((this.m_masterMatch) || (!shouldOutputMasterOuter()))
    {
      bool = this.m_masterJoinUnit.moveToNextRow();
    }
    else
    {
      bool = moveMasterUnmatch();
      this.m_masterMatch = true;
    }
    if (bool) {
      return true;
    }
    if ((shouldOutputSlaveOuter()) && (this.m_slaveUnit.hasOuterRows()))
    {
      this.m_outputSlaveOuter = true;
      this.m_slaveUnit.setOutputOuter();
      return false;
    }
    return false;
  }
  
  public boolean moveSlave()
    throws ErrorException
  {
    if (this.m_outputSlaveOuter) {
      return this.m_slaveUnit.moveOuter();
    }
    return this.m_slaveUnit.moveToNextRow();
  }
  
  public void seekSlave()
  {
    this.m_masterMatch = false;
    this.m_slaveUnit.seek(this.m_masterJoinUnit.getRow());
  }
  
  public Pair<? extends IJoinUnit, ? extends IJoinUnit> loadNextJoinUnit()
    throws ErrorException
  {
    this.m_masterMatch = true;
    this.m_outputMasterOuter = false;
    this.m_outputSlaveOuter = false;
    Pair localPair = loadMasterSlave();
    if (localPair == null) {
      return null;
    }
    this.m_masterJoinUnit = ((IMasterJoinUnit)localPair.value());
    this.m_slaveUnit = ((ISlaveJoinUnit)localPair.key());
    this.m_isMasterOnLeft = isMasterJoinUnitOnLeft();
    if (!isMasterJoinUnitOnLeft()) {
      return localPair;
    }
    return new Pair(localPair.value(), localPair.key());
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener) {}
  
  public abstract Pair<ISlaveJoinUnit, IMasterJoinUnit> loadMasterSlave()
    throws ErrorException;
  
  public boolean moveMasterUnmatch()
    throws ErrorException
  {
    this.m_outputMasterOuter = true;
    return true;
  }
  
  private boolean shouldOutputMasterOuter()
  {
    return (this.m_joinType != AEJoin.AEJoinType.INNER_JOIN) && ((this.m_joinType != AEJoin.AEJoinType.LEFT_OUTER_JOIN) || (this.m_isMasterOnLeft)) && ((this.m_joinType != AEJoin.AEJoinType.RIGHT_OUTER_JOIN) || (!this.m_isMasterOnLeft));
  }
  
  private boolean shouldOutputSlaveOuter()
  {
    return (this.m_joinType != AEJoin.AEJoinType.INNER_JOIN) && ((this.m_joinType != AEJoin.AEJoinType.LEFT_OUTER_JOIN) || (!this.m_isMasterOnLeft)) && ((this.m_joinType != AEJoin.AEJoinType.RIGHT_OUTER_JOIN) || (this.m_isMasterOnLeft));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/AbstractJoinAlgorithmAdaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
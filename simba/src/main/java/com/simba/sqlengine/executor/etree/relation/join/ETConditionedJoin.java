package com.simba.sqlengine.executor.etree.relation.join;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.IWarningSource;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETCancelState;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.IMemManagerAgent;
import com.simba.sqlengine.executor.etree.IMemoryConsumer;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.bool.ETTrue;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;

public class ETConditionedJoin
  extends ETRelationalExpr
  implements IMemoryConsumer, IWarningSource
{
  private ETBooleanExpr m_joinCondition;
  private ETJoinedUnitWrapper m_leftJoinedUnitWrapper;
  private ETJoinedUnitWrapper m_rightJoinedUnitWrapper;
  private IJoinUnit m_leftUnit;
  private IJoinUnit m_rightUnit;
  private boolean m_isOpen;
  private ETRelationalExpr m_leftRelation;
  private ETRelationalExpr m_rightRelation;
  private final IJoinAlgorithmAdapter m_joinAlgorithm;
  private State m_state;
  private final ETCancelState m_cancelState;
  
  private ETConditionedJoin(ETBooleanExpr paramETBooleanExpr, ETJoinedUnitWrapper paramETJoinedUnitWrapper1, ETJoinedUnitWrapper paramETJoinedUnitWrapper2, ETRelationalExpr paramETRelationalExpr1, ETRelationalExpr paramETRelationalExpr2, IJoinAlgorithmAdapter paramIJoinAlgorithmAdapter, ETCancelState paramETCancelState, boolean[] paramArrayOfBoolean)
  {
    super(paramArrayOfBoolean);
    this.m_joinCondition = paramETBooleanExpr;
    this.m_leftJoinedUnitWrapper = paramETJoinedUnitWrapper1;
    this.m_rightJoinedUnitWrapper = paramETJoinedUnitWrapper2;
    this.m_leftRelation = paramETRelationalExpr1;
    this.m_rightRelation = paramETRelationalExpr2;
    this.m_joinAlgorithm = paramIJoinAlgorithmAdapter;
    this.m_isOpen = false;
    this.m_cancelState = paramETCancelState;
    initialize();
  }
  
  private void initialize()
  {
    this.m_leftUnit = null;
    this.m_rightUnit = null;
    this.m_state = null;
  }
  
  public void close()
  {
    this.m_joinAlgorithm.closeRelations();
    if (null != this.m_leftJoinedUnitWrapper) {
      this.m_leftJoinedUnitWrapper.close();
    }
    if (null != this.m_rightJoinedUnitWrapper) {
      this.m_rightJoinedUnitWrapper.close();
    }
    this.m_isOpen = false;
    this.m_state = null;
  }
  
  public boolean isOpen()
  {
    return this.m_isOpen;
  }
  
  public void reset()
    throws ErrorException
  {
    try
    {
      this.m_joinAlgorithm.reset();
      initialize();
      this.m_state = State.UNINITIALIZED;
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 3;
  }
  
  public IColumn getColumn(int paramInt)
  {
    if (paramInt >= this.m_leftRelation.getColumnCount()) {
      return this.m_rightRelation.getColumn(paramInt - this.m_leftRelation.getColumnCount());
    }
    return this.m_leftRelation.getColumn(paramInt);
  }
  
  public int getColumnCount()
  {
    return this.m_leftRelation.getColumnCount() + this.m_rightRelation.getColumnCount();
  }
  
  public long getRowCount()
  {
    return -1L;
  }
  
  public void open(CursorType paramCursorType)
    throws ErrorException
  {
    try
    {
      this.m_state = State.UNINITIALIZED;
      this.m_joinAlgorithm.open(paramCursorType);
      this.m_isOpen = true;
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
  }
  
  public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    try
    {
      if ((this.m_leftUnit == null) || (this.m_rightUnit == null)) {
        throw new IllegalStateException("Retrieving while no data left.");
      }
      int i = paramInt < this.m_leftRelation.getColumnCount() ? 1 : 0;
      int j = i != 0 ? paramInt : paramInt - this.m_leftRelation.getColumnCount();
      if (this.m_state == State.JOINING)
      {
        if (i != 0) {
          return this.m_leftUnit.retrieveData(paramInt, paramETDataRequest);
        }
        return this.m_rightUnit.retrieveData(j, paramETDataRequest);
      }
      if (this.m_state == State.MASTER_OUTER)
      {
        k = i == this.m_joinAlgorithm.isMasterJoinUnitOnLeft() ? 1 : 0;
        if (k != 0) {
          return getMasterUnit().retrieveData(j, paramETDataRequest);
        }
        paramETDataRequest.getData().setNull();
        return false;
      }
      int k = i != this.m_joinAlgorithm.isMasterJoinUnitOnLeft() ? 1 : 0;
      if (k != 0) {
        return getSlaveUnit().retrieveData(j, paramETDataRequest);
      }
      paramETDataRequest.getData().setNull();
      return false;
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
    catch (Throwable localThrowable)
    {
      close();
      throw SQLEngineExceptionFactory.convertRuntimeException(localThrowable);
    }
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    if ((paramInt < 0) || (paramInt >= 3)) {
      throw new IndexOutOfBoundsException("index: " + paramInt);
    }
    if (paramInt == 0) {
      return this.m_leftRelation;
    }
    if (paramInt == 1) {
      return this.m_rightRelation;
    }
    return this.m_joinCondition;
  }
  
  protected boolean doMove()
    throws ErrorException
  {
    try
    {
      for (;;)
      {
        switch (this.m_state)
        {
        case UNINITIALIZED: 
          this.m_cancelState.checkCancel();
          Pair localPair = this.m_joinAlgorithm.loadNextJoinUnit();
          if (localPair == null) {
            return false;
          }
          this.m_leftUnit = ((IJoinUnit)localPair.key());
          this.m_rightUnit = ((IJoinUnit)localPair.value());
          this.m_leftJoinedUnitWrapper.setJoinUnit(this.m_leftUnit);
          this.m_rightJoinedUnitWrapper.setJoinUnit(this.m_rightUnit);
          this.m_state = State.UNIT_LOADED;
          break;
        case UNIT_LOADED: 
        case MASTER_OUTER: 
          boolean bool1 = this.m_joinAlgorithm.moveMaster();
          boolean bool2 = this.m_joinAlgorithm.isOuterRow();
          if (bool1)
          {
            if (bool2)
            {
              this.m_state = State.MASTER_OUTER;
              return true;
            }
            this.m_state = State.JOINING;
            this.m_joinAlgorithm.seekSlave();
          }
          else if (bool2)
          {
            this.m_state = State.SLAVE_OUTER;
          }
          else
          {
            this.m_leftUnit.close();
            this.m_rightUnit.close();
            this.m_leftUnit = null;
            this.m_rightUnit = null;
            this.m_state = State.UNINITIALIZED;
          }
          break;
        case JOINING: 
          while (this.m_joinAlgorithm.moveSlave()) {
            if (this.m_joinCondition.evaluate() == ETBoolean.SQL_BOOLEAN_TRUE)
            {
              this.m_joinAlgorithm.match();
              return true;
            }
          }
          this.m_state = State.UNIT_LOADED;
          break;
        case SLAVE_OUTER: 
          if (this.m_joinAlgorithm.moveSlave())
          {
            assert (this.m_joinAlgorithm.isOuterRow());
            return true;
          }
          this.m_leftUnit.close();
          this.m_rightUnit.close();
          this.m_leftUnit = null;
          this.m_rightUnit = null;
          this.m_state = State.UNINITIALIZED;
        }
      }
      throw new IllegalStateException("Unknow state.");
    }
    catch (ErrorException localErrorException)
    {
      close();
      throw localErrorException;
    }
    catch (Throwable localThrowable)
    {
      close();
      throw SQLEngineExceptionFactory.convertRuntimeException(localThrowable);
    }
  }
  
  private IJoinUnit getMasterUnit()
  {
    if (this.m_joinAlgorithm.isMasterJoinUnitOnLeft()) {
      return this.m_leftUnit;
    }
    return this.m_rightUnit;
  }
  
  private IJoinUnit getSlaveUnit()
  {
    if (this.m_joinAlgorithm.isMasterJoinUnitOnLeft()) {
      return this.m_rightUnit;
    }
    return this.m_leftUnit;
  }
  
  public void registerManagerAgent(IMemManagerAgent paramIMemManagerAgent)
  {
    this.m_joinAlgorithm.registerManagerAgent(paramIMemManagerAgent);
  }
  
  public long assign(long paramLong)
  {
    return this.m_joinAlgorithm.assign(paramLong);
  }
  
  public long getRequiredMemory()
  {
    return this.m_joinAlgorithm.getRequiredMemory();
  }
  
  public void registerWarningListener(IWarningListener paramIWarningListener)
  {
    this.m_joinAlgorithm.registerWarningListener(paramIWarningListener);
  }
  
  private static enum State
  {
    UNINITIALIZED,  UNIT_LOADED,  JOINING,  SLAVE_OUTER,  MASTER_OUTER;
    
    private State() {}
  }
  
  public static class Builder
  {
    ETBooleanExpr m_joinCondition;
    ETJoinedUnitWrapper m_leftJoinedUnitWrapper;
    ETJoinedUnitWrapper m_rightJoinedUnitWrapper;
    ETRelationalExpr m_leftRelation;
    ETRelationalExpr m_rightRelation;
    IJoinAlgorithmAdapter m_joinAlgorithm;
    private boolean[] m_dataNeeded;
    private final ETCancelState m_cancelState;
    
    public Builder(ETRelationalExpr paramETRelationalExpr1, ETRelationalExpr paramETRelationalExpr2, ETCancelState paramETCancelState, boolean[] paramArrayOfBoolean)
    {
      if ((paramETRelationalExpr1 == null) || (paramETRelationalExpr2 == null) || (paramETCancelState == null)) {
        throw new NullPointerException("Null set for the ETConditionedJoin builer.");
      }
      this.m_leftRelation = paramETRelationalExpr1;
      this.m_rightRelation = paramETRelationalExpr2;
      boolean[] arrayOfBoolean1 = new boolean[this.m_leftRelation.getColumnCount()];
      for (int i = 0; i < arrayOfBoolean1.length; i++) {
        if ((this.m_leftRelation.getDataNeeded(i)) || (paramArrayOfBoolean[i] != 0)) {
          arrayOfBoolean1[i] = true;
        }
      }
      boolean[] arrayOfBoolean2 = new boolean[this.m_rightRelation.getColumnCount()];
      for (int j = 0; j < arrayOfBoolean2.length; j++) {
        if ((this.m_rightRelation.getDataNeeded(j)) || (paramArrayOfBoolean[(j + this.m_leftRelation.getColumnCount())] != 0)) {
          arrayOfBoolean2[j] = true;
        }
      }
      this.m_leftJoinedUnitWrapper = new ETJoinedUnitWrapper(paramETRelationalExpr1, arrayOfBoolean1);
      this.m_rightJoinedUnitWrapper = new ETJoinedUnitWrapper(paramETRelationalExpr2, arrayOfBoolean2);
      this.m_joinCondition = new ETTrue();
      this.m_dataNeeded = ((boolean[])paramArrayOfBoolean.clone());
      this.m_cancelState = paramETCancelState;
    }
    
    public ETRelationalExpr getLeftRelationWrapper()
    {
      return this.m_leftJoinedUnitWrapper;
    }
    
    public ETRelationalExpr getRightRelationWrapper()
    {
      return this.m_rightJoinedUnitWrapper;
    }
    
    public void setJoinAlgorithm(IJoinAlgorithmAdapter paramIJoinAlgorithmAdapter)
    {
      this.m_joinAlgorithm = paramIJoinAlgorithmAdapter;
    }
    
    public void setJoinCondition(ETBooleanExpr paramETBooleanExpr)
    {
      if (paramETBooleanExpr != null) {
        this.m_joinCondition = paramETBooleanExpr;
      }
    }
    
    public ETConditionedJoin build()
    {
      if (this.m_joinAlgorithm == null) {
        throw new NullPointerException("Join algorithm can not be null.");
      }
      return new ETConditionedJoin(this.m_joinCondition, this.m_leftJoinedUnitWrapper, this.m_rightJoinedUnitWrapper, this.m_leftRelation, this.m_rightRelation, this.m_joinAlgorithm, this.m_cancelState, this.m_dataNeeded, null);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/ETConditionedJoin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
package com.simba.sqlengine.aeprocessor.aemanipulator;

import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker.Action;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.support.exceptions.ErrorException;

public final class AEDeMorgansProcessor
{
  private AEDeMorgansProcessor()
  {
    throw new UnsupportedOperationException("Not instantiable");
  }
  
  public static void apply(SqlDataEngineContext paramSqlDataEngineContext, IAENode paramIAENode)
    throws ErrorException
  {
    AETreeWalker.walk(paramIAENode, new AETreeWalker.Action()
    {
      private final AEDeMorgansProcessor.AEDeMorgansVisitor m_deMorgans = new AEDeMorgansProcessor.AEDeMorgansVisitor(this.val$context);
      
      public void act(IAENode paramAnonymousIAENode)
        throws ErrorException
      {
        if ((paramAnonymousIAENode instanceof AEBooleanExpr))
        {
          skipChildren();
        }
        else
        {
          Object localObject;
          if ((paramAnonymousIAENode instanceof AEJoin))
          {
            localObject = (AEJoin)paramAnonymousIAENode;
            ((AEJoin)localObject).setJoinCondition((AEBooleanExpr)((AEJoin)localObject).getJoinCondition().acceptVisitor(this.m_deMorgans));
          }
          else if ((paramAnonymousIAENode instanceof AESelect))
          {
            localObject = (AESelect)paramAnonymousIAENode;
            ((AESelect)localObject).setSelectCond((AEBooleanExpr)((AESelect)localObject).getCondition().acceptVisitor(this.m_deMorgans));
          }
        }
      }
    });
  }
  
  static final class AENegateVisitor
    extends AEDefaultVisitor<AEBooleanExpr>
  {
    private SqlDataEngineContext m_context;
    private AEDeMorgansProcessor.AEDeMorgansVisitor m_deMorgans;
    
    public AENegateVisitor(AEDeMorgansProcessor.AEDeMorgansVisitor paramAEDeMorgansVisitor, SqlDataEngineContext paramSqlDataEngineContext)
    {
      this.m_deMorgans = paramAEDeMorgansVisitor;
      this.m_context = paramSqlDataEngineContext;
    }
    
    public AEBooleanExpr defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      if ((paramIAENode.getParent() instanceof AENot)) {
        return (AENot)paramIAENode.getParent();
      }
      if ((paramIAENode instanceof AEBooleanExpr)) {
        return new AENot((AEBooleanExpr)paramIAENode);
      }
      throw new IllegalArgumentException(paramIAENode.toString());
    }
    
    public AEBooleanExpr visit(AENot paramAENot)
      throws ErrorException
    {
      AEBooleanExpr localAEBooleanExpr = paramAENot.getOperand();
      if ((localAEBooleanExpr instanceof AENot)) {
        return negate(((AENot)localAEBooleanExpr).getOperand());
      }
      return (AEBooleanExpr)localAEBooleanExpr.acceptVisitor(this.m_deMorgans);
    }
    
    public AEBooleanExpr visit(AEAnd paramAEAnd)
      throws ErrorException
    {
      return new AEOr(negate(paramAEAnd.getLeftOperand()), negate(paramAEAnd.getRightOperand()));
    }
    
    public AEBooleanExpr visit(AEOr paramAEOr)
      throws ErrorException
    {
      return new AEAnd(negate(paramAEOr.getLeftOperand()), negate(paramAEOr.getRightOperand()));
    }
    
    public AEBooleanExpr visit(AEComparison paramAEComparison)
      throws ErrorException
    {
      return new AEComparison(this.m_context, paramAEComparison.getComparisonOp().complement(), paramAEComparison.getLeftOperand(), paramAEComparison.getRightOperand());
    }
    
    public AEBooleanExpr negate(AEBooleanExpr paramAEBooleanExpr)
      throws ErrorException
    {
      return (AEBooleanExpr)paramAEBooleanExpr.acceptVisitor(this);
    }
  }
  
  static final class AEDeMorgansVisitor
    extends AEDefaultVisitor<AEBooleanExpr>
  {
    private final AEDeMorgansProcessor.AENegateVisitor m_negateVisitor;
    
    public AEDeMorgansVisitor(SqlDataEngineContext paramSqlDataEngineContext)
    {
      this.m_negateVisitor = new AEDeMorgansProcessor.AENegateVisitor(this, paramSqlDataEngineContext);
    }
    
    public AEBooleanExpr visit(AEAnd paramAEAnd)
      throws ErrorException
    {
      AEBooleanExpr localAEBooleanExpr1 = (AEBooleanExpr)paramAEAnd.getLeftOperand().acceptVisitor(this);
      AEBooleanExpr localAEBooleanExpr2 = (AEBooleanExpr)paramAEAnd.getRightOperand().acceptVisitor(this);
      if (paramAEAnd.getLeftOperand() != localAEBooleanExpr1) {
        paramAEAnd.setLeftOperand(localAEBooleanExpr1);
      }
      if (paramAEAnd.getRightOperand() != localAEBooleanExpr2) {
        paramAEAnd.setRightOperand(localAEBooleanExpr2);
      }
      return paramAEAnd;
    }
    
    public AEBooleanExpr visit(AEOr paramAEOr)
      throws ErrorException
    {
      AEBooleanExpr localAEBooleanExpr1 = (AEBooleanExpr)paramAEOr.getLeftOperand().acceptVisitor(this);
      AEBooleanExpr localAEBooleanExpr2 = (AEBooleanExpr)paramAEOr.getRightOperand().acceptVisitor(this);
      if (paramAEOr.getLeftOperand() != localAEBooleanExpr1) {
        paramAEOr.setLeftOperand(localAEBooleanExpr1);
      }
      if (paramAEOr.getRightOperand() != localAEBooleanExpr2) {
        paramAEOr.setRightOperand(localAEBooleanExpr2);
      }
      return paramAEOr;
    }
    
    public AEBooleanExpr visit(AENot paramAENot)
      throws ErrorException
    {
      return (AEBooleanExpr)paramAENot.getOperand().acceptVisitor(this.m_negateVisitor);
    }
    
    protected AEBooleanExpr defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      if ((paramIAENode instanceof AEBooleanExpr)) {
        return (AEBooleanExpr)paramIAENode;
      }
      throw new IllegalArgumentException(paramIAENode.toString());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aemanipulator/AEDeMorgansProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
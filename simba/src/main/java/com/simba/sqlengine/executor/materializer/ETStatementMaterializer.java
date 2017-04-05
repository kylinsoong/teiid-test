package com.simba.sqlengine.executor.materializer;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AECreateTable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDelete;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDropTable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsert;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsertDefaults;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEQuery;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEUpdate;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.exceptions.SQLEngineMemoryException;
import com.simba.sqlengine.executor.IStatementExecutor;
import com.simba.sqlengine.executor.StatementExecutor;
import com.simba.sqlengine.executor.conversions.SqlConverterGenerator;
import com.simba.sqlengine.executor.etree.ETMemoryManager;
import com.simba.sqlengine.executor.etree.ETResourceManager;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.statement.ETQuery;
import com.simba.sqlengine.executor.etree.statement.ETSearchedDelete;
import com.simba.sqlengine.executor.etree.statement.IETStatement;
import com.simba.sqlengine.executor.etree.statement.RowCountStatement;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.InvalidOperationException;

public class ETStatementMaterializer
  extends AEDefaultVisitor<IETStatement>
  implements IStatementMaterializer
{
  private IQueryPlan m_plan;
  private MaterializerContext m_materializerContext;
  
  public ETStatementMaterializer(SqlConverterGenerator paramSqlConverterGenerator, IWarningListener paramIWarningListener, SqlDataEngineContext paramSqlDataEngineContext, ILogger paramILogger)
  {
    this.m_materializerContext = new MaterializerContext(paramSqlConverterGenerator, paramIWarningListener, paramSqlDataEngineContext, new ETResourceManager(paramILogger), paramILogger);
  }
  
  public IStatementExecutor materialize(IQueryPlan paramIQueryPlan)
    throws ErrorException
  {
    this.m_plan = paramIQueryPlan;
    IETStatement localIETStatement = null;
    try
    {
      IAEStatement localIAEStatement = paramIQueryPlan.getAETree();
      localIAEStatement.notifyDataNeeded();
      localIETStatement = (IETStatement)localIAEStatement.acceptVisitor(this);
    }
    catch (StackOverflowError localStackOverflowError)
    {
      throw new SQLEngineMemoryException(SQLEngineMessageKey.STACK_OVERFLOW.name());
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      throw new SQLEngineMemoryException(SQLEngineMessageKey.OUT_OF_MEMORY.name());
    }
    catch (RuntimeException localRuntimeException)
    {
      throw new InvalidOperationException(7, SQLEngineMessageKey.INVALID_OPERATION.name(), new String[] { localRuntimeException.getLocalizedMessage() }, localRuntimeException);
    }
    ETMemoryManager localETMemoryManager = ETMemoryManager.createMemoryManager(localIETStatement);
    this.m_materializerContext.getResourceManager().registerResource(localETMemoryManager);
    return new StatementExecutor(localIETStatement, this.m_materializerContext.getResourceManager(), this.m_materializerContext.getCancelState());
  }
  
  public ETQuery visit(AEQuery paramAEQuery)
    throws ErrorException
  {
    MaterializerContext localMaterializerContext = this.m_materializerContext;
    ETRelationalExpr localETRelationalExpr = (ETRelationalExpr)paramAEQuery.getOperand().acceptVisitor(new ETRelationalExprMaterializer(this.m_plan, localMaterializerContext));
    return new ETQuery(localETRelationalExpr, this.m_materializerContext.getParameters());
  }
  
  public IETStatement visit(AECreateTable paramAECreateTable)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("CREATE statement");
  }
  
  public IETStatement visit(AEDelete paramAEDelete)
    throws ErrorException
  {
    MaterializerContext localMaterializerContext = this.m_materializerContext;
    ETTable localETTable = (ETTable)paramAEDelete.getTable().acceptVisitor(new ETTableMaterializer(this.m_plan, localMaterializerContext));
    ETBooleanExpr localETBooleanExpr = (ETBooleanExpr)paramAEDelete.getCondition().acceptVisitor(new ETBoolExprMaterializer(this.m_plan, localMaterializerContext));
    return new ETSearchedDelete(localETTable, localETBooleanExpr, localMaterializerContext.getParameters());
  }
  
  public IETStatement visit(AEDropTable paramAEDropTable)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("DROP statement");
  }
  
  public RowCountStatement visit(AEInsert paramAEInsert)
    throws ErrorException
  {
    return (RowCountStatement)paramAEInsert.acceptVisitor(new ETDmlMaterializer(this.m_plan, this.m_materializerContext));
  }
  
  public IETStatement visit(AEInsertDefaults paramAEInsertDefaults)
    throws ErrorException
  {
    return new ETDmlMaterializer(this.m_plan, this.m_materializerContext).visit(paramAEInsertDefaults);
  }
  
  public IETStatement visit(AEUpdate paramAEUpdate)
    throws ErrorException
  {
    return new ETDmlMaterializer(this.m_plan, this.m_materializerContext).visit(paramAEUpdate);
  }
  
  protected IETStatement defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    throw new UnsupportedOperationException("Logic Error: Default visit method is called with " + paramIAENode + " from base AE tree builder class");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETStatementMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
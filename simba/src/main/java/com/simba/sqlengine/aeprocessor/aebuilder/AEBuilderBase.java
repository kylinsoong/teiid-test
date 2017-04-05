package com.simba.sqlengine.aeprocessor.aebuilder;

import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.exceptions.SQLEngineMemoryException;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTDefaultVisitor;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.InvalidOperationException;

public abstract class AEBuilderBase<T extends IAENode>
  extends PTDefaultVisitor<T>
  implements IAEBuilder
{
  private AEQueryScope m_queryScope;
  
  protected AEBuilderBase(AEQueryScope paramAEQueryScope)
  {
    this.m_queryScope = paramAEQueryScope;
  }
  
  public T build(IPTNode paramIPTNode)
    throws ErrorException
  {
    try
    {
      return (IAENode)paramIPTNode.acceptVisitor(this);
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
  }
  
  protected AEQueryScope getQueryScope()
  {
    return this.m_queryScope;
  }
  
  protected final T defaultVisit(IPTNode paramIPTNode)
  {
    throw new UnsupportedOperationException("Logic Error: Default visit method is called with " + paramIPTNode + " from base AE tree builder class");
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/AEBuilderBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
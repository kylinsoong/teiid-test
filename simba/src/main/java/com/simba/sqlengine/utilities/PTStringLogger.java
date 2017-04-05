package com.simba.sqlengine.utilities;

import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.IPTVisitor;
import com.simba.sqlengine.parser.parsetree.PTDefaultParameterNode;
import com.simba.sqlengine.parser.parsetree.PTDynamicParameterNode;
import com.simba.sqlengine.parser.parsetree.PTEmptyNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTLiteralNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.parsetree.PTWalker;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Stack;

public class PTStringLogger
{
  public static void writePTLogString(IPTNode paramIPTNode, OutputStream paramOutputStream)
  {
    try
    {
      PTWalker.walkPreorderDepthFirst(paramIPTNode, new PTLogVisitor(paramOutputStream));
    }
    catch (ErrorException localErrorException)
    {
      throw new IllegalStateException(localErrorException);
    }
  }
  
  private static class PTLogVisitor
    implements IPTVisitor<Void>
  {
    private static final String INDENT_STRING = "    ";
    private Stack<Integer> m_indentStack = new Stack();
    private PrintWriter m_printWriter;
    private Stack<String> m_childTypeStack;
    
    public PTLogVisitor(OutputStream paramOutputStream)
    {
      this.m_indentStack.push(Integer.valueOf(0));
      this.m_printWriter = new PrintWriter(paramOutputStream, true);
      this.m_childTypeStack = new Stack();
    }
    
    public Void visit(PTEmptyNode paramPTEmptyNode)
    {
      writeString(paramPTEmptyNode.toString(), 0);
      return null;
    }
    
    public Void visit(PTIdentifierNode paramPTIdentifierNode)
    {
      writeString(paramPTIdentifierNode.toString(), 0);
      return null;
    }
    
    public Void visit(PTFlagNode paramPTFlagNode)
    {
      writeString(paramPTFlagNode.toString(), 0);
      return null;
    }
    
    public Void visit(PTLiteralNode paramPTLiteralNode)
    {
      writeString(paramPTLiteralNode.toString(), 0);
      return null;
    }
    
    public Void visit(PTDynamicParameterNode paramPTDynamicParameterNode)
    {
      writeString(paramPTDynamicParameterNode.toString(), 0);
      return null;
    }
    
    public Void visit(PTDefaultParameterNode paramPTDefaultParameterNode)
    {
      writeString(paramPTDefaultParameterNode.toString(), 0);
      return null;
    }
    
    public Void visit(PTNonterminalNode paramPTNonterminalNode)
    {
      writeString(paramPTNonterminalNode.toString(), paramPTNonterminalNode.numChildren());
      PTPositionalType[] arrayOfPTPositionalType = (PTPositionalType[])paramPTNonterminalNode.getAllPositionalTypes().toArray(new PTPositionalType[0]);
      for (int i = arrayOfPTPositionalType.length - 1; i >= 0; i--) {
        this.m_childTypeStack.push(arrayOfPTPositionalType[i].name());
      }
      return null;
    }
    
    public Void visit(PTListNode paramPTListNode)
    {
      writeString(paramPTListNode.toString(), paramPTListNode.numChildren());
      for (int i = paramPTListNode.numChildren() - 1; i >= 0; i--) {
        this.m_childTypeStack.push("" + i);
      }
      return null;
    }
    
    private void writeString(String paramString, int paramInt)
    {
      int i = ((Integer)this.m_indentStack.pop()).intValue();
      int j = i + 1;
      while (paramInt > 0)
      {
        this.m_indentStack.push(Integer.valueOf(j));
        paramInt--;
      }
      StringBuilder localStringBuilder = new StringBuilder(1000);
      while (i > 0)
      {
        localStringBuilder.append("    ");
        i--;
      }
      if (!this.m_childTypeStack.isEmpty())
      {
        localStringBuilder.append((String)this.m_childTypeStack.pop());
        localStringBuilder.append(": ");
      }
      localStringBuilder.append(paramString);
      this.m_printWriter.println(localStringBuilder.toString());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/utilities/PTStringLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
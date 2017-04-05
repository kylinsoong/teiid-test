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
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.Stack;

public class PTToStringConverter
{
  private static final String INDENT_STR = "    ";
  private String m_lineInitials;
  private TreeVisitor m_visitor;
  
  public PTToStringConverter()
  {
    this.m_lineInitials = "";
    this.m_visitor = new TreeVisitor();
  }
  
  public PTToStringConverter(String paramString)
  {
    this.m_lineInitials = paramString;
    this.m_visitor = new TreeVisitor();
  }
  
  public String getTreeString(IPTNode paramIPTNode)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    printTreeString(paramIPTNode, localByteArrayOutputStream);
    return localByteArrayOutputStream.toString();
  }
  
  public void printTreeString(IPTNode paramIPTNode, OutputStream paramOutputStream)
  {
    if ((null == paramIPTNode) || (null == paramOutputStream)) {
      return;
    }
    PrintWriter localPrintWriter = new PrintWriter(paramOutputStream, true);
    traverseTree(paramIPTNode, localPrintWriter);
  }
  
  private void traverseTree(IPTNode paramIPTNode, PrintWriter paramPrintWriter)
  {
    Stack localStack = new Stack();
    localStack.push(new StackElement(0, null, paramIPTNode));
    while (!localStack.empty())
    {
      StackElement localStackElement = (StackElement)localStack.pop();
      writeElement(localStackElement, paramPrintWriter);
      pushChildren(localStack, localStackElement);
    }
  }
  
  private void pushChildren(Stack<StackElement> paramStack, StackElement paramStackElement)
  {
    Object localObject;
    int i;
    if ((paramStackElement.node instanceof PTListNode))
    {
      localObject = (PTListNode)paramStackElement.node;
      i = paramStackElement.indent + 1;
      for (int j = ((PTListNode)localObject).numChildren() - 1; j >= 0; j--) {
        try
        {
          paramStack.push(new StackElement(i, null, ((PTListNode)localObject).getChild(j)));
        }
        catch (ErrorException localErrorException)
        {
          throw new IndexOutOfBoundsException("node Corrupted: " + ((PTListNode)localObject).toString());
        }
      }
    }
    else if ((paramStackElement.node instanceof PTNonterminalNode))
    {
      localObject = (PTNonterminalNode)paramStackElement.node;
      i = paramStackElement.indent + 1;
      PTPositionalType[] arrayOfPTPositionalType1 = (PTPositionalType[])((PTNonterminalNode)localObject).getAllPositionalTypes().toArray(new PTPositionalType[0]);
      Arrays.sort(arrayOfPTPositionalType1, Collections.reverseOrder(new Comparator()
      {
        public int compare(PTPositionalType paramAnonymousPTPositionalType1, PTPositionalType paramAnonymousPTPositionalType2)
        {
          return paramAnonymousPTPositionalType1.name().compareTo(paramAnonymousPTPositionalType2.name());
        }
      }));
      for (PTPositionalType localPTPositionalType : arrayOfPTPositionalType1)
      {
        IPTNode localIPTNode = ((PTNonterminalNode)localObject).getChild(localPTPositionalType);
        paramStack.push(new StackElement(i, "POS_" + localPTPositionalType.name(), localIPTNode));
      }
    }
  }
  
  private void writeElement(StackElement paramStackElement, PrintWriter paramPrintWriter)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.m_lineInitials);
    for (int i = 0; i < paramStackElement.indent; i++) {
      localStringBuilder.append("    ");
    }
    try
    {
      localStringBuilder.append((String)paramStackElement.node.acceptVisitor(this.m_visitor));
    }
    catch (ErrorException localErrorException)
    {
      throw new IllegalStateException(localErrorException);
    }
    if (null != paramStackElement.positionStr)
    {
      localStringBuilder.append(" <-> ");
      localStringBuilder.append(paramStackElement.positionStr);
    }
    paramPrintWriter.println(localStringBuilder.toString());
  }
  
  private static class TreeVisitor
    implements IPTVisitor<String>
  {
    public String visit(PTEmptyNode paramPTEmptyNode)
    {
      return "EMPTY_NODE";
    }
    
    public String visit(PTIdentifierNode paramPTIdentifierNode)
    {
      return "IDENTIFIER : " + paramPTIdentifierNode.getIdentifier();
    }
    
    public String visit(PTFlagNode paramPTFlagNode)
    {
      return "FLAG_NODE : " + paramPTFlagNode.getFlagType().name();
    }
    
    public String visit(PTLiteralNode paramPTLiteralNode)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramPTLiteralNode.getLiteralType().name()).append("_LITERAL_NODE : ");
      switch (PTToStringConverter.2.$SwitchMap$com$simba$sqlengine$parser$type$PTLiteralType[paramPTLiteralNode.getLiteralType().ordinal()])
      {
      case 1: 
      case 2: 
      case 3: 
      case 4: 
        localStringBuilder.append("'").append(paramPTLiteralNode.getStringValue()).append("'");
        break;
      default: 
        localStringBuilder.append(paramPTLiteralNode.getStringValue());
      }
      return localStringBuilder.toString();
    }
    
    public String visit(PTDynamicParameterNode paramPTDynamicParameterNode)
    {
      return "DYNAMIC_PARAMETER_NODE : " + paramPTDynamicParameterNode.getIndex();
    }
    
    public String visit(PTNonterminalNode paramPTNonterminalNode)
    {
      return paramPTNonterminalNode.getNonterminalType().name() + "_NT_NODE";
    }
    
    public String visit(PTDefaultParameterNode paramPTDefaultParameterNode)
    {
      return "DEFAULT_PARAMETER_NODE";
    }
    
    public String visit(PTListNode paramPTListNode)
    {
      return paramPTListNode.getListType().name() + "_LIST_NODE";
    }
  }
  
  private static class StackElement
  {
    public int indent;
    public String positionStr;
    public IPTNode node;
    
    public StackElement(int paramInt, String paramString, IPTNode paramIPTNode)
    {
      this.indent = paramInt;
      this.positionStr = paramString;
      this.node = paramIPTNode;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/utilities/PTToStringConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
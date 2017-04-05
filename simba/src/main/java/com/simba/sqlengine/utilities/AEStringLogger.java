package com.simba.sqlengine.utilities;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.support.exceptions.ErrorException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class AEStringLogger
{
  public static void logAETree(IAENode paramIAENode, String paramString, String... paramVarArgs)
    throws IOException
  {
    try
    {
      File localFile = new File(paramString);
      if (!localFile.exists()) {
        localFile.createNewFile();
      }
      PrintWriter localPrintWriter = new PrintWriter(new FileWriter(localFile, true), true);
      for (String str : paramVarArgs)
      {
        localPrintWriter.print(str);
        localPrintWriter.print("\n");
      }
      walkPreorderDepthFirst(paramIAENode, new AELogVisitor(localPrintWriter));
      localPrintWriter.close();
    }
    catch (ErrorException localErrorException) {}
  }
  
  public static String logAETreeToString(IAENode paramIAENode)
    throws ErrorException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    PrintWriter localPrintWriter = new PrintWriter(localByteArrayOutputStream);
    walkPreorderDepthFirst(paramIAENode, new AELogVisitor(localPrintWriter));
    localPrintWriter.flush();
    return localByteArrayOutputStream.toString();
  }
  
  private static void walkPreorderDepthFirst(IAENode paramIAENode, AELogVisitor paramAELogVisitor)
    throws ErrorException
  {
    if (null == paramIAENode) {
      return;
    }
    Stack localStack = new Stack();
    localStack.push(paramIAENode);
    while (!localStack.empty())
    {
      IAENode localIAENode1 = (IAENode)localStack.pop();
      localIAENode1.acceptVisitor(paramAELogVisitor);
      Iterator localIterator = localIAENode1.getChildItr();
      ArrayList localArrayList = new ArrayList();
      while (localIterator.hasNext()) {
        localArrayList.add(localIterator.next());
      }
      for (int i = localArrayList.size() - 1; i >= 0; i--)
      {
        IAENode localIAENode2 = (IAENode)localArrayList.get(i);
        if (null == localIAENode2) {
          throw new NullPointerException("Tree nodes cannot be null.");
        }
        localStack.push(localIAENode2);
      }
    }
  }
  
  private static class AELogVisitor
    extends AEDefaultVisitor<Void>
  {
    private Stack<Integer> m_indentStack = new Stack();
    private static final String INDENT_STRING = "    ";
    private PrintWriter m_printWriter;
    private Stack<String> m_childTypeStack;
    
    public AELogVisitor(PrintWriter paramPrintWriter)
    {
      this.m_indentStack.push(Integer.valueOf(0));
      this.m_printWriter = paramPrintWriter;
      this.m_childTypeStack = new Stack();
    }
    
    protected Void defaultVisit(IAENode paramIAENode)
      throws ErrorException
    {
      writeString(paramIAENode.getLogString(), paramIAENode.getNumChildren());
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
      this.m_printWriter.print(localStringBuilder.toString());
      this.m_printWriter.print("\n");
      for (int k = paramInt - 1; k >= 0; k--) {
        this.m_childTypeStack.push("" + k);
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/utilities/AEStringLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
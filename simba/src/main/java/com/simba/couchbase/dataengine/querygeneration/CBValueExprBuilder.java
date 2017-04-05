/*      */ package com.simba.couchbase.dataengine.querygeneration;
/*      */ 
/*      */ import com.simba.couchbase.dataengine.CBSQLDataEngine;
/*      */ import com.simba.couchbase.dataengine.resultset.CBResultColumn;
/*      */ import com.simba.couchbase.dataengine.resultset.CBResultSet;
/*      */ import com.simba.couchbase.dataengine.resultset.CBTableColumn;
/*      */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*      */ import com.simba.couchbase.utils.CBCoreUtils;
/*      */ import com.simba.couchbase.utils.CBQueryUtils;
/*      */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*      */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.AENodeList;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.IAENode;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEBinaryValueExpr;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleWhenClause;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
/*      */ import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
/*      */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
/*      */ import com.simba.support.ILogger;
/*      */ import com.simba.support.LogUtilities;
/*      */ import com.simba.support.exceptions.ErrorException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CBValueExprBuilder
/*      */   extends AEDefaultVisitor<String>
/*      */ {
/*      */   private CBSQLDataEngine m_dataEngine;
/*      */   private String m_expression;
/*      */   private ILogger m_log;
/*      */   private Map<String, CBTableColumn> m_newLetClauses;
/*      */   private List<CBParamValue> m_newParamValues;
/*      */   private Map<String, CBTableColumn> m_outputLetClauses;
/*      */   private List<CBParamValue> m_outputParamValues;
/*      */   private Map<DSIExtJResultSet, CBResultSet> m_tableCopyMap;
/*      */   
/*      */   public CBValueExprBuilder(CBSQLDataEngine dataEngine, Map<String, CBTableColumn> outputLetClauses, Map<DSIExtJResultSet, CBResultSet> tableCopyMap, List<CBParamValue> outputParamValues, ILogger log)
/*      */   {
/*  108 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/*  109 */     this.m_log = log;
/*  110 */     this.m_dataEngine = dataEngine;
/*  111 */     this.m_tableCopyMap = tableCopyMap;
/*      */     
/*  113 */     this.m_outputLetClauses = outputLetClauses;
/*  114 */     this.m_outputParamValues = outputParamValues;
/*      */     
/*      */ 
/*  117 */     this.m_newLetClauses = new HashMap();
/*  118 */     this.m_newParamValues = new ArrayList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String buildExpression(AEValueExpr inputNode)
/*      */     throws ErrorException
/*      */   {
/*  133 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     try
/*      */     {
/*  136 */       this.m_expression = ((String)inputNode.acceptVisitor(this));
/*  137 */       if (null != this.m_expression)
/*      */       {
/*      */ 
/*  140 */         this.m_outputLetClauses.putAll(this.m_newLetClauses);
/*  141 */         this.m_outputParamValues.addAll(this.m_newParamValues);
/*      */       }
/*      */       
/*  144 */       LogUtilities.logInfo("Current Expression: " + this.m_expression, this.m_log);
/*  145 */       return this.m_expression;
/*      */     }
/*      */     catch (ErrorException ex)
/*      */     {
/*  149 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.PASSDOWN_BUILD_VALUEEXPR_ERROR.name(), new String[] { ex.getMessage() });
/*      */       
/*      */ 
/*  152 */       err.initCause(ex);
/*  153 */       throw err;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String buildColumnExpression(CBResultColumn column)
/*      */   {
/*  163 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     try
/*      */     {
/*  166 */       this.m_expression = visitColumn(column);
/*      */       
/*  168 */       if (null != this.m_expression)
/*      */       {
/*      */ 
/*  171 */         this.m_outputLetClauses.putAll(this.m_newLetClauses);
/*  172 */         this.m_outputParamValues.addAll(this.m_newParamValues);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */       LogUtilities.logInfo("Current Expression: " + this.m_expression, this.m_log);
/*  180 */       return this.m_expression;
/*      */     }
/*      */     catch (Exception ex) {}
/*      */     
/*      */ 
/*      */ 
/*  186 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visit(AEAdd addNode)
/*      */     throws ErrorException
/*      */   {
/*  195 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  196 */     StringBuilder addBuilder = new StringBuilder();
/*  197 */     TypeMetadata metadata = addNode.getTypeMetadata();
/*  198 */     if (metadata.isCharacterType())
/*      */     {
/*  200 */       String leftExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(addNode.getLeftOperand());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  206 */       if (null == leftExpr)
/*      */       {
/*  208 */         return null;
/*      */       }
/*      */       
/*  211 */       String rightExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(addNode.getRightOperand());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  217 */       if (null == rightExpr)
/*      */       {
/*  219 */         return null;
/*      */       }
/*  221 */       return "CONCAT(" + rightExpr + "," + rightExpr + ")";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  230 */     return visitSimpleBinaryValueExpr(addNode, "+");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visit(AEColumnReference columnRef)
/*      */     throws ErrorException
/*      */   {
/*  240 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  241 */     StringBuilder columnExpr = new StringBuilder();
/*      */     try
/*      */     {
/*  244 */       AENamedRelationalExpr nameExpr = columnRef.getNamedRelationalExpr();
/*  245 */       if ((nameExpr instanceof AETable))
/*      */       {
/*  247 */         AETable table = (AETable)nameExpr;
/*  248 */         DSIExtJResultSet originalResultSet = table.getTable();
/*  249 */         CBResultSet copyResultSet = (CBResultSet)this.m_tableCopyMap.get(originalResultSet);
/*  250 */         CBResultColumn column = (CBResultColumn)copyResultSet.getSelectColumns().get(columnRef.getColumnNum());
/*      */         
/*      */ 
/*  253 */         columnExpr.append(visitColumn(column));
/*      */       }
/*  255 */       return columnExpr.toString();
/*      */     }
/*      */     catch (ErrorException ex)
/*      */     {
/*  259 */       throw ex;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visit(AEDivide divideNode)
/*      */     throws ErrorException
/*      */   {
/*  269 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  270 */     return visitSimpleBinaryValueExpr(divideNode, "/");
/*      */   }
/*      */   
/*      */ 
/*      */   public String visit(AELiteral literalNode)
/*      */   {
/*  276 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  277 */     StringBuilder literalResult = new StringBuilder();
/*  278 */     switch (literalNode.getLiteralType())
/*      */     {
/*      */ 
/*      */ 
/*      */     case CHARSTR: 
/*  283 */       literalResult.append("'").append(literalNode.getStringValue()).append("'");
/*      */       
/*      */ 
/*      */ 
/*  287 */       return literalResult.toString();
/*      */     
/*      */ 
/*      */     case USINT: 
/*  291 */       return literalNode.getStringValue();
/*      */     
/*      */ 
/*      */     case DECIMAL: 
/*  295 */       return literalNode.getStringValue();
/*      */     
/*      */ 
/*      */     case APPROXNUM: 
/*  299 */       return literalNode.getStringValue();
/*      */     
/*      */ 
/*      */     case NULL: 
/*  303 */       literalResult.append("NULL");
/*      */       
/*  305 */       return literalResult.toString();
/*      */     
/*      */ 
/*      */     case DATE: 
/*      */     case TIME: 
/*      */     case TIMESTAMP: 
/*  311 */       literalResult.append("'").append(literalNode.getStringValue()).append("'");
/*      */       
/*      */ 
/*      */ 
/*  315 */       return literalResult.toString();
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  322 */     return literalResult.toString();
/*      */   }
/*      */   
/*      */   public String visit(AEMultiply multiplyNode)
/*      */     throws ErrorException
/*      */   {
/*  328 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  329 */     return visitSimpleBinaryValueExpr(multiplyNode, "*");
/*      */   }
/*      */   
/*      */   public String visit(AENegate negateNode)
/*      */     throws ErrorException
/*      */   {
/*  335 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  336 */     StringBuilder negateNodeResult = new StringBuilder();
/*  337 */     String operandExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(negateNode.getOperand());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  343 */     if (null == operandExpr)
/*      */     {
/*  345 */       return null;
/*      */     }
/*  347 */     return "(-" + operandExpr + ")";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visit(AENull nullNode)
/*      */   {
/*  356 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  357 */     return "NULL";
/*      */   }
/*      */   
/*      */   public String visit(AEParameter parameterNode)
/*      */     throws ErrorException
/*      */   {
/*  363 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     
/*  365 */     String paramName = "$param" + parameterNode.getIndex();
/*      */     
/*  367 */     DataWrapper inputData = parameterNode.getInputData();
/*      */     
/*      */ 
/*  370 */     String paramValue = CBCoreUtils.getParameterValue(inputData);
/*  371 */     this.m_newParamValues.add(new CBParamValue(paramName, paramValue));
/*      */     
/*  373 */     return paramName;
/*      */   }
/*      */   
/*      */   public String visit(AEScalarFn scalarNode)
/*      */     throws ErrorException
/*      */   {
/*  379 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*  380 */     StringBuilder scalarResult = new StringBuilder();
/*  381 */     AEValueExprList arguments = scalarNode.getArguments();
/*  382 */     switch (scalarNode.getScalarFnId())
/*      */     {
/*      */ 
/*      */     case ABS: 
/*  386 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  388 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  395 */         if (null != operand)
/*      */         {
/*  397 */           return null;
/*      */         }
/*      */         
/*  400 */         return "ABS(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case ACOS: 
/*  409 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  411 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  418 */         if (null != operand)
/*      */         {
/*  420 */           return null;
/*      */         }
/*      */         
/*  423 */         return "ACOS(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case ASIN: 
/*  432 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  434 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  441 */         if (null != operand)
/*      */         {
/*  443 */           return null;
/*      */         }
/*      */         
/*  446 */         return "ASIN(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case ATAN: 
/*  455 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  457 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  464 */         if (null != operand)
/*      */         {
/*  466 */           return null;
/*      */         }
/*      */         
/*  469 */         return "ATAN(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case ATAN2: 
/*  478 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/*  480 */         String expr1 = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  486 */         if (null != expr1)
/*      */         {
/*  488 */           return null;
/*      */         }
/*      */         
/*  491 */         String expr2 = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  497 */         if (null != expr2)
/*      */         {
/*  499 */           return null;
/*      */         }
/*      */         
/*  502 */         return "ATAN2(" + expr1 + "," + expr2 + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case CEILING: 
/*  513 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  515 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  522 */         if (null != operand)
/*      */         {
/*  524 */           return null;
/*      */         }
/*      */         
/*  527 */         return "CEIL(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case COS: 
/*  536 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  538 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  545 */         if (null != operand)
/*      */         {
/*  547 */           return null;
/*      */         }
/*      */         
/*  550 */         return "COS(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case CONCAT: 
/*  559 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/*  561 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  567 */         if (null != leftOp)
/*      */         {
/*  569 */           return null;
/*      */         }
/*      */         
/*  572 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  578 */         if (null != rightOp)
/*      */         {
/*  580 */           return null;
/*      */         }
/*      */         
/*  583 */         return "(" + leftOp + " || " + leftOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case CAST: 
/*      */     case CONVERT: 
/*  595 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/*      */ 
/*  598 */         AEValueExpr operand = (AEValueExpr)arguments.getChild(0);
/*  599 */         AEValueExpr type = (AEValueExpr)arguments.getChild(1);
/*      */         
/*  601 */         String scalarFnParameter = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(operand);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  607 */         if (null == scalarFnParameter)
/*      */         {
/*  609 */           return null;
/*      */         }
/*      */         
/*  612 */         TypeMetadata typeMeta = type.getTypeMetadata();
/*  613 */         if (null != typeMeta)
/*      */         {
/*  615 */           if ((typeMeta.getType() == 12) || (typeMeta.getType() == 1))
/*      */           {
/*      */ 
/*  618 */             return "TO_STR(" + scalarFnParameter + ")";
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  623 */           if ((typeMeta.getType() == 4) || (typeMeta.getType() == -5) || (typeMeta.getType() == 8))
/*      */           {
/*      */ 
/*      */ 
/*  627 */             return "TO_NUM(" + scalarFnParameter + ")";
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  634 */           return scalarFnParameter;
/*      */         }
/*      */       }
/*      */       
/*  638 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case COT: 
/*  643 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  645 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  652 */         if (null != operand)
/*      */         {
/*  654 */           return null;
/*      */         }
/*      */         
/*  657 */         return "(1 / TAN(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case CURDATE: 
/*      */     case CURRENT_DATE: 
/*  667 */       if (null != arguments)
/*      */       {
/*  669 */         return "NOW_STR('2006-01-02')";
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case CURTIME: 
/*      */     case CURRENT_TIME: 
/*  676 */       if (null == arguments)
/*      */       {
/*  678 */         return "NOW_STR('15:04:05.999')";
/*      */       }
/*  680 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  682 */         String timePrecision = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  689 */         if (null != timePrecision)
/*      */         {
/*  691 */           return null;
/*      */         }
/*      */         
/*  694 */         int timePrecisionInt = Integer.parseInt(timePrecision);
/*  695 */         scalarResult.append("NOW_STR('15:04:05.')");
/*  696 */         for (int i = 0; i < timePrecisionInt; i++)
/*      */         {
/*  698 */           scalarResult.append("9");
/*      */         }
/*  700 */         scalarResult.append("'");
/*  701 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case CURRENT_TIMESTAMP: 
/*  707 */       if (null == arguments)
/*      */       {
/*  709 */         return "NOW_STR('2006-01-02 15:04:05')";
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case DAYNAME: 
/*  715 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/*  717 */         return null;
/*      */       }
/*  719 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  726 */       if (null == expression)
/*      */       {
/*  728 */         return null;
/*      */       }
/*  730 */       scalarResult.append("CASE DATE_PART_STR(").append(expression).append(", 'day_of_week') \\WHEN 0 THEN 'Sunday' \\WHEN 1 THEN 'Monday' \\WHEN 2 THEN 'Tuesday' \\WHEN 3 THEN 'Wednesday' \\WHEN 4 THEN 'Thursday' \\WHEN 5 THEN 'Friday' \\WHEN 6 THEN 'Saturday' END");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  740 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case DAYOFWEEK: 
/*  744 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/*  746 */         return null;
/*      */       }
/*  748 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  755 */       if (null == expression)
/*      */       {
/*  757 */         return null;
/*      */       }
/*      */       
/*  760 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'day_of_week')+1 ");
/*      */       
/*      */ 
/*      */ 
/*  764 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case DAYOFMONTH: 
/*  768 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/*  770 */         return null;
/*      */       }
/*  772 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  779 */       if (null == expression)
/*      */       {
/*  781 */         return null;
/*      */       }
/*      */       
/*  784 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'day') ");
/*      */       
/*      */ 
/*      */ 
/*  788 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case DAYOFYEAR: 
/*  792 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/*  794 */         return null;
/*      */       }
/*  796 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  803 */       if (null == expression)
/*      */       {
/*  805 */         return null;
/*      */       }
/*      */       
/*  808 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'day_of_year') ");
/*      */       
/*      */ 
/*      */ 
/*  812 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case DEGREES: 
/*  816 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  818 */         String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  825 */         scalarResult.append("DEGREES(").append(expression).append(")");
/*      */         
/*      */ 
/*      */ 
/*  829 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case EXP: 
/*  835 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  837 */         String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  844 */         scalarResult.append("EXP(").append(expression).append(")");
/*      */         
/*      */ 
/*      */ 
/*  848 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case FLOOR: 
/*  854 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  856 */         String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  863 */         scalarResult.append("FLOOR(").append(expression).append(")");
/*      */         
/*      */ 
/*      */ 
/*  867 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case HOUR: 
/*  873 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/*  875 */         return null;
/*      */       }
/*  877 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  884 */       if (null == expression)
/*      */       {
/*  886 */         return null;
/*      */       }
/*      */       
/*  889 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'hour') ");
/*      */       
/*      */ 
/*      */ 
/*  893 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case IFNULL: 
/*  897 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/*  899 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  905 */         if (null != leftOp)
/*      */         {
/*  907 */           return null;
/*      */         }
/*      */         
/*  910 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  916 */         if (null != rightOp)
/*      */         {
/*  918 */           return null;
/*      */         }
/*      */         
/*  921 */         return "IFNULL(" + leftOp + "," + leftOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case LOG: 
/*  932 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  934 */         String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  941 */         scalarResult.append("LN(").append(expression).append(")");
/*      */         
/*      */ 
/*      */ 
/*  945 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case LOG10: 
/*  951 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  953 */         String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  960 */         scalarResult.append("LOG(").append(expression).append(")");
/*      */         
/*      */ 
/*      */ 
/*  964 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case LCASE: 
/*      */     case LOWER: 
/*  971 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/*  973 */         String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  980 */         scalarResult.append("LOWER(").append(expression).append(")");
/*      */         
/*      */ 
/*      */ 
/*  984 */         return scalarResult.toString();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case LEFT: 
/*  990 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/*  992 */         String strExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  998 */         if (null != strExpr)
/*      */         {
/* 1000 */           return null;
/*      */         }
/*      */         
/* 1003 */         String numCharsStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1009 */         if (null != numCharsStr)
/*      */         {
/* 1011 */           return null;
/*      */         }
/*      */         
/* 1014 */         return "SUBSTR(" + strExpr + ", 0, " + numCharsStr + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case LOCATE2: 
/*      */     case LOCATE3: 
/* 1026 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1028 */         String substring = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1034 */         if (null != substring)
/*      */         {
/* 1036 */           return null;
/*      */         }
/*      */         
/* 1039 */         String mainString = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1045 */         if (null != mainString)
/*      */         {
/* 1047 */           return null;
/*      */         }
/*      */         
/* 1050 */         return " POSITION(" + mainString + "," + substring + ") + 1";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case LTRIM: 
/* 1061 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1063 */         return null;
/*      */       }
/* 1065 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1072 */       if (null == expression)
/*      */       {
/* 1074 */         return null;
/*      */       }
/*      */       
/* 1077 */       scalarResult.append("LTRIM(").append(expression).append(")");
/*      */       
/*      */ 
/*      */ 
/* 1081 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case MINUTE: 
/* 1085 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1087 */         return null;
/*      */       }
/* 1089 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1096 */       if (null == expression)
/*      */       {
/* 1098 */         return null;
/*      */       }
/*      */       
/* 1101 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'minute') ");
/*      */       
/*      */ 
/*      */ 
/* 1105 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case MOD: 
/* 1109 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1111 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1117 */         if (null != leftOp)
/*      */         {
/* 1119 */           return null;
/*      */         }
/*      */         
/* 1122 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1128 */         if (null != rightOp)
/*      */         {
/* 1130 */           return null;
/*      */         }
/*      */         
/* 1133 */         return "MOD(" + leftOp + "," + rightOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case MONTH: 
/* 1144 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1146 */         return null;
/*      */       }
/* 1148 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1155 */       if (null == expression)
/*      */       {
/* 1157 */         return null;
/*      */       }
/*      */       
/* 1160 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'month') ");
/*      */       
/*      */ 
/*      */ 
/* 1164 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case MONTHNAME: 
/* 1168 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1170 */         return null;
/*      */       }
/* 1172 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1179 */       if (null == expression)
/*      */       {
/* 1181 */         return null;
/*      */       }
/*      */       
/* 1184 */       scalarResult.append("CASE DATE_PART_STR(").append(expression).append(", 'month') \\WHEN 1 THEN 'January' \\WHEN 2 THEN 'February' \\WHEN 3 THEN 'March' \\WHEN 4 THEN 'April' \\WHEN 5 THEN 'May' \\WHEN 6 THEN 'June' \\WHEN 7 THEN 'July' \\WHEN 8 THEN 'August' \\WHEN 9 THEN 'September' \\WHEN 10 THEN 'October' \\WHEN 11 THEN 'November' \\WHEN 12 THEN 'December' END");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1200 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case NOW: 
/* 1204 */       if (null == arguments)
/*      */       {
/* 1206 */         return "NOW_STR()";
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case NULL: 
/* 1212 */       if (null == arguments)
/*      */       {
/* 1214 */         return "null";
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case PI: 
/* 1220 */       if (null == arguments)
/*      */       {
/* 1222 */         return "PI()";
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case POWER: 
/* 1228 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1230 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1236 */         if (null != leftOp)
/*      */         {
/* 1238 */           return null;
/*      */         }
/*      */         
/* 1241 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1247 */         if (null != rightOp)
/*      */         {
/* 1249 */           return null;
/*      */         }
/*      */         
/* 1252 */         return "POWER(" + leftOp + "," + rightOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case QUARTER: 
/* 1263 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1265 */         return null;
/*      */       }
/* 1267 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1274 */       if (null == expression)
/*      */       {
/* 1276 */         return null;
/*      */       }
/*      */       
/* 1279 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'quarter') ");
/*      */       
/*      */ 
/*      */ 
/* 1283 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case RADIANS: 
/* 1287 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/* 1289 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1296 */         if (null != operand)
/*      */         {
/* 1298 */           return null;
/*      */         }
/*      */         
/* 1301 */         return "RADIANS(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case RAND0: 
/* 1310 */       if (null == arguments)
/*      */       {
/* 1312 */         return "RANDOM()";
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case RAND1: 
/* 1318 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/* 1320 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1327 */         if (null != operand)
/*      */         {
/* 1329 */           return null;
/*      */         }
/*      */         
/* 1332 */         return "RANDOM(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case REPEAT: 
/* 1341 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1343 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1349 */         if (null != leftOp)
/*      */         {
/* 1351 */           return null;
/*      */         }
/*      */         
/* 1354 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1360 */         if (null != rightOp)
/*      */         {
/* 1362 */           return null;
/*      */         }
/*      */         
/* 1365 */         return "REPEAT(" + leftOp + "," + rightOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case REPLACE: 
/* 1376 */       if ((null != arguments) && (arguments.getNumChildren() == 3))
/*      */       {
/* 1378 */         String orgStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1384 */         if (null != orgStr)
/*      */         {
/* 1386 */           return null;
/*      */         }
/*      */         
/* 1389 */         String lookUpStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1395 */         if (null != lookUpStr)
/*      */         {
/* 1397 */           return null;
/*      */         }
/*      */         
/* 1400 */         String replaceStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(2));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1406 */         if (null != replaceStr)
/*      */         {
/* 1408 */           return null;
/*      */         }
/*      */         
/* 1411 */         return "REPLACE(" + orgStr + "," + lookUpStr + "," + replaceStr + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case RIGHT: 
/* 1424 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1426 */         String strExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1432 */         if (null != strExpr)
/*      */         {
/* 1434 */           return null;
/*      */         }
/*      */         
/* 1437 */         String numCharsStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1443 */         if (null != numCharsStr)
/*      */         {
/* 1445 */           return null;
/*      */         }
/*      */         
/* 1448 */         return "SUBSTR(" + strExpr + ", TO_NUM(LENGTH(" + strExpr + ") - " + numCharsStr + "))";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case ROUND: 
/* 1461 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1463 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1469 */         if (null != leftOp)
/*      */         {
/* 1471 */           return null;
/*      */         }
/*      */         
/* 1474 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1480 */         if (null != rightOp)
/*      */         {
/* 1482 */           return null;
/*      */         }
/*      */         
/* 1485 */         return "ROUND(" + leftOp + "," + rightOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case RTRIM: 
/* 1496 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1498 */         return null;
/*      */       }
/* 1500 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1507 */       if (null == expression)
/*      */       {
/* 1509 */         return null;
/*      */       }
/*      */       
/* 1512 */       scalarResult.append("RTRIM(").append(expression).append(")");
/*      */       
/*      */ 
/*      */ 
/* 1516 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case SECOND: 
/* 1520 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1522 */         return null;
/*      */       }
/* 1524 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1531 */       if (null == expression)
/*      */       {
/* 1533 */         return null;
/*      */       }
/*      */       
/* 1536 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'second') ");
/*      */       
/*      */ 
/*      */ 
/* 1540 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case SIGN: 
/* 1544 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/* 1546 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1553 */         if (null != operand)
/*      */         {
/* 1555 */           return null;
/*      */         }
/*      */         
/* 1558 */         return "SIGN(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case SIN: 
/* 1567 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/* 1569 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1576 */         if (null != operand)
/*      */         {
/* 1578 */           return null;
/*      */         }
/*      */         
/* 1581 */         return "SIN(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case SPACE: 
/* 1590 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1592 */         return null;
/*      */       }
/* 1594 */       String spaceCount = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1601 */       if (null == spaceCount)
/*      */       {
/* 1603 */         return null;
/*      */       }
/*      */       
/* 1606 */       scalarResult.append("REPEAT(' ', ").append(spaceCount).append(")");
/*      */       
/*      */ 
/*      */ 
/* 1610 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case SQRT: 
/* 1614 */       if ((null != arguments) && (arguments.getNumChildren() == 1))
/*      */       {
/* 1616 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1623 */         if (null != operand)
/*      */         {
/* 1625 */           return null;
/*      */         }
/*      */         
/* 1628 */         return "SQRT(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case SUBSTRING2: 
/* 1637 */       if ((null == arguments) || (arguments.getNumChildren() != 2))
/*      */       {
/* 1639 */         return null;
/*      */       }
/* 1641 */       String strExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1648 */       if (null == strExpr)
/*      */       {
/* 1650 */         return null;
/*      */       }
/*      */       
/* 1653 */       String numCharsStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1660 */       if (null == numCharsStr)
/*      */       {
/* 1662 */         return null;
/*      */       }
/*      */       
/* 1665 */       int position = Integer.parseInt(numCharsStr);
/*      */       
/* 1667 */       scalarResult.append("SUBSTR( ").append(strExpr).append(",").append(position).append(")");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1673 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case SUBSTRING3: 
/* 1677 */       if ((null == arguments) || (arguments.getNumChildren() != 3))
/*      */       {
/* 1679 */         return null;
/*      */       }
/* 1681 */       String strExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1688 */       if (null == strExpr)
/*      */       {
/* 1690 */         return null;
/*      */       }
/*      */       
/* 1693 */       String positionStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1700 */       if (null == positionStr)
/*      */       {
/* 1702 */         return null;
/*      */       }
/*      */       
/* 1705 */       String lengthStr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1712 */       if (null == lengthStr)
/*      */       {
/* 1714 */         return null;
/*      */       }
/*      */       
/* 1717 */       int position = Integer.parseInt(positionStr);
/*      */       
/* 1719 */       scalarResult.append("SUBSTR( ").append(strExpr).append(",").append(position).append(",").append(lengthStr).append(")");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1727 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case TAN: 
/* 1731 */       if (null != arguments)
/*      */       {
/* 1733 */         String operand = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1740 */         if (null != operand)
/*      */         {
/* 1742 */           return null;
/*      */         }
/*      */         
/* 1745 */         return "TAN(" + operand + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case TRUNCATE: 
/* 1754 */       if ((null != arguments) && (arguments.getNumChildren() == 2))
/*      */       {
/* 1756 */         String leftOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1763 */         if (null != leftOp)
/*      */         {
/* 1765 */           return null;
/*      */         }
/*      */         
/* 1768 */         String rightOp = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1774 */         if (null != rightOp)
/*      */         {
/* 1776 */           return null;
/*      */         }
/*      */         
/* 1779 */         return "TRUNC(" + leftOp + "," + rightOp + ")";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case UCASE: 
/*      */     case UPPER: 
/* 1791 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1793 */         return null;
/*      */       }
/* 1795 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1802 */       if (null == expression)
/*      */       {
/* 1804 */         return null;
/*      */       }
/*      */       
/* 1807 */       scalarResult.append("UPPER(").append(expression).append(")");
/*      */       
/*      */ 
/*      */ 
/* 1811 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case WEEK: 
/* 1815 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1817 */         return null;
/*      */       }
/* 1819 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1826 */       if (null == expression)
/*      */       {
/* 1828 */         return null;
/*      */       }
/*      */       
/* 1831 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'week') ");
/*      */       
/*      */ 
/*      */ 
/* 1835 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */     case YEAR: 
/* 1839 */       if ((null == arguments) || (arguments.getNumChildren() != 1))
/*      */       {
/* 1841 */         return null;
/*      */       }
/* 1843 */       String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)arguments.getChild(0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1850 */       if (null == expression)
/*      */       {
/* 1852 */         return null;
/*      */       }
/*      */       
/* 1855 */       scalarResult.append("DATE_PART_STR(").append(expression).append(", 'year') ");
/*      */       
/*      */ 
/*      */ 
/* 1859 */       return scalarResult.toString();
/*      */     
/*      */ 
/*      */ 
/*      */     default: 
/* 1864 */       return null;
/*      */     }
/*      */     
/* 1867 */     return null;
/*      */   }
/*      */   
/*      */   public String visit(AESearchedCase searchNode)
/*      */     throws ErrorException
/*      */   {
/* 1873 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 1874 */     StringBuilder caseResult = new StringBuilder();
/* 1875 */     AENodeList<AESearchedWhenClause> whenClauses = searchNode.getWhenClauseList();
/* 1876 */     caseResult.append("CASE ");
/* 1877 */     for (int index = 0; index < whenClauses.getNumChildren(); index++)
/*      */     {
/* 1879 */       String whenExpr = visit((AESearchedWhenClause)whenClauses.getChild(index));
/* 1880 */       if (null == whenExpr) {
/*      */         break;
/*      */       }
/*      */       
/* 1884 */       caseResult.append(whenExpr);
/*      */     }
/*      */     
/* 1887 */     if (null != searchNode.getElseClause())
/*      */     {
/* 1889 */       String elseExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(searchNode.getElseClause());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1895 */       if (null == elseExpr)
/*      */       {
/* 1897 */         return null;
/*      */       }
/*      */       
/* 1900 */       caseResult.append(" ELSE ").append(elseExpr);
/*      */     }
/*      */     
/*      */ 
/* 1904 */     return " END";
/*      */   }
/*      */   
/*      */   public String visit(AESearchedWhenClause searchWhenNode)
/*      */     throws ErrorException
/*      */   {
/* 1910 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 1911 */     StringBuilder whenResult = new StringBuilder();
/* 1912 */     String leftExpr = new CBBooleanExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(searchWhenNode.getWhenCondition());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1918 */     if (null == leftExpr)
/*      */     {
/* 1920 */       return null;
/*      */     }
/*      */     
/* 1923 */     String rightExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(searchWhenNode.getThenExpression());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1929 */     if (null == rightExpr)
/*      */     {
/* 1931 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1935 */     return "(WHEN " + leftExpr + " THEN " + rightExpr + ")";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visit(AESimpleCase simpleCaseNode)
/*      */     throws ErrorException
/*      */   {
/* 1946 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 1947 */     StringBuilder simpleCaseResult = new StringBuilder();
/* 1948 */     AENodeList<AESimpleWhenClause> whenClauses = simpleCaseNode.getWhenClauseList();
/* 1949 */     simpleCaseResult.append("CASE ");
/* 1950 */     String expression = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(simpleCaseNode.getCaseOperand());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1956 */     if (null != expression)
/*      */     {
/* 1958 */       return null;
/*      */     }
/* 1960 */     simpleCaseResult.append(expression).append(" ");
/*      */     
/*      */ 
/* 1963 */     for (int index = 0; index < whenClauses.getNumChildren(); index++)
/*      */     {
/* 1965 */       String whenExpr = visit((AESimpleWhenClause)whenClauses.getChild(index));
/* 1966 */       if (null == whenExpr) {
/*      */         break;
/*      */       }
/*      */       
/* 1970 */       simpleCaseResult.append(whenExpr);
/*      */     }
/*      */     
/* 1973 */     if (null != simpleCaseNode.getElseOperand())
/*      */     {
/* 1975 */       String elseExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(simpleCaseNode.getElseOperand());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1981 */       if (null == elseExpr)
/*      */       {
/* 1983 */         return null;
/*      */       }
/*      */       
/* 1986 */       simpleCaseResult.append(" ELSE ").append(elseExpr);
/*      */     }
/*      */     
/*      */ 
/* 1990 */     return " END";
/*      */   }
/*      */   
/*      */   public String visit(AESimpleWhenClause simpleWhenNode)
/*      */     throws ErrorException
/*      */   {
/* 1996 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 1997 */     StringBuilder whenResult = new StringBuilder();
/* 1998 */     String leftExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(simpleWhenNode.getWhenExpression());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2004 */     if (null == leftExpr)
/*      */     {
/* 2006 */       return null;
/*      */     }
/*      */     
/* 2009 */     String rightExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(simpleWhenNode.getThenExpression());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2015 */     if (null == rightExpr)
/*      */     {
/* 2017 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 2021 */     return "(WHEN " + leftExpr + " THEN " + rightExpr + ")";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visit(AESubtract subtractNode)
/*      */     throws ErrorException
/*      */   {
/* 2032 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 2033 */     return visitSimpleBinaryValueExpr(subtractNode, "-");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String visitColumn(CBResultColumn column)
/*      */   {
/* 2043 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 2044 */     String resultExpr = null;
/* 2045 */     if (null != column.getQueryString())
/*      */     {
/* 2047 */       return column.getQueryString();
/*      */     }
/* 2049 */     if (column.isTableColumn())
/*      */     {
/* 2051 */       String colName = column.getName();
/* 2052 */       String generatedColumnAlias = this.m_dataEngine.getColumnAliasGenerator().addAlias(colName);
/*      */       
/* 2054 */       this.m_newLetClauses.put(generatedColumnAlias, (CBTableColumn)column);
/*      */       
/* 2056 */       column.setQueryString(generatedColumnAlias, true);
/* 2057 */       resultExpr = generatedColumnAlias;
/*      */     }
/* 2059 */     return resultExpr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String visitSimpleBinaryValueExpr(AEBinaryValueExpr inputNode, String inputOperation)
/*      */     throws ErrorException
/*      */   {
/* 2067 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 2068 */     StringBuilder simpleBinaryValueResult = new StringBuilder();
/* 2069 */     String leftExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(inputNode.getLeftOperand());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2075 */     if (null != leftExpr)
/*      */     {
/* 2077 */       return null;
/*      */     }
/*      */     
/* 2080 */     String rightExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(inputNode.getRightOperand());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2086 */     if (null != rightExpr)
/*      */     {
/* 2088 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 2092 */     return "(" + leftExpr + inputOperation + rightExpr + ")";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String defaultVisit(IAENode node)
/*      */     throws ErrorException
/*      */   {
/* 2103 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*      */     
/* 2105 */     return null;
/*      */   }
/*      */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/querygeneration/CBValueExprBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
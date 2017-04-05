/*     */ package com.simba.couchbase.dataengine.querygeneration;
/*     */ 
/*     */ import com.simba.couchbase.dataengine.CBSQLDataEngine;
/*     */ import com.simba.couchbase.dataengine.resultset.CBResultSet;
/*     */ import com.simba.couchbase.dataengine.resultset.CBTableColumn;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.IAENode;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEAnd;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEExistsPredicate;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AELikePredicate;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEOr;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.bool.AEQuantifiedComparison;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
/*     */ import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
/*     */ import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBBooleanExprBuilder
/*     */   extends AEDefaultVisitor<String>
/*     */ {
/*     */   private CBSQLDataEngine m_dataEngine;
/*     */   private String m_expression;
/*     */   private ILogger m_log;
/*     */   private Map<String, CBTableColumn> m_newLetClauses;
/*     */   private List<CBParamValue> m_newParamValues;
/*     */   private Map<String, CBTableColumn> m_outputLetClauses;
/*     */   private List<CBParamValue> m_outputParamValues;
/*     */   private Map<DSIExtJResultSet, CBResultSet> m_tableCopyMap;
/*     */   
/*     */   public CBBooleanExprBuilder(CBSQLDataEngine dataEngine, Map<String, CBTableColumn> outputLetExpression, List<CBParamValue> outputParamValues, Map<DSIExtJResultSet, CBResultSet> tableCopyMap, ILogger log)
/*     */   {
/* 100 */     LogUtilities.logFunctionEntrance(log, new Object[0]);
/* 101 */     this.m_log = log;
/* 102 */     this.m_dataEngine = dataEngine;
/* 103 */     this.m_tableCopyMap = tableCopyMap;
/*     */     
/* 105 */     this.m_outputLetClauses = outputLetExpression;
/* 106 */     this.m_outputParamValues = outputParamValues;
/*     */     
/*     */ 
/* 109 */     this.m_newLetClauses = new HashMap();
/* 110 */     this.m_newParamValues = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String buildExpression(AEBooleanExpr inputNode)
/*     */   {
/* 124 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     try
/*     */     {
/* 127 */       this.m_expression = ((String)inputNode.acceptVisitor(this));
/* 128 */       if (null != this.m_expression)
/*     */       {
/* 130 */         this.m_outputLetClauses.putAll(this.m_newLetClauses);
/* 131 */         this.m_outputParamValues.addAll(this.m_newParamValues);
/*     */       }
/*     */       
/* 134 */       LogUtilities.logInfo("Current Expression: " + this.m_expression, this.m_log);
/* 135 */       return this.m_expression;
/*     */     }
/*     */     catch (Exception ex) {}
/*     */     
/*     */ 
/*     */ 
/* 141 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visit(AEAnd andNode)
/*     */   {
/* 150 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 151 */     StringBuilder andResult = new StringBuilder();
/* 152 */     String leftExpr = new CBBooleanExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(andNode.getLeftOperand());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */     if (null == leftExpr)
/*     */     {
/* 160 */       return null;
/*     */     }
/*     */     
/* 163 */     String rightExpr = new CBBooleanExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(andNode.getRightOperand());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 169 */     if (null == rightExpr)
/*     */     {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     andResult.append("(").append(leftExpr).append(" AND ").append(rightExpr).append(")");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */     return andResult.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visit(AEComparison compareNode)
/*     */     throws ErrorException
/*     */   {
/* 190 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 191 */     StringBuilder compBuilder = new StringBuilder();
/* 192 */     AEValueExprList leftOperand = compareNode.getLeftOperand();
/* 193 */     AEValueExprList rightOperand = compareNode.getRightOperand();
/*     */     
/*     */ 
/* 196 */     String leftExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)leftOperand.getChild(0));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */     if (null == leftExpr)
/*     */     {
/* 204 */       return null;
/*     */     }
/*     */     
/* 207 */     String rightExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)rightOperand.getChild(0));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 213 */     if (null == rightExpr)
/*     */     {
/* 215 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 219 */     compBuilder.append("(").append(leftExpr).append(compareNode.getComparisonOp()).append(rightExpr).append(")");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 226 */     return compBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String visit(AEExistsPredicate exiPredNode)
/*     */   {
/* 232 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 234 */     return null;
/*     */   }
/*     */   
/*     */   public String visit(AEInPredicate inPredNode)
/*     */     throws ErrorException
/*     */   {
/* 240 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 241 */     StringBuilder inPredBuilder = new StringBuilder();
/* 242 */     AEValueExprList leftOperand = inPredNode.getLeftOperand();
/* 243 */     String leftExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)leftOperand.getChild(0));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 249 */     if (null == leftExpr)
/*     */     {
/* 251 */       return null;
/*     */     }
/*     */     
/* 254 */     inPredBuilder.append("(").append(leftExpr).append(" IN [");
/*     */     
/*     */ 
/*     */ 
/* 258 */     int childNum = inPredNode.getRightOperand().getNumChildren();
/* 259 */     AEValueExprList rightOperand = (AEValueExprList)inPredNode.getRightOperand();
/* 260 */     boolean comma = false;
/* 261 */     for (int childIndex = 0; childIndex < childNum; childIndex++)
/*     */     {
/* 263 */       if (comma)
/*     */       {
/* 265 */         inPredBuilder.append(",");
/*     */       }
/* 267 */       String childExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)rightOperand.getChild(childIndex));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 273 */       inPredBuilder.append(childExpr);
/* 274 */       comma = true;
/*     */     }
/* 276 */     inPredBuilder.append("])");
/* 277 */     return inPredBuilder.toString();
/*     */   }
/*     */   
/*     */   public String visit(AELikePredicate likePredNode)
/*     */     throws ErrorException
/*     */   {
/* 283 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 284 */     AEValueExpr leftOp = likePredNode.getLeftOperand();
/* 285 */     AEValueExpr rightOp = likePredNode.getRightOperand();
/* 286 */     StringBuilder likeBuilder = new StringBuilder();
/* 287 */     String leftExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(leftOp);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */     if (null == leftExpr)
/*     */     {
/* 295 */       return null;
/*     */     }
/*     */     
/* 298 */     String rightExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression(rightOp);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 304 */     if (null == rightExpr)
/*     */     {
/* 306 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 310 */     AEValueExpr escapeNode = likePredNode.getEscapeChar();
/*     */     
/* 312 */     if (null != escapeNode)
/*     */     {
/*     */ 
/* 315 */       return null;
/*     */     }
/*     */     
/* 318 */     likeBuilder.append("(").append(leftExpr).append(" LIKE ").append(rightExpr).append(")");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 325 */     return likeBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String visit(AENot notNode)
/*     */   {
/* 331 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 332 */     StringBuilder notResult = new StringBuilder();
/* 333 */     String operand = new CBBooleanExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(notNode.getOperand());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 339 */     if (null == operand)
/*     */     {
/* 341 */       return null;
/*     */     }
/*     */     
/* 344 */     return "(NOT " + operand + ")";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visit(AENullPredicate nullNode)
/*     */     throws ErrorException
/*     */   {
/* 353 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 354 */     StringBuilder nullResult = new StringBuilder();
/* 355 */     AEValueExprList operand = nullNode.getOperand();
/* 356 */     String operandExpr = new CBValueExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_tableCopyMap, this.m_newParamValues, this.m_log).buildExpression((AEValueExpr)operand.getChild(0));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 363 */     if (null == operandExpr)
/*     */     {
/* 365 */       return null;
/*     */     }
/*     */     
/* 368 */     return "(" + operandExpr + " IS NULL)";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visit(AEOr orNode)
/*     */   {
/* 378 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/* 379 */     StringBuilder orResult = new StringBuilder();
/* 380 */     String leftExpr = new CBBooleanExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(orNode.getLeftOperand());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 386 */     if (null == leftExpr)
/*     */     {
/* 388 */       return null;
/*     */     }
/*     */     
/* 391 */     String rightExpr = new CBBooleanExprBuilder(this.m_dataEngine, this.m_newLetClauses, this.m_newParamValues, this.m_tableCopyMap, this.m_log).buildExpression(orNode.getRightOperand());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 397 */     if (null == rightExpr)
/*     */     {
/* 399 */       return null;
/*     */     }
/*     */     
/* 402 */     return "(" + leftExpr + " OR " + rightExpr + ")";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visit(AEQuantifiedComparison quantifiedCompareNode)
/*     */   {
/* 413 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 415 */     return null;
/*     */   }
/*     */   
/*     */   protected String defaultVisit(IAENode node)
/*     */     throws ErrorException
/*     */   {
/* 421 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 423 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/querygeneration/CBBooleanExprBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
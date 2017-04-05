/*     */ package com.simba.couchbase.dataengine;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.simba.couchbase.client.N1QLPlan;
/*     */ import com.simba.couchbase.client.N1QLQueryResult;
/*     */ import com.simba.couchbase.client.N1QLQueryRow;
/*     */ import com.simba.couchbase.client.N1QLRowCountSet;
/*     */ import com.simba.couchbase.core.CBClient;
/*     */ import com.simba.couchbase.core.CBConnectionSettings;
/*     */ import com.simba.couchbase.dataengine.resultset.CBNativeResultSet;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.utils.CBCoreUtils;
/*     */ import com.simba.couchbase.utils.CBDataTypeUtils;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.dsi.dataengine.impl.DSIEmptyResultSet;
/*     */ import com.simba.dsi.dataengine.impl.DSISimpleRowCountResult;
/*     */ import com.simba.dsi.dataengine.interfaces.IQueryExecutor;
/*     */ import com.simba.dsi.dataengine.utilities.ColumnMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.dataengine.utilities.ExecutionContext;
/*     */ import com.simba.dsi.dataengine.utilities.ExecutionContexts;
/*     */ import com.simba.dsi.dataengine.utilities.ExecutionResults;
/*     */ import com.simba.dsi.dataengine.utilities.ParameterInputValue;
/*     */ import com.simba.dsi.dataengine.utilities.ParameterMetadata;
/*     */ import com.simba.dsi.dataengine.utilities.ParameterType;
/*     */ import com.simba.dsi.dataengine.utilities.TypeMetadata;
/*     */ import com.simba.dsi.exceptions.BadDefaultParamException;
/*     */ import com.simba.dsi.exceptions.DefaultParamException;
/*     */ import com.simba.dsi.exceptions.ExecutingException;
/*     */ import com.simba.dsi.exceptions.OperationCanceledException;
/*     */ import com.simba.dsi.exceptions.ParamAlreadyPushedException;
/*     */ import com.simba.dsi.exceptions.ParsingException;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.IWarningListener;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class CBNativeQueryExecutor
/*     */   implements IQueryExecutor
/*     */ {
/*     */   private CBClient m_client;
/*     */   private ILogger m_log;
/*     */   private String m_query;
/*     */   private ExecutionResults m_results;
/*     */   private N1QLQueryResult m_currentQueryResult;
/*     */   private boolean m_isRowCountQuery;
/*     */   private boolean m_isDirectExecution;
/*     */   private N1QLPlan m_prepareQueryPlan;
/* 107 */   public static List<String> m_subqueryAliases = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArrayList<ColumnMetadata> m_metadataColumns;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, Short> m_queryOriginalIndexPair;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean m_isParamPrepareStage;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 128 */   private int m_prepareWithParameterCounter = 0;
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
/*     */   private ArrayList<ParameterMetadata> m_paramMetadataList;
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
/*     */   private ArrayList<CBParamInfo> m_paramInfoList;
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
/*     */   public CBNativeQueryExecutor(String query, CBConnectionSettings settings, CBClient client, boolean isDirectExecution, ILogger log)
/*     */     throws ErrorException
/*     */   {
/* 167 */     LogUtilities.logFunctionEntrance(log, new Object[] { query, this.m_client, Boolean.valueOf(isDirectExecution) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */     this.m_query = query;
/* 174 */     this.m_log = log;
/* 175 */     this.m_client = client;
/* 176 */     this.m_isDirectExecution = isDirectExecution;
/* 177 */     this.m_isRowCountQuery = CBCoreUtils.isRowCountResult(this.m_query);
/* 178 */     this.m_results = new ExecutionResults();
/* 179 */     this.m_paramInfoList = new ArrayList();
/*     */     
/* 181 */     this.m_prepareWithParameterCounter = CBCoreUtils.parameterCounter(query, this.m_paramInfoList);
/*     */     
/* 183 */     if (this.m_prepareWithParameterCounter > 0)
/*     */     {
/* 185 */       this.m_isParamPrepareStage = true;
/*     */     }
/*     */     
/*     */ 
/* 189 */     this.m_prepareQueryPlan = this.m_client.preparedStatement(this.m_query, null, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */     if (this.m_isRowCountQuery)
/*     */     {
/* 197 */       this.m_results.addRowCountResult(new DSISimpleRowCountResult(0L));
/*     */     }
/*     */     else
/*     */     {
/* 201 */       this.m_results.addResultSet(new DSIEmptyResultSet());
/*     */     }
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
/*     */   public void cancelExecute()
/*     */     throws ErrorException
/*     */   {
/* 216 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearCancel()
/*     */   {
/* 226 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
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
/*     */   public void clearPushedParamData()
/*     */     throws ErrorException
/*     */   {
/* 240 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 242 */     this.m_paramMetadataList.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 259 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
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
/*     */   public void execute(ExecutionContexts contexts, IWarningListener warningListener)
/*     */     throws BadDefaultParamException, ParsingException, ExecutingException, OperationCanceledException, ErrorException
/*     */   {
/* 288 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { contexts, warningListener });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 293 */     this.m_results.getResults().clear();
/*     */     
/*     */ 
/* 296 */     Iterator<ExecutionContext> parameterItr = contexts.contextIterator();
/* 297 */     while (parameterItr.hasNext())
/*     */     {
/* 299 */       ExecutionContext paramDataContext = (ExecutionContext)parameterItr.next();
/*     */       
/* 301 */       ArrayList<ParameterInputValue> paramList = new ArrayList();
/* 302 */       ArrayList<String> posParams = new ArrayList();
/* 303 */       HashMap<String, String> namedParams = new HashMap();
/*     */       
/*     */ 
/*     */ 
/* 307 */       if (this.m_prepareWithParameterCounter > 0)
/*     */       {
/* 309 */         for (int i = 0; i < paramDataContext.getInputs().size(); i++)
/*     */         {
/* 311 */           ParameterInputValue paramDataStruct = (ParameterInputValue)paramDataContext.getInputs().get(i);
/* 312 */           paramList.add(paramDataStruct);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 318 */         for (int i = 0; i < paramList.size(); i++)
/*     */         {
/* 320 */           CBParamInfo paramInfo = (CBParamInfo)this.m_paramInfoList.get(i);
/*     */           
/*     */           try
/*     */           {
/* 324 */             DataWrapper paramData = ((ParameterInputValue)paramList.get(i)).getData();
/* 325 */             String paramValue = CBCoreUtils.getParameterValue(paramData);
/*     */             
/* 327 */             if (paramInfo.isPositional())
/*     */             {
/* 329 */               posParams.add(paramInfo.getPosition() - 1, paramValue);
/*     */             }
/*     */             else
/*     */             {
/* 333 */               namedParams.put(paramInfo.getName(), paramValue);
/*     */             }
/*     */           }
/*     */           catch (ParamAlreadyPushedException ex)
/*     */           {
/* 338 */             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.PARAMETER_INPUT_VALUE_ERROR.name(), new String[] { ex.getMessage() });
/*     */             
/*     */ 
/* 341 */             throw err;
/*     */           }
/*     */           catch (DefaultParamException ex)
/*     */           {
/* 345 */             ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.PARAMETER_INPUT_VALUE_ERROR.name(), new String[] { ex.getMessage() });
/*     */             
/*     */ 
/* 348 */             throw err;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 353 */       if (!this.m_isRowCountQuery)
/*     */       {
/*     */ 
/*     */ 
/* 357 */         if (this.m_isDirectExecution)
/*     */         {
/* 359 */           this.m_currentQueryResult = this.m_client.executeStatement(this.m_query, posParams, namedParams);
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 366 */           this.m_currentQueryResult = this.m_client.executePreparedStatement(this.m_prepareQueryPlan, posParams, namedParams);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 374 */         initColumnMetadata();
/*     */         
/*     */ 
/* 377 */         CBNativeResultSet result = new CBNativeResultSet(true, this.m_currentQueryResult, this.m_metadataColumns, this.m_queryOriginalIndexPair, null, false, true, this.m_log);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 387 */         this.m_results.addResultSet(result);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 392 */         N1QLRowCountSet rowCountSet = this.m_client.executeUpdate(this.m_query, posParams, namedParams);
/*     */         
/*     */ 
/*     */ 
/* 396 */         int tempRowCount = rowCountSet.getRowCount();
/* 397 */         this.m_results.addRowCountResult(new DSISimpleRowCountResult(tempRowCount));
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finalizePushedParamData()
/*     */     throws ErrorException
/*     */   {
/* 418 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<ParameterMetadata> getMetadataForParameters()
/*     */     throws ErrorException
/*     */   {
/* 437 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 441 */     if (this.m_prepareWithParameterCounter > 0)
/*     */     {
/* 443 */       if (this.m_isParamPrepareStage)
/*     */       {
/*     */ 
/* 446 */         this.m_paramMetadataList = new ArrayList();
/* 447 */         for (int parameterIndex = 0; parameterIndex < this.m_prepareWithParameterCounter; parameterIndex++)
/*     */         {
/* 449 */           ParameterMetadata tempParameter = new ParameterMetadata(parameterIndex, ParameterType.INPUT, 12);
/*     */           
/*     */ 
/*     */ 
/* 453 */           this.m_paramMetadataList.add(tempParameter);
/*     */         }
/*     */         
/* 456 */         this.m_isParamPrepareStage = false;
/*     */       }
/*     */       
/* 459 */       return this.m_paramMetadataList;
/*     */     }
/*     */     
/*     */ 
/* 463 */     return new ArrayList();
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
/*     */   public int getNumParams()
/*     */     throws ErrorException
/*     */   {
/* 477 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 481 */     return 0;
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
/*     */   public ExecutionResults getResults()
/*     */     throws ErrorException
/*     */   {
/* 495 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 497 */     if (null == this.m_results)
/*     */     {
/*     */ 
/* 500 */       this.m_results = new ExecutionResults();
/*     */     }
/*     */     
/* 503 */     return this.m_results;
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
/*     */   public void pushParamData(int parameterSet, ParameterInputValue value)
/*     */     throws BadDefaultParamException, ErrorException
/*     */   {
/* 540 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[] { Integer.valueOf(parameterSet), value });
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
/*     */   public void pushMappedParamTypes(Map<Integer, TypeMetadata> setParameterMetadata)
/*     */     throws ErrorException
/*     */   {
/* 575 */     if (this.m_prepareWithParameterCounter > 0)
/*     */     {
/* 577 */       this.m_paramMetadataList.clear();
/*     */       
/*     */ 
/* 580 */       if (null == setParameterMetadata)
/*     */       {
/*     */ 
/*     */ 
/* 584 */         for (int paramIndex = 0; paramIndex < this.m_prepareWithParameterCounter; paramIndex++)
/*     */         {
/* 586 */           ParameterMetadata param = new ParameterMetadata(paramIndex + 1, ParameterType.INPUT, 12);
/*     */           
/*     */ 
/* 589 */           this.m_paramMetadataList.add(param);
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 594 */         for (int paramIndex = 0; paramIndex < this.m_prepareWithParameterCounter; paramIndex++)
/*     */         {
/*     */ 
/* 597 */           TypeMetadata typeMetadata = (TypeMetadata)setParameterMetadata.get(Integer.valueOf(paramIndex));
/* 598 */           short parameterType; short parameterType; if (null == typeMetadata)
/*     */           {
/*     */ 
/*     */ 
/* 602 */             parameterType = 12;
/*     */           }
/*     */           else
/*     */           {
/* 606 */             parameterType = ((TypeMetadata)setParameterMetadata.get(Integer.valueOf(paramIndex))).getType();
/*     */           }
/* 608 */           ParameterMetadata param = new ParameterMetadata(paramIndex + 1, ParameterType.INPUT, parameterType);
/*     */           
/*     */ 
/* 611 */           this.m_paramMetadataList.add(param);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initColumnMetadata()
/*     */     throws ErrorException
/*     */   {
/* 624 */     LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
/*     */     
/* 626 */     if (!this.m_isRowCountQuery)
/*     */     {
/*     */ 
/* 629 */       if (this.m_query.trim().toUpperCase().startsWith("EXPLAIN"))
/*     */       {
/* 631 */         this.m_metadataColumns = CBDataTypeUtils.createFlattenColumn("Couchbase", "", "", this.m_currentQueryResult.getFirstRow().value(), this.m_log);
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
/*     */       }
/* 643 */       else if (this.m_prepareQueryPlan.getSignature().fieldNames().hasNext())
/*     */       {
/* 645 */         String signature = (String)this.m_prepareQueryPlan.getSignature().fieldNames().next();
/*     */         
/* 647 */         if (signature.equals("*"))
/*     */         {
/* 649 */           String schemaName = this.m_prepareQueryPlan.allRowsRawJson().findValue("namespace").textValue();
/*     */           
/*     */ 
/* 652 */           String tableName = this.m_prepareQueryPlan.allRowsRawJson().findValue("keyspace").textValue();
/*     */           
/*     */ 
/*     */ 
/* 656 */           ArrayList<N1QLQueryRow> allRows = this.m_currentQueryResult.allRowsRawData();
/*     */           
/* 658 */           if (!allRows.isEmpty())
/*     */           {
/* 660 */             JsonNode SchemaMapRow = ((N1QLQueryRow)allRows.get(0)).value().findValue("Delimiter");
/*     */             
/*     */ 
/*     */ 
/* 664 */             if ((SchemaMapRow != null) && (SchemaMapRow.textValue().contentEquals("~~~SchemaMap")))
/*     */             {
/*     */ 
/*     */ 
/* 668 */               if (this.m_currentQueryResult.getRowCount() == 1L)
/*     */               {
/* 670 */                 ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SCHEMAMAP_ONLY_RESULT_ERROR.name(), new String[0]);
/*     */                 
/*     */ 
/* 673 */                 throw err;
/*     */               }
/*     */               
/* 676 */               this.m_metadataColumns = CBDataTypeUtils.createFlattenColumn("Couchbase", schemaName, tableName, ((N1QLQueryRow)allRows.get(1)).value(), this.m_log);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 687 */               this.m_metadataColumns = CBDataTypeUtils.createFlattenColumn("Couchbase", schemaName, tableName, ((N1QLQueryRow)allRows.get(0)).value(), this.m_log);
/*     */ 
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 698 */           this.m_metadataColumns = CBDataTypeUtils.createColumnFromSignature("Couchbase", this.m_prepareQueryPlan.getFirstRow().value(), this.m_log);
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 707 */         this.m_metadataColumns = CBDataTypeUtils.createColumnFromSignature("Couchbase", this.m_prepareQueryPlan.getFirstRow().value(), this.m_log);
/*     */       }
/*     */     }
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
/* 732 */     short originalQueryIndex = 0;
/* 733 */     this.m_queryOriginalIndexPair = new HashMap();
/* 734 */     for (ColumnMetadata column : this.m_metadataColumns)
/*     */     {
/* 736 */       this.m_queryOriginalIndexPair.put(column.getLabel(), Short.valueOf(originalQueryIndex));
/* 737 */       originalQueryIndex = (short)(originalQueryIndex + 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/CBNativeQueryExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
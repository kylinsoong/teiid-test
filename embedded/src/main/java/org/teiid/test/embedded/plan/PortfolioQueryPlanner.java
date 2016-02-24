package org.teiid.test.embedded.plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.teiid.core.types.DataTypeManager;
import org.teiid.metadata.Schema;
import org.teiid.metadata.Table;
import org.teiid.query.optimizer.relational.AliasGenerator;
import org.teiid.query.optimizer.relational.plantree.NodeConstants;
import org.teiid.query.optimizer.relational.plantree.NodeEditor;
import org.teiid.query.optimizer.relational.plantree.NodeFactory;
import org.teiid.query.optimizer.relational.plantree.PlanNode;
import org.teiid.query.optimizer.relational.plantree.NodeConstants.Info;
import org.teiid.query.processor.relational.AccessNode;
import org.teiid.query.processor.relational.RelationalNode;
import org.teiid.query.processor.relational.RelationalPlan;
import org.teiid.query.sql.LanguageObject.Util;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.sql.lang.From;
import org.teiid.query.sql.lang.FromClause;
import org.teiid.query.sql.lang.Query;
import org.teiid.query.sql.lang.QueryCommand;
import org.teiid.query.sql.lang.Select;
import org.teiid.query.sql.lang.SourceHint;
import org.teiid.query.sql.lang.UnaryFromClause;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.GroupSymbol;
import org.teiid.query.sql.visitor.GroupsUsedByElementsVisitor;

public class PortfolioQueryPlanner {
    
    static Query command;
    
    static {
        command = new Query();
        
        From from = new From();
        UnaryFromClause clause = new UnaryFromClause();
        GroupSymbol group = new GroupSymbol("A", "Accounts.PRODUCT");
        clause.setGroup(group);
        from.addClause(clause);
        command.setFrom(from);
        
        ElementSymbol id = new ElementSymbol("ID", group);
        id.setType(DataTypeManager.DefaultDataClasses.STRING);
        ElementSymbol symbol = new ElementSymbol("SYMBOL", group);
        symbol.setType(DataTypeManager.DefaultDataClasses.STRING);
        ElementSymbol name = new ElementSymbol("COMPANY_NAME", group);
        symbol.setType(DataTypeManager.DefaultDataClasses.STRING);

        Select select = new Select();
        select.addSymbol(id);
        select.addSymbol(symbol);
        select.addSymbol(name);
        command.setSelect(select);
        
    }

    public static void main(String[] args) {

        // 1. GENERATE CANONICAL
        PlanNode plan = generate_canonical_plan();
        
        // 2. EXECUTING PlaceAccess
        execute_placeAccess(plan);
        
        // 3. EXECUTING RaiseAccess
        plan = execute_raiseAccess(plan);
        
        // 4. EXECUTING AssignOutputElements
        plan = execute_assignOutputElements(plan);
        
        // 5. EXECUTING CalculateCost
        plan = execute_calculateCost(plan);
        
        // 6. EXECUTING PlanSorts
        plan = execute_planSorts(plan);
        
        // 7. EXECUTING CollapseSource
        plan = execute_collapseSource(plan);
        
        // 8. CONVERTING PLAN TREE TO PROCESS TREE
        RelationalPlan result = logic_plan_to_processor_plan(plan);
        
        List<Expression> topCols = Util.deepClone(command.getProjectedSymbols(), Expression.class);
        result.setOutputElements(topCols);
        
        prompt("OPTIMIZATION COMPLETE");
        
        println("PROCESSOR PLAN: " + result);
        
        println(result.getDescriptionProperties());
    }

    private static RelationalPlan logic_plan_to_processor_plan(PlanNode node) {
        
        prompt("CONVERTING PLAN TREE TO PROCESS TREE");
        
        Command command = (Command) node.getProperty(NodeConstants.Info.ATOMIC_REQUEST);
        Object modelID = node.getProperty(NodeConstants.Info.MODEL_ID);
        
        AccessNode aNode = new AccessNode(1);
        RelationalNode processNode = aNode;
        
        aNode.setModelName("Accounts");
        aNode.setModelId(modelID);
        aNode.setConformedTo((Set<Object>) node.getProperty(Info.CONFORMED_SOURCES));
        
        AliasGenerator visitor = new AliasGenerator(true, false);
        command.acceptVisitor(visitor);
        
        aNode.setShouldEvaluateExpressions(false);
        aNode.setCommand(command);
        
        aNode.minimizeProject(command);
        
        List cols = (List) node.getProperty(NodeConstants.Info.OUTPUT_COLS);
        processNode.setElements(cols);
        
        Number estimateNodeCardinality = (Number) node.getProperty(NodeConstants.Info.EST_CARDINALITY);
        processNode.setEstimateNodeCardinality(estimateNodeCardinality);
        Number estimateNodeSetSize = (Number) node.getProperty(NodeConstants.Info.EST_SET_SIZE);
        processNode.setEstimateNodeSetSize(estimateNodeSetSize);
        Number estimateDepAccessCardinality = (Number) node.getProperty(NodeConstants.Info.EST_DEP_CARDINALITY);
        processNode.setEstimateDepAccessCardinality(estimateDepAccessCardinality);
        Number estimateDepJoinCost = (Number) node.getProperty(NodeConstants.Info.EST_DEP_JOIN_COST);
        processNode.setEstimateDepJoinCost(estimateDepJoinCost);
        Number estimateJoinCost = (Number) node.getProperty(NodeConstants.Info.EST_JOIN_COST);
        processNode.setEstimateJoinCost(estimateJoinCost);
        
        println("PROCESS PLAN = " + processNode);
        
        RelationalPlan processPlan = new RelationalPlan(processNode);
        
        return processPlan;
    }

    private static PlanNode execute_collapseSource(PlanNode plan) {
        
        prompt("EXECUTING CollapseSource");
        
        PlanNode accessNode = NodeEditor.findAllNodes(plan, NodeConstants.Types.ACCESS).get(0);
        
        Query query = new Query();
        Select select = new Select();
        List<Expression> columns = (List<Expression>)accessNode.getProperty(NodeConstants.Info.OUTPUT_COLS);
        select.addSymbols(columns);
        query.setSelect(select);
        query.setFrom(new From());
        
        PlanNode node = plan.getChildren().get(0).getChildren().get(0);
        GroupSymbol symbol = node.getGroups().iterator().next();
        query.getFrom().addGroup(symbol);
        
        From from = query.getFrom();
        List<FromClause> clauses = from.getClauses();
        FromClause rootClause = clauses.get(0);
        
        from.setClauses(new ArrayList<FromClause>());
        query.getFrom().addClause(rootClause);
        
        QueryCommand queryCommand = query;
        queryCommand.setSourceHint((SourceHint) accessNode.getProperty(Info.SOURCE_HINT));
        queryCommand.getProjectedQuery().setSourceHint((SourceHint) accessNode.getProperty(Info.SOURCE_HINT));
        
        accessNode.setProperty(NodeConstants.Info.ATOMIC_REQUEST, command);
        accessNode.removeAllChildren();
        
        println(plan.nodeToString(true));
        
        return plan;
    }

    private static PlanNode execute_planSorts(PlanNode plan) {
        
        prompt("EXECUTING PlanSorts");
        
        // to set 'modified' to false;
        plan.nodeToString(true);
        
        println(plan.nodeToString(true));
        
        return plan;
    }

    private static PlanNode execute_calculateCost(PlanNode plan) {
        
        prompt("EXECUTING CalculateCost");
        
        PlanNode node = plan.getChildren().get(0);
        node = node.getChildren().get(0);
        
        float cost = -1.0f;
        
        GroupSymbol group = node.getGroups().iterator().next();
        
        List<? extends Expression> outputCols = (List<Expression>)node.getProperty(Info.OUTPUT_COLS);
        
        ColStats colStats = new ColStats();
        for (Expression expr : outputCols) {
            ElementSymbol es = (ElementSymbol)expr;
            float[] vals = new float[2];
            vals[0] = cost;
            vals[1] = cost;
            colStats.put(es, vals);
        }
        
        node.setProperty(Info.EST_COL_STATS, colStats);
        node.setProperty(NodeConstants.Info.EST_CARDINALITY, cost);
        node.getParent().setProperty(Info.EST_CARDINALITY, cost);
        node.getParent().getParent().setProperty(Info.EST_CARDINALITY, cost);
        
        println(plan);
        
        return plan;
    }
    
    private static class ColStats extends LinkedHashMap<Expression, float[]> {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            
            int j = 0;
            for (Iterator<Entry<Expression, float[]>> i = this.entrySet().iterator(); i.hasNext();) {
                Entry<Expression, float[]> e = i.next();
                sb.append(e.getKey());
                sb.append('=');
                sb.append(Arrays.toString(e.getValue()));
                j++;
                if (i.hasNext()) {
                    sb.append(", "); //$NON-NLS-1$
                    if (j > 3) {
                        sb.append("..."); //$NON-NLS-1$
                        break;
                    }
                }
            }
            return sb.append('}').toString();
        }
    }

    private static PlanNode execute_assignOutputElements(PlanNode plan) {
        
        prompt("EXECUTING AssignOutputElements");
        
        PlanNode projectNode = NodeEditor.findNodePreOrder(plan, NodeConstants.Types.PROJECT);
        List<Expression> projectCols = (List<Expression>)projectNode.getProperty(NodeConstants.Info.PROJECT_COLS);
        
        plan.setProperty(NodeConstants.Info.OUTPUT_COLS, projectCols);
        
        PlanNode root = plan.getLastChild();
        root.setProperty(NodeConstants.Info.OUTPUT_COLS, projectCols);
        root.setProperty(NodeConstants.Info.PROJECT_COLS, projectCols);
        
        root = root.getLastChild();
        root.setProperty(NodeConstants.Info.OUTPUT_COLS, projectCols);
        
        println(plan);
        
        return plan;
    }

    private static PlanNode execute_raiseAccess(PlanNode rootNode) {

        prompt("EXECUTING RaiseAccess");
        
        PlanNode accessNode = NodeEditor.findAllNodes(rootNode, NodeConstants.Types.ACCESS).get(0);
        
        PlanNode parentNode = accessNode.getParent();

        accessNode.setProperty(Info.SOURCE_HINT, SourceHint.combine((SourceHint)parentNode.getProperty(Info.SOURCE_HINT), (SourceHint)accessNode.getProperty(Info.SOURCE_HINT)));
        NodeEditor.removeChildNode(parentNode, accessNode);
        parentNode.addAsParent(accessNode);
        
        rootNode = accessNode;
        
        println(rootNode);
        
        return rootNode;
    }

    private static void execute_placeAccess(PlanNode plan) {
        
        prompt("EXECUTING PlaceAccess");
        
        PlanNode sourceNode = NodeEditor.findAllNodes(plan, NodeConstants.Types.SOURCE).get(0);
                
        PlanNode accessNode = NodeFactory.getNewNode(NodeConstants.Types.ACCESS);
        accessNode.addGroups(sourceNode.getGroups());
        accessNode.setProperty(Info.SOURCE_HINT, null);
        
        Object modelId = getMetadataTable();
        accessNode.setProperty(NodeConstants.Info.MODEL_ID, modelId);
        
        sourceNode.addAsParent(accessNode);
                
        println(plan);
    }

    private static Object getMetadataTable() {
        Schema schema = new Schema();
        schema.setName("Accounts");
        schema.setUUID("tid:b7365020b6df-84782006-00000000");
        Table table = new Table();
        table.setName("PRODUCT");
        table.setNameInSource("\"ACCOUNT\".\"PUBLIC\".\"PRODUCT\"");
        table.setUUID("tid:b7365020b6df-185958cf-00000022");
        schema.addTable(table);
        return schema;
    }

    private static PlanNode generate_canonical_plan() {
        
        prompt("GENERATE CANONICAL PLAN");

        UnaryFromClause ufc = (UnaryFromClause) command.getFrom().getClauses().get(0);
        
        PlanNode parent = new PlanNode();
        
        GroupSymbol group = ufc.getGroup();
        PlanNode node = NodeFactory.getNewNode(NodeConstants.Types.SOURCE);
        node.addGroup(group);
        
        parent.addLastChild(node);
        
        PlanNode plan = parent.getFirstChild();
        
        List<? extends Expression> select = command.getSelect().getProjectedSymbols();
        PlanNode projectNode = NodeFactory.getNewNode(NodeConstants.Types.PROJECT);
        projectNode.setProperty(NodeConstants.Info.PROJECT_COLS, select);
        projectNode.addGroups(GroupsUsedByElementsVisitor.getGroups(select));
        projectNode.addLastChild(plan);
        
        plan = projectNode;
        
        println(plan);
        
        return plan;
    }

    private static void prompt(String string) {
        println("----------------------------------------------------------------------------------------------");
        println("\n\t" + string + "\n");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void println(Object obj) {
        System.out.println(obj);
    }

}

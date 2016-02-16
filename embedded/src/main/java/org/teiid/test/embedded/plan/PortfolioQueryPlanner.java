package org.teiid.test.embedded.plan;

import java.util.List;

import org.teiid.core.types.DataTypeManager;
import org.teiid.metadata.Schema;
import org.teiid.metadata.Table;
import org.teiid.query.optimizer.relational.plantree.NodeConstants;
import org.teiid.query.optimizer.relational.plantree.NodeEditor;
import org.teiid.query.optimizer.relational.plantree.NodeFactory;
import org.teiid.query.optimizer.relational.plantree.PlanNode;
import org.teiid.query.optimizer.relational.plantree.NodeConstants.Info;
import org.teiid.query.sql.LanguageObject.Util;
import org.teiid.query.sql.lang.From;
import org.teiid.query.sql.lang.Query;
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
        
        // 5. EXECUTING CalculateCost
        
        // 6. EXECUTING PlanSorts
        
        // 7. EXECUTING CollapseSource
        
        // 8. CONVERTING PLAN TREE TO PROCESS TREE
        
        List<Expression> topCols = Util.deepClone(command.getProjectedSymbols(), Expression.class);
        
        println(plan);
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
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void println(Object obj) {
        System.out.println(obj);
    }

}

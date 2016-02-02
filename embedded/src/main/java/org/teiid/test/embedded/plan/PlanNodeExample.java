package org.teiid.test.embedded.plan;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.optimizer.relational.plantree.NodeConstants;
import org.teiid.query.optimizer.relational.plantree.NodeEditor;
import org.teiid.query.optimizer.relational.plantree.NodeFactory;
import org.teiid.query.optimizer.relational.plantree.PlanNode;
import org.teiid.query.optimizer.relational.plantree.NodeConstants.Info;
import org.teiid.query.sql.lang.Select;
import org.teiid.query.sql.lang.SubqueryContainer;
import org.teiid.query.sql.lang.WithQueryCommand;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.GroupSymbol;
import org.teiid.query.sql.visitor.GroupsUsedByElementsVisitor;

public class PlanNodeExample {

    public static void main(String[] args) {

        PlanNode node = NodeFactory.getNewNode(NodeConstants.Types.SOURCE);
        System.out.println(node);
        
        node.addGroup(new GroupSymbol("Accounts.PRODUCT"));
        System.out.println(node);
        
        PlanNode parent = new PlanNode();
        parent.addLastChild(node);
        System.out.println(parent);
        
        PlanNode plan = parent.getFirstChild();
        System.out.println(plan);
        
        List<? extends Expression> select = getProjectedSymbols();
        PlanNode projectNode = NodeFactory.getNewNode(NodeConstants.Types.PROJECT);
        projectNode.setProperty(NodeConstants.Info.PROJECT_COLS, select);
        projectNode.addGroups(GroupsUsedByElementsVisitor.getGroups(select));
        System.out.println(projectNode);
        
        projectNode.addLastChild(plan);
        plan = projectNode;
        System.out.println(plan);
        
        List<PlanNode> list = NodeEditor.findAllNodes(plan, NodeConstants.Types.PROJECT | NodeConstants.Types.SELECT | NodeConstants.Types.JOIN | NodeConstants.Types.SOURCE | NodeConstants.Types.GROUP | NodeConstants.Types.SORT);
        for (PlanNode n : list) {
            node.addGroups(GroupsUsedByElementsVisitor.getGroups(n.getCorrelatedReferenceElements()));
        }
        
        PlanNode sourceNode = NodeEditor.findAllNodes(plan, NodeConstants.Types.SOURCE).get(0);
        addAccessNode(sourceNode);
        PlanNode parentProject = NodeEditor.findParent(sourceNode, NodeConstants.Types.PROJECT);
        
        PlanNode accessNode = NodeEditor.findAllNodes(plan, NodeConstants.Types.ACCESS).get(0);
        raiseAccessNode(plan, accessNode);
        
        System.out.println(accessNode);
        
        
    }
    
    private static void raiseAccessNode(PlanNode plan, PlanNode accessNode) {

        
    }

    private static void addAccessNode(PlanNode sourceNode) {

        PlanNode apNode = sourceNode;
        if (sourceNode.getChildCount() == 0){
            PlanNode accessNode = NodeFactory.getNewNode(NodeConstants.Types.ACCESS);
            accessNode.addGroups(sourceNode.getGroups());
            accessNode.setProperty(Info.SOURCE_HINT, null);
            sourceNode.addAsParent(accessNode);
            System.out.println(sourceNode);
        }
        
    }

    static Set<GroupSymbol> getGroupSymbols(PlanNode plan) {
        Set<GroupSymbol> groupSymbols = new HashSet<GroupSymbol>();
        for (PlanNode source : NodeEditor.findAllNodes(plan, NodeConstants.Types.SOURCE | NodeConstants.Types.GROUP, NodeConstants.Types.GROUP)) {
            groupSymbols.addAll(source.getGroups());
        }
        return groupSymbols;
    }
    
    static List<? extends Expression> getProjectedSymbols() {
        ElementSymbol id = new ElementSymbol("Accounts.PRODUCT.ID");
        id.setType(DataTypeManager.DefaultDataClasses.STRING);
        ElementSymbol symbol = new ElementSymbol("Accounts.PRODUCT.SYMBOL");
        symbol.setType(DataTypeManager.DefaultDataClasses.STRING);
        ElementSymbol name = new ElementSymbol("Accounts.PRODUCT.COMPANY_NAME");
        symbol.setType(DataTypeManager.DefaultDataClasses.STRING);

        Select select = new Select();
        select.addSymbol(id);
        select.addSymbol(symbol);
        select.addSymbol(name);
        return select.getProjectedSymbols();
    }

}

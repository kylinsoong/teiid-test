package org.teiid.test.embedded.plan;

import org.teiid.query.optimizer.relational.plantree.NodeConstants;
import org.teiid.query.optimizer.relational.plantree.NodeFactory;
import org.teiid.query.optimizer.relational.plantree.PlanNode;

public class PlanNodeExample {

    public static void main(String[] args) {

        PlanNode plan = NodeFactory.getNewNode(NodeConstants.Types.SOURCE);
        
        System.out.println(plan.toString());
    }

}

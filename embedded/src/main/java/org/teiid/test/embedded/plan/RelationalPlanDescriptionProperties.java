package org.teiid.test.embedded.plan;

import static org.teiid.query.analysis.AnalysisRecord.PROP_ID;

import org.teiid.client.plan.PlanNode;

public class RelationalPlanDescriptionProperties {

    public static void main(String[] args) {

        PlanNode result = new PlanNode("AccessNode");
        result.addProperty(PROP_ID, "1");
        
        System.out.println(result);
    }

}

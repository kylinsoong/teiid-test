package org.teiid.test.embedded.plan;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;

public class ElementSymbolExample {

    public static void main(String[] args) {

        ElementSymbol id = new ElementSymbol("Test.PRODUCTView.product_id", true);
        id.setType(DataTypeManager.DefaultDataClasses.INTEGER);
        
        System.out.println(id);
    }

}

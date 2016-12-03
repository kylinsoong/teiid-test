package org.teiid.test.teiid4455.sql.symbol;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.GroupSymbol;

public class ExampleOfElementSymbol {

    public static void main(String[] args) {

        GroupSymbol group = new GroupSymbol("A", "Accounts.PRODUCT");
        ElementSymbol id = new ElementSymbol("ID", group);
        id.setType(DataTypeManager.DefaultDataClasses.STRING);
        
        System.out.println(id);
    }

}

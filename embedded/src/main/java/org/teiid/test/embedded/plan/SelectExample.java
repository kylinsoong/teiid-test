package org.teiid.test.embedded.plan;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.lang.Select;
import org.teiid.query.sql.symbol.ElementSymbol;

public class SelectExample {

    public static void main(String[] args) {
        
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
        
        //  Accounts.PRODUCT.SYMBOL, Accounts.PRODUCT.COMPANY_NAME
    }

}

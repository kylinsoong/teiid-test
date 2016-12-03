package org.teiid.test.teiid4455.sql;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.lang.From;
import org.teiid.query.sql.lang.Query;
import org.teiid.query.sql.lang.Select;
import org.teiid.query.sql.lang.UnaryFromClause;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.GroupSymbol;

public class ExampleOfQuery {

    public static void main(String[] args) {

        GroupSymbol group = new GroupSymbol("A", "Accounts.PRODUCT");
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
        
        From from = new From();
        UnaryFromClause clause = new UnaryFromClause();
        clause.setGroup(group);
        from.addClause(clause);
        
        Query command = new Query();
        command.setSelect(select);
        command.setFrom(from);
        System.out.println(command);
    }

}

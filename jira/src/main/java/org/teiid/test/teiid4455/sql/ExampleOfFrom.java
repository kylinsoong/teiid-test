package org.teiid.test.teiid4455.sql;

import org.teiid.query.sql.lang.From;
import org.teiid.query.sql.lang.UnaryFromClause;
import org.teiid.query.sql.symbol.GroupSymbol;

public class ExampleOfFrom {

    public static void main(String[] args) {

        From from = new From();
        UnaryFromClause clause = new UnaryFromClause();
        GroupSymbol group = new GroupSymbol("A", "Accounts.PRODUCT");
        clause.setGroup(group);
        from.addClause(clause);
        
        System.out.println(from);
    }

}

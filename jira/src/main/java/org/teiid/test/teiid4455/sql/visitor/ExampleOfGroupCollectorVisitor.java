package org.teiid.test.teiid4455.sql.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.lang.From;
import org.teiid.query.sql.lang.Query;
import org.teiid.query.sql.lang.Select;
import org.teiid.query.sql.lang.UnaryFromClause;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.GroupSymbol;
import org.teiid.query.sql.visitor.GroupCollectorVisitor;

public class ExampleOfGroupCollectorVisitor {

    public static void main(String[] args) {

//        example_1();
        
//        example_2();
        
        example_x();
    }

    static void example_x() {

        GroupSymbol group = new GroupSymbol("A", "Accounts.PRODUCT");
        ElementSymbol id = new ElementSymbol("ID", group, String.class);
        ElementSymbol symbol = new ElementSymbol("SYMBOL", group, String.class);
        ElementSymbol name = new ElementSymbol("COMPANY_NAME", group, String.class);

        List<ElementSymbol> symbols = Arrays.asList(id, symbol, name);
        Select select = new Select(symbols);

        From from = new From();
        UnaryFromClause clause = new UnaryFromClause();
        clause.setGroup(group);
        from.addClause(clause);

        Query command = new Query();
        command.setSelect(select);
        command.setFrom(from);
        
        System.out.println(GroupCollectorVisitor.getGroups(command, true));
    }

    static void example_2() {

        GroupSymbol gs = new GroupSymbol("A", "Accounts.PRODUCT");
        Collection<GroupSymbol> actualGroups = GroupCollectorVisitor.getGroups(gs, true);
        
        System.out.println(actualGroups);
    }

    static void example_1() {

        GroupSymbol gs = new GroupSymbol("group." + 1);
        Collection<GroupSymbol> actualGroups = GroupCollectorVisitor.getGroups(gs, true);
        
        System.out.println(actualGroups);
    }

}

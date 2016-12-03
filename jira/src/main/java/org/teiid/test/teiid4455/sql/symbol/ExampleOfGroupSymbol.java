package org.teiid.test.teiid4455.sql.symbol;

import org.teiid.query.sql.symbol.GroupSymbol;

public class ExampleOfGroupSymbol {

    public static void main(String[] args) {

        GroupSymbol a = new GroupSymbol("A", "Accounts.PRODUCT");
        GroupSymbol b = new GroupSymbol("B");
        GroupSymbol c = new GroupSymbol("Accounts.C");
        
        System.out.println(a);
        System.out.println(b);
        
        System.out.println(c);
    }

}

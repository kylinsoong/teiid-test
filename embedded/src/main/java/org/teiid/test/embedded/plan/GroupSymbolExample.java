package org.teiid.test.embedded.plan;

import org.teiid.query.sql.symbol.GroupSymbol;

public class GroupSymbolExample {

    public static void main(String[] args) {

        GroupSymbol a = new GroupSymbol("g1", "a");
        GroupSymbol b = new GroupSymbol("g1", "b");
        assertEquals(a, b);
        assertFalse(a.isTempGroupSymbol());
        
        GroupSymbol group = new GroupSymbol("Accounts.PRODUCT", "p");
        assertEquals("Accounts", group.getSchema());
        assertEquals("PRODUCT", group.getShortName());
        assertEquals("Accounts.PRODUCT", group.getName());
        assertEquals("p AS \"Accounts.PRODUCT\"", group.toString());
        
        System.out.println(group); // 'x AS "Accounts.PRODUCT"' is expected
    }

    private static void assertEquals(String a, String b) {
        if(!a.equals(b)){
            throw new RuntimeException("assert failed");
        }
    }

    private static void assertFalse(boolean tempGroupSymbol) {
        if(tempGroupSymbol){
            throw new RuntimeException("assert failed");
        }
    }

    private static void assertEquals(GroupSymbol a, GroupSymbol b) {
        if(!a.equals(b)){
            throw new RuntimeException("assert failed");
        }
    }

}

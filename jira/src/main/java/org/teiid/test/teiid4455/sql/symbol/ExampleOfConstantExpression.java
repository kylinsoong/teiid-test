package org.teiid.test.teiid4455.sql.symbol;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.Constant;

public class ExampleOfConstantExpression {

    public static void main(String[] args) {

        Constant c1 = new Constant("Hello Wolrd");
        Constant c2 = new Constant(10000L);
        Constant c3 = new Constant(null);
        Constant c4 = new Constant(null, DataTypeManager.DefaultDataClasses.STRING);
        
        test(c1, c2, c3, c4);
    }

    private static void test(Constant... items) {

        for(Constant c : items) {
            System.out.println(c);
            System.out.println(c.getValue());
            System.out.println(c.getType());
            System.out.println(c.isNull());
            System.out.println();
        }
    }

}

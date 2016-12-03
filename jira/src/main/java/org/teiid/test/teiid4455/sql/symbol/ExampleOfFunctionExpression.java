package org.teiid.test.teiid4455.sql.symbol;

import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.Function;

public class ExampleOfFunctionExpression {

    public static void main(String[] args) {
        
        Function function = new Function("FROM_UNIXTIME",  new Expression[]{new Constant(1900000000)});
        System.out.println(function);
    }

}

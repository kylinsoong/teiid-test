package org.teiid.test.teiid4455.sql.symbol;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.ExpressionSymbol;
import org.teiid.query.sql.symbol.Function;

public class ExampleOfExpressionSymbol {

    public static void main(String[] args) {
        
        Constant constant = new Constant(1900000000, DataTypeManager.DefaultDataClasses.INTEGER);
        Function function = new Function("FROM_UNIXTIME",  new Expression[]{constant});
        ExpressionSymbol symbol = new ExpressionSymbol("expr1", function);
        System.out.println(symbol);
    }

}

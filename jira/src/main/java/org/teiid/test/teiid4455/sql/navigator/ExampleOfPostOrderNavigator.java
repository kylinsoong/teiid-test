package org.teiid.test.teiid4455.sql.navigator;

import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.lang.Select;
import org.teiid.query.sql.navigator.PostOrderNavigator;
import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.ExpressionSymbol;
import org.teiid.query.sql.symbol.Function;
import org.teiid.query.sql.visitor.ExpressionMappingVisitor;

public class ExampleOfPostOrderNavigator {

    public static void main(String[] args) {

        Constant constant = new Constant(1900000000, DataTypeManager.DefaultDataClasses.INTEGER);
        Function function = new Function("FROM_UNIXTIME",  new Expression[]{constant});
        ExpressionSymbol symbol = new ExpressionSymbol("expr1", function);      
        Select select = new Select();
        select.addSymbol(symbol);
        
        ExpressionMappingVisitor visitor = new ExpressionMappingVisitor(null);
        PostOrderNavigator.doVisit(select, visitor);
        
        System.out.println(select);
    }

}

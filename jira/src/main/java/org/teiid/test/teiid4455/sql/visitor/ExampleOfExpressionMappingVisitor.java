package org.teiid.test.teiid4455.sql.visitor;

import java.util.HashMap;
import java.util.Map;

import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.Function;
import org.teiid.query.sql.visitor.ExpressionMappingVisitor;

public class ExampleOfExpressionMappingVisitor {

    public static void main(String[] args) {

        ElementSymbol a = new ElementSymbol("a"); 
        ElementSymbol b = new ElementSymbol("b"); 
        ElementSymbol c = new ElementSymbol("c");
        Function addition = new Function("+", new Expression[] {a, b });
        Function multiplication = new Function("*", new Expression[] {a, b });
        Function original = new Function("+", new Expression[] {addition, c });
        Map<Expression, Expression> map = new HashMap<Expression, Expression>();
        map.put(c, multiplication);
        System.out.print(original + " -> ");
        ExpressionMappingVisitor.mapExpressions(original, map);        
        System.out.println(original);
    }

}

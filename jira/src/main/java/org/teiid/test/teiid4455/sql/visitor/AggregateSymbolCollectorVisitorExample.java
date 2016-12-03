package org.teiid.test.teiid4455.sql.visitor;

import java.util.ArrayList;
import java.util.List;

import org.teiid.api.exception.query.QueryParserException;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.visitor.AggregateSymbolCollectorVisitor;

public class AggregateSymbolCollectorVisitorExample {

    public static void main(String[] args) throws QueryParserException {

        String sql = "SELECT COUNT(e1), MAX(DISTINCT e1) FROM pm1.g1 GROUP BY e1 HAVING MAX(e2) > 0 AND NOT MIN(e2) < 100";
        Command command = QueryParser.getQueryParser().parseCommand(sql);
        
        List<Expression> aggs = new ArrayList<Expression>();
        List<Expression> elements = new ArrayList<Expression>();
        AggregateSymbolCollectorVisitor.getAggregates(command, aggs, elements, null, null, null);
        
        System.out.println(aggs);
        System.out.println(elements);
    }

}

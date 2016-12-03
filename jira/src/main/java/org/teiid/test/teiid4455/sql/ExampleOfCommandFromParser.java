package org.teiid.test.teiid4455.sql;

import org.teiid.api.exception.query.QueryParserException;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.sql.lang.Command;

public class ExampleOfCommandFromParser {

    public static void main(String[] args) throws QueryParserException {

        String sql = "select from_unixtime(x.e2) from pm1.g1 as x";
        Command command = QueryParser.getQueryParser().parseCommand(sql);
        System.out.println(command);
    }

}

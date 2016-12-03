package org.teiid.test.teiid4455.sql;

import java.sql.Timestamp;

import org.teiid.api.exception.query.QueryParserException;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.sql.lang.Criteria;

public class ExampleOfCriteriaFromParser {

    public static void main(String[] args) throws QueryParserException {

        String critStr = "timestampadd(SQL_TSI_SECOND, pm1.g1.e2, {ts'1970-01-01 08:00:00.0'}) = {ts'1992-12-01 07:00:00.0'}";
        Criteria crit = QueryParser.getQueryParser().parseCriteria(critStr);
        System.out.println(crit);
        System.out.println(crit.getClass());
    }

}

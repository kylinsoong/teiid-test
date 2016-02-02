package org.teiid.test.embedded.plan;

import org.teiid.api.exception.query.QueryParserException;
import org.teiid.api.exception.query.QueryResolverException;
import org.teiid.core.TeiidComponentException;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.resolver.QueryResolver;
import org.teiid.query.sql.lang.Command;
import org.teiid.test.embedded.metadata.MetadataLoadExample;

public class ExampleRequest {

    public static void main(String[] args) throws QueryParserException, QueryResolverException, TeiidComponentException {

        QueryMetadataInterface metadata = MetadataLoadExample.getMetadata();
        
        Command command = QueryParser.getQueryParser().parseCommand("select city, name, amount, product from Customer");
        
        QueryResolver.resolveCommand(command, metadata);
        
        System.out.println(command);
    }

}

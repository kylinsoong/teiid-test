package org.teiid.test.teiid3999;

import org.teiid.api.exception.query.QueryParserException;
import org.teiid.api.exception.query.QueryResolverException;
import org.teiid.core.TeiidComponentException;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.resolver.QueryResolver;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.validator.AbstractValidationVisitor;
import org.teiid.query.validator.ValidationVisitor;
import org.teiid.query.validator.Validator;
import org.teiid.query.validator.ValidatorFailure;
import org.teiid.query.validator.ValidatorReport;

public class ParseResolveValidate {
    
    static String[] commands = new String[]{"INSERT INTO SampleTable_staging SELECT * FROM TestExterMat.SAMPLEEXTERMATVIEW option nocache TestExterMat.SAMPLEEXTERMATVIEW", 
                                            "DELETE FROM Accounts.status WHERE Name= 'SAMPLEEXTERMATVIEW' AND schemaname = 'TestExterMat'",
                                            "execute accounts.native('truncate table SampleTable_staging')"};
    
    static QueryMetadataInterface metadata;

    public static void main(String[] args) throws QueryParserException, QueryResolverException, TeiidComponentException {
        
        for(String commandStr : commands) {
            QueryParser queryParser = QueryParser.getQueryParser();
            Command command = queryParser.parseCommand(commandStr);
            
            QueryResolver.resolveCommand(command, metadata);
            
            AbstractValidationVisitor visitor = new ValidationVisitor();
            ValidatorReport report = Validator.validate(command, metadata, visitor);
            if (report.hasItems()) {
                ValidatorFailure firstFailure = report.getItems().iterator().next();
                firstFailure.getMessage();
            }
        }

    }

}

package org.teiid.test.teiid4699;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.util.Properties;

import org.teiid.metadata.MetadataFactory;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.translator.prestodb.PrestoDBMetadataProcessor;

public class TestPrestoDBMetadataProcessor {
    
    private static final String JDBC_DRIVER = "com.facebook.presto.jdbc.PrestoDriver";
    private static final String JDBC_URL = "jdbc:presto://localhost:8080";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "sa";

    public static void main(String[] args) throws Exception {
        
//        filterFromCatalog();
        
       filterFromSchema();
       
//       filterFromTable();
    }

    static void filterFromSchema() throws Exception {

        PrestoDBMetadataProcessor metadataProcessor = new PrestoDBMetadataProcessor();
        metadataProcessor.setSchemaPattern("teiid*");
        
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        MetadataFactory mf = new MetadataFactory("vdb", 1, "x", SystemMetadata.getInstance().getRuntimeTypeMap(), new Properties(), null);       
        metadataProcessor.process(mf, conn);

        close(conn);
    }

    static void filterFromCatalog() throws Exception {

        PrestoDBMetadataProcessor metadataProcessor = new PrestoDBMetadataProcessor();
        metadataProcessor.setCatalog("mysql");
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        MetadataFactory mf = new MetadataFactory("vdb", 1, "x", SystemMetadata.getInstance().getRuntimeTypeMap(), new Properties(), null);       
        metadataProcessor.process(mf, conn);

        close(conn);
    }

}

package org.teiid.test.teiid2820;

import java.util.Map;
import java.util.Properties;

import javax.resource.ResourceException;

import org.teiid.couchbase.CouchbaseConnection;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.query.metadata.DDLStringVisitor;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.resource.adapter.couchbase.CouchbaseManagedConnectionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.couchbase.CouchbaseMetadataProcessor;

public class TestMetadata {
    
    static CouchbaseConnection sample() throws ResourceException {
        
        CouchbaseManagedConnectionFactory mcf = new CouchbaseManagedConnectionFactory();
        mcf.setConnectionString("10.66.192.120"); //$NON-NLS-1$
        mcf.setKeyspace("test"); //$NON-NLS-1$
        mcf.setNamespace("default");
        return mcf.createConnectionFactory().getConnection();
    }

    public static void main(String[] args) throws ResourceException, TranslatorException {

        Map<String, Datatype> datatypes = SystemMetadata.getInstance().getRuntimeTypeMap();
        MetadataFactory mf = new MetadataFactory("vdb", 1, "couchbase", datatypes, new Properties(), null);
        CouchbaseConnection conn = sample();
        CouchbaseMetadataProcessor mp = new CouchbaseMetadataProcessor();
        mp.setTypeNameList("`test`:`type`,`beer-sample`:`type`,` travel-sample`:`type`");
        mp.process(mf, conn);
        conn.close();
        
        String metadataDDL = DDLStringVisitor.getDDLString(mf.getSchema(), null, null);
        
        System.out.println(metadataDDL);
    }

}

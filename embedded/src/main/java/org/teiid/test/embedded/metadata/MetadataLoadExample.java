package org.teiid.test.embedded.metadata;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.core.TeiidRuntimeException;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.MetadataStore;
import org.teiid.query.function.SystemFunctionManager;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.MetadataValidator;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.validator.ValidatorReport;

public class MetadataLoadExample {
    
    public static QueryMetadataInterface getMetadata() {
        VDBMetaData vdb = new VDBMetaData();
        vdb.setName("ExampleVDB");
        vdb.setVersion(1);
        Properties p = new Properties();
        QueryParser parser = new QueryParser();
        
        Map<String, Datatype> typeMap = SystemMetadata.getInstance().getRuntimeTypeMap();
        
        ModelMetaData mmd = new ModelMetaData();
        mmd.setName("ExampleMode");
        vdb.addModel(mmd);
        MetadataFactory factory = new MetadataFactory(vdb.getName(), vdb.getVersion(), "ExampleMode", typeMap, p, null);
        parser.parseDDL(factory, loadReader("customer.ddl"));
        MetadataStore systemStore = factory.asMetadataStore();
        
        TransformationMetadata tm = new TransformationMetadata(vdb, new CompositeMetadataStore(systemStore), null, new SystemFunctionManager(typeMap).getSystemFunctions(), null);
        vdb.addAttchment(QueryMetadataInterface.class, tm);
        MetadataValidator validator = new MetadataValidator(typeMap, parser);
        ValidatorReport report = validator.validate(vdb, systemStore);
        if (report.hasItems()) {
            throw new TeiidRuntimeException(report.getFailureMessage());
        }
        
        return tm;
    }

    public static void main(String[] args) {

        VDBMetaData vdb = new VDBMetaData();
        vdb.setName("ExampleVDB");
        vdb.setVersion(1);
        Properties p = new Properties();
        QueryParser parser = new QueryParser();
        
        Map<String, Datatype> typeMap = SystemMetadata.getInstance().getRuntimeTypeMap();
        
        ModelMetaData mmd = new ModelMetaData();
        mmd.setName("ExampleMode");
        vdb.addModel(mmd);
        MetadataFactory factory = new MetadataFactory(vdb.getName(), vdb.getVersion(), "ExampleMode", typeMap, p, null);
        parser.parseDDL(factory, loadReader("customer.ddl"));
        MetadataStore systemStore = factory.asMetadataStore();
        
        TransformationMetadata tm = new TransformationMetadata(vdb, new CompositeMetadataStore(systemStore), null, new SystemFunctionManager(typeMap).getSystemFunctions(), null);
        vdb.addAttchment(QueryMetadataInterface.class, tm);
        MetadataValidator validator = new MetadataValidator(typeMap, parser);
        ValidatorReport report = validator.validate(vdb, systemStore);
        if (report.hasItems()) {
            throw new TeiidRuntimeException(report.getFailureMessage());
        }
    }

    private static InputStreamReader loadReader(String path) {
        InputStream is = MetadataLoadExample.class.getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(is, Charset.forName("UTF-8"));
    }

}

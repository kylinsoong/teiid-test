package org.teiid.test.teiid4121;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.metadata.Column;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Table;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.parser.QueryParser;

public class MFParseDDLExample {
    
    static String ddl;
    
    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/kylin/src/kylinsoong.github.io/assets/download/teiid-sample-ddl-txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            
            ddl = sb.toString();
            br.close();
        } catch (IOException e) {
        }
        
    }

    public static void main(String[] args) {
        
        System.out.println(ddl);

            ModelMetaData mmd = new ModelMetaData();
            mmd.setName("ExampleMode");    
        Map<String, Datatype> datatypes = SystemMetadata.getInstance().getRuntimeTypeMap();
        MetadataFactory factory = new MetadataFactory("VDB", "1", datatypes, mmd);
        factory.setBuiltinDataTypes(datatypes);
        factory.getSchema().setPhysical(false);
        factory.setParser(new QueryParser()); 
        
        factory.parse(new StringReader(ddl));
        
        for (Table t :factory.getSchema().getTables().values()) {
            List<Column> matViewColumns = t.getColumns();
            for(int i = 0 ; i < matViewColumns.size() ; i ++){
                Column c = matViewColumns.get(i);
                System.out.println(c.getName() + ", " + c.getDatatype());
            }
            System.out.println(t.getProperty("{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE", false));
        }
    }

}

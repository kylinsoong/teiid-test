package org.teiid.test.embedded.h2;

import static org.teiid.example.util.JDBCUtils.execute;

import java.io.InputStreamReader;
import java.sql.Connection;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;

public class H2DirectQuery {

    public static void main(String[] args) throws Exception {

        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(H2DirectQuery.class.getClassLoader().getResourceAsStream("data/h2-test-schema.sql")));
        
        Connection conn = ds.getConnection();
        
        execute(conn, "SELECT COUNT(*) AS count FROM A, B", false);
    }

}

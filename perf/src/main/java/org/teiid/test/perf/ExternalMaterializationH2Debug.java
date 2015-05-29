package org.teiid.test.perf;

import java.sql.Connection;

import org.teiid.example.util.JDBCUtils;

public class ExternalMaterializationH2Debug {

    public static void main(String[] args) throws Exception {
        
        ExternalMaterializationH2.startup();
        
        Connection conn = ExternalMaterializationH2.conn;
        
        JDBCUtils.execute(conn, "SELECT Name FROM VirtualDatabases", false);
        JDBCUtils.execute(conn, "SELECT convert(Version, integer) FROM VirtualDatabases", false);
        JDBCUtils.execute(conn, "SELECT UID FROM Sys.Tables WHERE VDBName = 'MatVDB' AND SchemaName = 'Stocks' AND Name = 'MatView'", false);
        JDBCUtils.execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:6021235fbeea-95513c05-00000001'", false);
        JDBCUtils.execute(conn, "SELECT \"Value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'", false);
        JDBCUtils.execute(conn, "SELECT \"Value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_ONERROR_ACTION'", false);
        
        String sql = "SELECT TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality, LoadNumber, THROW_EXCEPTION FROM status  WHERE VDBName = 'MatVDB' AND VDBVersion = 1 AND schemaName = 'Stocks' AND Name = 'MatView'";

        JDBCUtils.execute(conn, sql, false);
        
        //        JDBCUtils.execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:6021235fbeea-95513c05-00000001'", false);
//        JDBCUtils.execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:6021235fbeea-95513c05-00000001'", false);
//        JDBCUtils.execute(conn, "SELECT UID, IsMaterialized FROM SYS.Tables", false);
        
        
//        ExternalMaterializationH2.teardown();
    }

}

package org.teiid.test.perf;

import java.sql.Connection;

import org.teiid.example.util.JDBCUtils;

public class ExternalMaterializationMysqlDebug {

    public static void main(String[] args) throws Exception {

        ExternalMaterializationMysql.startup();
        
        Connection conn = ExternalMaterializationMysql.conn;
        
        JDBCUtils.execute(conn, "SELECT Name FROM VirtualDatabases", false);
        JDBCUtils.execute(conn, "SELECT convert(Version, integer) FROM VirtualDatabases", false);
        JDBCUtils.execute(conn, "SELECT UID FROM Sys.Tables WHERE VDBName = 'MatViewMySQLVDB' AND SchemaName = 'Test' AND Name = 'PERFTESTEXTER_MATVIEW'", false);
        JDBCUtils.execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:793a149b7fdc-8a7f93f9-00000001'", false);
        
//        ExternalMaterializationMysql.teardown();
    }

}

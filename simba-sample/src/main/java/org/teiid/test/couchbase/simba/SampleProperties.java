package org.teiid.test.couchbase.simba;

public interface SampleProperties {

    String JDBC4_DRIVER = "com.simba.couchbase.jdbc4.Driver";
    String JDBC4_DATASOURCE = "com.simba.couchbase.jdbc4.DataSource";
    
    String JDBC_URL = "jdbc:couchbase://10.66.192.120:8093";
    String JDBC_URL_TEST = "jdbc:couchbase://10.66.192.120:8093;LogLevel=6;LogPath=target;SampleSize=100;TypeNameList=`test`:`type`";
    String JDBC_URL_LOG = "jdbc:couchbase://10.66.192.120:8093;LogLevel=6;LogPath=target";
    String JDBC_URL_SCHEMA = "jdbc:couchbase://10.66.192.120:8093;LocalSchemaFile=/path/to/Sample_Schema.json";
    String JDBC_URL_SCHEMA_AUTO = "jdbc:couchbase://10.66.192.120:8093/test;SampleSize=100;TypeNameList=`Customer`:`type`,`Order`:`type`";
    
}

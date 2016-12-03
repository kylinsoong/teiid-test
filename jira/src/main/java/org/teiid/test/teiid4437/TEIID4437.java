package org.teiid.test.teiid4437;

import static org.teiid.test.utils.JDBCUtils.close;

import java.sql.Connection;
import java.sql.Statement;

import org.teiid.jdbc.TeiidDriver;

public class TEIID4437 {

    public static void main(String[] args) throws Exception {

        Connection conn = TeiidDriver.getInstance().connect("jdbc:teiid:bqt@mm://localhost:31000;user=user;password=user", null);
        
        Statement s = conn.createStatement();
        //test each functional area - jts, proj4j, and geojson
        s.executeQuery("select st_geomfromtext('POINT(0 0)')");
        s.executeQuery("select ST_AsText(ST_Transform(ST_GeomFromText('POLYGON((743238 2967416,743238 2967450,743265 2967450,743265.625 2967416,743238 2967416))',2249),4326))");
        s.executeQuery("select ST_AsGeoJson(ST_GeomFromText('POINT (-48.23456 20.12345)'))");
        s.executeQuery("select ST_AsText(ST_GeomFromGeoJSON('{\"coordinates\":[-48.23456,20.12345],\"type\":\"Point\"}'))");
        
        close(s, conn);
    }

}

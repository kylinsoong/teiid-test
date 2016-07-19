package org.teiid.test.teiid4311;

import java.lang.reflect.Method;

import org.h2.jdbcx.JdbcDataSource;
import org.mariadb.jdbc.MariaDbDataSource;
import org.postgresql.xa.PGXADataSource;
import org.teiid.jdbc.TeiidDataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

public class XADataSourcePropertiesList {

    public static void main(String[] args) {

        printSetProperty(MysqlXADataSource.class.getMethods());
        
        printSetProperty(JdbcDataSource.class.getMethods());
        
        printSetProperty(PGXADataSource.class.getMethods());
        
        printSetProperty(PGXADataSource.class.getMethods());
        
        printSetProperty(MariaDbDataSource.class.getMethods());
        
        printSetProperty(TeiidDataSource.class.getMethods());
    }

    private static void printSetProperty(Method[] methods) {

        for(Method m : methods){
            String name = m.getName();
            if(name.startsWith("set")){
                System.out.println(name.substring(3));
            }        
        }
        
        System.out.println();
    }

}

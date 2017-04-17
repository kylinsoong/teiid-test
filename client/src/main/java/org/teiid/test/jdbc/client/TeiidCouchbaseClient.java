package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.util.Arrays;

public class TeiidCouchbaseClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:CouchbaseVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String[] inserts = new String[] {
            "INSERT INTO Customer VALUES ('customer-3', 'Customer_12346', 'Customer', 'Kylin Soong')",
            "INSERT INTO Customer_SavedAddresses VALUES ('customer-3', 0,  'Beijing')",
            "INSERT INTO Customer_SavedAddresses VALUES ('customer-3', 1,  'Beijing')",
            "INSERT INTO Oder (documentID, CustomerID, type, CreditCard_CardNumber, CreditCard_Type, CreditCard_CVN, CreditCard_Expiry, Name) VALUES ('order-3', 'Customer_12346', 'Oder', '4111 1111 1111 111', 'Visa', 123, '12/12', 'Air Ticket')",
            "INSERT INTO Oder_Items (documentID, Oder_Items_idx, Oder_Items_Quantity, Oder_Items_ItemID) VALUES ('order-3', 0, 5, 92312)",
            "INSERT INTO Oder_Items (documentID, Oder_Items_idx, Oder_Items_Quantity, Oder_Items_ItemID) VALUES ('order-3', 1, 5, 92312)"
    };
    
    static String[] querys = new String[] {
            "SELECT * FROM Customer WHERE documentID = 'customer-3'",
            "SELECT * FROM Customer_SavedAddresses WHERE documentID = 'customer-3'",
            "SELECT * FROM Oder WHERE documentID = 'order-3'",
            "SELECT * FROM Oder_Items WHERE documentID = 'order-3'",
        };
    
    static String[] updates = new String[] {
            "UPDATE Customer SET Name = 'John Doe' WHERE documentID = 'customer-3'",
            "UPDATE Customer_SavedAddresses SET Customer_SavedAddresses = 'ChaoYang Beijing' WHERE documentID = 'customer-3' AND Customer_SavedAddresses_idx = 0",
            "UPDATE Customer_SavedAddresses SET Customer_SavedAddresses = 'ChaoYang Beijing' WHERE documentID = 'customer-3' AND Customer_SavedAddresses_idx = 1",
            "UPDATE Oder SET CreditCard_CVN = 100, CreditCard_CardNumber = '4111 1111 1111 112', CreditCard_Expiry = '14/12' WHERE documentID = 'order-3'",
            "UPDATE Oder_Items SET Oder_Items_ItemID = 80000, Oder_Items_Quantity = 10 WHERE documentID = 'order-3' AND Oder_Items_idx = 0",
            "UPDATE Oder_Items SET Oder_Items_ItemID = 80000, Oder_Items_Quantity = 10 WHERE documentID = 'order-3' AND Oder_Items_idx = 1"
    };
    
    static String[] deletes = new String[] {
            "DELETE FROM Customer_SavedAddresses WHERE documentID = 'customer-3' AND Customer_SavedAddresses_idx = 0",
            "DELETE FROM Customer_SavedAddresses WHERE documentID = 'customer-3' AND Customer_SavedAddresses_idx = 1",
            "DELETE FROM Customer WHERE documentID = 'customer-3'",
            "DELETE FROM Oder_Items WHERE documentID = 'order-3' AND Oder_Items_idx = 0",
            "DELETE FROM Oder_Items WHERE documentID = 'order-3' AND Oder_Items_idx = 1",
            "DELETE FROM Oder WHERE documentID = 'order-3'"
    };


    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
     // delete if exist
        execute(conn, "EXEC CouchbaseVDB.native('DELETE FROM test USE KEYS [\"customer-3\", \"order-3\"]')");
        
        Arrays.asList(inserts).forEach(query -> {
            try {
                execute(conn, query);
            } catch (Exception e) {
                assert(false);
            }
        });
        
        Arrays.asList(querys).forEach(query -> {
            try {
                execute(conn, query);
            } catch (Exception e) {
                assert(false);
            }
        });
        
        Arrays.asList(updates).forEach(query -> {
            try {
                execute(conn, query);
            } catch (Exception e) {
                assert(false);
            }
        });
        
        Arrays.asList(querys).forEach(query -> {
            try {
                execute(conn, query);
            } catch (Exception e) {
                assert(false);
            }
        });
        
        Arrays.asList(deletes).forEach(query -> {
            try {
                execute(conn, query);
            } catch (Exception e) {
                assert(false);
            }
        });
        
        Arrays.asList(querys).forEach(query -> {
            try {
                execute(conn, query);
            } catch (Exception e) {
                assert(false);
            }
        });
        
        close(conn);
    }

}

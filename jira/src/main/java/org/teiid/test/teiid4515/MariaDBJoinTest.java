package org.teiid.test.teiid4515;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

public class MariaDBJoinTest {
    
    static final String SQL_INNER_JOIN = "SELECT O.OrderID, C.CustomerName, O.Date FROM Orders AS O INNER JOIN Customers AS C ON O.CustomerID = C.CustomerID ORDER BY O.OrderID";
    static final String SQL_LEFT_JOIN = "SELECT O.OrderID, C.CustomerName, O.Date FROM Orders AS O LEFT OUTER JOIN Customers AS C ON O.CustomerID = C.CustomerID ORDER BY O.OrderID";
    static final String SQL_RIGHT_JOIN = "SELECT O.OrderID, C.CustomerName, O.Date FROM Orders AS O RIGHT OUTER JOIN Customers AS C ON O.CustomerID = C.CustomerID ORDER BY O.OrderID";
    
    static final String SQL_JOIN_1 = "SELECT O.OrderID, C.CustomerName, C.Country, O.Date FROM Orders AS O INNER JOIN Customers AS C ON O.CustomerID = C.CustomerID ORDER BY O.OrderID";
    static final String SQL_JOIN_2 = "SELECT ItemName, sum(Price * Quantity) AS OrderValue FROM Items JOIN Orders ON Items.ItemID = Orders.ItemID WHERE Orders.CustomerID > 'C002' GROUP BY ItemName";
    static final String SQL_JOIN_3 = "SELECT O.OrderID, C.CustomerName, C.Country, I.ItemName, I.Price, O.Date FROM Orders AS O INNER JOIN Customers AS C ON O.CustomerID = C.CustomerID INNER JOIN Items AS I ON O.ItemID = I.ItemID ORDER BY O.OrderID";
    
    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/JOINSTEST", "test_user", "test_pass");
        
//        execute(conn, "SELECT FROM_UNIXTIME(1900000000)", false);      
//        execute(conn, "SELECT FROM_UNIXTIME(1900000000, '%Y %D %M %h:%i:%s')", false);
        
        execute(conn, SQL_INNER_JOIN, false);
        execute(conn, SQL_LEFT_JOIN, false);
        execute(conn, SQL_RIGHT_JOIN, false);
        
        execute(conn, SQL_JOIN_1, false);
        execute(conn, SQL_JOIN_2, false);
        execute(conn, SQL_JOIN_3, false);
        
        execute(conn, "SELECT VERSION()", false);

        close(conn);
        
    }

}

package org.teiid.test.embedded.plan;

import static org.teiid.example.util.JDBCUtils.execute;

import java.io.InputStreamReader;
import java.sql.Connection;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;

public class SQLTester {

    public static void main(String[] args) throws Exception {

        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(TeiidEmbeddedPortfolioPlan.class.getClassLoader().getResourceAsStream("data/h2-test-schema.sql")));
        
        Connection conn = ds.getConnection();
        
//        execute(conn, "SELECT * FROM Departments", false);
//        execute(conn, "SELECT * FROM Employees", false);
//        execute(conn, "SELECT e.title, e.lastname FROM Employees AS e JOIN Departments AS d ON e.dept_id = d.dept_id WHERE year(e.birthday) >= 1970 AND d.dept_name = 'Engineering'", true);
//        execute(conn, "", false);
        
        //INNER JOIN
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders INNER JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
        
        //LEFT JOIN
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders LEFT JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders LEFT OUTER JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
        
        //RIGHT JOIN
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders RIGHT JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders RIGHT OUTER JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
        
        //FULL OUTER JOIN
        execute(conn, "SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate FROM Orders FULL OUTER JOIN Customers ON Orders.CustomerID=Customers.CustomerID", false);
    }

}

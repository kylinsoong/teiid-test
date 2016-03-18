CREATE TABLE Departments
(
   dept_id integer,
   dept_name varchar(32),
   CONSTRAINT Departments_PK PRIMARY KEY(dept_id)
);

CREATE TABLE Employees
(
   id integer,
   title varchar(32),
   firstname varchar(32),
   lastname varchar(32),
   birthday timestamp,
   dept_id integer,
   CONSTRAINT Employees_PK PRIMARY KEY(id),
   CONSTRAINT Departments_FK FOREIGN KEY (dept_id) REFERENCES Departments (dept_id)
);

CREATE TABLE g1
(
   e1 varchar(32),
   e2 integer,
   e3 boolean
);

CREATE TABLE g2
(
   e1 varchar(32),
   e2 integer,
   e3 boolean
);

CREATE TABLE g3
(
   e1 varchar(32),
   e2 integer,
   e3 boolean
);

CREATE TABLE A
(
   x integer,
   y integer 
);

CREATE TABLE B
(
   x integer,
   y integer 
);

CREATE TABLE Customers
(
   CustomerID integer,
   CustomerName varchar(64),
   ContactName varchar(64),
   Country varchar(32),
   CONSTRAINT Customers_PK PRIMARY KEY(CustomerID)
);

CREATE TABLE Orders
(
   OrderID integer,
   CustomerID integer,
   OrderDate timestamp,
   CONSTRAINT Orders_PK PRIMARY KEY(OrderID),
   CONSTRAINT Customers_FK FOREIGN KEY (CustomerID) REFERENCES Customers (CustomerID)
);

INSERT INTO Departments (dept_id,dept_name) VALUES (101, 'Engineering');
INSERT INTO Departments (dept_id,dept_name) VALUES (102, 'Manager');
INSERT INTO Departments (dept_id,dept_name) VALUES (103, 'HR');
INSERT INTO Departments (dept_id,dept_name) VALUES (104, 'GSS');

INSERT INTO Employees (id, title, firstname, lastname, birthday, dept_id) VALUES (1, 'Software Engineer', 'Kylin', 'Soong', '1985-12-14 00:00:00.000', 101);
INSERT INTO Employees (id, title, firstname, lastname, birthday, dept_id) VALUES (2, 'CEO', 'Steve', 'Jobs', '1963-07-24 00:00:00.000', 102);
INSERT INTO Employees (id, title, firstname, lastname, birthday, dept_id) VALUES (3, 'Senior Manager', 'Tom', 'Jackson', '1961-09-11 00:00:00.000', 102);
INSERT INTO Employees (id, title, firstname, lastname, birthday, dept_id) VALUES (4, 'Talent Recruiter', 'Marry', 'Wang', '1990-03-12 00:00:00.000', 103);
INSERT INTO Employees (id, title, firstname, lastname, birthday, dept_id) VALUES (5, 'Support Engineer', 'Bill', 'Swift', '1987-10-04 00:00:00.000', 104);

//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (1, 1, 2, 3, 5, 6);
//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (2, 1, 3, 1, 6, 7);
//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (3, 1, 5, 4, 4, 1);
//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (4, 2, 9, 6, 8, 6);
//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (5, 2, 3, 8, 2, 3);
//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (6, 3, 8, 4, 1, 9);
//INSERT INTO g1 (e1, e2, e3, e4, e5, e6) VALUES (7, 3, 1, 7, 9, 5);

INSERT INTO Customers (CustomerID, CustomerName, ContactName, Country) VALUES (1, 'Alfreds Futterkiste', 'Maria Anders', 'Germany');
INSERT INTO Customers (CustomerID, CustomerName, ContactName, Country) VALUES (2, 'Ana Trujillo Emparedados y helados', 'Ana Trujillo', 'Mexico');
INSERT INTO Customers (CustomerID, CustomerName, ContactName, Country) VALUES (3, 'Antonio Moreno Taquería', 'Antonio Moreno', 'Mexico');

INSERT INTO Orders (OrderID, CustomerID, OrderDate) VALUES (10308, 2, '2015-09-11 00:00:00.000');
//INSERT INTO Orders (OrderID, CustomerID, OrderDate) VALUES (10309, 37, '1961-09-12 00:00:00.000');
//NSERT INTO Orders (OrderID, CustomerID, OrderDate) VALUES (10310, 77, '1961-09-13 00:00:00.000');
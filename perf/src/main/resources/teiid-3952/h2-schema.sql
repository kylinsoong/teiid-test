DROP TABLE IF EXISTS SampleTable;

CREATE TABLE SampleTable(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));


INSERT INTO SampleTable (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');
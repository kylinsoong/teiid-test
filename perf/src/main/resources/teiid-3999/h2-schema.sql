DROP TABLE IF EXISTS SampleTable;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS SampleTable_staging;
DROP TABLE IF EXISTS SampleTable_mat;

CREATE TABLE SampleTable(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));

CREATE TABLE status
(
  VDBName varchar(50) not null,
  VDBVersion varchar(50) not null,
  SchemaName varchar(50) not null,
  Name varchar(256) not null,
  TargetSchemaName varchar(50),
  TargetName varchar(256) not null,
  Valid boolean not null,
  LoadState varchar(25) not null,
  Cardinality long,
  Updated timestamp not null,
  LoadNumber long not null,
  PRIMARY KEY (VDBName, VDBVersion, SchemaName, Name)
);

CREATE TABLE SampleTable_staging(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));

CREATE TABLE SampleTable_mat(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));


INSERT INTO SampleTable (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');
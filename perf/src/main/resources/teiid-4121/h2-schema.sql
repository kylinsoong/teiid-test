DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS SampleTable;
DROP TABLE IF EXISTS SampleTable_staging;
DROP TABLE IF EXISTS SampleTable_mat;
DROP TABLE IF EXISTS SampleTable_A;
DROP TABLE IF EXISTS SampleTable_staging_A;
DROP TABLE IF EXISTS SampleTable_mat_A;
DROP TABLE IF EXISTS SampleTable_B;
DROP TABLE IF EXISTS SampleTable_staging_B;
DROP TABLE IF EXISTS SampleTable_mat_B;
DROP TABLE IF EXISTS SampleTable_C;
DROP TABLE IF EXISTS SampleTable_staging_C;
DROP TABLE IF EXISTS SampleTable_mat_C;
DROP TABLE IF EXISTS SampleTable_D;
DROP TABLE IF EXISTS SampleTable_staging_D;
DROP TABLE IF EXISTS SampleTable_mat_D;
DROP TABLE IF EXISTS SampleTable_E;
DROP TABLE IF EXISTS SampleTable_staging_E;
DROP TABLE IF EXISTS SampleTable_mat_E;
DROP TABLE IF EXISTS SampleTable_F;
DROP TABLE IF EXISTS SampleTable_staging_F;
DROP TABLE IF EXISTS SampleTable_mat_F;


CREATE TABLE SampleTable(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_A(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_B(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_C(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_D(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_E(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_F(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));

CREATE TABLE SampleTable_mat(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_mat_A(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_mat_B(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_mat_C(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_mat_D(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_mat_E(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_mat_F(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));

CREATE TABLE SampleTable_staging(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_staging_A(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_staging_B(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_staging_C(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_staging_D(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_staging_E(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));
CREATE TABLE SampleTable_staging_F(id CHAR(4), a CHAR(16), b CHAR(40), c CHAR(40), PRIMARY KEY (id));

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

INSERT INTO SampleTable (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');

INSERT INTO SampleTable_A (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable_A (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable_A (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');

INSERT INTO SampleTable_B (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable_B (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable_B (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');

INSERT INTO SampleTable_C (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable_C (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable_C (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');

INSERT INTO SampleTable_D (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable_D (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable_D (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');

INSERT INTO SampleTable_E (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable_E (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable_E (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');

INSERT INTO SampleTable_F (id, a, b, c) VALUES ('100', 'a0', 'b0', 'c0');
INSERT INTO SampleTable_F (id, a, b, c) VALUES ('101', 'a1', 'b1', 'c1');
INSERT INTO SampleTable_F (id, a, b, c) VALUES ('102', 'a2', 'b2', 'c2');
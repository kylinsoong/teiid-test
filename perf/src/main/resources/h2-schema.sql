DROP TABLE IF EXISTS PERFTEST;
DROP TABLE IF EXISTS PERFTEST_MAT;
DROP TABLE IF EXISTS PERFTEST_STAGING;
DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS QUERYSQL;
DROP TABLE IF EXISTS PERFRESULT;
DROP TABLE IF EXISTS status;

CREATE TABLE PERFTEST(id CHAR(4), col_a CHAR(16), col_b CHAR(40), col_c CHAR(40));
CREATE TABLE PERFTEST_MAT(id CHAR(4), col_a CHAR(16), col_b CHAR(40), col_c CHAR(40));
CREATE TABLE PERFTEST_STAGING(id CHAR(4), col_a CHAR(16), col_b CHAR(40), col_c CHAR(40));
CREATE TABLE QUERYSQL (id integer not null auto_increment, content varchar(255), PRIMARY KEY (id));
CREATE TABLE ITEMS (id integer not null auto_increment, item varchar(40), PRIMARY KEY (id));
CREATE TABLE PERFRESULT (id integer not null auto_increment, value integer, item_id integer, querysql_id integer, PRIMARY KEY (id));


CREATE TABLE status
(
  VDBName varchar(50) not null,
  VDBVersion integer not null,
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


ALTER TABLE PERFRESULT ADD CONSTRAINT FK_TO_QUERYSQL FOREIGN KEY (querysql_id) REFERENCES QUERYSQL (id);
ALTER TABLE PERFRESULT ADD CONSTRAINT FK_TO_ITEMS FOREIGN KEY (item_id) REFERENCES ITEMS (id);

INSERT INTO ITEMS (item) VALUES ('Query Time');
INSERT INTO ITEMS (item) VALUES ('Deserialize Time');
INSERT INTO ITEMS (item) VALUES ('Serialize Time');
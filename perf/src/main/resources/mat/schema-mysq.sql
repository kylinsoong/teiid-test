DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS test_mat;
DROP TABLE IF EXISTS test_mat_staging;
DROP TABLE IF EXISTS PRODUCT;

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
  Cardinality bigint,
  Updated timestamp not null,
  LoadNumber bigint not null,
  PRIMARY KEY (VDBName, VDBVersion, SchemaName, Name)
);

CREATE TABLE test_mat(
   product_id integer,
   SYMBOL varchar(16)
);

CREATE TABLE test_mat_staging(
   product_id integer,
   SYMBOL varchar(16)
);

CREATE TABLE  PRODUCT (
   ID integer,
   SYMBOL varchar(16),
   COMPANY_NAME varchar(256),
   CONSTRAINT PRODUCT_PK PRIMARY KEY(ID)
);


INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(100,'IBM','International Business Machines Corporation');
INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(101,'DELL','Dell Computer Corporation');
INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(102,'HPQ','Hewlett-Packard Company');
INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(103,'GE','General Electric Company');
INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(104,'SAP','SAP AG');
INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(105,'TM','Toyota Motor Corporation');
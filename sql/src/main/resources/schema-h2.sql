DROP TABLE IF EXISTS STATUS;

CREATE TABLE STATUS (
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

INSERT INTO STATUS VALUES ('VDB_1', 1, 'Schema', 'Name', 'TargetSchemaName', 'TargetName', true, 'LOADED', 1, '2015-06-07 21:08:26.856', 1);
INSERT INTO STATUS VALUES ('VDB_2', 1, 'Schema', 'Name', 'TargetSchemaName', 'TargetName', true, 'LOADED', 1, '2015-06-07 21:08:26.856', 1);
INSERT INTO STATUS VALUES ('VDB_3', 1, 'Schema', 'Name', 'TargetSchemaName', 'TargetName', true, 'LOADED', 1, '2015-06-07 21:08:26.856', 1);
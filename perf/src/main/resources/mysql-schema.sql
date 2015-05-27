DROP TABLE IF EXISTS PERFTEST;
DROP TABLE IF EXISTS PERFTEST_MAT;
DROP TABLE IF EXISTS PERFTEST_STAGING;

CREATE TABLE PERFTEST(id INTEGER, col_a CHAR(16), col_b CHAR(40), col_c CHAR(40));
CREATE TABLE PERFTEST_MAT(id INTEGER, col_a CHAR(16), col_b CHAR(40), col_c CHAR(40));
CREATE TABLE PERFTEST_STAGING(id INTEGER, col_a CHAR(16), col_b CHAR(40), col_c CHAR(40));


DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS QUERYSQL;
DROP TABLE IF EXISTS PERFRESULT;

CREATE TABLE QUERYSQL (id integer not null auto_increment, content varchar(255), PRIMARY KEY (id));
CREATE TABLE ITEMS (id integer not null auto_increment, item varchar(40), PRIMARY KEY (id));
CREATE TABLE PERFRESULT (id integer not null auto_increment, value integer, item_id integer, querysql_id integer, PRIMARY KEY (id));

ALTER TABLE PERFRESULT ADD CONSTRAINT FK_TO_QUERYSQL FOREIGN KEY (querysql_id) REFERENCES QUERYSQL (id);
ALTER TABLE PERFRESULT ADD CONSTRAINT FK_TO_ITEMS FOREIGN KEY (item_id) REFERENCES ITEMS (id);

INSERT INTO ITEMS (item) VALUES ('Query Time');
INSERT INTO ITEMS (item) VALUES ('Deserialize Time');
INSERT INTO ITEMS (item) VALUES ('Serialize Time');
DROP TABLE IF EXISTS SmallA;
DROP TABLE IF EXISTS SmallB;

CREATE TABLE SmallA (
    intkey integer,
    bytenum TINYINT,
    longnum BIGINT
);

CREATE TABLE SmallB (
    intkey integer
);

INSERT INTO SmallA (intkey, bytenum, longnum) VALUES (1, 127, 127);
INSERT INTO SmallB (intkey) VALUES (1);
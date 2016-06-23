DROP TABLE IF EXISTS share_market_data;

CREATE TABLE share_market_data(id int, frequency int, ts timestamp, PRIMARY KEY (id));


INSERT INTO share_market_data (id, frequency, ts) VALUES (100, 5000, {ts '2016-04-09 12:00:00.0'});
INSERT INTO share_market_data (id, frequency, ts) VALUES (101, 5000, {ts '2016-04-09 12:00:00.0'});
INSERT INTO share_market_data (id, frequency, ts) VALUES (102, 5000, {ts '2016-04-09 12:00:00.0'});
INSERT INTO share_market_data (id, frequency, ts) VALUES (103, 6000, {ts '2016-04-09 12:00:00.0'});
INSERT INTO share_market_data (id, frequency, ts) VALUES (104, 6000, {ts '2016-04-09 12:00:00.0'});
INSERT INTO share_market_data (id, frequency, ts) VALUES (105, 6000, {ts '2016-04-09 12:00:00.0'});
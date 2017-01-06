-- create db
CREATE DATABASE teiid;
CREATE USER 'teiid_user'@'%' IDENTIFIED BY 'teiid_pass';
GRANT ALL PRIVILEGES ON teiid.* TO 'teiid_user'@'%';

-- load data
mysql -u teiid_user -p teiid < teiid-transactions-mariadb.sql
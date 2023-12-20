DROP TABLE IF EXISTS news;
CREATE TABLE news
(
    id int NOT NULL AUTO_INCREMENT,
    keyword varchar(100) NOT NULL,
    title varchar(1000) NOT NULL,
    originallink varchar(1000) NOT NULL,
    link varchar(1000) NOT NULL,
    description text,
    pubDate datetime NOT NULL,
    PRIMARY KEY (id)
);
DELETE FROM news;
ALTER TABLE news AUTO_INCREMENT = 1;


SELECT * FROM news ORDER BY id;


DROP TABLE IF EXISTS api;


CREATE TABLE api
(
    docexamenddt varchar(50),
    docexamstartdt varchar(50),
    docpassdt varchar(50),
    docregenddt varchar(50),
    docregstartdt varchar(50) ,
    implplannm varchar(50) not null ,
    jmfldnm varchar(50) not null not null ,
    pracexamenddt varchar(50) not null,
    pracexamstartdt varchar(50) not null,
    pracpassenddt varchar(50) not null,
    pracpassstartdt varchar(50) not null,
    pracregenddt varchar(50) not null,
    pracregstartdt varchar(50) not null,
    fee varchar(50) not null
);

SELECT * FROM api;
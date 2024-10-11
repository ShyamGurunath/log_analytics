CREATE USER 'dsk_admin'@'%' IDENTIFIED BY 'dsk';

GRANT ALL PRIVILEGES ON * . * TO 'dsk_admin'@'%';

FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS dsk;

USE dsk;

CREATE TABLE IF NOT EXISTS DSK_APPLICATION (
    app_id int NOT NULL AUTO_INCREMENT,
    app_name varchar(255) NOT NULL,
    PRIMARY KEY (app_id),
    UNIQUE (app_name)
);

CREATE TABLE IF NOT EXISTS DSK_LOGGER (
    id int NOT NULL AUTO_INCREMENT,
    app_id int NOT NULL,
    name varchar(255) NOT NULL,
    kafka_topic varchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name),
    CONSTRAINT FK_app_id FOREIGN KEY (app_id)
    REFERENCES DSK_APPLICATION(app_id)
);
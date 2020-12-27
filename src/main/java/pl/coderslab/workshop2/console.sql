
CREATE TABLE users (
    id int(11) not null auto_increment primary key,
    email varchar(255) not null unique,
    username varchar(255) not null,
    password varchar(60) not null
);

INSERT INTO users (email, username, password) VALUES ('thedariusz@gmail.com', 'TheDariusz', 'testtest');
INSERT INTO users (email, username, password) VALUES ('mario@gmail.com', 'Mario69', 'nintendo');
INSERT INTO users (email, username, password) VALUES ('leon@yahoo.com', 'Leoniasty', 'malami');

SELECT * FROM users -- WHERE id=1;

UPDATE users SET username='costam', email='cos@costam.com', password='testtesttest' WHERE id=1;

DELETE FROM users WHERE id=3;
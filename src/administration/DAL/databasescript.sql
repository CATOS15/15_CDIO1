CREATE DATABASE UserDao3;
USE UserDao3;

DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Roles;
DROP TABLE IF EXISTS UserRoles;

CREATE TABLE Users (
    id int,
    username varchar(255),
    password varchar(255),
    CPRnummer varchar(255),
    PRIMARY KEY(id)
);

CREATE TABLE Roles(
	id int,
    rolename varchar(255),
    PRIMARY KEY(id)
);

CREATE TABLE UserRoles(
    userid int NOT NULL,
    roleid int NOT NULL,
    PRIMARY KEY (userid, roleid),
    CONSTRAINT fk_userid FOREIGN KEY (userid)
    REFERENCES users(id),
    CONSTRAINT fk_roleid FOREIGN KEY (roleid)
    REFERENCES roles(id)
);
INSERT INTO roles
VALUES (1, "Admin"), (2, "Pharmacist"), (3, "Foreman"), (4, "Operator");

/* HERFRA BEHØBES IKKE UDFØRES DA DET BLOT ER TIL TEST */
INSERT INTO users
VALUES (1, "kagen", "koden", "1010951999");
INSERT INTO userroles
VALUES (1, 1);
UPDATE users SET username="blah", password="lala", cprnummer="1234569393" WHERE id=1;
SELECT * FROM users;
SELECT username, rolename FROM roles, userroles, users WHERE roles.id = userroles.roleid AND users.id = userroles.userid;
CREATE TABLE user (
	id INTEGER auto_increment NOT NULL,
	name varchar(256) NOT NULL,
	email varchar(256) NOT NULL,
	password varchar(100) NOT NULL,
	linkedin varchar(128) NULL,
	CONSTRAINT user_PK PRIMARY KEY (id)
);

CREATE TABLE student (
	id INTEGER auto_increment NOT NULL,
	university varchar(128) NULL,
	graduation varchar(128) NULL,
	finish_date DATE NULL,
	user_id INTEGER NOT NULL,
	CONSTRAINT student_PK PRIMARY KEY (id),
	CONSTRAINT student_FK FOREIGN KEY (user_id) REFERENCES dea_management.`user`(id) ON DELETE CASCADE ON UPDATE CASCADE
)
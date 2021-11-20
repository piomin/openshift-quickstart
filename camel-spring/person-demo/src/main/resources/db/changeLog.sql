--liquibase formatted sql

--changeset piomin:1
create table person (
  id integer GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
  name varchar(255),
  gender varchar(255),
  age int,
  externalId int
);
insert into person(name, age, gender) values('John Smith', 25, 'MALE');
insert into person(name, age, gender) values('Paul Walker', 65, 'MALE');
insert into person(name, age, gender) values('Lewis Hamilton', 35, 'MALE');
insert into person(name, age, gender) values('Veronica Jones', 20, 'FEMALE');
insert into person(name, age, gender) values('Anne Brown', 60, 'FEMALE');
insert into person(name, age, gender) values('Felicia Scott', 45, 'FEMALE');

--changeset piomin:2
CREATE PROCEDURE COUNT_OLDER_THAN_4(IN age_in INTEGER, OUT count_out INTEGER)
IS
BEGIN
    SELECT COUNT(*) into count_out from person WHERE age > age_in;
END;
/
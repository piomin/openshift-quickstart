--liquibase formatted sql

--changeset piomin:1
create table person (
  id serial primary key,
  name varchar(255),
  gender varchar(255),
  age int
);
insert into person(name, age, gender) values('John Smith', 25, 'MALE');
insert into person(name, age, gender) values('Paul Walker', 65, 'MALE');
insert into person(name, age, gender) values('Lewis Hamilton', 35, 'MALE');
insert into person(name, age, gender) values('Veronica Jones', 20, 'FEMALE');
insert into person(name, age, gender) values('Anne Brown', 60, 'FEMALE');
insert into person(name, age, gender) values('Felicia Scott', 45, 'FEMALE');
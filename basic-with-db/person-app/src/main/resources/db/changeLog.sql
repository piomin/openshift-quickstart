--liquibase formatted sql

--changeset piomin:1
create table person (
  id serial primary key,
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
create table person2 (
  id serial primary key,
  first_name varchar(255),
  last_name varchar(255),
  gender varchar(255),
  age int,
  external_id int
);
insert into person2(first_name, last_name, age, gender, external_id) values('John', 'Smith', 25, 'MALE', 1001);
insert into person2(first_name, last_name, age, gender, external_id) values('Paul', 'Walker', 65, 'MALE', 1005);
insert into person2(first_name, last_name, age, gender, external_id) values('Lewis', 'Hamilton', 35, 'MALE', 1021);
insert into person2(first_name, last_name, age, gender, external_id) values('Veronica', 'Jones', 20, 'FEMALE', 1051);
insert into person2(first_name, last_name, age, gender, external_id) values('Anne', 'Brown', 60, 'FEMALE', 1081);
insert into person2(first_name, last_name, age, gender, external_id) values('Felicia', 'Scott', 45, 'FEMALE', 1201);
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
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');
insert into person(name, age, gender) values('Test1', 50, 'MALE');

--changeset piomin:2
update person set externalId = 100 where id = 1;
update person set externalId = 200 where id = 2;
update person set externalId = 300 where id = 3;
update person set externalId = 400 where id = 4;
update person set externalId = 101 where id = 5;
update person set externalId = 201 where id = 6;
update person set externalId = 301 where id = 7;
update person set externalId = 401 where id = 8;
update person set externalId = 102 where id = 9;
update person set externalId = 202 where id = 10;
update person set externalId = 302 where id = 11;
update person set externalId = 402 where id = 12;
update person set externalId = 103 where id = 13;
update person set externalId = 203 where id = 14;
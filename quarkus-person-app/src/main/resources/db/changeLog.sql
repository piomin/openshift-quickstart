--liquibase formatted sql

--changeset piomin:1
insert into person(id, name, age, gender) values(1, 'John Smith', 25, 'MALE');
insert into person(id, name, age, gender) values(2, 'Paul Walker', 65, 'MALE');
insert into person(id, name, age, gender) values(3, 'Lewis Hamilton', 35, 'MALE');
insert into person(id, name, age, gender) values(4, 'Veronica Jones', 20, 'FEMALE');
insert into person(id, name, age, gender) values(5, 'Anne Brown', 60, 'FEMALE');
insert into person(id, name, age, gender) values(6, 'Felicia Scott', 45, 'FEMALE');
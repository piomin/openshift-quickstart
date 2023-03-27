--liquibase formatted sql

--changeset piomin:1
create table insurance (
  id serial primary key,
  personId int,
  type varchar(255),
  amount int,
  expiry date
);
insert into insurance(personId, type, amount) values(1, 'MEDICAL', 1000);
insert into insurance(personId, type, amount) values(2, 'MEDICAL', 800);
insert into insurance(personId, type, amount) values(3, 'LIFE', 30000);
insert into insurance(personId, type, amount) values(4, 'LIFE', 20000);
insert into insurance(personId, type, amount) values(5, 'MEDICAL', 1500);
insert into insurance(personId, type, amount) values(6, 'LIFE', 50000);
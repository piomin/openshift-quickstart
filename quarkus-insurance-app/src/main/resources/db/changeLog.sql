--liquibase formatted sql

--changeset piomin:1
insert into insurance(id, personId, type, amount) values(1, 1, 'MEDICAL', 1000);
insert into insurance(id, personId, type, amount) values(2, 2, 'MEDICAL', 800);
insert into insurance(id, personId, type, amount) values(3, 3, 'LIFE', 30000);
insert into insurance(id, personId, type, amount) values(4, 4, 'LIFE', 20000);
insert into insurance(id, personId, type, amount) values(5, 5, 'MEDICAL', 1500);
insert into insurance(id, personId, type, amount) values(6, 6, 'LIFE', 50000);
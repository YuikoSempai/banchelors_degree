--liquibase formatted sql

--changeset bekmvlad27:create-table-room
create table room(
    id bigserial primary key,
    name text not null
);
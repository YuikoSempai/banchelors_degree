--liquibase formatted sql

--changeset bekmvlad27:create_quiz_table
create table quiz(
    id bigserial primary key,
    name text not null,
    data text not null
);
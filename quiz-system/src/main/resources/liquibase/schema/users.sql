--liquibase formatted sql

--changeset bekmvlad27:create-table-users
create table users(
    id bigserial primary key,
    username text not null,
    password text not null
);

--changeset bekmvlad27:add-unique-constraint
alter table users add constraint unique_username unique(username);
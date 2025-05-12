--liquibase formatted sql

--changeset bekmvlad27:create-table-room
create table user_room_mapping(
    id      bigserial primary key,
    user_id bigint not null,
    room_id bigint not null,
    unique (user_id, room_id)
);
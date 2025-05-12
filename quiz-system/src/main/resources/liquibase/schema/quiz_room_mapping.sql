--liquibase formatted sql

--changeset bekmvlad27:create-table-room
create table quiz_room_mapping(
    id      bigserial primary key,
    quiz_id bigint not null,
    room_id bigint not null,
    unique (quiz_id, room_id)
);
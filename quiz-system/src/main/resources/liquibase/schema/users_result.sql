--liquibase formatted sql

--changeset bekmvlad27:create-table-users
create table users_result(
    id bigserial primary key,
    user_id bigint not null,
    quiz_id bigint not null,
    correct_answers bigint not null,
    total_questions bigint not null
);
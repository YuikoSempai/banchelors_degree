--liquibase formatted sql

--changeset bekmvlad27:create_quiz_table
create table quiz(
    id bigserial primary key,
    name text not null,
    data text not null
);

--changeset bekmvlad27:add_page_count_column
alter table quiz add column page_count bigint not null default 0;

--changeset bekmvlad27:add_frontend_data_column
alter table quiz add column frontend_data text not null default '';

-- changeset bekmvlad27:remove-3
alter table quiz drop column if exists frontend_data;
alter table quiz drop column if exists data;
alter table quiz drop column if exists page_count;
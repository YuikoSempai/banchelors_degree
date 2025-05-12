--liquibase formatted sql

--changeset bekmvlad27:create_quiz_page_mappings_table
create table if not exists quiz_page_mappings(
    id bigserial primary key,
    main_quiz_id bigint not null,
    page_id bigint not null,
    data text not null
);

--changeset bekmvlad27:add_front_data
alter table quiz_page_mappings add column frontend_data text not null default '';
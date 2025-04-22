--liquibase formatted sql
--changeset heckfy88:2
--comment: добавлена таблица категорий

create type finances.category_type as enum ('INCOME', 'EXPENSE');

create table finances.category
(
    id          uuid                            default uuid_generate_v4(),
    name        varchar(100)           not null,
    description varchar(255),
    type        finances.category_type  not null,
    created_at  timestamp              not null default now(),

    constraint pk_category primary key (id)
);
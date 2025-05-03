--liquibase formatted sql
--changeset heckfy88:1
--comment: добавлена таблица пользователей

create type finances.user_type as enum ('PHYSICAL', 'LEGAL');

create table finances.user
(
    id            uuid      default uuid_generate_v4(),
    name          varchar(100)        not null,
    email         varchar(100) unique not null,
    password_hash text                not null,
    type          finances.user_type  not null,
    created_at    timestamp default now(),
    is_active     boolean   default true,

    constraint pk_user primary key (id)
);

insert into finances.user (id, name, email, password_hash, type, created_at, is_active)
values ('123e4567-e89b-12d3-a456-426614174000', 'John Doe', 'john.doe@example.com',
        '$2a$12$/Nvsbyo9zPNow9VtkA5yLufPgQCDQBdongv6cwb3QAZwIl7prYUnu', 'PHYSICAL', '2022-01-01 12:00:00', true),
       ('123e4567-e89b-12d3-a456-426614174001', 'Jane Doe', 'jane.doe@example.com',
        '$2a$12$hSNGkB5qkK/tJS2Qgyiv9eaTKGHSUs9ZJcg4Aqn7M1ayF8xCmOvDW', 'PHYSICAL', '2022-01-01 12:00:00', true),
       ('123e4567-e89b-12d3-a456-426614174002', 'John Smith', 'john.smith@example.com',
        '$2a$12$4qxGESYATnYD5kQyJDDU2OpwU9GOvDwgoETaVthsSUIywy4wSX2dK', 'LEGAL', '2022-01-01 12:00:00', true),
       ('123e4567-e89b-12d3-a456-426614174003', 'Company Inc.', 'company@example.com',
        '$2a$12$CROd88OYK1C1VMg9Bzv./uP4GuUr1NL.qYNPvWv29Xamb.Q8/ga4a', 'LEGAL', '2022-01-01 12:00:00', true);
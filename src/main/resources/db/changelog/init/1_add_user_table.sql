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


comment on type finances.user_type is 'Тип пользователя (физическое лицо или юридическое лицо)';

comment on table finances.user is 'Пользователи';

comment on column finances.user.id is 'Идентификатор пользователя';
comment on column finances.user.name is 'Имя пользователя';
comment on column finances.user.email is 'Электронная почта пользователя';
comment on column finances.user.password_hash is 'Хэш пароля пользователя';
comment on column finances.user.type is 'Тип пользователя (физическое лицо или юридическое лицо)';
comment on column finances.user.created_at is 'Дата создания пользователя';
comment on column finances.user.is_active is 'Активность пользователя';
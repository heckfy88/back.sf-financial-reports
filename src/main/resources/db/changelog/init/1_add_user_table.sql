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


comment on type finances.user_type is 'Тип пользователя (физическое лицо или юридическое лицо)';

comment on table finances.user is 'Пользователи';

comment on column finances.user.id is 'Идентификатор пользователя';
comment on column finances.user.name is 'Имя пользователя';
comment on column finances.user.email is 'Электронная почта пользователя';
comment on column finances.user.password_hash is 'Хэш пароля пользователя';
comment on column finances.user.type is 'Тип пользователя (физическое лицо или юридическое лицо)';
comment on column finances.user.created_at is 'Дата создания пользователя';
comment on column finances.user.is_active is 'Активность пользователя';
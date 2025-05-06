--liquibase formatted sql
--changeset heckfy88:4
--comment: добавлена таблица аудита

create table finances.audit
(
    id              uuid                   default uuid_generate_v4(),
    oper_uid        uuid          not null,
    user_id         uuid,
    message_type    varchar(50)   not null,
    request_type    varchar(100)  not null,
    request_path    varchar(100)  not null,
    request_headers varchar(1024) not null,
    request_params  varchar(255),
    payload         varchar,
    created_at      timestamp     not null default now(),

    constraint pk_audit primary key (id)
)
--liquibase formatted sql
--changeset heckfy88:4
--comment: добавлена таблица интеграционного логирования

create table finances.integration_log (
    id bigserial not null,
    oper_uid uuid not null,
    request_type varchar(100) not null,
    created_at timestamp not null default now(),
    payload json,
    request_params varchar(255),

    constraint pk_integration_log primary key(id)
)
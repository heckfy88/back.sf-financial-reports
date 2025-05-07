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
    request_headers varchar not null,
    request_params  varchar(255),
    payload         varchar,
    created_at      timestamp     not null default now(),

    constraint pk_audit primary key (id)
);

comment on table finances.audit is 'Аудит сервиса';

comment on column finances.audit.id is 'Идентификатор записи';
comment on column finances.audit.oper_uid is 'Уникальный идентификатор операции';
comment on column finances.audit.user_id is 'Идентификатор пользователя';
comment on column finances.audit.message_type is 'Тип сообщения';
comment on column finances.audit.request_type is 'Тип запроса';
comment on column finances.audit.request_path is 'Путь запроса';
comment on column finances.audit.request_headers is 'Заголовки запроса';
comment on column finances.audit.request_params is 'Параметры запроса';
comment on column finances.audit.payload is 'Тело запроса';
comment on column finances.audit.created_at is 'Дата создания записи';
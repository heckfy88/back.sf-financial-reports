--liquibase formatted sql
--changeset heckfy88:3
--comment: добавлена таблица транзакций

create type finances.transaction_status as enum (
    'NEW',
    'CONFIRMED',
    'PROCESSING',
    'COMPLETED',
    'RETURNED',
    'CANCELLED',
    'DELETED'
    );

create table finances.transaction
(
    id               uuid      default uuid_generate_v4(),
    user_id          uuid                        not null,
    category_name    varchar(100)                not null,
    "date"           date                        not null,
    description      text,
    amount           numeric(15, 5)              not null check (amount >= 0),
    status           finances.transaction_status not null,
    sender_bank      varchar(100),
    sender_account   varchar(30),
    receiver_user_type finances.user_type not null,
    receiver_bank    varchar(100),
    receiver_account varchar(30),
    receiver_inn     varchar(12),
    receiver_phone   varchar(20),
    created_at       timestamp default now(),

    constraint pk_transaction primary key (id),
    constraint fk_user_id foreign key (user_id) references finances.user (id),
    constraint transaction_category_fkey foreign key (user_id, category_name) references finances.category (user_id, name)
);

create index idx_transaction_user_id on finances.transaction (user_id);
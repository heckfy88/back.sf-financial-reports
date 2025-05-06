--liquibase formatted sql
--changeset heckfy88:2
--comment: добавлена таблица категорий

create type finances.category_type as enum ('INCOME', 'EXPENSE');

create table finances.category
(
    id          uuid                            default uuid_generate_v4(),
    user_id     uuid                            not null,
    name        varchar(100)           not null,
    description varchar(255),
    type        finances.category_type  not null,
    created_at  timestamp              not null default now(),

    constraint pk_category primary key (id),
    constraint fk_category_user_id_fk foreign key (user_id) references finances.user (id),
    constraint unique_category_name unique (user_id, name)
);


comment on type finances.category_type is 'Тип категории дохода/расхода';

comment on table finances.category is 'Категория дохода/расхода';

comment on column finances.category.id is 'Идентификатор категории';
comment on column finances.category.user_id is 'Идентификатор пользователя';
comment on column finances.category.name is 'Название категории';
comment on column finances.category.description is 'Описание категории';
comment on column finances.category.type is 'Тип категории';
comment on column finances.category.created_at is 'Дата создания категории';
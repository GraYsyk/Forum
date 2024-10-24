--liquibase formatted sql

--changeset vnavesnoj:1
create table userinf
(
    id        serial
        primary key,
    username  varchar(30)                               not null
        unique,
    avatar    bytea,
    createdat date,
    password  varchar,
    role      varchar(40),
    status    varchar default 'User'::character varying not null
);

--changeset vnavesnoj:2
create table post
(
    id         serial
        primary key,
    user_id    integer
                            references userinf
                                on delete set null,
    title      varchar(124) not null,
    descr      varchar(100),
    content    text         not null,
    created_at date,
    updated_at date,
    status     varchar default 'active'::character varying
);

--changelog vnavesnoj:3
create table comments
(
    id         serial
        primary key,
    post_id    integer not null
        references post,
    user_id    integer not null
        references userinf,
    content    text    not null,
    created_at date    not null,
    updated_at date    not null,
    status     varchar(20) default 'active'::character varying
        constraint comments_status_check
            check ((status)::text = ANY ((ARRAY ['active'::character varying, 'deleted'::character varying])::text[]))
);

--changelog vnavesnoj:4
create table media
(
    id        serial
        primary key,
    post_id   integer
        references post
            on delete cascade,
    media_url bytea not null,
    type      varchar,
    filename  varchar
);
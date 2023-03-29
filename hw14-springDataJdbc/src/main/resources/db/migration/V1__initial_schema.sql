create table client
(
    id bigint generated by default as identity,
    name varchar(255),
    address_id varchar(36),
    primary key (id)
);
create table address
(
    id varchar(36) not null,
    street varchar(255),
    primary key (id)
);
create table phone
(
    id varchar(36),
    number varchar(255),
    client_id bigint not null,
    primary key (id)
);
alter table phone add constraint FK_PHONE_CLIENT foreign key (client_id) references client (id);
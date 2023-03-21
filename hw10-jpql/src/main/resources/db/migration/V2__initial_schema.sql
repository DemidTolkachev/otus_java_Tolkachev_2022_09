create table address
(
    id   bigserial not null primary key,
    street varchar(50)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint not null
);

alter table client add column address_id bigint
CONSTRAINT fk_address_id REFERENCES address(id);
alter table phone add constraint fk_phone_client foreign key (client_id) references client(id);
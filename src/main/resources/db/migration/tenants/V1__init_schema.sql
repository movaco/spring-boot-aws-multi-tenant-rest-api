create table user_details(
    id uuid not null primary key,
    version integer not null,
    time_persisted timestamp not null,
    time_modified timestamp not null,
    user_name varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null,
    constraint UC_USER_NAME unique (user_name),
    constraint UC_EMAIL unique (email)
);
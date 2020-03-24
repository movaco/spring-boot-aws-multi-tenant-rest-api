create table tenant (
    id uuid not null primary key,
    version integer,
    time_persisted timestamp,
    time_modified timestamp,
    tenant_name varchar(255),
    schema_name varchar(255),
    constraint UC_TENANT_NAME unique (tenant_name),
    constraint UC_SCHEMA_NAME unique (schema_name)
);
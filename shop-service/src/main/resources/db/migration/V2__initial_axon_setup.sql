create sequence hibernate_sequence start 1 increment 1;

create table association_value_entry (
                                         id int8 not null,
                                         association_key varchar(255) not null,
                                         association_value varchar(255),
                                         saga_id varchar(255) not null,
                                         saga_type varchar(255),
                                         primary key (id)
);

create table domain_event_entry (
                                    global_index int8 not null,
                                    event_identifier varchar(255) not null,
                                    meta_data oid,
                                    payload oid not null,
                                    payload_revision varchar(255),
                                    payload_type varchar(255) not null,
                                    time_stamp varchar(255) not null,
                                    aggregate_identifier varchar(255) not null,
                                    sequence_number int8 not null,
                                    type varchar(255),
                                    primary key (global_index)
);

create table saga_entry (
                            saga_id varchar(255) not null,
                            revision varchar(255),
                            saga_type varchar(255),
                            serialized_saga oid,
                            primary key (saga_id)
);

create table snapshot_event_entry (
                                      aggregate_identifier varchar(255) not null,
                                      sequence_number int8 not null,
                                      type varchar(255) not null,
                                      event_identifier varchar(255) not null,
                                      meta_data oid,
                                      payload oid not null,
                                      payload_revision varchar(255),
                                      payload_type varchar(255) not null,
                                      time_stamp varchar(255) not null,
                                      primary key (aggregate_identifier, sequence_number, type)
);

create table token_entry (
                             processor_name varchar(255) not null,
                             segment int4 not null,
                             owner varchar(255),
                             timestamp varchar(255) not null,
                             token oid,
                             token_type varchar(255),
                             primary key (processor_name, segment)
);
create index IDX4056lb0rolits0rrkm49a22n on association_value_entry (saga_id, association_key);

alter table domain_event_entry
    add constraint UKdg43ia27ypo1jovw2x64vbwv8 unique (aggregate_identifier, sequence_number);

alter table domain_event_entry
    add constraint UK_k5lt6d2792amnloo7q91njp0v unique (event_identifier);

alter table snapshot_event_entry
    add constraint UK_sg7xx45yh4ajlsjd8t0uygnjn unique (event_identifier);

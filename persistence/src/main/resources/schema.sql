-- noinspection SqlNoDataSourceInspectionForFile
CREATE TABLE IF NOT EXISTS users (
    id serial primary key,
    email text not null unique,
    password text not null,
    role int not null,                                   --Should be 1 for only PATIENT, 2 for MEDIC, 3 for CLINIC, 4 for MEDIC AND CLINIC, 0 for ADMIN
    locale varchar(10) default 'en-US'
);

CREATE TABLE IF NOT EXISTS users (
    id serial primary key,
    email text not null unique,
    password text not null
);

CREATE TABLE IF NOT EXISTS clinics (
    user_id int not null unique,
    name text not null,
    email text not null unique,                --This is not necessarily the same email they use to login
    telephone text,
    verified boolean not null default false,        --Until verified by humans, it should remain false
    foreign key(user_id) references users
);

CREATE TABLE IF NOT EXISTS medical_studies (
    id serial primary key,
    name text not null,
    unique(name)
);

CREATE TABLE IF NOT EXISTS clinic_available_studies (
    clinic_id int not null,
    study_id int not null,
    primary key(clinic_id,study_id),
    foreign key(clinic_id) references clinics(user_id),
    foreign key(study_id) references medical_studies
);

CREATE TABLE IF NOT EXISTS medics (
    user_id int not null unique,
    name text not null,         --Secretary might handle the medic info and use same account as a patient, so we keep the separate
    email text not null unique,        --Business email might not be same used to login
    telephone text,
    identification_type text not null,
    identification bytea not null,
    licence_number text not null,   --Should be unique, but if we think internationally maybe numbers collide
    verified boolean not null default false,    --Until verified by humans, it should remain false
    foreign key(user_id) references users
);

CREATE TABLE IF NOT EXISTS medical_fields (
    id serial primary key,
    name text not null,
    unique(name)
);

CREATE TABLE IF NOT EXISTS medic_medical_fields (
    medic_id int not null,
    field_id int not null,
    primary key(medic_id, field_id),
    foreign key(medic_id) references medics(user_id),
    foreign key(field_id) references medical_fields
);

CREATE TABLE IF NOT EXISTS patients (
    user_id int not null unique,
    name text not null,
    medic_plan text,
    medic_plan_number text,
    foreign key(user_id) references  users
);

CREATE TABLE IF NOT EXISTS medical_orders (
   id bigserial primary key,
   medic_id int not null,
   date date not null,
   clinic_id int not null,
   patient_name text not null,
   patient_email text not null,
   study_id int not null,
   description text,
   identification_type text not null,       --We want to freeze info in time for the orders, so each order hold the plan/identification info used for it
   identification bytea not null,
   medic_plan text,medic_plan_number text,
   foreign key(medic_id) references medics(user_id),
   foreign key(clinic_id) references clinics(user_id),
   foreign key(study_id) references medical_studies
);

CREATE TABLE IF NOT EXISTS results (
   id bigserial primary key,
   order_id bigint not null,
   result_data_type text not null,
   result_data bytea not null,
   identification_type text not null,
   identification bytea not null,
   date date not null,
   responsible_name text not null,
   responsible_licence_number text not null,
   foreign key(order_id) references medical_orders
);
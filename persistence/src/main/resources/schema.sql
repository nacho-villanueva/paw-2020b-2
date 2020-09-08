-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE IF NOT EXISTS clinics (
    id serial primary key,
    name text not null,
    email text not null,
    telephone text,
    unique(email)
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
    foreign key(clinic_id) references clinics,
    foreign key(study_id) references medical_studies
);

CREATE TABLE IF NOT EXISTS medics (
    id serial primary key,
    name text not null,
    email text not null,
    telephone text,
    licence_number text not null,
    unique(email)
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
    foreign key(medic_id) references medics,
    foreign key(field_id) references medical_fields
);

CREATE TABLE IF NOT EXISTS patients (
    id serial primary key,
    email text not null,
    name text not null,
    unique(email)
);

CREATE TABLE IF NOT EXISTS medical_orders (
   id bigserial primary key,
   medic_id int not null,
   date date not null,
   clinic_id int not null,
   patient_id int not null,
   study_id int not null,
   description text,
   identification_type text not null,
   identification bytea not null,
   medic_plan text,medic_plan_number text,
   foreign key(medic_id) references medics,
   foreign key(clinic_id) references clinics,
   foreign key(patient_id) references patients,
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
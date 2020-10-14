CREATE TABLE IF NOT EXISTS users (
    id serial primary key,
    email text not null unique,
    password text not null,
    role int not null,
    locale varchar(10) default 'en-US'
);

CREATE TABLE IF NOT EXISTS clinics (
    user_id int not null unique,
    name text not null,
    telephone text,
    verified boolean not null default false,
    foreign key(user_id) references users
);

CREATE TABLE IF NOT EXISTS medical_studies (
    id serial primary key,
    name text not null,
    unique(name)
);

CREATE TABLE IF NOT EXISTS clinic_hours (
    clinic_id int not null,
    day_of_week int not null,
    open_time time not null,
    close_time time not null,
    primary key(clinic_id,day_of_week),
    foreign key(clinic_id) references clinics(user_id)
);

CREATE TABLE IF NOT EXISTS clinic_accepted_plans (
    clinic_id int not null,
    medic_plan text not null,
    primary key(clinic_id,medic_plan),
    foreign key(clinic_id) references clinics(user_id)
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
    name text not null,
    telephone text,
    identification_type text not null,
    identification bytea not null,
    licence_number text not null,
    verified boolean not null default false,
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
   identification_type text not null,
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
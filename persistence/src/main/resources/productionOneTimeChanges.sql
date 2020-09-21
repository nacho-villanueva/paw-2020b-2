--One time changes to go from first iteration schema to new schema (IMPORTANT: SHOULD ONLY BE EXECUTED ONCE BY HAND, DO NOT ADD TO CONFIGURATION)

--Begin transaction
begin;
CREATE TABLE IF NOT EXISTS users (
    id serial primary key,
    email text not null unique,
    password text not null,
    role int not null                                   --Should be 1 for only PATIENT, 2 for MEDIC, 3 for CLINIC, 4 for MEDIC AND CLINIC, 0 for ADMIN
);
--Removing constraints for ease of data manipulation
alter table only clinic_available_studies drop constraint clinic_available_studies_pkey;
alter table only clinic_available_studies drop constraint clinic_available_studies_clinic_id_fkey;
alter table only medic_medical_fields drop constraint medic_medical_fields_pkey;
alter table only medic_medical_fields drop constraint medic_medical_fields_medic_id_fkey;
alter table only medical_orders drop constraint medical_orders_clinic_id_fkey;
alter table only medical_orders drop constraint medical_orders_medic_id_fkey;
alter table only medical_orders drop constraint medical_orders_patient_id_fkey;

alter table only medics drop constraint medics_pkey;
alter table only patients drop constraint patients_pkey;
alter table only clinics drop constraint clinics_pkey;

--Adding Columns with few constraints cuz they start in null
alter table clinics add column user_id int;
alter table clinics add column verified boolean default false;
alter table medics add column user_id int;
alter table medics add column verified boolean default false;
alter table medics add column identification_type text;
alter table medics add column identification bytea;
alter table patients add column user_id int;
alter table patients add column medic_plan text;
alter table patients add column medic_plan_number text;
alter table medical_orders add column patient_name text;
alter table medical_orders add column patient_email text;

--Temporary changes to facilitate coding
alter table medic_medical_fields rename column medic_id to old_id;
alter table medic_medical_fields add column medic_id int;
alter table clinic_available_studies rename column clinic_id to old_id;
alter table clinic_available_studies add column clinic_id int;
alter table medical_orders rename column medic_id to old_medic_id;
alter table medical_orders add column medic_id int;
alter table medical_orders rename column clinic_id to old_clinic_id;
alter table medical_orders add column clinic_id int;

--Moving data
--Populating users table to get user_ids which we will later use to replace all the other ids
insert into users(email,password,role)
    (select email,email as password,1 from patients)
on conflict (email) do nothing;

insert into users(email,password,role)
    (select email,email as password,1 from medics)
on conflict (email) do nothing;

insert into users(email,password,role)
    (select email,email as password,1 from clinics)
on conflict (email) do nothing;

--Changing previous specific ids, with shared user id
update patients set (user_id) =
                        (select id from users
                         where users.email = patients.email);

update medics set (user_id) =
                      (select id from users
                       where users.email = medics.email);

update clinics set (user_id) =
                       (select id from users
                        where users.email = clinics.email);

--Using old ids and new ids to update the relation tables
update medic_medical_fields set (medic_id) =
                        (select user_id from medics
                            where medics.id = old_id);

update clinic_available_studies set (clinic_id) =
                        (select user_id from clinics
                            where clinics.id = old_id);

update medical_orders set (medic_id) =
                        (select user_id from medics
                            where medics.id = old_medic_id);

update medical_orders set (clinic_id) =
                        (select user_id from clinics
                            where clinics.id = old_clinic_id);

update medical_orders set (patient_name, patient_email) =
                        (select patients.name, patients.email from patients
                            where patients.id = patient_id);

--Adding default values for new fields so we can ask them to not be null later
update clinics set (verified) = (false);    --Has issues, we can skip tho

update medics set (identification_type, identification, verified) = ('image/png','\\000',false);

--Removing old columns
alter table clinics drop column id;
alter table medics drop column id;
alter table patients drop column id;
alter table patients drop column email;
alter table medical_orders drop column patient_id;
alter table medic_medical_fields drop column old_id;
alter table clinic_available_studies drop column old_id;
alter table medical_orders drop column old_medic_id;
alter table medical_orders drop column old_clinic_id;

--Reconstructing constraints, since we did this programatically, they should not fail
--Clinic constraints
alter table clinics alter column user_id set not null;
alter table clinics add constraint clinics_user_id_key unique(user_id);
alter table clinics alter column verified set not null;
alter table clinics add constraint clinics_user_id_fkey foreign key(user_id) references users;

--Clinic_available_studies constraints
alter table clinic_available_studies alter column clinic_id set not null;
alter table clinic_available_studies add constraint clinic_available_studies_clinic_id_fkey
    foreign key(clinic_id) references clinics(user_id);
alter table clinic_available_studies add primary key(clinic_id,study_id);

--Medics constraints
alter table medics alter column user_id set not null;
alter table medics add constraint medics_user_id_key unique(user_id);
alter table medics alter column verified set not null;
alter table medics add constraint medics_user_id_fkey foreign key(user_id) references users;
alter table medics alter column identification_type set not null;
alter table medics alter column identification set not null;

--Medic_medical_fields constraints
alter table medic_medical_fields alter column medic_id set not null;
alter table medic_medical_fields add constraint medic_medical_fields_medic_id_fkey
    foreign key(medic_id) references medics(user_id);
alter table medic_medical_fields add primary key(medic_id,field_id);

--Patients constraints
alter table patients alter column user_id set not null;
alter table patients add constraint patients_user_id_key unique(user_id);
alter table patients add constraint patients_user_id_fkey foreign key(user_id) references users;

--Medical_orders constraints
alter table medical_orders alter column patient_name set not null;
alter table medical_orders alter column patient_email set not null;
alter table medical_orders alter column medic_id set not null;
alter table medical_orders alter column clinic_id set not null;
alter table medical_orders add constraint medical_orders_medic_id_fkey
    foreign key(medic_id) references medics(user_id);
alter table medical_orders add constraint medical_orders_clinic_id_fkey
    foreign key(clinic_id) references clinics(user_id);

--Commit transaction
commit

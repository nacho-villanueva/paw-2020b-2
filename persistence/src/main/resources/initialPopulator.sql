delete from clinic_available_studies;
delete from clinics;
delete from medical_studies;
delete from medic_medical_fields;
delete from medics;
delete from medical_fields;

insert into medics(name,email,telephone,licence_number) values ('Gus Jhonson','gjhonson@medtransfer.com','+5491139588681','A28-674998236G');
insert into medics(name,email,telephone,licence_number) values ('Henry Hakase','etawara@itba.edu.ar','+5491180247816','A47-79862417H');
insert into medics(name,email,telephone,licence_number) values ('Zero Ullr', 'crojas@itba.edu.ar','+5491124000761','Z69-04200000U');
insert into medics(name,email,telephone,licence_number) values ('Nacho Inu', 'ivillanueva@itba.edu.ar','+136799123','B4123-51258273R');

insert into medical_fields(name) values ('Neurology');
insert into medical_fields(name) values ('Cardiology');
insert into medical_fields(name) values ('Endocrinology');
insert into medical_fields(name) values ('Oncology');
insert into medical_fields(name) values ('Pulmonology');
insert into medical_fields(name) values ('Ophthalmology');
insert into medical_fields(name) values ('Neurosurgery');

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'gjhonson@medtransfer.com'),
                                                               (select id from medical_fields where name = 'Neurology'));

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'gjhonson@medtransfer.com'),
                                                               (select id from medical_fields where name = 'Neurosurgery'));

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'etawara@itba.edu.ar'),
                                                               (select id from medical_fields where name = 'Oncology'));

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'etawara@itba.edu.ar'),
                                                               (select id from medical_fields where name = 'Endocrinology'));

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'crojas@itba.edu.ar'),
                                                               (select id from medical_fields where name = 'Pulmonology'));

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'crojas@itba.edu.ar'),
                                                               (select id from medical_fields where name = 'Ophthalmology'));

insert into medic_medical_fields(medic_id,field_id) values (
                                                               (select id from medics where email = 'ivillanueva@itba.edu.ar'),
                                                               (select id from medical_fields where name = 'Cardiology'));

insert into clinics(name,email,telephone) values ('Insituto Inclan','inclan@medtransfer.com','+541234556789'),
                                                 ('Universidad de Medicina','unimed@medtransfer.com','+54555555558'),
                                                 ('Consultorio Ricarte','mricarte@itba.edu.ar','+541143246577');

insert into medical_studies(name) values ('x-ray'),
                                         ('vaccine'),
                                         ('electrocardiography'),
                                         ('allergy test'),
                                         ('rehab'),
                                         ('surgery');

insert into clinic_available_studies(clinic_id, study_id) values
((select id from clinics where email='inclan@medtransfer.com'),(select id from medical_studies where name='x-ray')),
((select id from clinics where email='inclan@medtransfer.com'),(select id from medical_studies where name='vaccine')),
((select id from clinics where email='inclan@medtransfer.com'),(select id from medical_studies where name='surgery')),
((select id from clinics where email='unimed@medtransfer.com'),(select id from medical_studies where name='x-ray')),
((select id from clinics where email='unimed@medtransfer.com'),(select id from medical_studies where name='electrocardiography')),
((select id from clinics where email='mricarte@itba.edu.ar'),(select id from medical_studies where name='x-ray')),
((select id from clinics where email='mricarte@itba.edu.ar'),(select id from medical_studies where name='vaccine')),
((select id from clinics where email='mricarte@itba.edu.ar'),(select id from medical_studies where name='allergy test')),
((select id from clinics where email='mricarte@itba.edu.ar'),(select id from medical_studies where name='rehab')),
((select id from clinics where email='mricarte@itba.edu.ar'),(select id from medical_studies where name='surgery'));
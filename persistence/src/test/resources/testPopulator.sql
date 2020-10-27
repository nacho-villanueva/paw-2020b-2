insert into users(id,email,password,role) values (1,'zero@zero.com','passZero',1),
                                                (2,'one@one.com','passOne',2),
                                                (3,'two@two.com','passTwo',3),
                                                (4,'three@three.com','passThree',1),
                                                (5,'four@four.com','passFour',2),
                                                (6,'five@five.com','passFive',3),
                                                (7,'six@six.com','passSix',4),
                                                (8,'seven@seven.com','passSeven',4);


insert into medical_studies(id,name) values (1,'X-ray'),
                                         (2,'Vaccine'),
                                         (3,'Electrocardiography'),
                                         (4,'Allergy test'),
                                         (5,'Rehab'),
                                         (6,'Surgery');

insert into medical_fields(id,name) values (1,'Neurology'),
                                        (2,'Cardiology'), (3,'Endocrinology'), (4,'Pulmonology'), (5,'Ophthalmology');

insert into clinics(user_id,name,verified) values (3,'Clinic two',true),(6,'Clinic five',true);

insert into medics(user_id,name,identification_type,identification,licence_number,verified) values
                    (2,'Medic one','image/png','\000','1234567',true),
                    (5,'medic four','image/png','\000','1234567',false);

insert into medic_medical_fields(medic_id,field_id) values (2,1),(2,2),(2,4),(5,3),(5,4),(5,5);

insert into clinic_available_studies(clinic_id,study_id) values (3,1),(3,2),(3,4),(6,4),(6,5),(6,6);

insert into patients(user_id,name) values (1,'Patient Zero'),(4,'Patient Three');

insert into medical_orders(id, date, description, identification, identification_type, patient_email, patient_name, medic_plan_number, medic_plan, clinic_id, medic_id, study_id)
values (1, '2020-10-05', 'Description 1', '\000', 'image/png', 'patient1@patient.com', 'Patient one', 'insurance123', 'insurance plan one', 3, 2, 1),
        (2, '2020-10-05', 'Description 2', '\000', 'image/png', 'patient2@patient.com', 'Patient two', 'insurance123', 'insurance plan two', 3, 2, 1);


insert into results(id, order_id, result_data_type, result_data, identification_type, identification, date, responsible_name, responsible_licence_number)
values  (1, 1, 'image/png','\000', 'image/png', '\000', '2020-10-05', 'responsible_one', 'licence1234'),
        (2, 1, 'image/png','\000', 'image/png', '\000', '2020-10-05', 'responsible_two', 'licence1234');
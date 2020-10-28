insert into users(id,email,password,role) values ((next value for users_id_seq),'zero@zero.com','passZero',1),
                                                ((next value for users_id_seq),'one@one.com','passOne',2),
                                                ((next value for users_id_seq),'two@two.com','passTwo',3),
                                                ((next value for users_id_seq),'three@three.com','passThree',1),
                                                ((next value for users_id_seq),'four@four.com','passFour',2),
                                                ((next value for users_id_seq),'five@five.com','passFive',3),
                                                ((next value for users_id_seq),'six@six.com','passSix',4),
                                                ((next value for users_id_seq),'seven@seven.com','passSeven',4),
                                                ((next value for users_id_seq),'eight@eight.com','passEight',3),
                                                 ((next value for users_id_seq),'nine@nine.com','passNine',3),
                                                 ((next value for users_id_seq),'ten@ten.com','passTen',3),
                                                 ((next value for users_id_seq),'eleven@eleven.com','passEleven',3),
                                                 ((next value for users_id_seq),'twelve@twelve.com','passTwelve',3),
                                                 ((next value for users_id_seq),'thirteen@thirteen.com','passThirteen',3),
                                                 ((next value for users_id_seq),'fourteen@fourteen.com','passFourteen',3),
                                                 ((next value for users_id_seq),'fifteen@fifteen.com','passFifteen',3),
                                                 ((next value for users_id_seq),'sixteen@sixteen.com','passSixteen',3),
                                                 ((next value for users_id_seq),'seventeen@seventeen.com','passSeventeen',3),
                                                 ((next value for users_id_seq),'eighteen@eighteen.com','passEighteen',3),
                                                 ((next value for users_id_seq),'nineteen@nineteen.com','passNineteen',3),
                                                 ((next value for users_id_seq),'twenty@twenty.com','passTwenty',3);


insert into medical_studies(id,name) values ((next value for medical_studies_id_seq),'X-ray'),
                                         ((next value for medical_studies_id_seq),'Vaccine'),
                                         ((next value for medical_studies_id_seq),'Electrocardiography'),
                                         ((next value for medical_studies_id_seq),'Allergy test'),
                                         ((next value for medical_studies_id_seq),'Rehab'),
                                         ((next value for medical_studies_id_seq),'Surgery');

insert into medical_fields(id,name) values ((next value for medical_fields_id_seq),'Neurology'),
                                        ((next value for medical_fields_id_seq),'Cardiology'), ((next value for medical_fields_id_seq),'Endocrinology'), ((next value for medical_fields_id_seq),'Pulmonology'), ((next value for medical_fields_id_seq),'Ophthalmology');

insert into clinics(user_id,name,verified) values (3,'Clinic two',true),
                                                  (6,'Clinic five',true),
                                                  (9,'Clinic Eight',true),
                                                  (10,'Clinic Nine',true),
                                                  (11,'Clinic Ten',true),
                                                  (12,'Clinic Eleven',false),
                                                  (13,'Clinic Twelve',true),
                                                  (14,'Clinic Thirteen',true),
                                                  (15,'Clinic Fourteen',false),
                                                  (16,'Clinic Fifteen',true),
                                                  (17,'Clinic Sixteen',true),
                                                  (18,'Clinic Seventeen',false),
                                                  (19,'Clinic Eighteen',true),
                                                  (20,'Clinic Nineteen',true),
                                                  (21,'Clinic Twenty',true);

insert into clinic_hours(clinic_id,day_of_week,close_time,open_time) values
                                                                        (3,0,'08:00:00','22:00:00'),        --Valid
                                                                        (3,1,'09:00:00','15:00:00'),
                                                                        (3,2,'10:00:00','20:00:00'),
                                                                        (3,3,'11:00:00','19:00:00'),

                                                                        (6,0,'08:00:00','15:00:00'),        --Valid
                                                                        (6,1,'09:00:00','21:00:00'),

                                                                        (9,1,'09:00:00','15:00:00'),        --Valid
                                                                        (9,2,'10:00:00','20:00:00'),
                                                                        (9,3,'11:00:00','19:00:00'),

                                                                        (10,2,'10:00:00','23:00:00'),       --Valid
                                                                        (10,5,'08:00:00','19:00:00'),
                                                                        (10,6,'11:00:00','15:00:00'),

                                                                        (11,1,'10:00:00','19:00:00'),       --Valid
                                                                        (11,3,'11:00:00','20:00:00'),
                                                                        (11,0,'10:00:00','22:00:00'),

                                                                        (12,4,'08:00:00','22:00:00'),
                                                                        (12,2,'11:00:00','15:00:00'),
                                                                        (12,1,'10:00:00','22:00:00'),

                                                                        (13,5,'10:00:00','20:00:00'),       --Valid
                                                                        (13,2,'11:00:00','19:00:00'),
                                                                        (13,4,'11:00:00','22:00:00'),

                                                                        (14,1,'11:00:00','15:00:00'),       --Valid
                                                                        (14,0,'14:00:00','22:00:00'),
                                                                        (14,4,'10:00:00','15:00:00'),

                                                                        (15,4,'11:00:00','20:00:00'),
                                                                        (15,3,'10:00:00','20:00:00'),
                                                                        (15,1,'14:00:00','20:00:00'),

                                                                        (16,6,'08:00:00','15:00:00'),       --Valid
                                                                        (16,1,'10:00:00','23:00:00'),
                                                                        (16,3,'11:00:00','19:00:00'),

                                                                        (17,0,'14:00:00','20:00:00'),       --Valid
                                                                        (17,6,'11:00:00','23:00:00'),
                                                                        (17,5,'11:00:00','15:00:00'),

                                                                        (18,5,'10:00:00','22:00:00'),
                                                                        (18,6,'08:00:00','23:00:00'),
                                                                        (18,0,'11:00:00','22:00:00'),

                                                                        (19,1,'08:00:00','19:00:00'),       --Valid
                                                                        (19,3,'08:00:00','20:00:00'),
                                                                        (19,5,'11:00:00','23:00:00'),

                                                                        (20,2,'08:00:00','15:00:00'),       --Valid
                                                                        (20,5,'10:00:00','19:00:00'),
                                                                        (20,0,'11:00:00','23:00:00'),

                                                                        (21,4,'08:00:00','15:00:00'),       --Valid
                                                                        (21,3,'14:00:00','20:00:00'),
                                                                        (21,6,'10:00:00','19:00:00');

insert into clinic_accepted_plans(clinic_id,medic_plan) values (3,'Osde'),(6,'Osde'),(9,'OSDE'),(10,'Swiss'),
                                                               (11,'Osde 310'),(12,'Galeno'),(13,'Omit'),(14,'Galeno'),
                                                               (15,'Pami'),(16,'Osde 210'),(17,'Swiss'),(18,'Osde 210'),
                                                               (19,'Galeno'),(20,'Swiss'),(21,'Pami'),(11,'Pami'),(15,'Swiss'),
                                                               (17,'Galeno'),(19,'OSDE'),(20,'OSDE'),(11,'Omit'),(10,'Omit'),
                                                               (6,'Omit'),(15,'Omit');

insert into medics(user_id,name,identification_type,identification,licence_number,verified) values
                    (2,'Medic one','image/png','\000','1234567',true),
                    (5,'medic four','image/png','\000','1234567',false);

insert into medic_medical_fields(medic_id,field_id) values (2,1),(2,2),(2,4),(5,3),(5,4),(5,5);

insert into clinic_available_studies(clinic_id,study_id) values (3,1),(3,2),(3,4),(6,4),(6,5),(6,6);

insert into patients(user_id,name) values (1,'Patient Zero'),(4,'Patient Three');

insert into medical_orders(id, date, description, identification, identification_type, patient_email, patient_name, medic_plan_number, medic_plan, clinic_id, medic_id, study_id)
values ((next value for medical_orders_id_seq), '2020-10-05', 'Description 1', '\000', 'image/png', 'patient1@patient.com', 'Patient one', 'insurance123', 'insurance plan one', 3, 2, 1),
        ((next value for medical_orders_id_seq), '2020-10-05', 'Description 2', '\000', 'image/png', 'patient2@patient.com', 'Patient two', 'insurance123', 'insurance plan two', 3, 2, 1);


insert into results(id, order_id, result_data_type, result_data, identification_type, identification, date, responsible_name, responsible_licence_number)
values  ((next value for results_id_seq), 1, 'image/png','\000', 'image/png', '\000', '2020-10-05', 'responsible_one', 'licence1234'),
        ((next value for results_id_seq), 1, 'image/png','\000', 'image/png', '\000', '2020-10-05', 'responsible_two', 'licence1234');
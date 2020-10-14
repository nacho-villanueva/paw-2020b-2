DROP TABLE IF EXISTS results CASCADE;
DROP TABLE IF EXISTS medical_orders CASCADE;
DROP TABLE IF EXISTS clinic_accepted_plans CASCADE;
DROP TABLE IF EXISTS clinic_available_studies CASCADE;
DROP TABLE IF EXISTS clinic_hours CASCADE;
DROP TABLE IF EXISTS clinics CASCADE;
DROP TABLE IF EXISTS medic_medical_fields CASCADE;
DROP TABLE IF EXISTS medical_fields CASCADE;
DROP TABLE IF EXISTS medical_studies CASCADE;
DROP TABLE IF EXISTS medics CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS users CASCADE;


insert into users(email,password,role,locale) values ('gjhonson@medtransfer.com','gjhonsonPass',4,'es-AR'),
                                              ('etawara@itba.edu.ar','etawaraPass',4,'es-AR'),
                                              ('crojas@itba.edu.ar','crojasPass',4,'en-US'),
                                              ('ivillanueva@itba.edu.ar','ivillanuevaPass',4,'es-AR'),
                                              ('inclan@medtransfer.com','inclanPass',4,'es-AR'),
                                              ('unimed@medtransfer.com','unimedPass',4,'es-AR'),
                                              ('mricarte@itba.edu.ar','mricartePass',4,'es-AR');

insert into medical_fields(name) values ('Neurology'),
            ('Cardiology'), ('Endocrinology'), ('Pulmonology'), ('Ophthalmology'),
            ('Neurosurgery'), ('Allergy and immunology'), ('Adolescent medicine'), ('Anesthesiology'),
            ('Aerospace medicine'),('Bariatrics'),('Cardiothoracic surgery'),('Child and adolescent psychiatry'),('Clinical neurophysiology'),
            ('Colorectal surgery'),('Dermatology'),('Developmental pediatrics'),('Emergency medicine'),('Family Medicine'),('Forensic pathology'),
            ('Forensic psychiatry'),('Gastroenterology'),('General surgery'),('General surgical oncology'),('Geriatrics'),('Geriatric psychiatry'),
            ('Gynecologic oncology'),('Hematology'),('Hematologic pathology'),('Infectious disease'),('Internal medicine'),('Interventional radiology'),
            ('Intensive care medicine'),('Maternal-fetal medicine'),('Medical biochemistry'),('Medical genetics'),('Medical oncology'),
            ('Neonatology'),('Nephrology'),('Neuropathology'),('Nuclear medicine'),('Obstetrics and gynecology'),('Occupational medicine'),
            ('Orthopedic surgery'),('Oral and maxillofacial surgery'),('Otorhinolaryngology'),('Palliative care'),('Pathology'),('Pediatrics'),
            ('Physical medicine and rehabilitation'),('Plastic, reconstructive and aesthetic surgery'),('Psychiatry'),('Radiation oncology'),('Radiology'),
            ('Reproductive endocrinology and infertility'),('Respiratory medicine'),('Rheumatology'),('Sports medicine'),('Neuroradiology'),('Urology');

insert into medical_studies(name) values ('X-ray'),
                                         ('Vaccine'),
                                         ('Electrocardiography'),
                                         ('Allergy test'),
                                         ('Rehab'),
                                         ('Surgery');
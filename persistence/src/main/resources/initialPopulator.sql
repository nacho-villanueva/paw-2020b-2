delete from results;
delete from medical_orders;
delete from clinic_available_studies;
delete from medic_medical_fields;
delete from clinic_hours;
delete from clinic_accepted_plans;
delete from medics;
delete from medical_fields;
delete from clinics;
delete from medical_studies;
delete from patients;
delete from users;

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
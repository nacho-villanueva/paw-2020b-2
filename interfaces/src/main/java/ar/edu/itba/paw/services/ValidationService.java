package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

import java.sql.Date;

public interface ValidationService {
    boolean isValid(User user);

    boolean isValid(Medic medic);

    boolean isValid(Order order);

    boolean isValid(Patient patient);

    boolean isValid(Result result);

    boolean isValid(MedicalField medicalField);

    boolean isValid(StudyType studyType);

    boolean isValid(Clinic clinic);

    boolean isValidEmail(String email);

    boolean isValidIdentification_Type(String identification_type);

    boolean isValidTelephone(String telephone);

    boolean isValidRole(int role);

    boolean isValidName(String name);

    boolean isValidPassword(String password);

    boolean isValidLicenceNumber(String licence_number);

    boolean isValidMedicPlan(String medic_plan);

    boolean isValidMedicPlanNumber(String medic_plan_number);

    boolean isValidResultDataType(String result_data_type);

    boolean isValidDate(Date date);
}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;

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

    boolean isValidIdentificationType(String identificationType);

    boolean isValidTelephone(String telephone);

    boolean isValidRole(int role);

    boolean isValidName(String name);

    boolean isValidPassword(String password);

    boolean isValidLicenceNumber(String licenceNumber);

    boolean isValidMedicPlan(String medicPlan);

    boolean isValidMedicPlanNumber(String medicPlanNumber);

    boolean isValidResultDataType(String resultDataType);

    boolean isValidDate(LocalDate date);
}

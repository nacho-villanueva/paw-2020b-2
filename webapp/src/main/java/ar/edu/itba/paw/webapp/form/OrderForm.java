package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.constraintGroups.ExistingPatientGroup;
import ar.edu.itba.paw.webapp.form.constraintGroups.OrderGroup;
import ar.edu.itba.paw.webapp.form.constraintGroups.OrderWithoutClinicGroup;
import ar.edu.itba.paw.webapp.form.validators.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class OrderForm {

    @MedicIdIsValid(groups = {OrderWithoutClinicGroup.class,OrderGroup.class})
    private Integer medicId;

    @ValidStudyTypeId(groups = {OrderWithoutClinicGroup.class,OrderGroup.class})
    private Integer studyId;

    private String description;

    private String patientInsurancePlan;
    private String patientInsuranceNumber;

    @NotBlank(groups = {ExistingPatientGroup.class,OrderWithoutClinicGroup.class,OrderGroup.class})
    @Email(groups = {ExistingPatientGroup.class,OrderWithoutClinicGroup.class,OrderGroup.class})
    @PatientEmailHasValidPatient(groups = {ExistingPatientGroup.class})
    private String patientEmail;

    @NotBlank(groups = {OrderWithoutClinicGroup.class,OrderGroup.class})
    private String patientName;

    @ClinicIdIsValid(groups = OrderGroup.class)
    private Integer clinicId;

    public OrderForm(){ }

    public OrderForm(Integer medicId, Integer studyId, String description, String patientInsurancePlan, String patientInsuranceNumber, String patientEmail, String patientName, Integer clinicId) {
        this.medicId = medicId;
        this.studyId = studyId;
        this.description = description;
        this.patientInsurancePlan = patientInsurancePlan;
        this.patientInsuranceNumber = patientInsuranceNumber;
        this.patientEmail = patientEmail;
        this.patientName = patientName;
        this.clinicId = clinicId;
    }

    public void copyOrderForm(OrderForm orderForm) {
        this.medicId = orderForm.medicId;
        this.studyId = orderForm.studyId;
        this.description = orderForm.description;
        this.patientInsurancePlan = orderForm.patientInsurancePlan;
        this.patientInsuranceNumber = orderForm.patientInsuranceNumber;
        this.patientEmail = orderForm.patientEmail;
        this.patientName = orderForm.patientName;
        this.clinicId = orderForm.clinicId;
    }

    public Integer getMedicId() {
        return medicId;
    }

    public void setMedicId(Integer medicId) {
        this.medicId = medicId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatientInsurancePlan() {
        return patientInsurancePlan;
    }

    public void setPatientInsurancePlan(String patientInsurancePlan) {
        this.patientInsurancePlan = patientInsurancePlan;
    }

    public String getPatientInsuranceNumber() {
        return patientInsuranceNumber;
    }

    public void setPatientInsuranceNumber(String patientInsuranceNumber) {
        this.patientInsuranceNumber = patientInsuranceNumber;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }
}

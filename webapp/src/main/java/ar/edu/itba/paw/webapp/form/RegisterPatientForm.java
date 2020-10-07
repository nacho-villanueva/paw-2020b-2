package ar.edu.itba.paw.webapp.form;

public class RegisterPatientForm {

    private String first_name, last_name;
    private String medical_insurance_plan;
    private String medical_insurance_number;


    public RegisterPatientForm() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMedical_insurance_plan() {
        return medical_insurance_plan;
    }

    public void setMedical_insurance_plan(String medical_insurance_plan) {
        this.medical_insurance_plan = medical_insurance_plan;
    }

    public String getMedical_insurance_number() {
        return medical_insurance_number;
    }

    public void setMedical_insurance_number(String medical_insurance_number) {
        this.medical_insurance_number = medical_insurance_number;
    }

    public String getFullname(){
        return getFirst_name() + " " + getLast_name();
    }
}

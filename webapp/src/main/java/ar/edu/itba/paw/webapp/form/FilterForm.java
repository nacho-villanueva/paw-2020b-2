package ar.edu.itba.paw.webapp.form;

public class FilterForm {
    private String patient_name, date;
    private Integer study_id, clinic_id, medic_id;

    public FilterForm() {
    }

    public FilterForm(Integer study_id, Integer clinic_id, Integer medic_id, String patient_name, String date){
        this.date = date;
        this.patient_name = patient_name;
        this.medic_id = medic_id;
        this.clinic_id = clinic_id;
        this.study_id = study_id;
    }

    public String getPatient_name() { return patient_name; }
    public void setPatient_name(String s){
        patient_name = s;
    }

    public String getDate(){ return date; }
    public void setDate(String s){
        date = s;
    }

    public Integer getStudy_id() {
        return study_id;
    }

    public void setStudy_id(Integer study_id) {
        this.study_id = study_id;
    }

    public Integer getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(Integer clinic_id) {
        this.clinic_id = clinic_id;
    }

    public Integer getMedic_id() {
        return medic_id;
    }

    public void setMedic_id(Integer medic_id) {
        this.medic_id = medic_id;
    }
}

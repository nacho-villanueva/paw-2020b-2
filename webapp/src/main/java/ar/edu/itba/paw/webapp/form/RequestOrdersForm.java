package ar.edu.itba.paw.webapp.form;

public class RequestOrdersForm {
    String patientEmail;
    Integer studyId;

    public RequestOrdersForm(){

    }

    public void setPatientEmail(String s){
        this.patientEmail = s;
    }
    public String getPatientEmail(){
        return this.patientEmail;
    }

    public void setStudyId(Integer id){
        this.studyId = id;
    }
    public Integer getStudyId(){
        return this.studyId;
    }
}

package ar.edu.itba.paw.model;

public class ResultForm {
    private String responsible_name;
    private String responsible_licence_number;

    public ResultForm(String responsible_name, String responsible_licence_number){
        this.responsible_name = responsible_name;
        this.responsible_licence_number = responsible_licence_number;
    }

    public ResultForm(){}

    public void setResponsible_name(String responsible_name){
        this.responsible_name = responsible_name;
    }
    public String getResponsible_name(){
        return responsible_name;
    }

    public void setResponsible_licence_number(String responsible_licence_number){
        this.responsible_licence_number = responsible_licence_number;
    }
    public String getResponsible_licence_number(){
        return responsible_licence_number;
    }
}

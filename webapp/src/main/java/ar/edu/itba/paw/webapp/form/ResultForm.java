package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.FileNotEmpty;
import ar.edu.itba.paw.webapp.form.validators.MultifileNotEmpty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotNull;

public class ResultForm {
    @NotBlank
    private String responsible_name;
    @NotBlank
    private String responsible_licenceNumber;

    @MultifileNotEmpty
    MultipartFile[] files;

    @FileNotEmpty
    MultipartFile sign;

    public ResultForm(String responsible_name, String responsible_licenceNumber){
        this.responsible_name = responsible_name;
        this.responsible_licenceNumber = responsible_licenceNumber;
    }

    public ResultForm(){}

    public void setResponsible_name(String responsible_name){
        this.responsible_name = responsible_name;
    }
    public String getResponsible_name(){
        return responsible_name;
    }

    public void setResponsible_licenceNumber(String responsible_licenceNumber){
        this.responsible_licenceNumber = responsible_licenceNumber;
    }
    public String getResponsible_licenceNumber(){
        return responsible_licenceNumber;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public MultipartFile getSign() {
        return sign;
    }

    public void setSign(MultipartFile sign) {
        this.sign = sign;
    }
}

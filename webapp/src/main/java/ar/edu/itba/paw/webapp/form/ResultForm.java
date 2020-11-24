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
    private String responsibleName;
    @NotBlank
    private String responsibleLicenceNumber;

    @MultifileNotEmpty
    MultipartFile[] files;

    @FileNotEmpty
    MultipartFile sign;

    public ResultForm(String responsibleName, String responsibleLicenceNumber){
        this.responsibleName = responsibleName;
        this.responsibleLicenceNumber = responsibleLicenceNumber;
    }

    public ResultForm(){}

    public void setResponsibleName(String responsibleName){
        this.responsibleName = responsibleName;
    }
    public String getResponsibleName(){
        return responsibleName;
    }

    public void setResponsibleLicenceNumber(String responsibleLicenceNumber){
        this.responsibleLicenceNumber = responsibleLicenceNumber;
    }
    public String getResponsibleLicenceNumber(){
        return responsibleLicenceNumber;
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

package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.MedicalField;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class MedicPostDto {

    // Constants
    public static final String CONTENT_TYPE = "application/vnd.medic.v1";

    // Variables
    @NotBlank(message = "Please provide a name.")
    private String name;

    @NotBlank(message = "Please provide a phone number.")
    @Pattern(regexp = "\\+?[0-9]*",message = "Please provide a valid phone number")
    private String telephone;

    @Valid
    @NotNull(message = "Please provide an identification image.")
    private ImageDto identification;

    @NotBlank(message = "Please provide a license number.")
    //TODO: see if we use a pattern for this one, we dont know what a licence looks like @Pattern(regexp = "[0-9a-zA-Z]*",message = "MedicPostAndPutDto.licenceNumber.Pattern")
    private String licenceNumber;

    @Valid
    @NotEmpty(message="Please provide at least one medical field.")
    private Collection<MedicalFieldDto> medicalFields;

    // Constructors
    public MedicPostDto() {
        // Use factory method
    }

    // Getters&Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ImageDto getIdentification() {
        return identification;
    }

    public void setIdentification(ImageDto identification) {
        this.identification = identification;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public Collection<MedicalFieldDto> getMedicalFields() {
        return medicalFields;
    }

    public void setMedicalFields(Collection<MedicalFieldDto> medicalFields) {
        this.medicalFields = medicalFields;
    }


    // Equals&HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicPostDto that = (MedicPostDto) o;
        return Objects.equals(name, that.name) && Objects.equals(telephone, that.telephone) && Objects.equals(identification, that.identification) && Objects.equals(licenceNumber, that.licenceNumber) && Objects.equals(medicalFields, that.medicalFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, telephone, identification, licenceNumber, medicalFields);
    }

    // etc.
    public Collection<MedicalField> getMedicalFieldCollection(){
        Collection<MedicalField> medicalFields = new ArrayList<>();
        if(this.medicalFields==null || this.medicalFields.isEmpty())
            return medicalFields;

        return this.getMedicalFields().stream().map(mf -> new MedicalField(mf.getName())).collect(Collectors.toList());
    }
}

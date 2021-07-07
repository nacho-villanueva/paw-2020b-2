package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.annotations.ClinicId;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class OrderChangeClinicDto {

    @NotNull(message = "OrderPostAndPutDto.clinicId.NotNull")
    @ClinicId(message = "OrderPostAndPutDto.clinicId.ClinicIdIsValid")
    private Integer clinicId;

    public OrderChangeClinicDto() {

    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderChangeClinicDto that = (OrderChangeClinicDto) o;
        return Objects.equals(clinicId, that.clinicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clinicId);
    }
}

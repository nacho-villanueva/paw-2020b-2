package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validators.ValidMedicEmail;
import org.hibernate.validator.constraints.Email;

public class ShareOrderForm {
    @Email
    @ValidMedicEmail
    String medicEmail;

    public String getMedicEmail() {
        return medicEmail;
    }

    public void setMedicEmail(String medicEmail) {
        this.medicEmail = medicEmail;
    }
}

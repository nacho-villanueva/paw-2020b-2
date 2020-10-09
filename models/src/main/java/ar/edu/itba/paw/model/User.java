package ar.edu.itba.paw.model;

public class User {

    public static final int ADMIN_ROLE_ID = 0;
    public static final int USER_ROLE_ID = 1;
    public static final int MEDIC_ROLE_ID = 2;
    public static final int CLINIC_ROLE_ID = 3;
    public static final int CLINIC_MEDIC_ROLE_ID = 4;
    private static final String DEFAULT_LOCALE = "en-US";

    private int id;
    private String email;
    private String password;
    private int role;
    private boolean isVerifyingMedic;
    private boolean isVerifyingClinic;
    private String locale;

    public User() {

    }

    public User(final int id, final String email, final String password, final int role, final String locale) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.locale = locale;
    }

    public User(final int id, final String email, final String password, final int role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.locale = DEFAULT_LOCALE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isMedic() {
        return this.role == MEDIC_ROLE_ID || this.role == CLINIC_MEDIC_ROLE_ID;
    }

    public boolean isClinic() {
        return this.role == CLINIC_ROLE_ID || this.role == CLINIC_MEDIC_ROLE_ID;
    }

    public boolean isVerifyingMedic() {
        return isVerifyingMedic;
    }

    public void setVerifyingMedic(boolean verifyingMedic) {
        isVerifyingMedic = verifyingMedic;
    }

    public boolean isVerifyingClinic() {
        return isVerifyingClinic;
    }

    public void setVerifyingClinic(boolean verifyingClinic) {
        isVerifyingClinic = verifyingClinic;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}

package ar.edu.itba.paw.model;

public class User {

    public static final int ADMIN_ROLE_ID = 0;
    public static final int PATIENT_ROLE_ID = 1;
    public static final int MEDIC_ROLE_ID = 2;
    public static final int CLINIC_ROLE_ID = 3;

    private int id;
    private String email;
    private String password;
    private int role;
    private boolean isRegistered;
    private boolean isVerifying;

    public User() {

    }

    public User(final int id, final String email, final String password, final int role, final boolean isRegistered, final boolean isVerifying) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isRegistered = isRegistered;
        this.isVerifying = isVerifying;
    }

    public User(final int id, final String email, final String password, final int role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isRegistered = true;
        this.isVerifying = false;
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
        return this.role == MEDIC_ROLE_ID;
    }

    public boolean isClinic() {
        return this.role == CLINIC_ROLE_ID;
    }

    public boolean isPatient() { return this.role == PATIENT_ROLE_ID; }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isVerifying() {
        return isVerifying;
    }

    public void setVerifying(boolean verifying) {
        isVerifying = verifying;
    }
}

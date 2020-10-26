package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    public static final int ADMIN_ROLE_ID = 0;
    public static final int PATIENT_ROLE_ID = 1;
    public static final int MEDIC_ROLE_ID = 2;
    public static final int CLINIC_ROLE_ID = 3;
    public static final int UNDEFINED_ROLE_ID = 4;

    private static final String DEFAULT_LOCALE = "en-US";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int role;

    @Transient
    private boolean isRegistered;

    @Transient
    private boolean isVerifying;

    @Column(length = 10)
    private String locale;

    protected User() {
        //Just for hibernate
    }

    public User(final String email, final String password, final int role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.locale = DEFAULT_LOCALE;
        this.isRegistered = true;
        this.isVerifying = false;
    }

    public User(final int id, final String email, final String password, final int role) {
        this(email,password,role);
        this.id = id;
    }

    public User(final String email, final String password, final int role, final String locale) {
        this(email,password,role);
        this.locale = locale;
    }

    public User(final int id, final String email, final String password, final int role, final String locale) {
        this(email,password,role,locale);
        this.id = id;
    }

    public User(final int id, final String email, final String password, final int role, final boolean isRegistered, final boolean isVerifying, final String locale) {
        this(id,email,password,role,locale);
        this.isRegistered = isRegistered;
        this.isVerifying = isVerifying;
    }

    public Integer getId() {
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

    public boolean isUndefined() { return this.role == UNDEFINED_ROLE_ID; }

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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}

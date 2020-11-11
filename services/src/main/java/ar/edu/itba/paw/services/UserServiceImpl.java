package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;


import ar.edu.itba.paw.persistence.ClinicDao;
import ar.edu.itba.paw.persistence.MedicDao;
import ar.edu.itba.paw.persistence.PatientDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Primary
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ValidationService vs;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private MedicService medicService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @Override
    public Optional<User> findById(int id) {
        Optional<User> maybeUser = userDao.findById(id);
        maybeUser.ifPresent(this::setUserFlags);
        return maybeUser;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> maybeUser = userDao.findByEmail(email);
        maybeUser.ifPresent(this::setUserFlags);
        return maybeUser;
    }

    @Override
    public User register(String email, String password, String locale) {
        final User user = userDao.register(email,encoder.encode(password), User.UNDEFINED_ROLE_ID, locale);
        //Since user has just been created, he is not registered as patient/clinic or medic
        user.setRegistered(false);

        //Create token and send email
        String token = UUID.randomUUID().toString();
        userDao.setVerificationToken(user,token);

        mailService.sendVerificationMessage(email,token);
        return user;
    }

    @Override
    public User updateRole(User user, int role) {
        if(vs.isValidRole(role)) {
            return userDao.updateRole(user,role);
        }
        return null;
    }

    @Override
    public User updateEmail(User user, String email) {
        //TODO: Verification of email
        return userDao.updateEmail(user,email);
    }

    @Override
    public User updatePassword(User user, String password) {
        return userDao.updatePassword(user,encoder.encode(password));
    }

    @Override
    public boolean checkPassword(int user_id, String password) {
        Optional<User> maybeUser = findById(user_id);
        return maybeUser.filter(user -> encoder.matches(password, user.getPassword())).isPresent();
    }

    @Override
    public User verify(User user) {
        User returnUser = userDao.verify(user);
        userDao.freeVerificationToken(user);
        return returnUser;
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        Optional<VerificationToken> maybeToken = userDao.getVerificationToken(token);
        maybeToken.ifPresent(verificationToken -> {
            this.setUserFlags(verificationToken.getUser());
        });
        return maybeToken;
    }

    private void setUserFlags(final User user) {
        switch (user.getRole()) {
            case User.CLINIC_ROLE_ID:
                Optional<Clinic> maybeClinic = clinicService.findByUserId(user.getId());
                user.setRegistered(maybeClinic.isPresent());
                maybeClinic.ifPresent(clinic -> user.setVerifying(!clinic.isVerified()));
                break;
            case User.MEDIC_ROLE_ID:
                Optional<Medic> maybeMedic = medicService.findByUserId(user.getId());
                user.setRegistered(maybeMedic.isPresent());
                maybeMedic.ifPresent(medic -> user.setVerifying(!medic.isVerified()));
                break;
            case User.PATIENT_ROLE_ID:
                Optional<Patient> maybePatient = patientService.findByUser_id(user.getId());
                user.setRegistered(maybePatient.isPresent());
                break;
            case User.UNDEFINED_ROLE_ID:
                user.setRegistered(false);
                break;
            case User.ADMIN_ROLE_ID:
            default:
        }
    }
}

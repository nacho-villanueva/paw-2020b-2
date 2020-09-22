package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final int[] VALID_ROLES = {0,1,2,3,4};
    private static final String[] VALID_IDENTIFICATION_TYPES = {"image/png","image/jpg","image/jpeg"};
    private static final String[] VALID_DATA_TYPES = {"image/png","image/jpg","image/jpeg","application/pdf","application/zip","application/rar", "text/plain","text/csv","text/html"};

    //Email regex are a topic in it on itself, i copied the regex from https://stackoverflow.com/questions/46155/how-to-validate-an-email-address-in-javascript 3rd Answer
    private static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    private final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final Pattern PHONE_PATTERN = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");

    @Autowired
    private UserService us;

    @Autowired
    private MedicService ms;

    @Autowired
    private OrderService os;

    @Autowired
    private PatientService ps;

    @Autowired
    private ResultService rs;

    @Autowired
    private StudyTypeService sts;

    @Autowired
    private MedicalFieldService mfs;

    @Autowired
    private ClinicService cs;

    @Override
    public boolean isValid(User user) {
        if(user == null) {
            return false;
        }
        Optional<User> maybeUser = us.findById(user.getId());

        if(!maybeUser.isPresent()) {
            return false;
        }
        User userDB = maybeUser.get();

        return user.getEmail().equals(userDB.getEmail());
    }

    @Override
    public boolean isValid(Medic medic) {
        if(medic == null) {
            return false;
        }
        Optional<Medic> maybeMedic = ms.findByUserId(medic.getUser_id());

        if(!maybeMedic.isPresent()) {
            return false;
        }

        Medic medicDB = maybeMedic.get();

        return medic.getEmail().equals(medicDB.getEmail());
    }

    @Override
    public boolean isValid(Order order) {
        if(order == null) {
            return false;
        }

        Optional<Order> maybeOrder = os.findById(order.getOrder_id());

        if(!maybeOrder.isPresent()) {
            return false;
        }

        Order orderDB = maybeOrder.get();

        return order.getDate().equals(orderDB.getDate());
    }

    @Override
    public boolean isValid(Patient patient) {
        if(patient == null) {
            return false;
        }

        Optional<Patient> maybePatient = ps.findByUser_id(patient.getUser_id());

        if(!maybePatient.isPresent()) {
            return false;
        }

        Patient patientDB = maybePatient.get();

        return patient.getEmail().equals(patientDB.getEmail());
    }

    @Override
    public boolean isValid(Result result) {
        if(result == null) {
            return false;
        }

        Optional<Result> maybeResult = rs.findById(result.getId());

        if(!maybeResult.isPresent()) {
            return false;
        }

        Result resultDB = maybeResult.get();

        return result.getDate().equals(resultDB.getDate());
    }

    @Override
    public boolean isValid(MedicalField medicalField) {
        if(medicalField == null) {
            return false;
        }

        Optional<MedicalField> maybeField = mfs.findById(medicalField.getId());

        if(!maybeField.isPresent()) {
            return false;
        }

        MedicalField medicalFieldDB = maybeField.get();

        return medicalField.getName().equals(medicalFieldDB.getName());
    }

    @Override
    public boolean isValid(StudyType studyType) {
        if(studyType == null) {
            return false;
        }

        Optional<StudyType> maybeStudy = sts.findById(studyType.getId());

        if(!maybeStudy.isPresent()) {
            return false;
        }

        StudyType studyTypeDB = maybeStudy.get();

        return studyType.getName().equals(studyTypeDB.getName());
    }

    @Override
    public boolean isValid(Clinic clinic) {
        if(clinic == null) {
            return false;
        }

        Optional<Clinic> maybeClinic = cs.findByUserId(clinic.getUser_id());

        if(!maybeClinic.isPresent()) {
            return false;
        }

        Clinic clinicDB = maybeClinic.get();

        return clinic.getEmail().equals(clinicDB.getEmail());
    }

    @Override
    public boolean isValidEmail(String email) {
        if(email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isValidIdentification_Type(String identification_type) {
        return isValidType(identification_type,VALID_IDENTIFICATION_TYPES);
    }

    @Override
    public boolean isValidTelephone(String telephone) {
        if(telephone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(telephone).matches();
    }

    @Override
    public boolean isValidRole(int role) {
        boolean isValid = false;
        for(int validRole : VALID_ROLES) {
            if (role == validRole) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    //TODO: check for other validations in project, see that they are the same
    @Override
    public boolean isValidName(String name) {
        if(name == null) {
            return false;
        }

        return !name.isEmpty();
    }

    //TODO: check of other validations in project, see that they are the same
    @Override
    public boolean isValidPassword(String password) {
        if(password == null) {
            return false;
        }

        return password.length() >= 8;
    }

    //TODO: Investigate, see if we can make a better validation
    @Override
    public boolean isValidLicenceNumber(String licence_number) {
        return isValidName(licence_number);
    }

    @Override
    public boolean isValidMedicPlan(String medic_plan) {
        return isValidName(medic_plan);
    }

    @Override
    public boolean isValidMedicPlanNumber(String medic_plan_number) {
        return isValidName(medic_plan_number);
    }

    @Override
    public boolean isValidResultDataType(String result_data_type) {
        return isValidType(result_data_type,VALID_DATA_TYPES);
    }

    @Override
    public boolean isValidDate(Date date) {
        return date != null;
    }

    private boolean isValidType(String type, String[] validType) {
        if(type == null) {
            return false;
        }

        boolean isValid = false;

        for (String valid: validType) {
            if(type.equals(valid)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
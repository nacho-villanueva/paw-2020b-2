package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    // Pagination-related constants
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_MAX_PAGE_SIZE = 10;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudyTypeService studyService;


    @Override
    public Optional<Order> findById(long id) {
        return orderDao.findById(id);
    }

    @Override
    public Order register(Medic medic, LocalDate date, Clinic clinic, String patientName, String patientEmail, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber) {
        Order order = orderDao.register(medic,date,clinic,patientName,patientEmail,studyType,description,identificationType,identification,medicPlan,medicPlanNumber);
        mailNotificationService.sendOrderMail(order);
        return order;
    }

    @Override
    public Collection<Order> getAllAsClinic(User user) {
        return getAllAsClinic(user,DEFAULT_PAGE);
    }

    @Override
    public Collection<Order> getAllAsClinic(User user, int page) {
        return getAllAsClinic(user, page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsClinic(User user, int page, int pageSize) {
        return orderDao.getAllAsClinic(user, page, pageSize);
    }

    @Override
    public long getAllAsClinicCount(User user) {
        return orderDao.getAllAsClinicCount(user);
    }

    @Override
    public long getAllAsClinicLastPage(User user) {
        return getAllAsClinicLastPage(user,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsClinicLastPage(User user, int pageSize) {
        return getLastPage(getAllAsClinicCount(user),pageSize);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user) {
        return getAllAsMedic(user,DEFAULT_PAGE);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user, int page) {
        return getAllAsMedic(user,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user, int page, int pageSize) {
        return getAllAsMedic(user,false,page,pageSize);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user, boolean includeShared, int page) {
        return getAllAsMedic(user,includeShared,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user, boolean includeShared, int page, int pageSize) {
        if(includeShared)
            return orderDao.getAllParticipatingAsMedic(user,page,pageSize);
        else
            return orderDao.getAllAsMedic(user,page,pageSize);
    }

    @Override
    public long getAllAsMedicCount(User user) {
        return getAllAsMedicCount(user,false);
    }

    @Override
    public long getAllAsMedicLastPage(User user) {
        return getAllAsMedicLastPage(user,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsMedicLastPage(User user, int pageSize) {
        return getAllAsMedicLastPage(user,false,pageSize);
    }

    @Override
    public long getAllAsMedicCount(User user, boolean includeShared) {
        if(includeShared)
            return orderDao.getAllParticipatingAsMedicCount(user);
        else
            return orderDao.getAllAsMedicCount(user);
    }

    @Override
    public long getAllAsMedicLastPage(User user, boolean includeShared) {
        return getAllAsMedicLastPage(user,includeShared,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsMedicLastPage(User user, boolean includeShared, int pageSize) {
        return getLastPage(getAllAsMedicCount(user,includeShared),pageSize);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user) {
        return getAllAsPatient(user,DEFAULT_PAGE);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user, int page) {
        return getAllAsPatient(user,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user, int page, int pageSize) {
        return orderDao.getAllAsPatient(user,page,pageSize);
    }

    @Override
    public long getAllAsPatientCount(User user) {
        return orderDao.getAllAsPatientCount(user);
    }

    @Override
    public long getAllAsPatientLastPage(User user) {
        return getAllAsPatientLastPage(user,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsPatientLastPage(User user, int pageSize) {
        return getLastPage(getAllAsPatientCount(user),pageSize);
    }

    @Override
    public Collection<Order> getAllAsPatientOfType(String email, StudyType type) {
        return getAllAsPatientOfType(email, type,DEFAULT_PAGE);
    }

    @Override
    public Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page) {
        return getAllAsPatientOfType(email, type,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page, int pageSize) {
        return orderDao.getAllAsPatientOfType(email, type,page,pageSize);
    }

    @Override
    public long getAllAsPatientOfTypeCount(String email, StudyType type) {
        return orderDao.getAllAsPatientOfTypeCount(email,type);
    }

    @Override
    public long getAllAsPatientOfTypeLastPage(String email, StudyType type) {
        return getAllAsPatientOfTypeLastPage(email,type,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsPatientOfTypeLastPage(String email, StudyType type, int pageSize) {
        return getLastPage(getAllAsPatientOfTypeCount(email,type),pageSize);
    }

    @Override
    public Collection<Order> getAllAsUser(User user){
        return getAllAsUser(user,DEFAULT_PAGE);
    }

    @Override
    public Collection<Order> getAllAsUser(User user, int page) {
        return getAllAsUser(user,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsUser(User user, int page, int pageSize) {
        return getAllAsUser(user,false,page,pageSize);
    }

    @Override
    public Collection<Order> getAllAsUser(User user, boolean includeShared, int page) {
        return getAllAsUser(user,includeShared,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> getAllAsUser(User user, boolean includeShared, int page, int pageSize) {
        return orderDao.getAllAsUser(user,includeShared,page,pageSize);
    }

    @Override
    public long getAllAsUserCount(User user) {
        return getAllAsUserCount(user,false);
    }

    @Override
    public long getAllAsUserLastPage(User user) {
        return getAllAsUserLastPage(user,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsUserLastPage(User user, int pageSize) {
        return getAllAsUserLastPage(user,false,pageSize);
    }

    @Override
    public long getAllAsUserCount(User user, boolean includeShared) {
        return orderDao.getAllAsUserCount(user,includeShared);
    }

    @Override
    public long getAllAsUserLastPage(User user, boolean includeShared) {
        return getAllAsUserLastPage(user,includeShared,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllAsUserLastPage(User user, boolean includeShared, int pageSize) {
        return getLastPage(getAllAsUserCount(user,includeShared),pageSize);
    }

    @Override
    public Collection<Order> filterOrders(User user, Map<Parameters, String> parameters){

        User clinicUser = null;
        User medicUser = null;
        String patientEmail = null;
        LocalDate date = null;
        StudyType type = null;
        boolean includeSharedIfMedic = true;

        if(parameters.containsKey(Parameters.CLINIC)){
            int aux = Integer.parseInt(parameters.get(Parameters.CLINIC));
            Optional<Clinic> clinicOptional = clinicService.findByUserId(aux);
            if(clinicOptional.isPresent())
                clinicUser = clinicOptional.get().getUser();
        }
        if(parameters.containsKey(Parameters.MEDIC)){
            int aux = Integer.parseInt(parameters.get(Parameters.MEDIC));
            Optional<Medic> medicOptional = medicService.findByUserId(aux);
            if(medicOptional.isPresent())
                medicUser = medicOptional.get().getUser();
        }
        if(parameters.containsKey(Parameters.PATIENT)){
            patientEmail = parameters.get(Parameters.PATIENT);
        }
        if(parameters.containsKey(Parameters.DATE)){
            boolean wrongFormatting = false;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try{
                dateFormat.parse(parameters.get(Parameters.DATE).trim());
            }catch (ParseException pe){
                wrongFormatting = true;
            }
            if(!wrongFormatting) {
                date = LocalDate.parse(parameters.get(Parameters.DATE));
            }
        }
        if(parameters.containsKey(Parameters.STUDYTYPE)){
            int aux = Integer.parseInt(parameters.get(Parameters.STUDYTYPE));
            Optional<StudyType> studyTypeOptional = studyService.findById(aux);
            if(studyTypeOptional.isPresent())
                type = studyTypeOptional.get();
        }

        return filterOrders(user,clinicUser,medicUser,patientEmail,date,type,includeSharedIfMedic,DEFAULT_PAGE);
    }

    @Override
    public Collection<Order> filterOrders(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic, int page) {
        return filterOrders(user, clinicUser, medicUser, patientEmail, date, type, includeSharedIfMedic, page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Order> filterOrders(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic, int page, int pageSize) {
        return orderDao.getFiltered(user, clinicUser, medicUser, patientEmail, date, type, includeSharedIfMedic, page, pageSize);
    }

    @Override
    public long filterOrdersCount(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic) {
        return orderDao.getFilteredCount(user, clinicUser, medicUser, patientEmail, date, type, includeSharedIfMedic);
    }

    @Override
    public long filterOrdersLastPage(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic) {
        return filterOrdersLastPage(user, clinicUser, medicUser, patientEmail, date, type, includeSharedIfMedic,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long filterOrdersLastPage(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic, int pageSize) {
        return getLastPage(filterOrdersCount(user, clinicUser, medicUser, patientEmail, date, type, includeSharedIfMedic),pageSize);
    }

    @Override
    public Order shareWithMedic(Order order, User user){
        Order o = orderDao.shareWithMedic(order, user);
        if(o != null){
            mailNotificationService.sendSharedOrderMail(order, user);
        }
        return o;
    }

    @Override
    public Order changeOrderClinic(Order order, Clinic clinic) {
        Order newOrder = orderDao.changeOrderClinic(order, clinic);
        mailNotificationService.sendChangeClinicMail(order);
        return newOrder;
    }

    // auxiliar functions and comparator
    private long getLastPage(final long count, final int pageSize){
        return (long) (Math.ceil(count / pageSize)+1);
    }
}
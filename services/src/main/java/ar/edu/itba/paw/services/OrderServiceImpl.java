package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private OrderDao orderDao;


    @Override
    public Optional<Order> findById(long id) {
        return orderDao.findById(id);
    }

    @Override
    public Order register(Medic medic, Date date, Clinic clinic, String patient_name, String patient_email, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber) {
        Order order = orderDao.register(medic,date,clinic,patient_name,patient_email,studyType,description,identificationType,identification,medicPlan,medicPlanNumber);
        mailNotificationService.sendOrderMail(order);
        return order;
    }

    @Override
    public Collection<Order> getAllAsClinic(User user) {
        return orderDao.getAllAsClinic(user);
    }

    private Collection<Order> getAllSharedWithMedic(User user) { return orderDao.getAllSharedAsMedic(user); }

    @Override
    public Collection<Order> getAllAsMedic(User user) {
        return orderDao.getAllAsMedic(user);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user) {
        return orderDao.getAllAsPatient(user);
    }

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudyTypeService studyService;

    @Override
    public Collection<Order> getAllAsUser(User user){
        if(user.isMedic() && !user.isVerifying()){
            Collection<Order> orders = getAllAsMedic(user);
            orders.addAll(getAllSharedWithMedic(user));
            return orders;
        }else if(user.isClinic() && !user.isVerifying()){
            return getAllAsClinic(user);
        }else if(user.isPatient()){
            return getAllAsPatient(user);
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public Collection<Order> filterOrders(User user, HashMap<Parameters, String> parameters){
        Collection<Order> orders;

        orders = getAllAsUser(user);

        if(parameters.containsKey(Parameters.CLINIC)){
            int aux = Integer.parseInt(parameters.get(Parameters.CLINIC));
            if(clinicService.findByUserId(aux).isPresent())
                orders.removeIf(order -> order.getClinic().getUserId() != aux);
        }
        if(parameters.containsKey(Parameters.MEDIC)){
            int aux = Integer.parseInt(parameters.get(Parameters.MEDIC));
            if(medicService.findByUserId(aux).isPresent())
                orders.removeIf(order -> order.getMedic().getUserId() != aux);
        }
        if(parameters.containsKey(Parameters.PATIENT)){
            orders.removeIf(order -> !order.getPatient_email().equals(parameters.get(Parameters.PATIENT)));
        }
        if(parameters.containsKey(Parameters.DATE)){
            boolean wrong_formatting = false;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try{
                dateFormat.parse(parameters.get(Parameters.DATE).trim());
            }catch (ParseException pe){
                wrong_formatting = true;
            }
            if(!wrong_formatting) {
                orders.removeIf(order -> !order.getDate().equals(Date.valueOf(parameters.get(Parameters.DATE))));
            }
        }
        if(parameters.containsKey(Parameters.STUDYTYPE)){
            int aux = Integer.parseInt(parameters.get(Parameters.STUDYTYPE));
            if(studyService.findById(aux).isPresent())
                orders.removeIf(order -> order.getStudy().getId() != Integer.parseInt(parameters.get(Parameters.STUDYTYPE)));
        }

        return orders;
    }

    @Override
    public Order shareWithMedic(Order order, User user){
        return orderDao.shareWithMedic(order, user);
    }
}
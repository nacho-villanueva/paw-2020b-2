package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao ud;

    @Autowired
    private ClinicDao cd;

    @Override
    public Optional<Order> findById(final long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    @Override
    public Order register(final Medic medic, final LocalDate date, final Clinic clinic,
                          final String patientEmail, final String patientName,
                          final StudyType studyType, final String description,
                          final String identificationType, final byte[] identification,
                          final String insurancePlan, final String insuranceNumber) {

        final Medic medicReference = em.getReference(Medic.class, medic.getUser().getId());
        final Clinic clinicReference = em.getReference(Clinic.class, clinic.getUser().getId());
        final StudyType studyTypeReference = em.getReference(StudyType.class, studyType.getId());


        final Order order = new Order(
                medicReference,
                date,
                clinicReference,
                studyTypeReference,
                description,
                identificationType,
                identification,
                insurancePlan,
                insuranceNumber,
                patientEmail,
                patientName);

        em.persist(order);
        em.flush();
        return order;
    }

    @Override
    public Collection<Order> getAllAsClinic(final User user, final int page, final int pageSize) {

        String queryString = "SELECT o FROM Order o JOIN o.clinic c "+
                "WHERE c.user.id = :id "+
                "ORDER BY o.orderId DESC";

        final TypedQuery<Order> query = em.createQuery(queryString, Order.class);
        query.setParameter("id", user.getId());

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getAllAsClinicCount(User user) {

        String queryString = "SELECT COUNT(o) FROM Order o JOIN o.clinic c "+
                "WHERE c.user.id = :id";

        final TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("id", user.getId());

        return query.getSingleResult();
    }

    @Override
    public Collection<Order> getAllAsMedic(final User user, final int page, final int pageSize) {
        return getAllAsMedic(user,true,false,page,pageSize);
    }

    @Override
    public long getAllAsMedicCount(User user) {
        return getAllAsMedicCount(user,true,false);
    }

    @Override
    public Collection<Order> getAllSharedAsMedic(final User user, final int page, final int pageSize) {

        return getAllAsMedic(user,false,true,page,pageSize);
    }

    @Override
    public long getAllSharedAsMedicCount(User user) {

        return getAllAsMedicCount(user,false,true);
    }

    @Override
    public Collection<Order> getAllParticipatingAsMedic(User user, int page, int pageSize) {
        return getAllAsMedic(user,true,true,page,pageSize);
    }

    @Override
    public long getAllParticipatingAsMedicCount(User user) {
        return getAllAsMedicCount(user,true,true);
    }

    private Collection<Order> getAllAsMedic(User user,boolean includeOwned, boolean includeShared, int page, int pageSize){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cr = cb.createQuery(Order.class);
        Root<Order> root = cr.from(Order.class);

        cr.select(root);

        cr.where(getAllAsMedicQueryPredicate(root,user,includeOwned,includeShared));

        cr.orderBy(cb.desc(root.get("orderId")));

        final TypedQuery<Order> query = em.createQuery(cr);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    private long getAllAsMedicCount(User user, boolean includeOwned, boolean includeShared){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<Order> root = cr.from(Order.class);

        cr.select(cb.count(root));

        cr.where(getAllAsMedicQueryPredicate(root,user,includeOwned,includeShared));


        final TypedQuery<Long> query = em.createQuery(cr);

        return query.getResultList().get(0);
    }

    @Override
    public Order shareWithMedic(Order order, User user){
        final Optional<Order> maybeOrder = findById(order.getOrderId());
        if(order.getMedic().getUserId() == user.getId())
            return null;
        final Optional<User> maybeUser = ud.findById(user.getId());
        if(maybeOrder.isPresent() && maybeUser.isPresent()){
            maybeOrder.get().addToSharedWith(maybeUser.get());
            em.flush();
        }

        return maybeOrder.orElse(null);
    }

    @Override
    public Collection<Order> getAllAsPatient(final User user, final int page, final int pageSize) {

        String queryString = "SELECT o FROM Order o "+
                "WHERE o.patientEmail = :email "+
                "ORDER BY o.orderId DESC";

        final TypedQuery<Order> query = em.createQuery(queryString, Order.class);
        query.setParameter("email", user.getEmail());

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getAllAsPatientCount(User user) {

        String queryString = "SELECT o FROM Order o "+
                "WHERE o.patientEmail = :email";

        final TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("email", user.getEmail());

        return query.getSingleResult();
    }

    @Override
    public Order changeOrderClinic(Order order, Clinic clinic){
        Optional<Clinic> maybeClinic = cd.findByUserId(clinic.getUserId());
        if(!maybeClinic.isPresent())
            return null;
        Optional<Order> orderDB = findById(order.getOrderId());
        if(!orderDB.isPresent())
            return null;

        orderDB.get().setClinic(maybeClinic.get());
        em.flush();
        return orderDB.get();
    }

    @Override
    public Collection<Order> getAllAsPatientOfType(final String email, final StudyType type, final int page, final int pageSize){

        String queryString = "SELECT o FROM Order o "+
                "WHERE o.patientEmail = :email AND o.study = :type "+
                "ORDER BY o.orderId DESC";

        final TypedQuery<Order> query = em.createQuery(queryString, Order.class);
        query.setParameter("email", email);
        query.setParameter("type", type);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getAllAsPatientOfTypeCount(String email, StudyType type) {
        String queryString = "SELECT COUNT(o) FROM Order o "+
                "WHERE o.patientEmail = :email AND o.study = :type";

        final TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("email", email);
        query.setParameter("type", type);

        return query.getSingleResult();
    }

    @Override
    public Collection<Order> getFiltered(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic, int page, int pageSize) {

        if(user == null)
            return null;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cr = cb.createQuery(Order.class);
        Root<Order> root = cr.from(Order.class);

        Collection<Predicate> predicates = getAllAsUserQueryPredicate(new ArrayList<>(),root,user,includeSharedIfMedic);
        predicates = getFilteredPredicate(predicates,root,clinicUser,medicUser,patientEmail,date,type);

        cr.select(root);

        cr.where(predicates.toArray(new Predicate[0]));

        cr.orderBy(cb.desc(root.get("orderId")));

        final TypedQuery<Order> query = em.createQuery(cr);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getFilteredCount(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic) {

        if(user == null)
            return -1;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<Order> root = cr.from(Order.class);

        Collection<Predicate> predicates = getAllAsUserQueryPredicate(new ArrayList<>(),root,user,includeSharedIfMedic);
        predicates = getFilteredPredicate(predicates,root,clinicUser,medicUser,patientEmail,date,type);

        cr.select(cb.count(root));

        cr.where(predicates.toArray(new Predicate[0]));

        cr.orderBy(cb.desc(root.get("orderId")));

        final TypedQuery<Long> query = em.createQuery(cr);

        return query.getResultList().get(0);
    }

    private Collection<Predicate> getFilteredPredicate(Collection<Predicate> predicates, Root<Order> root, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        if(clinicUser != null)
            getAllAsClinicQueryPredicate(predicates, root, clinicUser);

        if(medicUser != null)
            getAllAsMedicQueryPredicate(predicates, root, medicUser, false);

        if(patientEmail != null && patientEmail.trim().length() >0)
            getAllAsPatientQueryPredicate(predicates, root, patientEmail);

        if(date != null)
            predicates.add(cb.equal(root.get("date"),date));

        if(type != null)
            predicates.add(cb.equal(root.get("study"),type));

        return predicates;
    }

    @Override
    public Collection<Order> getAllAsUser(User user, boolean includeSharedIfMedic, int page, int pageSize){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cr = cb.createQuery(Order.class);
        Root<Order> root = cr.from(Order.class);

        cr.select(root);

        cr.where(getAllAsUserQueryPredicate(new ArrayList<>(),root,user,includeSharedIfMedic).toArray(new Predicate[0]));

        cr.orderBy(cb.desc(root.get("orderId")));

        final TypedQuery<Order> query = em.createQuery(cr);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getAllAsUserCount(User user, boolean includeSharedIfMedic){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<Order> root = cr.from(Order.class);

        cr.select(cb.count(root));

        cr.where(getAllAsUserQueryPredicate(new ArrayList<>(),root,user,includeSharedIfMedic).toArray(new Predicate[0]));

        final TypedQuery<Long> query = em.createQuery(cr);

        return query.getResultList().get(0);
    }

    private Collection<Predicate> getAllAsUserQueryPredicate(Collection<Predicate> predicates, Root<Order> root, User user, boolean includeSharedIfMedic){
        if(user.isMedic() && !user.isVerifying()){
            return getAllAsMedicQueryPredicate(predicates,root,user,includeSharedIfMedic);
        }else if(user.isClinic() && !user.isVerifying()){
            return getAllAsClinicQueryPredicate(predicates,root,user);
        }else if(user.isPatient()){
            return getAllAsPatientQueryPredicate(predicates,root,user.getEmail());
        }else{
            return predicates;
        }
    }

    private Collection<Predicate> getAllAsPatientQueryPredicate(Collection<Predicate> predicates, Root<Order> root, String patientEmail){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        predicates.add(cb.equal(root.get("patientEmail"),patientEmail));
        return predicates;
    }

    private Collection<Predicate> getAllAsMedicQueryPredicate(Collection<Predicate> predicates, Root<Order> root, User medic, boolean includeShared){
        predicates.add(getAllAsMedicQueryPredicate(root,medic,true,includeShared));
        return predicates;
    }

    private Collection<Predicate> getAllAsClinicQueryPredicate(Collection<Predicate> predicates, Root<Order> root, User clinic){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        predicates.add(cb.equal(root.get("clinic").get("user").get("id"),clinic.getId()));
        return predicates;
    }

    private Predicate getAllAsMedicQueryPredicate(Root<Order> root, User user, boolean includeOwned, boolean includeShared){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        Predicate ownedPredicate = (includeOwned)?(cb.equal(root.get("medic").get("user").get("id"),user.getId())):(cb.isFalse(cb.literal(true)));
        Predicate sharedPredicate = (includeShared)?(cb.isMember(user,root.get("sharedWith"))):(cb.isFalse(cb.literal(true)));

        return cb.or(ownedPredicate,sharedPredicate);
    }
}

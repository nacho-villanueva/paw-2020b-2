package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalTime;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Repository
public class ClinicJpaDao implements ClinicDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private StudyTypeDao studyTypeDao;

    @Autowired
    private MedicPlanDao medicPlanDao;

    @Override
    public Optional<Clinic> findByUserId(int userId) {
        return Optional.ofNullable(em.find(Clinic.class,userId));
    }

    @Override
    public Collection<Clinic> getAll(final int page, final int pageSize) {
        return getAll(true,page,pageSize);
    }

    @Override
    public long getAllCount() {
        return getAllCount(true);
    }

    @Override
    public Collection<Clinic> getAllUnverified(final int page, final int pageSize) {
        return getAll(false,page,pageSize);
    }

    @Override
    public long getAllUnverifiedCount() {
        return getAllCount(false);
    }

    private Long getAllCount(final boolean verified){

        final String queryString = "SELECT COUNT(c) FROM Clinic c " +
                "WHERE c.verified = :isVerified";

        final TypedQuery<Long> countQuery = em.createQuery(queryString,Long.class);
        countQuery.setParameter("isVerified",verified);
        return countQuery.getSingleResult();
    }

    private Collection<Clinic> getAll(final boolean verified, final int page, final int pageSize) {

        if(pageSize <= 0 || page <= 0)
            return new ArrayList<>();

        String queryString = "SELECT c FROM Clinic c " +
                "WHERE c.verified = :isVerified "+
                "ORDER BY c.name ASC, c.user.id ASC";

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);
        query.setParameter("isVerified",verified);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(final int studyTypeId, final int page, final int pageSize) {

        Optional<StudyType> studyTypeOptional = Optional.ofNullable(em.getReference(StudyType.class,studyTypeId));

        if(!studyTypeOptional.isPresent())
            return new ArrayList<>();

        String queryString = "SELECT c FROM Clinic c " +
                "INNER JOIN FETCH c.medicalStudies " +
                "WHERE c.verified = true " +
                "AND :studyType MEMBER OF c.medicalStudies " +
                "ORDER BY c.name ASC, c.user.id ASC";

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);
        query.setParameter("studyType",studyTypeOptional.get());

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getByStudyTypeIdCount(int studyTypeId) {
        Optional<StudyType> studyTypeOptional = Optional.ofNullable(em.getReference(StudyType.class,studyTypeId));

        if(!studyTypeOptional.isPresent())
            return 0;

        String queryString = "SELECT COUNT(c) FROM Clinic c " +
                "INNER JOIN c.medicalStudies " +
                "WHERE c.verified = true " +
                "AND :studyType MEMBER OF c.medicalStudies";

        final TypedQuery<Long> query = em.createQuery(queryString,Long.class);
        query.setParameter("studyType",studyTypeOptional.get());

        return query.getSingleResult();
    }

    @Override
    public void verifyClinic(int clinicId) {
        Optional<Clinic> clinicDB = findByUserId(clinicId);

        clinicDB.ifPresent(clinic -> {
            if(!clinic.isVerified())
                clinic.setVerified(true);
            em.flush();
        });
    }


    @Override
    public Clinic register(final User user, final String name, final String telephone, final Collection<StudyType> availableStudies, final Collection<MedicPlan> medicPlans, final ClinicHours hours, final boolean verified) {

        //Getting references
        User userRef = em.getReference(User.class,user.getId());
        Collection<StudyType> studyTypesRef = new HashSet<>();
        availableStudies.forEach(studyType -> {
            studyTypesRef.add(getStudyRef(studyType));
        });
        Collection<MedicPlan> plansRef = new HashSet<>();
        medicPlans.forEach(plan -> {
            plansRef.add(getPlanRef(plan));
        });

        final Clinic clinic = new Clinic(userRef,name,telephone,studyTypesRef,plansRef,false);

        em.persist(clinic);

        clinic.setHours(hours); //TODO: check
        em.flush();
        return clinic;
    }

    @Override
    public Clinic updateClinicInfo(final User user, final String name, final String telephone,
                                   final Collection<StudyType> availableStudies, final Collection<MedicPlan> medicPlans,
                                   final ClinicHours hours) {
        Optional<Clinic> clinicDB = findByUserId(user.getId());

        clinicDB.ifPresent(clinic -> {
            if(!isEmpty(name))
                clinic.setName(name);
            if(!isEmpty(telephone))
                clinic.setTelephone(telephone);
            if(medicPlans != null) {
                Collection<MedicPlan> plansRef = new HashSet<>();
                medicPlans.forEach(plan -> {
                    plansRef.add(getPlanRef(plan));
                });
                clinic.setAcceptedPlans(plansRef);
            }
            if(availableStudies != null) {
                Collection<StudyType> studiesRef = new HashSet<>();
                availableStudies.forEach(study -> {
                    studiesRef.add(getStudyRef(study));
                });
                clinic.setMedicalStudies(studiesRef);
            }
            //Updating hours
            if(hours != null)
                clinic.setHours(hours);

            em.flush();
        });

        return clinicDB.orElse(null);
    }

    private StudyType getStudyRef(StudyType studyType) {
        StudyType retRef;
        if(studyType.getId() != null) {
            retRef = em.getReference(StudyType.class, studyType.getId());
            if(retRef != null) {
                return retRef;
            }
        }

        //If study does not exists we try and create it then add its reference that we know it wont fail (in theory)
        StudyType newStudy = studyTypeDao.findOrRegister(studyType.getName());
        return em.getReference(StudyType.class,newStudy.getId());
    }

    private MedicPlan getPlanRef(MedicPlan medicPlan) {
        MedicPlan planRef;
        if(medicPlan.getId() != null) {
            planRef = em.getReference(MedicPlan.class, medicPlan.getId());
            if(planRef != null) {
                return planRef;
            }
        }

        MedicPlan newPlan = medicPlanDao.findOrRegister(medicPlan.getName());
        return em.getReference(MedicPlan.class, newPlan.getId());
    }

    @Override
    public boolean hasStudy(final int clinicId, final int studyTypeId) {
        Optional<Clinic> clinicOptional = findByUserId(clinicId);
        Optional<StudyType> studyTypeOptional = studyTypeDao.findById(studyTypeId);

        return clinicOptional.isPresent() && studyTypeOptional.isPresent() && clinicOptional.get().getMedicalStudies().contains(studyTypeOptional.get());
    }

    @Override
    public boolean acceptsPlan(final int clinicId, final int planId) {
        Optional<Clinic> clinicOptional = findByUserId(clinicId);
        Optional<MedicPlan> planOptional = medicPlanDao.findById(planId);

        return clinicOptional.isPresent() && planOptional.isPresent() && clinicOptional.get().getAcceptedPlans().contains(planOptional.get());
    }

    @Override
    public StudyType registerStudyToClinic(final int clinicId, final StudyType studyType) {

        Optional<Clinic> clinicOptional = Optional.ofNullable(em.find(Clinic.class,clinicId));

        StudyType studyTypeFromDB = null;

        if(clinicOptional.isPresent()){
            //We check if it exists
            studyTypeFromDB = studyTypeDao.findOrRegister(studyType.getName());
            Clinic clinic = clinicOptional.get();

            //TODO check it works
            clinic.getMedicalStudies().add(studyTypeFromDB);
            em.flush();
        }

        //Todo: verify success
        return studyTypeFromDB;
    }

    @Override
    public MedicPlan registerPlanToClinic(final int clinicId, final MedicPlan medicPlan) {

        Optional<Clinic> clinicOptional = Optional.ofNullable(em.find(Clinic.class,clinicId));

        MedicPlan medicPlanFromDB = null;

        if(clinicOptional.isPresent()){
            //We check if it exists
            medicPlanFromDB = medicPlanDao.findOrRegister(medicPlan.getName());
            Clinic clinic = clinicOptional.get();

            //TODO check it works
            clinic.getAcceptedPlans().add(medicPlanFromDB);
            em.flush();
        }

        //Todo: verify success
        return medicPlanFromDB;
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int page, int pageSize) {
        String queryString = getSearchQueryString("DISTINCT c",clinicName,hours,acceptedPlan,studyName)
                +" ORDER BY c.name ASC, c.user.id ASC";

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);

        fillSearchClinicsParams(query,clinicName,hours,acceptedPlan,studyName);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long searchClinicsByCount(String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        String queryString = getSearchQueryString("COUNT(DISTINCT c)",clinicName,hours,acceptedPlan,studyName);

        final TypedQuery<Long> query = em.createQuery(queryString,Long.class);

        fillSearchClinicsParams(query,clinicName,hours,acceptedPlan,studyName);

        return query.getResultList().get(0);
    }

    private void fillSearchClinicsParams(TypedQuery<?> query,String clinicName, ClinicHours hours, String acceptedPlan, String studyName){
        //filling params
        if(clinicName!=null)
            query.setParameter("clinicName","%"+clinicName.toLowerCase()+"%");
        if(acceptedPlan!=null)
            query.setParameter("clinicAcceptedPlan","%"+acceptedPlan.toLowerCase()+"%");
        if(studyName!=null)
            query.setParameter("clinicMedicalStudy","%"+studyName.toLowerCase()+"%");
        if(hours!=null){
            for (int i = 0; i < hours.getDays().length; i++) {
                //If we have a filter on this day, we add condition
                if(hours.getDays()[i]) {
                    query.setParameter("day_of_the_week_"+String.valueOf(i),i);
                    query.setParameter("from_time_"+String.valueOf(i),hours.getOpenHours()[i]);
                    query.setParameter("until_time_"+String.valueOf(i),hours.getCloseHours()[i]);
                }
            }
        }
    }

    private String getSearchQueryString(String returnValue,String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        //Base Query
        StringBuilder query = new StringBuilder("SELECT "+returnValue+" FROM Clinic c");

        //Joins
        if(hours != null) {
            //Add hours part
            query.append(" INNER JOIN c.hours as cdh");
        }

        if(acceptedPlan != null) {
            //Add plans part
            query.append(" INNER JOIN c.acceptedPlans as cap");
        }

        if(studyName != null) {
            //Add study name part
            query.append(" INNER JOIN c.medicalStudies as ms");
        }

        //Search
        query.append(" WHERE c.verified = true");

        if(clinicName != null) {
            //Add clinic name condition
            query.append(" AND (LOWER(c.name) LIKE :clinicName)");
        }

        if(hours != null) {
            //Add hours condition
            query.append(" AND (");
            for (int i = 0; i < hours.getDays().length; i++) {
                //If we have a filter on this day, we add condition
                if(hours.getDays()[i]) {
                    //This person, on this day is available from X to Y
                    //I want clinics that are open at least some part of the time frame of this day filter
                    LocalTime availableFrom = hours.getOpenHours()[i];
                    LocalTime availableUntil = hours.getCloseHours()[i];
                    query.append("( cdh.dayOfWeek = :day_of_the_week_");
                    query.append(i);
                    query.append(" AND NOT (cdh.closeTime <= :from_time_");
                    query.append(i);
                    query.append(" OR cdh.openTime >= :until_time_");
                    query.append(i);
                    query.append(" ))");
                } else {
                    //writing 'false' failed
                    query.append("1=0");
                }
                if(i < hours.getDays().length - 1) {
                    query.append(" OR ");
                }
            }
            query.append(")");
        }

        if(acceptedPlan != null) {
            //Add plan condition
            query.append(" AND LOWER(cap.name) LIKE :clinicAcceptedPlan");
        }

        if(studyName != null) {
            //Add study name condition
            query.append(" AND LOWER(ms.name) LIKE :clinicMedicalStudy");
        }

        return query.toString();
    }
}

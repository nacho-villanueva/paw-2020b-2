package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Time;
import java.util.*;

//@Repository
public class ClinicJpaDao implements ClinicDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StudyTypeDao studyTypeDao;

    @Override
    public Optional<Clinic> findByUserId(int user_id) {
        return Optional.ofNullable(em.find(Clinic.class,user_id));
    }

    @Override
    public Collection<Clinic> getAll() {
        return getAll(true);
    }

    @Override
    public Collection<Clinic> getAllUnverified() {
        return getAll(false);
    }

    private Collection<Clinic> getAll(final boolean verified) {
        // with hibernate, the validation that a medic is associated with an user should be unnecesary
        final TypedQuery<Clinic> query = em.createQuery("SELECT c FROM Clinic c WHERE c.verified = :isVerified",Clinic.class);
        query.setParameter("isVerified",String.valueOf(verified));
        return query.getResultList();
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(final int studyType_id) {

        Optional<StudyType> studyTypeOptional = studyTypeDao.findById(studyType_id);

        if(!studyTypeOptional.isPresent())
            return new ArrayList<>();

        String queryString = "SELECT c FROM Clinic c " +
                "INNER JOIN FETCH c.medical_studies " +
                "WHERE c.verified = true " +
                "AND :studyType MEMBER OF c.medical_studies";

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);
        query.setParameter("studyType",studyTypeOptional.get());

        return query.getResultList();
    }

    private ClinicHours getClinicHours(int clinic_id) {
        ClinicHours clinicHours = new ClinicHours();

        final TypedQuery<ClinicDayHours> query = em.createQuery("SELECT cdh FROM ClinicDayHours cdh WHERE cdh.clinic_id = :clinicId",ClinicDayHours.class);
        query.setParameter("clinicId",clinic_id);
        Collection<ClinicDayHours> clinicDayHours =  query.getResultList();

        clinicDayHours.forEach(day -> clinicHours.setDayHour(day.getDay_of_week(),day.getOpen_time(),day.getClose_time()));

        return clinicHours;
    }

    //TODO: when moving to aspect oriented programming make this function transactional
    @Override
    public Clinic register(final User user, final String name, final String telephone, final Collection<StudyType> available_studies, final Set<String> medic_plans, final ClinicHours hours, final boolean verified) {
        Clinic clinic = new Clinic(user,name,telephone,false);

        userDao.updateRole(user, User.CLINIC_ROLE_ID);

        em.persist(clinic);
        //Todo: Check success

        registerStudiesToClinic(available_studies,user.getId());

        //Add hours to database
        registerHours(user.getId(), hours);

        return clinic;
    }

    private void registerHours(int clinic_id, ClinicHours hours) {

        Optional<Clinic> clinicOptional = findByUserId(clinic_id);
        if(clinicOptional.isPresent()){
            Clinic clinic = clinicOptional.get();

            for(int i = 0; i < hours.getDays().length; i++) {
                if(hours.getDays()[i]) {
                    ClinicDayHours clinicDayHours = new ClinicDayHours(i,clinic,hours.getOpen_hours()[i],hours.getClose_hours()[i]);
                    em.persist(clinicDayHours);
                }
            }
        }
    }

    @Override
    public Clinic updateClinicInfo(final User user, final String name, final String telephone, final Collection<StudyType> available_studies, final Set<String> medic_plans, final ClinicHours hours, final boolean verified) {

        Optional<Clinic> clinicOptional = findByUserId(user.getId());

        if(!clinicOptional.isPresent())
            return null;

        Clinic clinic = clinicOptional.get();

        em.detach(clinic);
        em.getTransaction().begin();
        clinic.setUser(user);
        clinic.setName(name);
        clinic.setTelephone(telephone);
        clinic.setAccepted_plans(medic_plans);
        clinic.setVerified(verified);
        em.getTransaction().commit();

        registerStudiesToClinic(available_studies,user.getId());

        updateHours(user.getId(),hours);

        return clinic;
    }

    private void updateHours(int clinic_id, ClinicHours hours) {

        // remove existing ClinicDayHours and then register the new ones
        final TypedQuery<ClinicDayHours> query = em.createQuery("SELECT cdh FROM ClinicDayHours cdh WHERE cdh.clinic_id = :clinicId",ClinicDayHours.class);
        query.setParameter("clinicId",clinic_id);
        Collection<ClinicDayHours> clinicDayHours =  query.getResultList();

        for (ClinicDayHours cdh:clinicDayHours) {
            em.detach(cdh);
            em.getTransaction().begin();
            em.remove(cdh);
            em.getTransaction().commit();
        }

        registerHours(clinic_id,hours);
    }

    @Override
    public boolean hasStudy(final int clinic_id, final int studyType_id) {
        Optional<Clinic> clinicOptional = findByUserId(clinic_id);
        Optional<StudyType> studyTypeOptional = studyTypeDao.findById(studyType_id);

        return clinicOptional.isPresent() && studyTypeOptional.isPresent() && clinicOptional.get().getMedical_studies().contains(studyTypeOptional.get());
    }

    @Override
    public StudyType registerStudyToClinic(final int clinic_id, final StudyType studyType) {
        //We check if it exists
        StudyType studyTypeFromDB = studyTypeDao.findOrRegister(studyType.getName());

        Optional<Clinic> clinicOptional = findByUserId(clinic_id);

        if(clinicOptional.isPresent()){
            Clinic clinic = clinicOptional.get();

            //TODO check it works
            em.detach(clinic);
            clinic.getMedical_studies().add(studyTypeFromDB);
            em.getTransaction().begin();
            em.merge(clinic);
            em.getTransaction().commit();
        }

        //Todo: verify success

        return studyTypeFromDB;
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinic_name, ClinicHours hours, String accepted_plan, String study_name) {
        String queryString = getSearchQueryString(clinic_name,hours,accepted_plan,study_name);

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);

        if(clinic_name!=null)
            query.setParameter("clinicName",clinic_name);
        if(accepted_plan!=null)
            query.setParameter("clinicAcceptedPlan",accepted_plan.toLowerCase());
        if(study_name!=null)
            query.setParameter("clinicMedicalStudy",study_name.toLowerCase());
        if(hours!=null){
            for (int i = 0; i < hours.getDays().length; i++) {
                //If we have a filter on this day, we add condition
                if(hours.getDays()[i]) {
                    query.setParameter("day_of_the_week_"+String.valueOf(i),i);
                    query.setParameter("from_time_"+String.valueOf(i),hours.getOpen_hours()[i]);
                    query.setParameter("until_time_"+String.valueOf(i),hours.getClose_hours()[i]);
                }
            }
        }

        return query.getResultList();
    }

    private String getSearchQueryString(String clinic_name, ClinicHours hours, String accepted_plan, String study_name) {
        //Base Query
        StringBuilder query = new StringBuilder("SELECT DISTINCT c FROM Clinics c");

        //Joins
        if(hours != null) {
            //Add hours part
            query.append(" INNER JOIN ClinicDayHours cdh ON cdh.clinic_id = c.user.id");
        }

        if(accepted_plan != null) {
            //Add plans part
            query.append(" INNER JOIN FETCH c.accepted_plans as cap");
        }

        if(study_name != null) {
            //Add study name part
            query.append(" INNER JOIN FETCH c.medical_studies as ms");
        }

        //Search
        query.append(" WHERE c.verified = true");

        if(clinic_name != null) {
            //Add clinic name condition
            query.append(" AND lower(c.name) LIKE '%:clinicName%'");
        }

        if(hours != null) {
            //Add hours condition
            query.append(" AND (");
            for (int i = 0; i < hours.getDays().length; i++) {
                //If we have a filter on this day, we add condition
                if(hours.getDays()[i]) {
                    //This person, on this day is available from X to Y
                    //I want clinics that are open at least some part of the time frame of this day filter
                    Time availableFrom = hours.getOpen_hours()[i];
                    Time availableUntil = hours.getClose_hours()[i];
                    query.append("( cdh.day_of_week = :day_of_the_week_");
                    query.append(i);
                    query.append(" AND NOT (cdh.close_time <= :from_time_");
                    query.append(i);
                    query.append(" OR cdh.open_time >= :until_time_");
                    query.append(i);
                    query.append(" ))");
                } else {
                    query.append("false");
                }
                if(i < hours.getDays().length - 1) {
                    query.append(" OR ");
                }
            }
            query.append(")");
        }

        if(accepted_plan != null) {
            //Add plan condition
            query.append(" AND lower(cap.name) LIKE '%:clinicAcceptedPlan%'");
        }

        if(study_name != null) {
            //Add study name condition
            query.append(" AND lower(ms.name) LIKE '%:clinicMedicalStudy%'");
        }

        return query.toString();
    }

    private void registerStudiesToClinic(final Collection<StudyType> available_studies, final int clinic_id) {
        Collection<StudyType> old_studies = studyTypeDao.findByClinicId(clinic_id);
        Collection<StudyType> available_studiesDB = new ArrayList<>();

        available_studies.forEach(studyType -> {
            StudyType studyTypeFromDB = this.registerStudyToClinic(clinic_id, studyType);
            available_studiesDB.add(studyTypeFromDB);
        });
        
        //We delete the ones that are not in the new list but are still on database
        old_studies.forEach(studyType -> {
            if(available_studiesDB.stream().noneMatch(s -> {return s.getId() == studyType.getId();})) {
                unregisterStudyToClinic(clinic_id,studyType.getId());
            }
        });

        return;
    }

    private void unregisterStudyToClinic(int clinic_id, int study_id) {
        Optional<Clinic> clinicOptional = findByUserId(clinic_id);
        Optional<StudyType> studyTypeOptional = studyTypeDao.findById(study_id);

        if(clinicOptional.isPresent() && studyTypeOptional.isPresent()){
            Clinic clinic = clinicOptional.get();
            StudyType medicalField = studyTypeOptional.get();

            //TODO check it works
            em.detach(clinic);
            clinic.getMedical_studies().remove(medicalField);
            em.getTransaction().begin();
            em.merge(clinic);
            em.getTransaction().commit();

        }

        return;
    }
}

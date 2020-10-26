package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Time;
import java.util.*;

@Repository
public class ClinicJpaDao implements ClinicDao {

    @PersistenceContext
    private EntityManager em;

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
        query.setParameter("isVerified",verified);
        return query.getResultList();
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(final int studyType_id) {

        Optional<StudyType> studyTypeOptional = Optional.ofNullable(em.getReference(StudyType.class,studyType_id));

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

    //TODO: when moving to aspect oriented programming make this function transactional
    @Override
    public Clinic register(final User user, final String name, final String telephone, final Collection<StudyType> available_studies, final Set<String> medic_plans, final ClinicHours hours, final boolean verified) {

        //Getting references
        User userRef = em.getReference(User.class,user.getId());
        Collection<StudyType> studyTypesRef = new HashSet<>();
        available_studies.forEach(studyType -> {
            studyTypesRef.add(em.getReference(StudyType.class,studyType.getId()));
        });

        final Clinic clinic = new Clinic(userRef,name,telephone,studyTypesRef,hours,medic_plans,false);

        em.persist(clinic);
        //Todo: Check success

        return clinic;
    }

    @Override
    public Clinic updateClinicInfo(final User user, final String name, final String telephone, final Collection<StudyType> available_studies, final Set<String> medic_plans, final ClinicHours hours, final boolean verified) {

        Optional<Clinic> clinicOptional = this.findByUserId(user.getId());

        if(!clinicOptional.isPresent())
            return null;

        Clinic clinic = clinicOptional.get();

        //Getting references
        User userRef = em.getReference(User.class,user.getId());
        Collection<StudyType> studyTypeRef = new HashSet<>();
        available_studies.forEach(studyType -> {
            studyTypeRef.add(em.getReference(StudyType.class,studyType.getId()));
        });

        em.detach(clinic);
        clinic.setUser(userRef);
        clinic.setName(name);
        clinic.setTelephone(telephone);
        clinic.setAccepted_plans(medic_plans);
        clinic.setMedical_studies(studyTypeRef);
        clinic.setHours(hours);
        clinic.setVerified(verified);

        em.merge(clinic);

        return clinic;
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

        Optional<Clinic> clinicOptional = Optional.ofNullable(em.find(Clinic.class,clinic_id));

        if(clinicOptional.isPresent()){
            Clinic clinic = clinicOptional.get();

            //TODO check it works
            em.detach(clinic);
            clinic.getMedical_studies().add(studyTypeFromDB);
            em.merge(clinic);
        }

        //Todo: verify success

        return studyTypeFromDB;
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinic_name, ClinicHours hours, String accepted_plan, String study_name) {
        String queryString = getSearchQueryString(clinic_name,hours,accepted_plan,study_name);

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);

        //filling params

        if(clinic_name!=null)
            query.setParameter("clinicName","%"+clinic_name.toLowerCase()+"%");
        if(accepted_plan!=null)
            query.setParameter("clinicAcceptedPlan","%"+accepted_plan.toLowerCase()+"%");
        if(study_name!=null)
            query.setParameter("clinicMedicalStudy","%"+study_name.toLowerCase()+"%");
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
        StringBuilder query = new StringBuilder("SELECT DISTINCT c FROM Clinic c");

        //Joins
        if(hours != null) {
            //Add hours part
            query.append(" INNER JOIN c.hours as cdh");
        }

        if(accepted_plan != null) {
            //Add plans part
            query.append(" INNER JOIN c.accepted_plans as cap");
        }

        if(study_name != null) {
            //Add study name part
            query.append(" INNER JOIN c.medical_studies as ms");
        }

        //Search
        query.append(" WHERE c.verified = true");

        if(clinic_name != null) {
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
                    //writing 'false' failed
                    query.append("1=0");
                }
                if(i < hours.getDays().length - 1) {
                    query.append(" OR ");
                }
            }
            query.append(")");
        }

        if(accepted_plan != null) {
            //Add plan condition
            query.append(" AND LOWER(cap) LIKE :clinicAcceptedPlan");
        }

        if(study_name != null) {
            //Add study name condition
            query.append(" AND LOWER(ms.name) LIKE :clinicMedicalStudy");
        }

        return query.toString();
    }

}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.CollectionTable;
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
    public Optional<Clinic> findByUserId(int userId) {
        return Optional.ofNullable(em.find(Clinic.class,userId));
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
    public Collection<Clinic> getByStudyTypeId(final int studyTypeId) {

        Optional<StudyType> studyTypeOptional = Optional.ofNullable(em.getReference(StudyType.class,studyTypeId));

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


    @Override
    public Clinic register(final User user, final String name, final String telephone, final Collection<StudyType> availableStudies, final Set<String> medicPlans, final ClinicHours hours, final boolean verified) {

        //Getting references
        User userRef = em.getReference(User.class,user.getId());
        Collection<StudyType> studyTypesRef = new HashSet<>();
        availableStudies.forEach(studyType -> {
            studyTypesRef.add(getStudyRef(studyType));
        });

        final Clinic clinic = new Clinic(userRef,name,telephone,studyTypesRef,medicPlans,false);

        em.persist(clinic);

        clinic.setHours(hours); //TODO: check
        em.flush();
        return clinic;
    }

    @Override
    public Clinic updateClinicInfo(final User user, final String name, final String telephone, final Collection<StudyType> availableStudies, final Set<String> medicPlans, final ClinicHours hours, final boolean verified) {
        Optional<Clinic> clinicDB = findByUserId(user.getId());

        clinicDB.ifPresent(clinic -> {
            clinic.setName(name);
            clinic.setTelephone(telephone);
            clinic.setVerified(verified);
            clinic.setAcceptedPlans(medicPlans);

            Collection<StudyType> studiesRef = new HashSet<>();
            availableStudies.forEach(study -> {
                studiesRef.add(getStudyRef(study));
            });
            clinic.setMedical_studies(studiesRef);

            //Updating hours
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

    @Override
    public boolean hasStudy(final int clinicId, final int studyTypeId) {
        Optional<Clinic> clinicOptional = findByUserId(clinicId);
        Optional<StudyType> studyTypeOptional = studyTypeDao.findById(studyTypeId);

        return clinicOptional.isPresent() && studyTypeOptional.isPresent() && clinicOptional.get().getMedical_studies().contains(studyTypeOptional.get());
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
            clinic.getMedical_studies().add(studyTypeFromDB);
            em.flush();
        }

        //Todo: verify success
        return studyTypeFromDB;
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        String queryString = getSearchQueryString(clinicName,hours,acceptedPlan,studyName);

        final TypedQuery<Clinic> query = em.createQuery(queryString,Clinic.class);

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
                    query.setParameter("from_time_"+String.valueOf(i),hours.getOpen_hours()[i]);
                    query.setParameter("until_time_"+String.valueOf(i),hours.getClose_hours()[i]);
                }
            }
        }

        return query.getResultList();
    }

    private String getSearchQueryString(String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        //Base Query
        StringBuilder query = new StringBuilder("SELECT DISTINCT c FROM Clinic c");

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
            query.append(" INNER JOIN c.medical_studies as ms");
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

        if(acceptedPlan != null) {
            //Add plan condition
            query.append(" AND LOWER(cap) LIKE :clinicAcceptedPlan");
        }

        if(studyName != null) {
            //Add study name condition
            query.append(" AND LOWER(ms.name) LIKE :clinicMedicalStudy");
        }

        return query.toString();
    }
}

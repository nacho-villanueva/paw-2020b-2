package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

//@Repository
public class StudyTypeJpaDao implements StudyTypeDao{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ClinicDao clinicDao;

    @Override
    public Optional<StudyType> findById(int id) {
        return Optional.ofNullable(em.find(StudyType.class,id));
    }

    @Override
    public Optional<StudyType> findByName(String name) {

        final TypedQuery<StudyType> query = em.createQuery("SELECT st FROM StudyType st WHERE lower(st.name) = lower(:studyTypeName)",StudyType.class);
        query.setParameter("studyTypeName",name);
        List<StudyType> list = query.getResultList();

        return Optional.ofNullable(list.isEmpty() ? null : list.get(0));
    }

    @Override
    public Collection<StudyType> getAll() {
        final TypedQuery<StudyType> query = em.createQuery("SELECT st FROM StudyType st",StudyType.class);
        return query.getResultList();
    }

    @Override
    public Collection<StudyType> findByClinicId(int clinic_id) {
        Optional<Clinic> clinicOptional = clinicDao.findByUserId(clinic_id);
        return clinicOptional.map(Clinic::getMedical_studies).orElse(null);

    }

    @Override
    public StudyType findOrRegister(String name) {
        Optional<StudyType> studyType = this.findByName(name);

        return studyType.orElseGet(() -> this.register(name.substring(0, 1).toUpperCase() + name.substring(1)));

    }

    private StudyType register(final String name) {
        StudyType studyType = new StudyType(name);

        em.persist(studyType);

        //TODO: Check success
        return studyType;
    }
}

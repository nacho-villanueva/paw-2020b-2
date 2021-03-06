package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.StudyType;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class StudyTypeJpaDao implements StudyTypeDao{

    @PersistenceContext
    private EntityManager em;

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
    public StudyType findOrRegister(String name) {
        Optional<StudyType> studyType = this.findByName(name);

        return studyType.orElseGet(() -> this.register(name.substring(0, 1).toUpperCase() + name.substring(1)));

    }

    @Override
    public StudyType register(final String name) {
        String cleanName = StringUtils.capitalize(name.toLowerCase());
        StudyType studyType = new StudyType(cleanName);

        em.persist(studyType);
        em.flush();
        //TODO: Check success
        return studyType;
    }
}

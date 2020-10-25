package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.MedicalField;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

public class MedicalFieldJpaDao implements MedicalFieldDao{

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<MedicalField> findById(int id) {
        return Optional.ofNullable(em.find(MedicalField.class, id));
    }

    @Override
    public Optional<MedicalField> findByName(String name) {
        final TypedQuery<MedicalField> query = em.createQuery("SELECT mf FROM MedicalField mf WHERE lower(mf.name)=lower(:name)", MedicalField.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Collection<MedicalField> getAll() {
        final TypedQuery<MedicalField> query = em.createQuery("SELECT mf from MedicalField mf", MedicalField.class);
        return query.getResultList();
    }

    @Override
    public Collection<MedicalField> findByMedicId(int medic_id) {
        final TypedQuery<MedicalField> query = em.createQuery("SELECT m.medical_fields from Medic m WHERE m.user_id=:medic_id", MedicalField.class);
        query.setParameter("medic_id",medic_id);
        return query.getResultList();
    }

    @Override
    public MedicalField findOrRegister(String name) {
        return findByName(name).orElse(register(name));
    }

    private MedicalField register(String name){
        String cleanName = StringUtils.capitalize(name);
        MedicalField mf = new MedicalField(cleanName);
        em.persist(mf);
        return mf;
    }
}

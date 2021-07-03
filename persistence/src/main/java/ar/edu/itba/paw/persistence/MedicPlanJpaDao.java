package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicPlan;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

@Repository
public class MedicPlanJpaDao implements MedicPlanDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<MedicPlan> findById(int id) {
        return Optional.ofNullable(em.find(MedicPlan.class, id));
    }

    @Override
    public Optional<MedicPlan> findByName(String name) {
        final TypedQuery<MedicPlan> query = em.createQuery("SELECT mp FROM MedicPlan mp WHERE lower(mp.name)=lower(:name)", MedicPlan.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Collection<MedicPlan> getAll() {
        final TypedQuery<MedicPlan> query = em.createQuery("SELECT mp FROM MedicPlan mp", MedicPlan.class);
        return query.getResultList();
    }

    @Override
    public MedicPlan register(String name) {
        String cleanName = StringUtils.capitalize(name);
        MedicPlan mp = new MedicPlan(cleanName);
        em.persist(mp);
        em.flush();
        return mp;
    }

    @Override
    public MedicPlan findOrRegister(String name) {
        Optional<MedicPlan> maybePlan = findByName(name);

        return maybePlan.orElseGet(() -> register(name));
    }
}

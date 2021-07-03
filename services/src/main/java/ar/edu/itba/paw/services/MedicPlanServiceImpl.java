package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MedicPlan;
import ar.edu.itba.paw.persistence.MedicPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class MedicPlanServiceImpl implements MedicPlanService {

    @Autowired
    private MedicPlanDao dao;

    @Override
    public Optional<MedicPlan> findById(int id) {
        return dao.findById(id);
    }

    @Override
    public Optional<MedicPlan> findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public Collection<MedicPlan> getAll() {
        return dao.getAll();
    }

    @Override
    public MedicPlan register(String name) {
        return dao.findOrRegister(name);
    }
}

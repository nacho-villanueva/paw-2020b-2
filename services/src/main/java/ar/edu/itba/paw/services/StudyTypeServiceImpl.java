package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.persistence.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudyTypeServiceImpl implements StudyTypeService {

    @Autowired
    private StudyTypeDao studyTypeDao;

    @Override
    public Collection<StudyType> getAll() {
        return studyTypeDao.getAll();
    }

    @Override
    public Collection<StudyType> findByClinicId(int clinic_id) {
        return studyTypeDao.findByClinicId(clinic_id);
    }

    @Override
    public Optional<StudyType> findById(int id){
        return studyTypeDao.findById(id);
    }

    @Override
    public Optional<StudyType> findByName(String name) {
        return studyTypeDao.findByName(name);
    }

    @Override
    public StudyType findOrRegister(String name) {
        return studyTypeDao.findOrRegister(name);
    }

    @Override
    public StudyType register(String name) {
        return studyTypeDao.register(name);
    }

}

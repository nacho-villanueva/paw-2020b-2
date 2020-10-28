package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.persistence.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class StudyTypeServiceImpl implements StudyTypeService {

    @Autowired
    private StudyTypeDao studyTypeDao;

    @Override
    public Collection<StudyType> getAll() {
        return studyTypeDao.getAll();
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

}

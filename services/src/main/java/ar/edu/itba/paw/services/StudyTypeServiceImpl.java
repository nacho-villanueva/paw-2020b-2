package ar.edu.itba.paw.services;

import ar.edu.itba.paw.service.StudyTypeService;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.persistence.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudyTypeServiceImpl implements StudyTypeService {

    @Autowired
    StudyTypeDao studyTypeDao;

    @Override
    public Collection<StudyType> getAllStudyTypes() {
        return studyTypeDao.getAll();
    }

    @Override
    public Optional<StudyType> findById(int id){
        return studyTypeDao.findById(id);
    }

}

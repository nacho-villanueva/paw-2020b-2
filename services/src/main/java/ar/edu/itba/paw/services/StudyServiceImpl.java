package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.StudyService;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.persistence.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudyServiceImpl implements StudyService {

    @Autowired
    StudyTypeDao studyTypeDao;

    @Override
    public Collection<StudyType> getAllStudyTypes() {
        return studyTypeDao.getAll();
    }
}

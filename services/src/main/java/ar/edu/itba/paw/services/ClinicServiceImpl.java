package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.ClinicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    private ClinicDao clinicDao;

    @Override
    public Collection<Clinic> getAll() {
        return clinicDao.getAll();
    }

    @Override
    public Optional<Clinic> findByUserId(int user_id) {
        return clinicDao.findByUserId(user_id);
    }

    @Override
    public Collection<Clinic> getAllUnverified() {
        return clinicDao.getAllUnverified();
    }

    @Override
    public Clinic register(User user, String name, String email, String telephone, boolean verified, Collection<StudyType> available_studies) {
        return clinicDao.register(user,name,email,telephone,verified,available_studies);
    }

    @Override
    public StudyType registerStudyToClinic(int clinic_id, StudyType studyType) {
        return clinicDao.registerStudyToClinic(clinic_id, studyType);
    }
    @Override
    public Collection<Clinic> getByStudyTypeId(int studyType_id) {
        return clinicDao.getByStudyTypeId(studyType_id);
    }
}

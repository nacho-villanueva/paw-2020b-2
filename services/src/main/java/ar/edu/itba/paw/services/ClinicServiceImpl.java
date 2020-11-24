package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.ClinicHours;
import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.ClinicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    private ClinicDao clinicDao;

    @Autowired
    private UserService userService;

    @Override
    public Collection<Clinic> getAll() {
        return clinicDao.getAll();
    }

    @Override
    public Optional<Clinic> findByUserId(int userId) {
        return clinicDao.findByUserId(userId);
    }

    @Override
    public Collection<Clinic> getAllUnverified() {
        return clinicDao.getAllUnverified();
    }

    @Override
    public Clinic register(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours) {
        Clinic clinic = clinicDao.register(user, name, telephone, availableStudies, medicPlans, hours, false);
        userService.updateRole(user, User.CLINIC_ROLE_ID);
        return clinic;
    }

    @Override
    public Clinic updateClinicInfo(User user, String name, String telephone, Collection<StudyType> availableStudies, Set<String> medicPlans, ClinicHours hours, boolean verified) {
        return clinicDao.updateClinicInfo(user,name,telephone,availableStudies,medicPlans,hours,verified);
    }

    @Override
    public boolean hasStudy(int clinic_id, int studyType_id) {
        return clinicDao.hasStudy(clinic_id,studyType_id);
    }

    @Override
    public StudyType registerStudyToClinic(int clinic_id, StudyType studyType) {
        return clinicDao.registerStudyToClinic(clinic_id, studyType);
    }
    @Override
    public Collection<Clinic> getByStudyTypeId(int studyType_id) {
        return clinicDao.getByStudyTypeId(studyType_id);
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinic_name, ClinicHours hours, String accepted_plan, String study_name) {
        return clinicDao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);
    }

}

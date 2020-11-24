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
    public boolean hasStudy(int clinicId, int studyTypeId) {
        return clinicDao.hasStudy(clinicId,studyTypeId);
    }

    @Override
    public StudyType registerStudyToClinic(int clinicId, StudyType studyType) {
        return clinicDao.registerStudyToClinic(clinicId, studyType);
    }
    @Override
    public Collection<Clinic> getByStudyTypeId(int studyTypeId) {
        return clinicDao.getByStudyTypeId(studyTypeId);
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String study_name) {
        return clinicDao.searchClinicsBy(clinicName,hours,acceptedPlan,study_name);
    }

}

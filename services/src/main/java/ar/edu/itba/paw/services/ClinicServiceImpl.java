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

    // Pagination-related constants
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_MAX_PAGE_SIZE = 20;

    @Autowired
    private ClinicDao clinicDao;

    @Autowired
    private UserService userService;

    @Override
    public Collection<Clinic> getAll() {
        return getAll(DEFAULT_PAGE);
    }

    @Override
    public Collection<Clinic> getAll(int page) {
        return getAll(page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Clinic> getAll(int page, int pageSize) {
        return clinicDao.getAll(page, pageSize);
    }

    @Override
    public long getAllCount() {
        return clinicDao.getAllCount();
    }

    @Override
    public long getAllLastPage() {
        return getAllLastPage(DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllLastPage(int pageSize) {
        return getLastPage(getAllCount(),pageSize);
    }

    @Override
    public Optional<Clinic> findByUserId(int userId) {
        return clinicDao.findByUserId(userId);
    }

    @Override
    public Collection<Clinic> getAllUnverified() {
        return getAllUnverified(DEFAULT_PAGE);
    }

    @Override
    public Collection<Clinic> getAllUnverified(int page) {
        return getAllUnverified(page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Clinic> getAllUnverified(int page, int pageSize) {
        return clinicDao.getAllUnverified(page,pageSize);
    }

    @Override
    public long getAllUnverifiedCount() {
        return clinicDao.getAllUnverifiedCount();
    }

    @Override
    public long getAllUnverifiedLastPage() {
        return getAllUnverifiedLastPage(DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getAllUnverifiedLastPage(int pageSize) {
        return getLastPage(getAllUnverifiedCount(),pageSize);
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
        return getByStudyTypeId(studyTypeId,DEFAULT_PAGE);
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(int studyTypeId, int page) {
        return getByStudyTypeId(studyTypeId,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(int studyTypeId, int page, int pageSize) {
        return clinicDao.getByStudyTypeId(studyTypeId,page,pageSize);
    }

    @Override
    public long getByStudyTypeIdCount(int studyTypeId) {
        return clinicDao.getByStudyTypeIdCount(studyTypeId);
    }

    @Override
    public long getByStudyTypeIdLastPage(int studyTypeId) {
        return getByStudyTypeIdLastPage(studyTypeId,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long getByStudyTypeIdLastPage(int studyTypeId, int pageSize) {
        return getLastPage(getByStudyTypeIdCount(studyTypeId),pageSize);
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        return searchClinicsBy(clinicName,hours,acceptedPlan,studyName,DEFAULT_PAGE);
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int page) {
        return searchClinicsBy(clinicName,hours,acceptedPlan,studyName,page,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public Collection<Clinic> searchClinicsBy(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int page, int pageSize) {
        return clinicDao.searchClinicsBy(clinicName,hours,acceptedPlan,studyName,page,pageSize);
    }

    @Override
    public long searchClinicsByCount(String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        return clinicDao.searchClinicsByCount(clinicName,hours,acceptedPlan,studyName);
    }

    @Override
    public long searchClinicsByLastPage(String clinicName, ClinicHours hours, String acceptedPlan, String studyName) {
        return searchClinicsByLastPage(clinicName,hours,acceptedPlan,studyName,DEFAULT_MAX_PAGE_SIZE);
    }

    @Override
    public long searchClinicsByLastPage(String clinicName, ClinicHours hours, String acceptedPlan, String studyName, int pageSize) {
        return getLastPage(searchClinicsByCount(clinicName,hours,acceptedPlan,studyName),pageSize);
    }

    // auxiliar functions
    private long getLastPage(final long count, final int pageSize){
        if(count <= 0)
            return 0;

        return (count / pageSize)+1;
    }
}

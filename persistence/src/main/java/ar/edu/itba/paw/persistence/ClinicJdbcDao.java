package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.ClinicHours;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.*;

@Repository
public class ClinicJdbcDao implements ClinicDao {

    private static class DayHours {
        private int day_of_week;
        private Time open_time;
        private Time close_time;

        public DayHours(final int day_of_week, final Time open_time, final Time close_time) {
            this.day_of_week = day_of_week;
            this.open_time = open_time;
            this.close_time = close_time;
        }

        public int getDay_of_week() {
            return day_of_week;
        }

        public Time getOpen_time() {
            return open_time;
        }

        public Time getClose_time() {
            return close_time;
        }
    }

    @Autowired
    private UserDao userDao;

    private static final RowMapper<Clinic> CLINIC_ROW_MAPPER = (rs, rowNum) ->
            new Clinic(rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getBoolean("verified"));

    private static final RowMapper<String> PLAN_ROW_MAPPER = (rs, rowNum) ->
            rs.getString("medic_plan");

    private static final RowMapper<DayHours> DAY_HOURS_ROW_MAPPER = (rs, rowNum) ->
            new DayHours(rs.getInt("day_of_week"), rs.getTime("open_time"), rs.getTime("close_time"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertClinic;
    private final SimpleJdbcInsert jdbcInsertStudies;
    private final SimpleJdbcInsert jdbcInsertPlans;
    private final SimpleJdbcInsert jdbcInsertHours;

    @Autowired
    private StudyTypeDao studyTypeDao;

    @Autowired
    public ClinicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertClinic = new SimpleJdbcInsert(ds)
                .withTableName("clinics");
        jdbcInsertStudies = new SimpleJdbcInsert(ds)
                .withTableName("clinic_available_studies");
        jdbcInsertPlans = new SimpleJdbcInsert(ds)
                .withTableName("clinic_accepted_plans");
        jdbcInsertHours = new SimpleJdbcInsert(ds)
                .withTableName("clinic_hours");
    }

    @Override
    public Optional<Clinic> findByUserId(int user_id) {
        //We get the basic clinic info
        Optional<Clinic> clinic = jdbcTemplate.query("SELECT * FROM clinics INNER JOIN users u ON user_id = u.id WHERE u.id = ?", new Object[] { user_id }, CLINIC_ROW_MAPPER).stream().findFirst();
        clinic.ifPresent(this::loadClinicInfo);
        return clinic;
    }

    @Override
    public Collection<Clinic> getAll() {
        return getAll(true);
    }

    @Override
    public Collection<Clinic> getAllUnverified() {
        return getAll(false);
    }

    private Collection<Clinic> getAll(final boolean verified) {
        Collection<Clinic> clinics = jdbcTemplate.query("SELECT * FROM clinics INNER JOIN users u ON user_id = u.id WHERE verified = ?", new Object[]{verified}, CLINIC_ROW_MAPPER);
        clinics.forEach(this::loadClinicInfo);
        return clinics;
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(final int studyType_id) {
        Collection<Clinic> clinics = jdbcTemplate.query("SELECT c.user_id, c.name, u.email, telephone, verified FROM clinics c" +
                " INNER JOIN users u" +
                " ON c.user_id = u.id" +
                " INNER JOIN clinic_available_studies cs " +
                " ON clinic_id = c.user_id" +
                " INNER JOIN medical_studies s" +
                " ON study_id = s.id AND s.id = ? WHERE c.verified = true", new Object[]{studyType_id}, CLINIC_ROW_MAPPER);

        clinics.forEach(this::loadClinicInfo);

        return clinics;
    }

    private void loadClinicInfo(Clinic clinic) {
        //Load available studies
        clinic.setMedical_studies(studyTypeDao.findByClinicId(clinic.getUser_id()));

        //Load medical plans
        clinic.setAccepted_plans(getAcceptedPlans(clinic.getUser_id()));

        //Load clinic hours
        clinic.setHours(getClinicHours(clinic.getUser_id()));
    }

    private ClinicHours getClinicHours(int clinic_id) {
        ClinicHours clinicHours = new ClinicHours();

        Collection<DayHours> clinicDayHours = jdbcTemplate.query("SELECT * FROM clinic_hours WHERE clinic_id = ?", new Object[]{clinic_id},DAY_HOURS_ROW_MAPPER);

        clinicDayHours.forEach(day -> {
            clinicHours.setDayHour(day.getDay_of_week(),day.getOpen_time(),day.getClose_time());
        });

        return clinicHours;
    }

    private Set<String> getAcceptedPlans(int clinic_id) {
        Collection<String> plans = jdbcTemplate.query("SELECT * FROM clinic_accepted_plans WHERE clinic_id = ?", new Object[]{clinic_id}, PLAN_ROW_MAPPER);

        return new HashSet<>(plans);
    }

    //TODO: when moving to aspect oriented programming make this function transactional
    @Override
    public Clinic register(final User user, final String name, final String telephone, final Collection<StudyType> available_studies, final Set<String> medic_plans, final ClinicHours hours, final boolean verified) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user.getId());
        insertMap.put("name", name);
        insertMap.put("email", user.getEmail());
        insertMap.put("telephone", telephone);
        insertMap.put("verified", verified);

        jdbcInsertClinic.execute(insertMap);
        //Todo: Check success

        Collection<StudyType> available_studiesDB = registerStudiesToClinic(available_studies,user.getId());

        //Add plans to database
        registerMedicPlans(user.getId(), medic_plans);

        //Add hours to database
        registerHours(user.getId(), hours);

        userDao.updateRole(user, User.CLINIC_ROLE_ID);

        return new Clinic(user.getId(),name,user.getEmail(),telephone,available_studiesDB,hours,medic_plans,false);
    }

    private void registerHours(int clinic_id, ClinicHours hours) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id", clinic_id);

        for(int i = 0; i < hours.getDays().length; i++) {
            if(hours.getDays()[i]) {
                insertMap.put("day_of_week",i);
                insertMap.put("open_time",hours.getOpen_hours()[i]);
                insertMap.put("close_time",hours.getClose_hours()[i]);
                jdbcInsertHours.execute(insertMap);
            }
        }
    }

    private void registerMedicPlans(int clinic_id, Set<String> medic_plans) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id", clinic_id);

        medic_plans.forEach(plan -> {
            insertMap.put("medic_plan", plan);
            jdbcInsertPlans.execute(insertMap);
        });
    }

    @Override
    public Clinic updateClinicInfo(final User user, final String name, final String telephone, final Collection<StudyType> available_studies, final Set<String> medic_plans, final ClinicHours hours, final boolean verified) {
        jdbcTemplate.update("UPDATE clinics Set name = ?, telephone = ?, verified = ? WHERE user_id = ?", name, telephone, verified, user.getId());

        Collection<StudyType> available_studiesDB = registerStudiesToClinic(available_studies,user.getId());

        updatePlans(user.getId(),medic_plans);

        updateHours(user.getId(),hours);

        return new Clinic(user.getId(),name,user.getEmail(),telephone,available_studiesDB,hours,medic_plans,verified);
    }

    private void updateHours(int clinic_id, ClinicHours hours) {
        jdbcTemplate.update("DELETE FROM clinic_hours WHERE clinic_id = ?",clinic_id);

        registerHours(clinic_id,hours);
    }

    private void updatePlans(int clinic_id, Set<String> medic_plans) {
        //We get rid of old values
        jdbcTemplate.update("DELETE FROM clinic_accepted_plans WHERE clinic_id = ?",clinic_id);

        registerMedicPlans(clinic_id,medic_plans);
    }

    @Override
    public boolean hasStudy(final int clinic_id, final int studyType_id) {
        Integer cnt = jdbcTemplate.queryForObject("SELECT count(*) FROM clinic_available_studies WHERE clinic_id = ? AND study_id = ?", Integer.class, clinic_id, studyType_id);

        return cnt != null && cnt > 0;
    }

    @Override
    public StudyType registerStudyToClinic(final int clinic_id, final StudyType studyType) {
        //We check if it exists
        StudyType studyTypeFromDB = studyTypeDao.findOrRegister(studyType.getName());

        //We check if it was already registered
        if(!hasStudy(clinic_id, studyTypeFromDB.getId())) {
            //Now that we made sure it exist and it is not registered already, we add the relation
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("clinic_id", clinic_id);
            insertMap.put("study_id", studyTypeFromDB.getId());

            jdbcInsertStudies.execute(insertMap);
        }

        //Todo: verify success

        return studyTypeFromDB;
    }

    private Collection<StudyType> registerStudiesToClinic(final Collection<StudyType> available_studies, final int clinic_id) {
        Collection<StudyType> old_studies = studyTypeDao.findByClinicId(clinic_id);
        Collection<StudyType> available_studiesDB = new ArrayList<>();

        available_studies.forEach(studyType -> {
            StudyType studyTypeFromDB = this.registerStudyToClinic(clinic_id, studyType);
            available_studiesDB.add(studyTypeFromDB);
        });
        
        //We delete the ones that are not in the new list but are still on database
        old_studies.forEach(studyType -> {
            if(!available_studiesDB.contains(studyType)) {
                unregisterStudyToClinic(clinic_id,studyType.getId());
            }
        });

        return available_studiesDB;
    }

    private void unregisterStudyToClinic(int clinic_id, int study_id) {
        jdbcTemplate.update("DELETE FROM clinic_available_studies WHERE clinic_id = ? AND study_id = ?", clinic_id, study_id);
    }
}

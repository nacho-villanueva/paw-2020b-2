package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.*;

@Repository
public class OrderJdbcDao implements OrderDao {

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) ->
            new Order(rs.getLong("order_id"),
                    new Medic(rs.getInt("medic_id"),
                            rs.getString("medic_name"),
                            rs.getString("medic_email"),
                            rs.getString("medic_telephone"),
                            rs.getString("medic_licence_number")),
                    rs.getDate("date"),
                    new Clinic(rs.getInt("clinic_id"),
                            rs.getString("clinic_name"),
                            rs.getString("clinic_email"),
                            rs.getString("clinic_telephone")),
                    new StudyType(rs.getInt("study_id"),
                            rs.getString("study_name")),
                    rs.getString("description"),
                    rs.getString("identification_type"),
                    rs.getBytes("identification"),
                    rs.getString("medic_plan"),
                    rs.getString("medic_plan_number"),
                    new Patient(rs.getInt("patient_id"),
                            rs.getString("patient_name"),
                            rs.getString("patient_email")));

   @Autowired
   ResultDao resultDao;

   @Autowired
   PatientDao patientDao;

   private JdbcTemplate jdbcTemplate;
   private SimpleJdbcInsert jdbcInsert;

   @Autowired
   public OrderJdbcDao(DataSource ds) {
       jdbcTemplate = new JdbcTemplate(ds);
       jdbcInsert = new SimpleJdbcInsert(ds)
               .withTableName("medical_orders")
               .usingGeneratedKeyColumns("id");
   }

   @Override
   public Optional<Order> findById(long id) {

       //To make code less confusing, we name the sql query
       String sqlQuery = "select order_id, medic_id, medic_name, medic_email, medic_telephone, medic_licence_number, patient_id, patient_name, patient_email, clinic_id, clinic_name, clinic_email, clinic_telephone, medical_studies.name as study_name, medical_studies.id as study_id, date, description, identification_type, identification, medic_plan, medic_plan_number from " +
               "(select clinics.id as clinic_id, clinics.name as clinic_name, clinics.email as clinic_email, clinics.telephone as clinic_telephone, * from " +
               "(select medics.id as medic_id, medics.name as medic_name, medics.email as medic_email, medics.telephone as medic_telephone, medics.licence_number as medic_licence_number, * from " +
               "(select medical_orders.id as order_id, patients.name as patient_name, patients.email as patient_email, patients.id as patient_id, * from " +
               "medical_orders join patients " +
               "on patient_id = patients.id and medical_orders.id = ?) as order_patient join medics " +
               "on medic_id = medics.id) as order_medic join clinics " +
               "on clinic_id = clinics.id) as order_clinic join medical_studies " +
               "on study_id = medical_studies.id";

       Optional<Order> order = jdbcTemplate.query(sqlQuery, new Object[]{ id }, ORDER_ROW_MAPPER).stream().findFirst();

       if(order.isPresent()) {
           Collection<Result> results = resultDao.findByOrderId(id);

           if(!results.isEmpty()) {
               order.get().setStudy_results(results);
           }
       }

       return order;
   }

   @Override
   public Order register(final Medic medic, final Date date, final Clinic clinic, final Patient patient, final StudyType studyType, final String description, final String identification_type, final byte[] identification, final String medic_plan, final String medic_plan_number) {
       //When creating an order, all the info is guaranteed to already exist (since they are choosing from the list of available info) except for patient info, so lets see if patient exists already or not
       Patient patientFromDB = patientDao.findOrRegister(patient.getEmail(), patient.getName());

       Map<String, Object> insertMap = new HashMap<>();
       insertMap.put("medic_id", medic.getId());
       insertMap.put("date", date);
       insertMap.put("clinic_id", clinic.getId());
       insertMap.put("patient_id", patientFromDB.getId());
       insertMap.put("study_id", studyType.getId());
       insertMap.put("description", description);
       insertMap.put("identification_type", identification_type);
       insertMap.put("identification", identification);
       insertMap.put("medic_plan", medic_plan);
       insertMap.put("medic_plan_number", medic_plan_number);

       Number key = jdbcInsert.executeAndReturnKey(insertMap);

       //Todo: check success

       return new Order(key.longValue(),medic, date, clinic, studyType,description,identification_type,identification,medic_plan,medic_plan_number,patientFromDB);
   }
}

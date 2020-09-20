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
                            rs.getString("licence_number")),
                    rs.getDate("date"),
                    new Clinic(rs.getInt("clinic_id"),
                            rs.getString("clinic_name"),
                            rs.getString("clinic_email")),
                    new StudyType(rs.getInt("study_id"),rs.getString("study_name")),
                    rs.getString("description"),
                    rs.getString("identification_type"),
                    rs.getBytes("identification"),
                    rs.getString("medic_plan"),rs.getString("medic_plan_number"),
                    rs.getString("patient_email"),rs.getString("patient_name"));

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
       String sqlQuery = "select o.id as order_id, date, patient_email, patient_name, medic_plan, medic_plan_number, identification_type, identification, medic_id, m.name as medic_name, m.email as medic_email, licence_number, clinic_id, c.name as clinic_name, c.email as clinic_email, study_id, s.name as study_name, description from medical_orders o inner join medics m on o.medic_id = m.user_id inner join clinics c on o.clinic_id = c.user_id inner join medical_studies s on o.study_id = s.id where o.id = ?" ;

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
   public Order register(final Medic medic, final Date date, final Clinic clinic, final String patient_name, final String patient_email, final StudyType studyType, final String description, final String identification_type, final byte[] identification, final String medic_plan, final String medic_plan_number) {
       Map<String, Object> insertMap = new HashMap<>();
       insertMap.put("medic_id", medic.getUser_id());
       insertMap.put("date", date);
       insertMap.put("clinic_id", clinic.getUser_id());
       insertMap.put("patient_name", patient_name);
       insertMap.put("patient_email", patient_email);
       insertMap.put("study_id", studyType.getId());
       insertMap.put("description", description);
       insertMap.put("identification_type", identification_type);
       insertMap.put("identification", identification);
       insertMap.put("medic_plan", medic_plan);
       insertMap.put("medic_plan_number", medic_plan_number);

       Number key = jdbcInsert.executeAndReturnKey(insertMap);

       //Todo: check success

       return new Order(key.longValue(),medic,date,clinic,studyType,description,identification_type,identification,medic_plan,medic_plan_number,patient_email,patient_name);
   }
}

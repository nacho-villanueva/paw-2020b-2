package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

//@Repository
public class MedicJpaDao implements MedicDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MedicalFieldDao medicalFieldDao;

    @Override
    public Optional<Medic> findByUserId(int user_id) {
        return Optional.ofNullable(em.find(Medic.class,user_id));
    }

    @Override
    public Collection<Medic> getAll() {
        return getAll(true);
    }

    @Override
    public Collection<Medic> getAllUnverified() {
        return getAll(false);
    }

    private Collection<Medic> getAll(final boolean verified) {
        // with hibernate, the validation that a medic is associated with an user should be unnecesary
        final TypedQuery<Medic> query = em.createQuery("SELECT m FROM Medic m WHERE m.verified = :isVerified",Medic.class);
        query.setParameter("isVerified",String.valueOf(verified));
        return query.getResultList();
    }

    @Override
    public Medic register(final User user, final String name, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final Collection<MedicalField> known_fields, final boolean verified) {

        final Medic medic = new Medic(user,name,telephone,identification_type,identification,licence_number,verified,known_fields);

        userDao.updateRole(user, User.MEDIC_ROLE_ID);

        em.persist(medic);

        registerFieldsToMedic(known_fields,medic.getUser_id());

        return medic;
    }

    @Override
    public Medic updateMedicInfo(User user, final String name, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final Collection<MedicalField> known_fields, final boolean verified) {

        registerFieldsToMedic(known_fields,user.getId());

        Optional<Medic> medicOptional = findByUserId(user.getId());

        Medic medic = new Medic(user.getId(),user.getEmail(),name,telephone,identification_type,identification,licence_number,verified,known_fields);

        if(medicOptional.isPresent()){
            medic = medicOptional.get();

            //TODO check it works
            em.detach(medic);

            medic.setUser(user);
            medic.setName(name);
            medic.setTelephone(telephone);
            medic.setIdentification_type(identification_type);
            medic.setIdentification(identification);
            medic.setLicence_number(licence_number);
            medic.setVerified(verified);

            em.getTransaction().begin();
            em.merge(medic);
            em.getTransaction().commit();
        }
        registerFieldsToMedic(known_fields,user.getId());

        //Todo: Verify success

        return medic;
    }

    @Override
    public boolean knowsField(int medic_id, int field_id) {
        Optional<Medic> medicOptional = findByUserId(medic_id);
        Optional <MedicalField> medicalFieldOptional = medicalFieldDao.findById(field_id);

        return medicOptional.isPresent() && medicalFieldOptional.isPresent() && medicOptional.get().getMedical_fields().contains(medicalFieldOptional.get());
    }

    @Override
    public MedicalField registerFieldToMedic(final int medic_id, MedicalField medicalField) {
        //We check if it exists
        MedicalField medicalFieldFromDB = medicalFieldDao.findOrRegister(medicalField.getName());

        Optional<Medic> medicOptional = findByUserId(medic_id);

        if(medicOptional.isPresent()){
            Medic medic = medicOptional.get();

            //TODO check it works
            em.detach(medic);
            medic.getMedical_fields().add(medicalField);
            em.getTransaction().begin();
            em.merge(medic);
            em.getTransaction().commit();
        }

        //Todo: Verify success

        return medicalFieldFromDB;
    }

    private void registerFieldsToMedic(final Collection<MedicalField> known_fields, final int medic_id) {

        Collection<MedicalField> old_fields = medicalFieldDao.findByMedicId(medic_id);
        Collection<MedicalField> known_fieldsDB = new ArrayList<>();

        known_fields.forEach(medicalField -> {
            MedicalField medicalFieldDB = this.registerFieldToMedic(medic_id, medicalField);
            known_fieldsDB.add(medicalFieldDB);
        });

        old_fields.forEach(field -> {
            if(!known_fieldsDB.contains(field)) {
                unregisterFieldToMedic(medic_id,field.getId());
            }
        });

        return;
    }

    private void unregisterFieldToMedic(int medic_id, int field_id) {

        Optional<Medic> medicOptional = findByUserId(medic_id);
        Optional<MedicalField> medicalFieldOptional = medicalFieldDao.findById(field_id);

        if(medicOptional.isPresent() && medicalFieldOptional.isPresent()){
            Medic medic = medicOptional.get();
            MedicalField medicalField = medicalFieldOptional.get();

            //TODO check it works
            em.detach(medic);
            medic.getMedical_fields().remove(medicalField);
            em.getTransaction().begin();
            em.merge(medic);
            em.getTransaction().commit();

        }

        return;
    }
}

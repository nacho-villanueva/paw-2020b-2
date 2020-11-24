package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class MedicJpaDao implements MedicDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MedicalFieldDao medicalFieldDao;

    @Override
    public Optional<Medic> findByUserId(int userId) {
        return Optional.ofNullable(em.find(Medic.class,userId));
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
        query.setParameter("isVerified",verified);
        return query.getResultList();
    }

    @Override
    public Medic register(final User user, final String name, final String telephone, final String identificationType, final byte[] identification, final String licenceNumber, final Collection<MedicalField> knownFields, final boolean verified) {
        //Getting user reference
        User userRef = em.getReference(User.class,user.getId());

        //Getting field references
        Collection<MedicalField> fieldsRef = new HashSet<>();
        knownFields.forEach(medicalField -> {
            MedicalField fieldRef = getFieldRef(medicalField);
            fieldsRef.add(fieldRef);
        });
        final Medic medic = new Medic(userRef,name,telephone,identificationType,identification,licenceNumber,verified,fieldsRef);
        em.persist(medic);
        em.flush();
        return medic;
    }

    private MedicalField getFieldRef(MedicalField medicalField) {
        MedicalField retRef;
        if(medicalField.getId() != null) {
            retRef = em.getReference(MedicalField.class, medicalField.getId());
            if(retRef != null) {
                return retRef;
            }
        }

        //If field does not exists we try and create it then add its reference that we know it wont fail (in theory)
        MedicalField newField = medicalFieldDao.findOrRegister(medicalField.getName());
        return em.getReference(MedicalField.class,newField.getId());

    }

    @Override
    public Medic updateMedicInfo(User user, final String name, final String telephone, final String identificationType, final byte[] identification, final String licenceNumber, final Collection<MedicalField> knownFields, final boolean verified) {

        Optional<Medic> medicDB = findByUserId(user.getId());

        medicDB.ifPresent(medic -> {
            medic.setName(name);
            medic.setTelephone(telephone);
            medic.setIdentificationType(identificationType);
            medic.setIdentification(identification);
            medic.setLicenceNumber(licenceNumber);
            medic.setVerified(verified);
            Collection<MedicalField> fieldsRef = new HashSet<>();
            knownFields.forEach(medicalField -> {
                fieldsRef.add(getFieldRef(medicalField));
            });
            medic.setMedical_fields(fieldsRef);
            em.flush();
        });

        return medicDB.orElse(null);
    }

    @Override
    public boolean knowsField(int medic_id, int field_id) {
        Optional<Medic> medicDB = findByUserId(medic_id);

        //No medic, false
        if(!medicDB.isPresent()) {
            return false;
        }

        Optional<MedicalField> medicalFieldDB = Optional.ofNullable(em.find(MedicalField.class,field_id));

        return medicalFieldDB.filter(medicalField -> medicDB.get().getMedical_fields().contains(medicalField)).isPresent();
    }

    @Override
    public MedicalField registerFieldToMedic(final int medic_id, MedicalField medicalField) {

        Optional<Medic> medicDB = findByUserId(medic_id);

        if(medicDB.isPresent()) {
            MedicalField medicalFieldDB = medicalFieldDao.findOrRegister(medicalField.getName());
            medicDB.get().getMedical_fields().add(medicalFieldDB);
            em.flush();
            return medicalFieldDB;
        }

        return null;
    }
}

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

import static org.springframework.util.StringUtils.isEmpty;

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
    public Collection<Medic> getAll(final int page, final int pageSize) {
        return getAll(true,page,pageSize);
    }

    @Override
    public long getAllCount() {
        return getAllCount(true);
    }

    @Override
    public Collection<Medic> getAllUnverified(final int page, final int pageSize) {
        return getAll(false,page,pageSize);
    }

    @Override
    public long getAllUnverifiedCount() {
        return getAllCount(false);
    }

    private Collection<Medic> getAll(final boolean verified, final int page, final int pageSize) {

        if(pageSize <= 0 || page <= 0)
            return null;

        String queryString = "SELECT m FROM Medic m "+
                "WHERE m.verified = :isVerified " +
                "ORDER BY m.name ASC, m.user.id ASC";


        final TypedQuery<Medic> query = em.createQuery(queryString,Medic.class);
        query.setParameter("isVerified",verified);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    private Long getAllCount(final boolean verified){

        final String queryString = "SELECT COUNT(m) FROM Medic m " +
                "WHERE m.verified = :isVerified";

        final TypedQuery<Long> countQuery = em.createQuery(queryString,Long.class);
        countQuery.setParameter("isVerified",verified);
        return countQuery.getSingleResult();
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

    @Override
    public void verifyMedic(int medicId) {
        Optional<Medic> medicDB = findByUserId(medicId);

        medicDB.ifPresent(medic -> {
            if(!medic.isVerified())
                medic.setVerified(true);
            em.flush();
        });
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
    public Medic updateMedicInfo(User user, final String name, final String telephone, final String identificationType, final byte[] identification, final String licenceNumber, final Collection<MedicalField> knownFields) {

        Optional<Medic> medicDB = findByUserId(user.getId());

        medicDB.ifPresent(medic -> {
            if(!isEmpty(name))
                medic.setName(name);
            medic.setTelephone(telephone);
            if(!isEmpty(identificationType))
                medic.setIdentificationType(identificationType);
            if(identification != null)
                medic.setIdentification(identification);
            if(!isEmpty(licenceNumber))
                medic.setLicenceNumber(licenceNumber);
            if(knownFields != null) {
                Collection<MedicalField> fieldsRef = new HashSet<>();
                knownFields.forEach(medicalField -> {
                    fieldsRef.add(getFieldRef(medicalField));
                });
                medic.setMedicalFields(fieldsRef);
            }
            em.flush();
        });

        return medicDB.orElse(null);
    }

    @Override
    public boolean knowsField(int medicId, int fieldId) {
        Optional<Medic> medicDB = findByUserId(medicId);

        //No medic, false
        if(!medicDB.isPresent()) {
            return false;
        }

        Optional<MedicalField> medicalFieldDB = Optional.ofNullable(em.find(MedicalField.class,fieldId));

        return medicalFieldDB.filter(medicalField -> medicDB.get().getMedicalFields().contains(medicalField)).isPresent();
    }

    @Override
    public MedicalField registerFieldToMedic(final int medicId, MedicalField medicalField) {

        Optional<Medic> medicDB = findByUserId(medicId);

        if(medicDB.isPresent()) {
            MedicalField medicalFieldDB = medicalFieldDao.findOrRegister(medicalField.getName());
            medicDB.get().getMedicalFields().add(medicalFieldDB);
            em.flush();
            return medicalFieldDB;
        }

        return null;
    }
}

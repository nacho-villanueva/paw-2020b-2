package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.ShareRequest;
import ar.edu.itba.paw.models.ShareRequestId;
import ar.edu.itba.paw.models.StudyType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

@Repository
public class ShareRequestJpaDao implements ShareRequestDao{

    @PersistenceContext
    EntityManager em;

    @Override
    public ShareRequest register(Medic medic, String patientEmail, StudyType type) {
        Optional<ShareRequest> maybeShareRequest = getShareRequest(medic, patientEmail, type);
        if (maybeShareRequest.isPresent())
            return maybeShareRequest.get();

        ShareRequest request = new ShareRequest(medic, patientEmail, type);
        em.persist(request);
        em.flush();
        return request;
    }

    @Override
    public void remove(ShareRequest shareRequest) {

        if(shareRequest.getMedic() == null || shareRequest.getMedic().getUser() == null || shareRequest.getMedic().getUser().getId() == null ||
                shareRequest.getStudyType() == null || shareRequest.getStudyType().getId() == null){
            return;
        }

        ShareRequestId shareRequestId = new ShareRequestId(shareRequest.getMedic().getUser().getId(),shareRequest.getPatientEmail(),shareRequest.getStudyType().getId());

        ShareRequest request = em.getReference(ShareRequest.class, shareRequestId);
        em.remove(request);
        em.flush();
    }

    @Override
    public Collection<ShareRequest> getAllPatientRequests(String patientEmail){
        final TypedQuery<ShareRequest> query = em.createQuery("SELECT sr FROM ShareRequest sr WHERE sr.patientEmail = :email", ShareRequest.class);
        query.setParameter("email", patientEmail);
        return query.getResultList();
    }

    @Override
    public Optional<ShareRequest> getShareRequest(Medic medic, String patientEmail, StudyType type) {
        final TypedQuery<ShareRequest> query = em.createQuery("SELECT sr FROM ShareRequest sr WHERE sr.patientEmail = :email AND sr.medic = :medic AND sr.studyType = :type", ShareRequest.class);
        query.setParameter("email", patientEmail);
        query.setParameter("medic", medic);
        query.setParameter("type", type);
        return query.getResultList().stream().findFirst();
    }
}
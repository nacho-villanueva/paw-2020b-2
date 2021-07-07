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
    public Collection<ShareRequest> getAllPatientRequests(String patientEmail, int page, int pageSize) {

        String queryString = "SELECT sr FROM ShareRequest sr " +
                "WHERE sr.patientEmail = :email " +
                "ORDER BY sr.studyType.id ASC, sr.medic.user.id ASC";

        final TypedQuery<ShareRequest> query = em.createQuery(queryString, ShareRequest.class);
        query.setParameter("email", patientEmail);

        query.setFirstResult((page-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long getAllPatientRequestsCount(String patientEmail) {

        String queryString = "SELECT COUNT(sr) FROM ShareRequest sr " +
                "WHERE sr.patientEmail = :email";

        final TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("email", patientEmail);

        return query.getSingleResult();
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

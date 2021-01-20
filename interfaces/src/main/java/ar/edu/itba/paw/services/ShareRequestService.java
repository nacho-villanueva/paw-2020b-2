package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.ShareRequest;
import ar.edu.itba.paw.models.StudyType;

import java.util.Collection;
import java.util.Optional;

public interface ShareRequestService {

    ShareRequest requestShare(Medic medic, String patientEmail, StudyType type);

    Optional<ShareRequest> getShareRequest(Medic medic, String patientEmail, StudyType type);

    void acceptOrDenyShare(ShareRequest request, boolean accepted);

    //TODO: deprecated, remove usages when possible
    Collection<ShareRequest> getAllPatientRequest(String patientEmail);

    Collection<ShareRequest> getAllPatientRequests(String patientEmail, int page);

    Collection<ShareRequest> getAllPatientRequests(String patientEmail, int page, int pageSize);

    long getAllPatientRequestsCount(String patientEmail);

    long getAllPatientRequestsLastPage(String patientEmail);

    long getAllPatientRequestsLastPage(String patientEmail, int pageSize);
}

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

    Collection<ShareRequest> getAllPatientRequest(String patientEmail);
}

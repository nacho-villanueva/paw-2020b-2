package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.ShareRequest;
import ar.edu.itba.paw.models.StudyType;

import java.util.Collection;

public interface ShareRequestDao {

    ShareRequest register(Medic medic, String patientEmail, StudyType type);

    void remove(ShareRequest shareRequest);

    Collection<ShareRequest> getAllPatientRequests(String patientEmail);
}

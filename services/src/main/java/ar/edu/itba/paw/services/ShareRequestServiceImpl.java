package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.ShareRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ShareRequestServiceImpl implements ShareRequestService{

    @Autowired
    ShareRequestDao srd;

    @Autowired
    OrderService os;

    @Autowired
    MedicService ms;

    @Override
    public ShareRequest requestShare(Medic medic, String patientEmail, StudyType type) {
        return srd.register(medic, patientEmail, type);
    }

    @Override
    public void acceptOrDenyShare(ShareRequest request, boolean accepted) {
        if(accepted){
            final Collection<Order> orders = os.getAllAsPatientOfType(request.getPatientEmail(), request.getStudyType());
            Optional<Medic> maybeMedic = ms.findByUserId(request.getMedic().getUser_id());
            if(maybeMedic.isPresent()) {
                for (Order o : orders) {
                    os.shareWithMedic(o, maybeMedic.get().getUser());
                }
            }
        }
        srd.remove(request);
    }

    @Override
    public Collection<ShareRequest> getAllPatientRequest(String patientEmail){
        return srd.getAllPatientRequests(patientEmail);
    }
}

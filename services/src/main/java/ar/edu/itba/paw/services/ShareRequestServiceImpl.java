package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.ShareRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Primary
@Service
public class ShareRequestServiceImpl implements ShareRequestService{

    @Autowired
    ShareRequestDao srd;

    @Autowired
    UserService us;

    @Autowired
    OrderService os;

    @Autowired
    MedicService ms;

    @Autowired
    MailNotificationService mns;

    @Transactional
    @Override
    public ShareRequest requestShare(Medic medic, String patientEmail, StudyType type) {
        ShareRequest shareRequest = srd.register(medic, patientEmail, type);
        mns.sendShareRequestMail(shareRequest);
        return shareRequest;
    }

    @Transactional
    @Override
    public Optional<ShareRequest> getShareRequest(Medic medic, String patientEmail, StudyType type){
        return srd.getShareRequest(medic, patientEmail, type);
    }

    @Transactional
    @Override
    public void acceptOrDenyShare(ShareRequest request, boolean accepted) {

        int PAGE_SIZE = 20;

        if(accepted){
            Collection<Order> orders;
            int ordersLastPage = (int) os.getAllAsPatientOfTypeLastPage(request.getPatientEmail(), request.getStudyType(), PAGE_SIZE);
            Optional<Medic> maybeMedic = ms.findByUserId(request.getMedic().getUser().getId());
            if(maybeMedic.isPresent()) {
                for (int page=1; page<=ordersLastPage; page++){
                    orders = os.getAllAsPatientOfType(request.getPatientEmail(), request.getStudyType(), page, PAGE_SIZE);
                    for (Order o : orders) {
                        os.shareWithMedic(o, maybeMedic.get().getUser());
                    }
                }
            }
            mns.sendAcceptRequestMail(request);
        }else{
            mns.sendDenyRequestMail(request);
        }
        srd.remove(request);
    }

    @Override
    public Collection<ShareRequest> getAllPatientRequests(String patientEmail, int page, int pageSize) {
        return srd.getAllPatientRequests(patientEmail, page, pageSize);
    }

    @Override
    public long getAllPatientRequestsCount(String patientEmail) {
        return srd.getAllPatientRequestsCount(patientEmail);
    }

    @Override
    public long getAllPatientRequestsLastPage(String patientEmail, int pageSize) {
        return getLastPage(getAllPatientRequestsCount(patientEmail),pageSize);
    }

    // auxiliar functions
    private long getLastPage(final long count, final int pageSize){
        return (long) Math.ceil((double)count / pageSize);
    }
}

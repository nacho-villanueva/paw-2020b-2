package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media;
import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private MedicService medicService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResultService resultService;

    @Override
    public Optional<Media> getMedicImage(int medicId, String attribute) {

        Optional<Media> ret = Optional.empty();

        Optional<Medic> medicOptional = medicService.findByUserId(medicId);

        if(medicOptional.isPresent()){

            Medic medic = medicOptional.get();

            Media image = new Media();

            switch (attribute){
                case "identification":
                    image.setName("medicIdentification");
                    image.setContentType(medic.getIdentificationType());
                    image.setFile(medic.getIdentification());
                    ret = Optional.of(image);
                    break;
            }
        }

        return ret;
    }

    @Override
    public Optional<Media> getOrderImage(long orderId, String attribute) {

        Optional<Media> ret = Optional.empty();

        Optional<Order> orderOptional = orderService.findById(orderId);

        if(orderOptional.isPresent()){

            Order order = orderOptional.get();

            Media image = new Media();

            switch (attribute){
                case "identification":
                    image.setName("orderIdentification");
                    image.setContentType(order.getIdentificationType());
                    image.setFile(order.getIdentification());
                    ret = Optional.of(image);
                    break;
            }
        }

        return ret;
    }

    @Override
    public Optional<Media> getResultImage(long resultId, long orderId, String attribute) {
        Optional<Media> ret = Optional.empty();

        Optional<Result> resultOptional = resultService.findById(resultId);

        if(resultOptional.isPresent() && resultOptional.get().getOrderId() == orderId){

            Result result = resultOptional.get();

            Media image = new Media();

            switch (attribute){
                case "identification":
                    image.setName("resultIdentification");
                    image.setContentType(result.getIdentificationType());
                    image.setFile(result.getIdentification());
                    ret = Optional.of(image);
                    break;
                case "result-data":
                    image.setName("resultData");
                    image.setContentType(result.getDataType());
                    image.setFile(result.getData());
                    ret = Optional.of(image);
                    break;
            }
        }

        return ret;
    }
}

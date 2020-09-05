package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {


    @Override
    public Order findById(long id) {
        return new Order(id);
    }
}

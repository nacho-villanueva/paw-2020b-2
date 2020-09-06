package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.persistence.OrderDao;
import ar.edu.itba.paw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;


    @Override
    public Order findById(long id) {
        return orderDao.findById(id);
    }
}

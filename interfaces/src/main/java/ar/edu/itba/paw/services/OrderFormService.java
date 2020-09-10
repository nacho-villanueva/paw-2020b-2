package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.OrderForm;

public interface OrderFormService {

    public Long HandleOrderForm(OrderForm orderForm, byte[] identification, String identificationType);

}

package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.OrderForm;

public interface OrderFormService {

    public String HandleOrderForm(OrderForm orderForm, byte[] identification);
}

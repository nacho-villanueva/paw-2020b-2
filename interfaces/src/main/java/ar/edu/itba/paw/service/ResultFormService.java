package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.ResultForm;

public interface ResultFormService {
    public Long HandleOrderForm(ResultForm resultForm, byte[] identification, String identificationType, byte[] resultData, String resultDataType, long orderId);
}

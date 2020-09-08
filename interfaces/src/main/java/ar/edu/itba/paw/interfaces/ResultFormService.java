package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.ResultForm;

public interface ResultFormService {
    public Integer HandleOrderForm(ResultForm resultForm, byte[] identification, String identificationType, byte[] resultData, String resultDataType, long orderId);
}

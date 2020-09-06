package ar.edu.itba.paw.model;

import java.sql.Date;

public class Result {
    private Date date;
    private String responsible_name;
    private String responsible_licence_number;
    private byte[] identification;
    private Object data;

    public Result(final Date date, final String responsible_name, final String responsible_licence_number, final byte[] identification, final Object data) {
        this.date = date;
        this.responsible_name = responsible_name;
        this.responsible_licence_number = responsible_licence_number;
        this.identification = identification;
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public String getResponsible_name() {
        return responsible_name;
    }

    public String getResponsible_licence_number() {
        return responsible_licence_number;
    }

    public byte[] getIdentification() {
        return identification;
    }

    public Object getData() {
        return data;
    }
}

package ar.edu.itba.paw.model;

import java.sql.Date;

public class Result {
    private int id;
    private long order_id;
    private Date date;
    private String responsible_name;
    private String responsible_licence_number;
    private byte[] identification;
    private byte[] data;

    public Result(final int id, final long order_id, final Date date, final String responsible_name, final String responsible_licence_number, final byte[] identification, final byte[] data) {
        this.id = id;
        this.order_id = order_id;
        this.date = date;
        this.responsible_name = responsible_name;
        this.responsible_licence_number = responsible_licence_number;
        this.identification = identification;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public long getOrder_id() {
        return order_id;
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

    public byte[] getData() {
        return data;
    }
}

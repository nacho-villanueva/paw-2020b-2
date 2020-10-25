package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "results")
public class Result {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "results_id_seq")
    @SequenceGenerator(sequenceName = "results_id_seq", name = "results_id_seq", allocationSize = 1)
    private long id;

    @Column(name="order_id", nullable=false)
    private long order_id;

    @Column(name="date", nullable=false)
    private Date date;

    @Column(name="responsible_name", nullable=false)
    private String responsible_name;

    @Column(name="responsible_licence_number", nullable=false)
    private String responsible_licence_number;

    @Column(name="identification_type", nullable=false)
    private String identification_type;

    @Column(name="identification", nullable=false)
    private byte[] identification;

    @Column(name="result_data_type", nullable=false)
    private String data_type;

    @Column(name="result_data", nullable=false)
    private byte[] data;

    protected Result() {
        //Just for hibernate
    }

    public Result(final long id,
                  final long order_id,
                  final Date date,
                  final String responsible_name,
                  final String responsible_licence_number,
                  final String identification_type,
                  final byte[] identification,
                  final String data_type,
                  final byte[] data) {
        this.id = id;
        this.order_id = order_id;
        this.date = date;
        this.responsible_name = responsible_name;
        this.responsible_licence_number = responsible_licence_number;
        this.identification_type = identification_type;
        this.identification = identification;
        this.data_type = data_type;
        this.data = data;
    }

    public Result(final long order_id,
                  final Date date,
                  final String responsible_name,
                  final String responsible_licence_number,
                  final String identification_type,
                  final byte[] identification,
                  final String data_type,
                  final byte[] data) {
        this.order_id = order_id;
        this.date = date;
        this.responsible_name = responsible_name;
        this.responsible_licence_number = responsible_licence_number;
        this.identification_type = identification_type;
        this.identification = identification;
        this.data_type = data_type;
        this.data = data;
    }

    public long getId() {
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

    public String getIdentification_type() {
        return identification_type;
    }

    public byte[] getIdentification() {
        return identification;
    }

    public String getData_type() {
        return data_type;
    }

    public byte[] getData() {
        return data;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    public void setIdentification(byte[] identification) {
        this.identification = identification;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public void setResponsible_licence_number(String responsible_licence_number) {
        this.responsible_licence_number = responsible_licence_number;
    }

    public void setResponsible_name(String responsible_name) {
        this.responsible_name = responsible_name;
    }
}

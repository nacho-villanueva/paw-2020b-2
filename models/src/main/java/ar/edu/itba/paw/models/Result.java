package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "results_id_seq")
    @SequenceGenerator(sequenceName = "results_id_seq", name = "results_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name="date", nullable=false)
    private LocalDate date;

    @Column(name="responsible_name", nullable=false)
    private String responsibleName;

    @Column(name="responsible_licence_number", nullable=false)
    private String responsibleLicenceNumber;

    @Column(name="identification_type", nullable=false)
    private String identificationType;

    @Column(name="identification", nullable=false, length = 32000000)  //Aprox 30Mb max file
    private byte[] identification;

    @Column(name="result_data_type", nullable=false)
    private String dataType;

    @Column(name="result_data", nullable=false, length = 32000000)
    private byte[] data;

    protected Result() {
        //Just for hibernate
    }

    public Result(final Long id,
                  final Order order,
                  final LocalDate date,
                  final String responsibleName,
                  final String responsibleLicenceNumber,
                  final String identificationType,
                  final byte[] identification,
                  final String dataType,
                  final byte[] data) {
        this.id = id;
        this.order = order;
        this.date = date;
        this.responsibleName = responsibleName;
        this.responsibleLicenceNumber = responsibleLicenceNumber;
        this.identificationType = identificationType;
        this.identification = identification;
        this.dataType = dataType;
        this.data = data;
    }

    public Result(final Order order,
                  final LocalDate date,
                  final String responsibleName,
                  final String responsibleLicenceNumber,
                  final String identificationType,
                  final byte[] identification,
                  final String dataType,
                  final byte[] data) {
        this.order = order;
        this.date = date;
        this.responsibleName = responsibleName;
        this.responsibleLicenceNumber = responsibleLicenceNumber;
        this.identificationType = identificationType;
        this.identification = identification;
        this.dataType = dataType;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public long getOrderId() { return order.getOrderId(); }

    public LocalDate getDate() {
        return date;
    }

    public Date getLegacyDate() {
        return Date.from(getDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public String getResponsibleLicenceNumber() {
        return responsibleLicenceNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public byte[] getIdentification() {
        return identification;
    }

    public String getDataType() {
        return dataType;
    }

    public byte[] getData() {
        return data;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public void setIdentification(byte[] identification) {
        this.identification = identification;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setResponsibleLicenceNumber(String responsibleLicenceNumber) {
        this.responsibleLicenceNumber = responsibleLicenceNumber;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }
}

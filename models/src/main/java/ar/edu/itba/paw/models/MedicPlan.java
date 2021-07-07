package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "medical_plans")
public class MedicPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_plans_id_seq")
    @SequenceGenerator(sequenceName = "medical_plans_id_seq", name = "medical_plans_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    protected MedicPlan() {
        //Just for hibernate
    }

    public MedicPlan(String name) {
        this.name = name;
    }

    public MedicPlan(int id, String name) {
        this(name);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicPlan that = (MedicPlan) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}

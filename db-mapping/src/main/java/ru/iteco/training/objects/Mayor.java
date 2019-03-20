package ru.iteco.training.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "mayor")
public class Mayor implements Serializable {

    @Id
    @GeneratedValue(generator = "mayor_seq")
    @SequenceGenerator(name = "mayor_seq", sequenceName = "mayor_mayor_id_seq", allocationSize = 1)
    @Column(name = "mayor_id", nullable = false, unique = true)
    Long id;

    @Column(name = "fio", nullable = false)
    String fio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public String toString() {
        return "Mayor{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mayor mayor = (Mayor) o;
        return id.equals(mayor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fio);
    }
}

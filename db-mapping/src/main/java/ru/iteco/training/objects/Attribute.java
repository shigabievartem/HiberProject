package ru.iteco.training.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "attribute")
public class Attribute implements Serializable {
    @Id
    @GeneratedValue(generator = "attribute_seq")
    @SequenceGenerator(name = "attribute_seq", sequenceName = "attribute_attribute_id_seq", allocationSize = 1)
    @Column(name = "attribute_id", nullable = false, unique = true)
    Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "value", nullable = false)
    String value;

    //TODO а если у нас колонка в таблице называется по одному, а ссылается на таблицу в которой колнка названа по другому?
    // @Column(name="одно наименование")  @JoinColumn(name = "другое имя")?
    @ManyToOne//(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "attribute_type_id")
    AttributeType type;

    @ManyToMany
    @JoinTable(
            name = "city_attribute",
            joinColumns = @JoinColumn(name = "attribute_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    Set<City> cities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return id.equals(attribute.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value);
    }
}

package ru.iteco.training.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity(name = "attribute_type")
public class AttributeType implements Serializable {
    @Id
    @GeneratedValue(generator = "attribute_type_seq")
    @SequenceGenerator(name = "attribute_type_seq", sequenceName = "attribute_type_attribute_type_id_seq", allocationSize = 1)
    @Column(name = "attribute_type_id", nullable = false, unique = true)
    Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "type"/*, fetch = FetchType.EAGER, cascade = CascadeType.ALL*/)
    private Set<Attribute> attributes;

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

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "AttributeType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeType that = (AttributeType) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

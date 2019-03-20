package ru.iteco.training.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity(name = "city")
public class City implements Serializable {
    @Id
    @GeneratedValue(generator = "city_seq")
    @SequenceGenerator(name = "city_seq", sequenceName = "city_city_id_seq", allocationSize = 1)
    @Column(name = "city_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(targetEntity = Mayor.class/*, fetch = FetchType.EAGER*/)
    @JoinColumn(name = "mayor_id")
    private Mayor mayor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "city_attribute",
            joinColumns = @JoinColumn(name = "city_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )

//    private Long version;

    private Collection<Attribute> attributes;

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

    public Mayor getMayor() {
        return mayor;
    }

    public void setMayor(Mayor mayor) {
        this.mayor = mayor;
    }

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<Attribute> attributes) {
        this.attributes = attributes;
    }

//    public void setVersion(Long version) {
//        this.version = version;
//    }
//
//    @Version
//    public Long getVersion() {
//        return version;
//    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mayor=" + mayor +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id.equals(city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

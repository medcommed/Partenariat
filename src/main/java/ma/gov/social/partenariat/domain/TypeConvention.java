package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TypeConvention.
 */
@Entity
@Table(name = "type_convention")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeConvention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_type_ar", length = 255, nullable = false, unique = true)
    private String nomTypeAr;

    @Size(max = 255)
    @Column(name = "nom_type_fr", length = 255, unique = true)
    private String nomTypeFr;

    @OneToMany(mappedBy = "typeConvention")
    @JsonIgnoreProperties(value = { "projet", "typeConvention", "partenaires" }, allowSetters = true)
    private Set<Convention> conventions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeConvention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomTypeAr() {
        return this.nomTypeAr;
    }

    public TypeConvention nomTypeAr(String nomTypeAr) {
        this.setNomTypeAr(nomTypeAr);
        return this;
    }

    public void setNomTypeAr(String nomTypeAr) {
        this.nomTypeAr = nomTypeAr;
    }

    public String getNomTypeFr() {
        return this.nomTypeFr;
    }

    public TypeConvention nomTypeFr(String nomTypeFr) {
        this.setNomTypeFr(nomTypeFr);
        return this;
    }

    public void setNomTypeFr(String nomTypeFr) {
        this.nomTypeFr = nomTypeFr;
    }

    public Set<Convention> getConventions() {
        return this.conventions;
    }

    public void setConventions(Set<Convention> conventions) {
        if (this.conventions != null) {
            this.conventions.forEach(i -> i.setTypeConvention(null));
        }
        if (conventions != null) {
            conventions.forEach(i -> i.setTypeConvention(this));
        }
        this.conventions = conventions;
    }

    public TypeConvention conventions(Set<Convention> conventions) {
        this.setConventions(conventions);
        return this;
    }

    public TypeConvention addConvention(Convention convention) {
        this.conventions.add(convention);
        convention.setTypeConvention(this);
        return this;
    }

    public TypeConvention removeConvention(Convention convention) {
        this.conventions.remove(convention);
        convention.setTypeConvention(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeConvention)) {
            return false;
        }
        return id != null && id.equals(((TypeConvention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeConvention{" +
            "id=" + getId() +
            ", nomTypeAr='" + getNomTypeAr() + "'" +
            ", nomTypeFr='" + getNomTypeFr() + "'" +
            "}";
    }
}

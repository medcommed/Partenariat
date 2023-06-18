package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "libelle_province_ar", length = 255, nullable = false, unique = true)
    private String libelleProvinceAr;

    @Size(max = 255)
    @Column(name = "libelle_province_fr", length = 255, unique = true)
    private String libelleProvinceFr;

    @ManyToOne
    @JsonIgnoreProperties(value = { "provinces" }, allowSetters = true)
    private Region region;

    @OneToMany(mappedBy = "provinces")
    @JsonIgnoreProperties(value = { "provinces", "projets" }, allowSetters = true)
    private Set<Commune> communes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Province id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleProvinceAr() {
        return this.libelleProvinceAr;
    }

    public Province libelleProvinceAr(String libelleProvinceAr) {
        this.setLibelleProvinceAr(libelleProvinceAr);
        return this;
    }

    public void setLibelleProvinceAr(String libelleProvinceAr) {
        this.libelleProvinceAr = libelleProvinceAr;
    }

    public String getLibelleProvinceFr() {
        return this.libelleProvinceFr;
    }

    public Province libelleProvinceFr(String libelleProvinceFr) {
        this.setLibelleProvinceFr(libelleProvinceFr);
        return this;
    }

    public void setLibelleProvinceFr(String libelleProvinceFr) {
        this.libelleProvinceFr = libelleProvinceFr;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Province region(Region region) {
        this.setRegion(region);
        return this;
    }

    public Set<Commune> getCommunes() {
        return this.communes;
    }

    public void setCommunes(Set<Commune> communes) {
        if (this.communes != null) {
            this.communes.forEach(i -> i.setProvinces(null));
        }
        if (communes != null) {
            communes.forEach(i -> i.setProvinces(this));
        }
        this.communes = communes;
    }

    public Province communes(Set<Commune> communes) {
        this.setCommunes(communes);
        return this;
    }

    public Province addCommune(Commune commune) {
        this.communes.add(commune);
        commune.setProvinces(this);
        return this;
    }

    public Province removeCommune(Commune commune) {
        this.communes.remove(commune);
        commune.setProvinces(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", libelleProvinceAr='" + getLibelleProvinceAr() + "'" +
            ", libelleProvinceFr='" + getLibelleProvinceFr() + "'" +
            "}";
    }
}

package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Commune.
 */
@Entity
@Table(name = "commune")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_commune_ar", length = 255, nullable = false, unique = true)
    private String nomCommuneAr;

    @Size(max = 255)
    @Column(name = "nom_commune_fr", length = 255, unique = true)
    private String nomCommuneFr;

    @ManyToOne
    @JsonIgnoreProperties(value = { "region", "communes" }, allowSetters = true)
    private Province provinces;

    @OneToMany(mappedBy = "comune")
    @JsonIgnoreProperties(value = { "comune", "domaineProjet", "conventions", "situationProjets", "tranches" }, allowSetters = true)
    private Set<Projet> projets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commune id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCommuneAr() {
        return this.nomCommuneAr;
    }

    public Commune nomCommuneAr(String nomCommuneAr) {
        this.setNomCommuneAr(nomCommuneAr);
        return this;
    }

    public void setNomCommuneAr(String nomCommuneAr) {
        this.nomCommuneAr = nomCommuneAr;
    }

    public String getNomCommuneFr() {
        return this.nomCommuneFr;
    }

    public Commune nomCommuneFr(String nomCommuneFr) {
        this.setNomCommuneFr(nomCommuneFr);
        return this;
    }

    public void setNomCommuneFr(String nomCommuneFr) {
        this.nomCommuneFr = nomCommuneFr;
    }

    public Province getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Province province) {
        this.provinces = province;
    }

    public Commune provinces(Province province) {
        this.setProvinces(province);
        return this;
    }

    public Set<Projet> getProjets() {
        return this.projets;
    }

    public void setProjets(Set<Projet> projets) {
        if (this.projets != null) {
            this.projets.forEach(i -> i.setComune(null));
        }
        if (projets != null) {
            projets.forEach(i -> i.setComune(this));
        }
        this.projets = projets;
    }

    public Commune projets(Set<Projet> projets) {
        this.setProjets(projets);
        return this;
    }

    public Commune addProjet(Projet projet) {
        this.projets.add(projet);
        projet.setComune(this);
        return this;
    }

    public Commune removeProjet(Projet projet) {
        this.projets.remove(projet);
        projet.setComune(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commune)) {
            return false;
        }
        return id != null && id.equals(((Commune) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commune{" +
            "id=" + getId() +
            ", nomCommuneAr='" + getNomCommuneAr() + "'" +
            ", nomCommuneFr='" + getNomCommuneFr() + "'" +
            "}";
    }
}

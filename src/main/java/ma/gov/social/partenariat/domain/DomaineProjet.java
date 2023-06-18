package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DomaineProjet.
 */
@Entity
@Table(name = "domaine_projet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DomaineProjet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "designation_ar", length = 255, nullable = false, unique = true)
    private String designationAr;

    @Size(max = 255)
    @Column(name = "designation_fr", length = 255, unique = true)
    private String designationFr;

    @OneToMany(mappedBy = "domaineProjet")
    @JsonIgnoreProperties(value = { "comune", "domaineProjet", "conventions", "situationProjets", "tranches" }, allowSetters = true)
    private Set<Projet> projets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DomaineProjet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignationAr() {
        return this.designationAr;
    }

    public DomaineProjet designationAr(String designationAr) {
        this.setDesignationAr(designationAr);
        return this;
    }

    public void setDesignationAr(String designationAr) {
        this.designationAr = designationAr;
    }

    public String getDesignationFr() {
        return this.designationFr;
    }

    public DomaineProjet designationFr(String designationFr) {
        this.setDesignationFr(designationFr);
        return this;
    }

    public void setDesignationFr(String designationFr) {
        this.designationFr = designationFr;
    }

    public Set<Projet> getProjets() {
        return this.projets;
    }

    public void setProjets(Set<Projet> projets) {
        if (this.projets != null) {
            this.projets.forEach(i -> i.setDomaineProjet(null));
        }
        if (projets != null) {
            projets.forEach(i -> i.setDomaineProjet(this));
        }
        this.projets = projets;
    }

    public DomaineProjet projets(Set<Projet> projets) {
        this.setProjets(projets);
        return this;
    }

    public DomaineProjet addProjet(Projet projet) {
        this.projets.add(projet);
        projet.setDomaineProjet(this);
        return this;
    }

    public DomaineProjet removeProjet(Projet projet) {
        this.projets.remove(projet);
        projet.setDomaineProjet(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomaineProjet)) {
            return false;
        }
        return id != null && id.equals(((DomaineProjet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DomaineProjet{" +
            "id=" + getId() +
            ", designationAr='" + getDesignationAr() + "'" +
            ", designationFr='" + getDesignationFr() + "'" +
            "}";
    }
}

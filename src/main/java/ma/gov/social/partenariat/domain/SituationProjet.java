package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SituationProjet.
 */
@Entity
@Table(name = "situation_projet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SituationProjet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_statut_valid", nullable = false)
    private LocalDate dateStatutValid;

    @NotNull
    @Column(name = "statut_projet", nullable = false)
    private String statutProjet;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "comune", "domaineProjet", "conventions", "situationProjets", "tranches" }, allowSetters = true)
    private Projet projet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SituationProjet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateStatutValid() {
        return this.dateStatutValid;
    }

    public SituationProjet dateStatutValid(LocalDate dateStatutValid) {
        this.setDateStatutValid(dateStatutValid);
        return this;
    }

    public void setDateStatutValid(LocalDate dateStatutValid) {
        this.dateStatutValid = dateStatutValid;
    }

    public String getStatutProjet() {
        return this.statutProjet;
    }

    public SituationProjet statutProjet(String statutProjet) {
        this.setStatutProjet(statutProjet);
        return this;
    }

    public void setStatutProjet(String statutProjet) {
        this.statutProjet = statutProjet;
    }

    public Projet getProjet() {
        return this.projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public SituationProjet projet(Projet projet) {
        this.setProjet(projet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SituationProjet)) {
            return false;
        }
        return id != null && id.equals(((SituationProjet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SituationProjet{" +
            "id=" + getId() +
            ", dateStatutValid='" + getDateStatutValid() + "'" +
            ", statutProjet='" + getStatutProjet() + "'" +
            "}";
    }
}

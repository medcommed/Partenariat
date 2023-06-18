package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Tranche.
 */
@Entity
@Table(name = "tranche")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tranche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_tranche", length = 255, nullable = false)
    private String nomTranche;

    @NotNull
    @Column(name = "date_deffet", nullable = false, unique = true)
    private LocalDate dateDeffet;

    @NotNull
    @Column(name = "montant_tranche", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantTranche;

    @NotNull
    @Column(name = "rapport_tranche", nullable = false)
    private String rapportTranche;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "comune", "domaineProjet", "conventions", "situationProjets", "tranches" }, allowSetters = true)
    private Projet projet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tranche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomTranche() {
        return this.nomTranche;
    }

    public Tranche nomTranche(String nomTranche) {
        this.setNomTranche(nomTranche);
        return this;
    }

    public void setNomTranche(String nomTranche) {
        this.nomTranche = nomTranche;
    }

    public LocalDate getDateDeffet() {
        return this.dateDeffet;
    }

    public Tranche dateDeffet(LocalDate dateDeffet) {
        this.setDateDeffet(dateDeffet);
        return this;
    }

    public void setDateDeffet(LocalDate dateDeffet) {
        this.dateDeffet = dateDeffet;
    }

    public BigDecimal getMontantTranche() {
        return this.montantTranche;
    }

    public Tranche montantTranche(BigDecimal montantTranche) {
        this.setMontantTranche(montantTranche);
        return this;
    }

    public void setMontantTranche(BigDecimal montantTranche) {
        this.montantTranche = montantTranche;
    }

    public String getRapportTranche() {
        return this.rapportTranche;
    }

    public Tranche rapportTranche(String rapportTranche) {
        this.setRapportTranche(rapportTranche);
        return this;
    }

    public void setRapportTranche(String rapportTranche) {
        this.rapportTranche = rapportTranche;
    }

    public Projet getProjet() {
        return this.projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Tranche projet(Projet projet) {
        this.setProjet(projet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tranche)) {
            return false;
        }
        return id != null && id.equals(((Tranche) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tranche{" +
            "id=" + getId() +
            ", nomTranche='" + getNomTranche() + "'" +
            ", dateDeffet='" + getDateDeffet() + "'" +
            ", montantTranche=" + getMontantTranche() +
            ", rapportTranche='" + getRapportTranche() + "'" +
            "}";
    }
}

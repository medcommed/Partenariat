package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Projet.
 */
@Entity
@Table(name = "projet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Projet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_projet", length = 255, nullable = false)
    private String nomProjet;

    @NotNull
    @Column(name = "annee_projet", nullable = false)
    private String anneeProjet;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "duree_projet", nullable = false)
    private String dureeProjet;

    @NotNull
    @Column(name = "montant_projet", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantProjet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "provinces", "projets" }, allowSetters = true)
    private Commune comune;

    @ManyToOne
    @JsonIgnoreProperties(value = { "projets" }, allowSetters = true)
    private DomaineProjet domaineProjet;

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties(value = { "projet", "typeConvention", "partenaires" }, allowSetters = true)
    private Set<Convention> conventions = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties(value = { "projet" }, allowSetters = true)
    private Set<SituationProjet> situationProjets = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @JsonIgnoreProperties(value = { "projet" }, allowSetters = true)
    private Set<Tranche> tranches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Projet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProjet() {
        return this.nomProjet;
    }

    public Projet nomProjet(String nomProjet) {
        this.setNomProjet(nomProjet);
        return this;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getAnneeProjet() {
        return this.anneeProjet;
    }

    public Projet anneeProjet(String anneeProjet) {
        this.setAnneeProjet(anneeProjet);
        return this;
    }

    public void setAnneeProjet(String anneeProjet) {
        this.anneeProjet = anneeProjet;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Projet dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDureeProjet() {
        return this.dureeProjet;
    }

    public Projet dureeProjet(String dureeProjet) {
        this.setDureeProjet(dureeProjet);
        return this;
    }

    public void setDureeProjet(String dureeProjet) {
        this.dureeProjet = dureeProjet;
    }

    public BigDecimal getMontantProjet() {
        return this.montantProjet;
    }

    public Projet montantProjet(BigDecimal montantProjet) {
        this.setMontantProjet(montantProjet);
        return this;
    }

    public void setMontantProjet(BigDecimal montantProjet) {
        this.montantProjet = montantProjet;
    }

    public Commune getComune() {
        return this.comune;
    }

    public void setComune(Commune commune) {
        this.comune = commune;
    }

    public Projet comune(Commune commune) {
        this.setComune(commune);
        return this;
    }

    public DomaineProjet getDomaineProjet() {
        return this.domaineProjet;
    }

    public void setDomaineProjet(DomaineProjet domaineProjet) {
        this.domaineProjet = domaineProjet;
    }

    public Projet domaineProjet(DomaineProjet domaineProjet) {
        this.setDomaineProjet(domaineProjet);
        return this;
    }

    public Set<Convention> getConventions() {
        return this.conventions;
    }

    public void setConventions(Set<Convention> conventions) {
        if (this.conventions != null) {
            this.conventions.forEach(i -> i.setProjet(null));
        }
        if (conventions != null) {
            conventions.forEach(i -> i.setProjet(this));
        }
        this.conventions = conventions;
    }

    public Projet conventions(Set<Convention> conventions) {
        this.setConventions(conventions);
        return this;
    }

    public Projet addConvention(Convention convention) {
        this.conventions.add(convention);
        convention.setProjet(this);
        return this;
    }

    public Projet removeConvention(Convention convention) {
        this.conventions.remove(convention);
        convention.setProjet(null);
        return this;
    }

    public Set<SituationProjet> getSituationProjets() {
        return this.situationProjets;
    }

    public void setSituationProjets(Set<SituationProjet> situationProjets) {
        if (this.situationProjets != null) {
            this.situationProjets.forEach(i -> i.setProjet(null));
        }
        if (situationProjets != null) {
            situationProjets.forEach(i -> i.setProjet(this));
        }
        this.situationProjets = situationProjets;
    }

    public Projet situationProjets(Set<SituationProjet> situationProjets) {
        this.setSituationProjets(situationProjets);
        return this;
    }

    public Projet addSituationProjet(SituationProjet situationProjet) {
        this.situationProjets.add(situationProjet);
        situationProjet.setProjet(this);
        return this;
    }

    public Projet removeSituationProjet(SituationProjet situationProjet) {
        this.situationProjets.remove(situationProjet);
        situationProjet.setProjet(null);
        return this;
    }

    public Set<Tranche> getTranches() {
        return this.tranches;
    }

    public void setTranches(Set<Tranche> tranches) {
        if (this.tranches != null) {
            this.tranches.forEach(i -> i.setProjet(null));
        }
        if (tranches != null) {
            tranches.forEach(i -> i.setProjet(this));
        }
        this.tranches = tranches;
    }

    public Projet tranches(Set<Tranche> tranches) {
        this.setTranches(tranches);
        return this;
    }

    public Projet addTranche(Tranche tranche) {
        this.tranches.add(tranche);
        tranche.setProjet(this);
        return this;
    }

    public Projet removeTranche(Tranche tranche) {
        this.tranches.remove(tranche);
        tranche.setProjet(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projet)) {
            return false;
        }
        return id != null && id.equals(((Projet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Projet{" +
            "id=" + getId() +
            ", nomProjet='" + getNomProjet() + "'" +
            ", anneeProjet='" + getAnneeProjet() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dureeProjet='" + getDureeProjet() + "'" +
            ", montantProjet=" + getMontantProjet() +
            "}";
    }
}

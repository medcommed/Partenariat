package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Convention.
 */
@Entity
@Table(name = "convention")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Convention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "aveanau", length = 255, nullable = false)
    private String aveanau;

    @NotNull
    @Column(name = "date_debut_conv", nullable = false)
    private LocalDate dateDebutConv;

    @NotNull
    @Size(max = 255)
    @Column(name = "duree_convention", length = 255, nullable = false)
    private String dureeConvention;

    @NotNull
    @Size(max = 255)
    @Column(name = "etat_convention", length = 255, nullable = false)
    private String etatConvention;

    @NotNull
    @Column(name = "nbr_tranche", nullable = false)
    private Long nbrTranche;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_convention", length = 255, nullable = false)
    private String nomConvention;

    @Lob
    @Column(name = "objectif", nullable = false)
    private String objectif;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "comune", "domaineProjet", "conventions", "situationProjets", "tranches" }, allowSetters = true)
    private Projet projet;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "conventions" }, allowSetters = true)
    private TypeConvention typeConvention;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_convention__partenaire",
        joinColumns = @JoinColumn(name = "convention_id"),
        inverseJoinColumns = @JoinColumn(name = "partenaire_id")
    )
    @JsonIgnoreProperties(value = { "conventions" }, allowSetters = true)
    private Set<Partenaire> partenaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Convention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAveanau() {
        return this.aveanau;
    }

    public Convention aveanau(String aveanau) {
        this.setAveanau(aveanau);
        return this;
    }

    public void setAveanau(String aveanau) {
        this.aveanau = aveanau;
    }

    public LocalDate getDateDebutConv() {
        return this.dateDebutConv;
    }

    public Convention dateDebutConv(LocalDate dateDebutConv) {
        this.setDateDebutConv(dateDebutConv);
        return this;
    }

    public void setDateDebutConv(LocalDate dateDebutConv) {
        this.dateDebutConv = dateDebutConv;
    }

    public String getDureeConvention() {
        return this.dureeConvention;
    }

    public Convention dureeConvention(String dureeConvention) {
        this.setDureeConvention(dureeConvention);
        return this;
    }

    public void setDureeConvention(String dureeConvention) {
        this.dureeConvention = dureeConvention;
    }

    public String getEtatConvention() {
        return this.etatConvention;
    }

    public Convention etatConvention(String etatConvention) {
        this.setEtatConvention(etatConvention);
        return this;
    }

    public void setEtatConvention(String etatConvention) {
        this.etatConvention = etatConvention;
    }

    public Long getNbrTranche() {
        return this.nbrTranche;
    }

    public Convention nbrTranche(Long nbrTranche) {
        this.setNbrTranche(nbrTranche);
        return this;
    }

    public void setNbrTranche(Long nbrTranche) {
        this.nbrTranche = nbrTranche;
    }

    public String getNomConvention() {
        return this.nomConvention;
    }

    public Convention nomConvention(String nomConvention) {
        this.setNomConvention(nomConvention);
        return this;
    }

    public void setNomConvention(String nomConvention) {
        this.nomConvention = nomConvention;
    }

    public String getObjectif() {
        return this.objectif;
    }

    public Convention objectif(String objectif) {
        this.setObjectif(objectif);
        return this;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public Projet getProjet() {
        return this.projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Convention projet(Projet projet) {
        this.setProjet(projet);
        return this;
    }

    public TypeConvention getTypeConvention() {
        return this.typeConvention;
    }

    public void setTypeConvention(TypeConvention typeConvention) {
        this.typeConvention = typeConvention;
    }

    public Convention typeConvention(TypeConvention typeConvention) {
        this.setTypeConvention(typeConvention);
        return this;
    }

    public Set<Partenaire> getPartenaires() {
        return this.partenaires;
    }

    public void setPartenaires(Set<Partenaire> partenaires) {
        this.partenaires = partenaires;
    }

    public Convention partenaires(Set<Partenaire> partenaires) {
        this.setPartenaires(partenaires);
        return this;
    }

    public Convention addPartenaire(Partenaire partenaire) {
        this.partenaires.add(partenaire);
        partenaire.getConventions().add(this);
        return this;
    }

    public Convention removePartenaire(Partenaire partenaire) {
        this.partenaires.remove(partenaire);
        partenaire.getConventions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Convention)) {
            return false;
        }
        return id != null && id.equals(((Convention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Convention{" +
            "id=" + getId() +
            ", aveanau='" + getAveanau() + "'" +
            ", dateDebutConv='" + getDateDebutConv() + "'" +
            ", dureeConvention='" + getDureeConvention() + "'" +
            ", etatConvention='" + getEtatConvention() + "'" +
            ", nbrTranche=" + getNbrTranche() +
            ", nomConvention='" + getNomConvention() + "'" +
            ", objectif='" + getObjectif() + "'" +
            "}";
    }
}

package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Projet} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.ProjetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomProjet;

    private StringFilter anneeProjet;

    private LocalDateFilter dateDebut;

    private StringFilter dureeProjet;

    private BigDecimalFilter montantProjet;

    private LongFilter comuneId;

    private LongFilter domaineProjetId;

    private LongFilter conventionId;

    private LongFilter situationProjetId;

    private LongFilter trancheId;

    private Boolean distinct;

    public ProjetCriteria() {}

    public ProjetCriteria(ProjetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomProjet = other.nomProjet == null ? null : other.nomProjet.copy();
        this.anneeProjet = other.anneeProjet == null ? null : other.anneeProjet.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dureeProjet = other.dureeProjet == null ? null : other.dureeProjet.copy();
        this.montantProjet = other.montantProjet == null ? null : other.montantProjet.copy();
        this.comuneId = other.comuneId == null ? null : other.comuneId.copy();
        this.domaineProjetId = other.domaineProjetId == null ? null : other.domaineProjetId.copy();
        this.conventionId = other.conventionId == null ? null : other.conventionId.copy();
        this.situationProjetId = other.situationProjetId == null ? null : other.situationProjetId.copy();
        this.trancheId = other.trancheId == null ? null : other.trancheId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProjetCriteria copy() {
        return new ProjetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomProjet() {
        return nomProjet;
    }

    public StringFilter nomProjet() {
        if (nomProjet == null) {
            nomProjet = new StringFilter();
        }
        return nomProjet;
    }

    public void setNomProjet(StringFilter nomProjet) {
        this.nomProjet = nomProjet;
    }

    public StringFilter getAnneeProjet() {
        return anneeProjet;
    }

    public StringFilter anneeProjet() {
        if (anneeProjet == null) {
            anneeProjet = new StringFilter();
        }
        return anneeProjet;
    }

    public void setAnneeProjet(StringFilter anneeProjet) {
        this.anneeProjet = anneeProjet;
    }

    public LocalDateFilter getDateDebut() {
        return dateDebut;
    }

    public LocalDateFilter dateDebut() {
        if (dateDebut == null) {
            dateDebut = new LocalDateFilter();
        }
        return dateDebut;
    }

    public void setDateDebut(LocalDateFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public StringFilter getDureeProjet() {
        return dureeProjet;
    }

    public StringFilter dureeProjet() {
        if (dureeProjet == null) {
            dureeProjet = new StringFilter();
        }
        return dureeProjet;
    }

    public void setDureeProjet(StringFilter dureeProjet) {
        this.dureeProjet = dureeProjet;
    }

    public BigDecimalFilter getMontantProjet() {
        return montantProjet;
    }

    public BigDecimalFilter montantProjet() {
        if (montantProjet == null) {
            montantProjet = new BigDecimalFilter();
        }
        return montantProjet;
    }

    public void setMontantProjet(BigDecimalFilter montantProjet) {
        this.montantProjet = montantProjet;
    }

    public LongFilter getComuneId() {
        return comuneId;
    }

    public LongFilter comuneId() {
        if (comuneId == null) {
            comuneId = new LongFilter();
        }
        return comuneId;
    }

    public void setComuneId(LongFilter comuneId) {
        this.comuneId = comuneId;
    }

    public LongFilter getDomaineProjetId() {
        return domaineProjetId;
    }

    public LongFilter domaineProjetId() {
        if (domaineProjetId == null) {
            domaineProjetId = new LongFilter();
        }
        return domaineProjetId;
    }

    public void setDomaineProjetId(LongFilter domaineProjetId) {
        this.domaineProjetId = domaineProjetId;
    }

    public LongFilter getConventionId() {
        return conventionId;
    }

    public LongFilter conventionId() {
        if (conventionId == null) {
            conventionId = new LongFilter();
        }
        return conventionId;
    }

    public void setConventionId(LongFilter conventionId) {
        this.conventionId = conventionId;
    }

    public LongFilter getSituationProjetId() {
        return situationProjetId;
    }

    public LongFilter situationProjetId() {
        if (situationProjetId == null) {
            situationProjetId = new LongFilter();
        }
        return situationProjetId;
    }

    public void setSituationProjetId(LongFilter situationProjetId) {
        this.situationProjetId = situationProjetId;
    }

    public LongFilter getTrancheId() {
        return trancheId;
    }

    public LongFilter trancheId() {
        if (trancheId == null) {
            trancheId = new LongFilter();
        }
        return trancheId;
    }

    public void setTrancheId(LongFilter trancheId) {
        this.trancheId = trancheId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjetCriteria that = (ProjetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomProjet, that.nomProjet) &&
            Objects.equals(anneeProjet, that.anneeProjet) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dureeProjet, that.dureeProjet) &&
            Objects.equals(montantProjet, that.montantProjet) &&
            Objects.equals(comuneId, that.comuneId) &&
            Objects.equals(domaineProjetId, that.domaineProjetId) &&
            Objects.equals(conventionId, that.conventionId) &&
            Objects.equals(situationProjetId, that.situationProjetId) &&
            Objects.equals(trancheId, that.trancheId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nomProjet,
            anneeProjet,
            dateDebut,
            dureeProjet,
            montantProjet,
            comuneId,
            domaineProjetId,
            conventionId,
            situationProjetId,
            trancheId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomProjet != null ? "nomProjet=" + nomProjet + ", " : "") +
            (anneeProjet != null ? "anneeProjet=" + anneeProjet + ", " : "") +
            (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
            (dureeProjet != null ? "dureeProjet=" + dureeProjet + ", " : "") +
            (montantProjet != null ? "montantProjet=" + montantProjet + ", " : "") +
            (comuneId != null ? "comuneId=" + comuneId + ", " : "") +
            (domaineProjetId != null ? "domaineProjetId=" + domaineProjetId + ", " : "") +
            (conventionId != null ? "conventionId=" + conventionId + ", " : "") +
            (situationProjetId != null ? "situationProjetId=" + situationProjetId + ", " : "") +
            (trancheId != null ? "trancheId=" + trancheId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

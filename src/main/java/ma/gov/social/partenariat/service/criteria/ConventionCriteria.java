package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Convention} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.ConventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConventionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter aveanau;

    private LocalDateFilter dateDebutConv;

    private StringFilter dureeConvention;

    private StringFilter etatConvention;

    private LongFilter nbrTranche;

    private StringFilter nomConvention;

    private LongFilter projetId;

    private LongFilter typeConventionId;

    private LongFilter partenaireId;

    private Boolean distinct;

    public ConventionCriteria() {}

    public ConventionCriteria(ConventionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.aveanau = other.aveanau == null ? null : other.aveanau.copy();
        this.dateDebutConv = other.dateDebutConv == null ? null : other.dateDebutConv.copy();
        this.dureeConvention = other.dureeConvention == null ? null : other.dureeConvention.copy();
        this.etatConvention = other.etatConvention == null ? null : other.etatConvention.copy();
        this.nbrTranche = other.nbrTranche == null ? null : other.nbrTranche.copy();
        this.nomConvention = other.nomConvention == null ? null : other.nomConvention.copy();
        this.projetId = other.projetId == null ? null : other.projetId.copy();
        this.typeConventionId = other.typeConventionId == null ? null : other.typeConventionId.copy();
        this.partenaireId = other.partenaireId == null ? null : other.partenaireId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConventionCriteria copy() {
        return new ConventionCriteria(this);
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

    public StringFilter getAveanau() {
        return aveanau;
    }

    public StringFilter aveanau() {
        if (aveanau == null) {
            aveanau = new StringFilter();
        }
        return aveanau;
    }

    public void setAveanau(StringFilter aveanau) {
        this.aveanau = aveanau;
    }

    public LocalDateFilter getDateDebutConv() {
        return dateDebutConv;
    }

    public LocalDateFilter dateDebutConv() {
        if (dateDebutConv == null) {
            dateDebutConv = new LocalDateFilter();
        }
        return dateDebutConv;
    }

    public void setDateDebutConv(LocalDateFilter dateDebutConv) {
        this.dateDebutConv = dateDebutConv;
    }

    public StringFilter getDureeConvention() {
        return dureeConvention;
    }

    public StringFilter dureeConvention() {
        if (dureeConvention == null) {
            dureeConvention = new StringFilter();
        }
        return dureeConvention;
    }

    public void setDureeConvention(StringFilter dureeConvention) {
        this.dureeConvention = dureeConvention;
    }

    public StringFilter getEtatConvention() {
        return etatConvention;
    }

    public StringFilter etatConvention() {
        if (etatConvention == null) {
            etatConvention = new StringFilter();
        }
        return etatConvention;
    }

    public void setEtatConvention(StringFilter etatConvention) {
        this.etatConvention = etatConvention;
    }

    public LongFilter getNbrTranche() {
        return nbrTranche;
    }

    public LongFilter nbrTranche() {
        if (nbrTranche == null) {
            nbrTranche = new LongFilter();
        }
        return nbrTranche;
    }

    public void setNbrTranche(LongFilter nbrTranche) {
        this.nbrTranche = nbrTranche;
    }

    public StringFilter getNomConvention() {
        return nomConvention;
    }

    public StringFilter nomConvention() {
        if (nomConvention == null) {
            nomConvention = new StringFilter();
        }
        return nomConvention;
    }

    public void setNomConvention(StringFilter nomConvention) {
        this.nomConvention = nomConvention;
    }

    public LongFilter getProjetId() {
        return projetId;
    }

    public LongFilter projetId() {
        if (projetId == null) {
            projetId = new LongFilter();
        }
        return projetId;
    }

    public void setProjetId(LongFilter projetId) {
        this.projetId = projetId;
    }

    public LongFilter getTypeConventionId() {
        return typeConventionId;
    }

    public LongFilter typeConventionId() {
        if (typeConventionId == null) {
            typeConventionId = new LongFilter();
        }
        return typeConventionId;
    }

    public void setTypeConventionId(LongFilter typeConventionId) {
        this.typeConventionId = typeConventionId;
    }

    public LongFilter getPartenaireId() {
        return partenaireId;
    }

    public LongFilter partenaireId() {
        if (partenaireId == null) {
            partenaireId = new LongFilter();
        }
        return partenaireId;
    }

    public void setPartenaireId(LongFilter partenaireId) {
        this.partenaireId = partenaireId;
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
        final ConventionCriteria that = (ConventionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(aveanau, that.aveanau) &&
            Objects.equals(dateDebutConv, that.dateDebutConv) &&
            Objects.equals(dureeConvention, that.dureeConvention) &&
            Objects.equals(etatConvention, that.etatConvention) &&
            Objects.equals(nbrTranche, that.nbrTranche) &&
            Objects.equals(nomConvention, that.nomConvention) &&
            Objects.equals(projetId, that.projetId) &&
            Objects.equals(typeConventionId, that.typeConventionId) &&
            Objects.equals(partenaireId, that.partenaireId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            aveanau,
            dateDebutConv,
            dureeConvention,
            etatConvention,
            nbrTranche,
            nomConvention,
            projetId,
            typeConventionId,
            partenaireId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConventionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (aveanau != null ? "aveanau=" + aveanau + ", " : "") +
            (dateDebutConv != null ? "dateDebutConv=" + dateDebutConv + ", " : "") +
            (dureeConvention != null ? "dureeConvention=" + dureeConvention + ", " : "") +
            (etatConvention != null ? "etatConvention=" + etatConvention + ", " : "") +
            (nbrTranche != null ? "nbrTranche=" + nbrTranche + ", " : "") +
            (nomConvention != null ? "nomConvention=" + nomConvention + ", " : "") +
            (projetId != null ? "projetId=" + projetId + ", " : "") +
            (typeConventionId != null ? "typeConventionId=" + typeConventionId + ", " : "") +
            (partenaireId != null ? "partenaireId=" + partenaireId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

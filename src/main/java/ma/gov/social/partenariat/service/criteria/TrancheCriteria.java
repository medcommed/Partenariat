package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Tranche} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.TrancheResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tranches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrancheCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomTranche;

    private LocalDateFilter dateDeffet;

    private BigDecimalFilter montantTranche;

    private StringFilter rapportTranche;

    private LongFilter projetId;

    private Boolean distinct;

    public TrancheCriteria() {}

    public TrancheCriteria(TrancheCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomTranche = other.nomTranche == null ? null : other.nomTranche.copy();
        this.dateDeffet = other.dateDeffet == null ? null : other.dateDeffet.copy();
        this.montantTranche = other.montantTranche == null ? null : other.montantTranche.copy();
        this.rapportTranche = other.rapportTranche == null ? null : other.rapportTranche.copy();
        this.projetId = other.projetId == null ? null : other.projetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TrancheCriteria copy() {
        return new TrancheCriteria(this);
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

    public StringFilter getNomTranche() {
        return nomTranche;
    }

    public StringFilter nomTranche() {
        if (nomTranche == null) {
            nomTranche = new StringFilter();
        }
        return nomTranche;
    }

    public void setNomTranche(StringFilter nomTranche) {
        this.nomTranche = nomTranche;
    }

    public LocalDateFilter getDateDeffet() {
        return dateDeffet;
    }

    public LocalDateFilter dateDeffet() {
        if (dateDeffet == null) {
            dateDeffet = new LocalDateFilter();
        }
        return dateDeffet;
    }

    public void setDateDeffet(LocalDateFilter dateDeffet) {
        this.dateDeffet = dateDeffet;
    }

    public BigDecimalFilter getMontantTranche() {
        return montantTranche;
    }

    public BigDecimalFilter montantTranche() {
        if (montantTranche == null) {
            montantTranche = new BigDecimalFilter();
        }
        return montantTranche;
    }

    public void setMontantTranche(BigDecimalFilter montantTranche) {
        this.montantTranche = montantTranche;
    }

    public StringFilter getRapportTranche() {
        return rapportTranche;
    }

    public StringFilter rapportTranche() {
        if (rapportTranche == null) {
            rapportTranche = new StringFilter();
        }
        return rapportTranche;
    }

    public void setRapportTranche(StringFilter rapportTranche) {
        this.rapportTranche = rapportTranche;
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
        final TrancheCriteria that = (TrancheCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomTranche, that.nomTranche) &&
            Objects.equals(dateDeffet, that.dateDeffet) &&
            Objects.equals(montantTranche, that.montantTranche) &&
            Objects.equals(rapportTranche, that.rapportTranche) &&
            Objects.equals(projetId, that.projetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomTranche, dateDeffet, montantTranche, rapportTranche, projetId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrancheCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomTranche != null ? "nomTranche=" + nomTranche + ", " : "") +
            (dateDeffet != null ? "dateDeffet=" + dateDeffet + ", " : "") +
            (montantTranche != null ? "montantTranche=" + montantTranche + ", " : "") +
            (rapportTranche != null ? "rapportTranche=" + rapportTranche + ", " : "") +
            (projetId != null ? "projetId=" + projetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

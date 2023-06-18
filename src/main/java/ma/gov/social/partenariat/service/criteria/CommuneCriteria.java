package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Commune} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.CommuneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /communes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommuneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomCommuneAr;

    private StringFilter nomCommuneFr;

    private LongFilter provincesId;

    private LongFilter projetId;

    private Boolean distinct;

    public CommuneCriteria() {}

    public CommuneCriteria(CommuneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomCommuneAr = other.nomCommuneAr == null ? null : other.nomCommuneAr.copy();
        this.nomCommuneFr = other.nomCommuneFr == null ? null : other.nomCommuneFr.copy();
        this.provincesId = other.provincesId == null ? null : other.provincesId.copy();
        this.projetId = other.projetId == null ? null : other.projetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommuneCriteria copy() {
        return new CommuneCriteria(this);
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

    public StringFilter getNomCommuneAr() {
        return nomCommuneAr;
    }

    public StringFilter nomCommuneAr() {
        if (nomCommuneAr == null) {
            nomCommuneAr = new StringFilter();
        }
        return nomCommuneAr;
    }

    public void setNomCommuneAr(StringFilter nomCommuneAr) {
        this.nomCommuneAr = nomCommuneAr;
    }

    public StringFilter getNomCommuneFr() {
        return nomCommuneFr;
    }

    public StringFilter nomCommuneFr() {
        if (nomCommuneFr == null) {
            nomCommuneFr = new StringFilter();
        }
        return nomCommuneFr;
    }

    public void setNomCommuneFr(StringFilter nomCommuneFr) {
        this.nomCommuneFr = nomCommuneFr;
    }

    public LongFilter getProvincesId() {
        return provincesId;
    }

    public LongFilter provincesId() {
        if (provincesId == null) {
            provincesId = new LongFilter();
        }
        return provincesId;
    }

    public void setProvincesId(LongFilter provincesId) {
        this.provincesId = provincesId;
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
        final CommuneCriteria that = (CommuneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomCommuneAr, that.nomCommuneAr) &&
            Objects.equals(nomCommuneFr, that.nomCommuneFr) &&
            Objects.equals(provincesId, that.provincesId) &&
            Objects.equals(projetId, that.projetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomCommuneAr, nomCommuneFr, provincesId, projetId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommuneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomCommuneAr != null ? "nomCommuneAr=" + nomCommuneAr + ", " : "") +
            (nomCommuneFr != null ? "nomCommuneFr=" + nomCommuneFr + ", " : "") +
            (provincesId != null ? "provincesId=" + provincesId + ", " : "") +
            (projetId != null ? "projetId=" + projetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

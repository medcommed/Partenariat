package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Region} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.RegionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /regions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelleRegionAr;

    private StringFilter libelleRegionFr;

    private LongFilter provinceId;

    private Boolean distinct;

    public RegionCriteria() {}

    public RegionCriteria(RegionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelleRegionAr = other.libelleRegionAr == null ? null : other.libelleRegionAr.copy();
        this.libelleRegionFr = other.libelleRegionFr == null ? null : other.libelleRegionFr.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RegionCriteria copy() {
        return new RegionCriteria(this);
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

    public StringFilter getLibelleRegionAr() {
        return libelleRegionAr;
    }

    public StringFilter libelleRegionAr() {
        if (libelleRegionAr == null) {
            libelleRegionAr = new StringFilter();
        }
        return libelleRegionAr;
    }

    public void setLibelleRegionAr(StringFilter libelleRegionAr) {
        this.libelleRegionAr = libelleRegionAr;
    }

    public StringFilter getLibelleRegionFr() {
        return libelleRegionFr;
    }

    public StringFilter libelleRegionFr() {
        if (libelleRegionFr == null) {
            libelleRegionFr = new StringFilter();
        }
        return libelleRegionFr;
    }

    public void setLibelleRegionFr(StringFilter libelleRegionFr) {
        this.libelleRegionFr = libelleRegionFr;
    }

    public LongFilter getProvinceId() {
        return provinceId;
    }

    public LongFilter provinceId() {
        if (provinceId == null) {
            provinceId = new LongFilter();
        }
        return provinceId;
    }

    public void setProvinceId(LongFilter provinceId) {
        this.provinceId = provinceId;
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
        final RegionCriteria that = (RegionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelleRegionAr, that.libelleRegionAr) &&
            Objects.equals(libelleRegionFr, that.libelleRegionFr) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelleRegionAr, libelleRegionFr, provinceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelleRegionAr != null ? "libelleRegionAr=" + libelleRegionAr + ", " : "") +
            (libelleRegionFr != null ? "libelleRegionFr=" + libelleRegionFr + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

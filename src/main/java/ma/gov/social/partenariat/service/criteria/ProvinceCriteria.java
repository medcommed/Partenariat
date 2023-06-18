package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Province} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.ProvinceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /provinces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProvinceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelleProvinceAr;

    private StringFilter libelleProvinceFr;

    private LongFilter regionId;

    private LongFilter communeId;

    private Boolean distinct;

    public ProvinceCriteria() {}

    public ProvinceCriteria(ProvinceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelleProvinceAr = other.libelleProvinceAr == null ? null : other.libelleProvinceAr.copy();
        this.libelleProvinceFr = other.libelleProvinceFr == null ? null : other.libelleProvinceFr.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
        this.communeId = other.communeId == null ? null : other.communeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProvinceCriteria copy() {
        return new ProvinceCriteria(this);
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

    public StringFilter getLibelleProvinceAr() {
        return libelleProvinceAr;
    }

    public StringFilter libelleProvinceAr() {
        if (libelleProvinceAr == null) {
            libelleProvinceAr = new StringFilter();
        }
        return libelleProvinceAr;
    }

    public void setLibelleProvinceAr(StringFilter libelleProvinceAr) {
        this.libelleProvinceAr = libelleProvinceAr;
    }

    public StringFilter getLibelleProvinceFr() {
        return libelleProvinceFr;
    }

    public StringFilter libelleProvinceFr() {
        if (libelleProvinceFr == null) {
            libelleProvinceFr = new StringFilter();
        }
        return libelleProvinceFr;
    }

    public void setLibelleProvinceFr(StringFilter libelleProvinceFr) {
        this.libelleProvinceFr = libelleProvinceFr;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public LongFilter regionId() {
        if (regionId == null) {
            regionId = new LongFilter();
        }
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }

    public LongFilter getCommuneId() {
        return communeId;
    }

    public LongFilter communeId() {
        if (communeId == null) {
            communeId = new LongFilter();
        }
        return communeId;
    }

    public void setCommuneId(LongFilter communeId) {
        this.communeId = communeId;
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
        final ProvinceCriteria that = (ProvinceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelleProvinceAr, that.libelleProvinceAr) &&
            Objects.equals(libelleProvinceFr, that.libelleProvinceFr) &&
            Objects.equals(regionId, that.regionId) &&
            Objects.equals(communeId, that.communeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelleProvinceAr, libelleProvinceFr, regionId, communeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelleProvinceAr != null ? "libelleProvinceAr=" + libelleProvinceAr + ", " : "") +
            (libelleProvinceFr != null ? "libelleProvinceFr=" + libelleProvinceFr + ", " : "") +
            (regionId != null ? "regionId=" + regionId + ", " : "") +
            (communeId != null ? "communeId=" + communeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

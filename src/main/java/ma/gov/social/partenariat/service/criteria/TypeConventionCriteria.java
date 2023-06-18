package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.TypeConvention} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.TypeConventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /type-conventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeConventionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomTypeAr;

    private StringFilter nomTypeFr;

    private LongFilter conventionId;

    private Boolean distinct;

    public TypeConventionCriteria() {}

    public TypeConventionCriteria(TypeConventionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomTypeAr = other.nomTypeAr == null ? null : other.nomTypeAr.copy();
        this.nomTypeFr = other.nomTypeFr == null ? null : other.nomTypeFr.copy();
        this.conventionId = other.conventionId == null ? null : other.conventionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TypeConventionCriteria copy() {
        return new TypeConventionCriteria(this);
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

    public StringFilter getNomTypeAr() {
        return nomTypeAr;
    }

    public StringFilter nomTypeAr() {
        if (nomTypeAr == null) {
            nomTypeAr = new StringFilter();
        }
        return nomTypeAr;
    }

    public void setNomTypeAr(StringFilter nomTypeAr) {
        this.nomTypeAr = nomTypeAr;
    }

    public StringFilter getNomTypeFr() {
        return nomTypeFr;
    }

    public StringFilter nomTypeFr() {
        if (nomTypeFr == null) {
            nomTypeFr = new StringFilter();
        }
        return nomTypeFr;
    }

    public void setNomTypeFr(StringFilter nomTypeFr) {
        this.nomTypeFr = nomTypeFr;
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
        final TypeConventionCriteria that = (TypeConventionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomTypeAr, that.nomTypeAr) &&
            Objects.equals(nomTypeFr, that.nomTypeFr) &&
            Objects.equals(conventionId, that.conventionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomTypeAr, nomTypeFr, conventionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeConventionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomTypeAr != null ? "nomTypeAr=" + nomTypeAr + ", " : "") +
            (nomTypeFr != null ? "nomTypeFr=" + nomTypeFr + ", " : "") +
            (conventionId != null ? "conventionId=" + conventionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

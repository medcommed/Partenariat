package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.DomaineProjet} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.DomaineProjetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /domaine-projets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DomaineProjetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter designationAr;

    private StringFilter designationFr;

    private LongFilter projetId;

    private Boolean distinct;

    public DomaineProjetCriteria() {}

    public DomaineProjetCriteria(DomaineProjetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.designationAr = other.designationAr == null ? null : other.designationAr.copy();
        this.designationFr = other.designationFr == null ? null : other.designationFr.copy();
        this.projetId = other.projetId == null ? null : other.projetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DomaineProjetCriteria copy() {
        return new DomaineProjetCriteria(this);
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

    public StringFilter getDesignationAr() {
        return designationAr;
    }

    public StringFilter designationAr() {
        if (designationAr == null) {
            designationAr = new StringFilter();
        }
        return designationAr;
    }

    public void setDesignationAr(StringFilter designationAr) {
        this.designationAr = designationAr;
    }

    public StringFilter getDesignationFr() {
        return designationFr;
    }

    public StringFilter designationFr() {
        if (designationFr == null) {
            designationFr = new StringFilter();
        }
        return designationFr;
    }

    public void setDesignationFr(StringFilter designationFr) {
        this.designationFr = designationFr;
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
        final DomaineProjetCriteria that = (DomaineProjetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(designationAr, that.designationAr) &&
            Objects.equals(designationFr, that.designationFr) &&
            Objects.equals(projetId, that.projetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, designationAr, designationFr, projetId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DomaineProjetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (designationAr != null ? "designationAr=" + designationAr + ", " : "") +
            (designationFr != null ? "designationFr=" + designationFr + ", " : "") +
            (projetId != null ? "projetId=" + projetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

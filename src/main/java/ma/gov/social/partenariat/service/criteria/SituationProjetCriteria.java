package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.SituationProjet} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.SituationProjetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /situation-projets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SituationProjetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateStatutValid;

    private StringFilter statutProjet;

    private LongFilter projetId;

    private Boolean distinct;

    public SituationProjetCriteria() {}

    public SituationProjetCriteria(SituationProjetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateStatutValid = other.dateStatutValid == null ? null : other.dateStatutValid.copy();
        this.statutProjet = other.statutProjet == null ? null : other.statutProjet.copy();
        this.projetId = other.projetId == null ? null : other.projetId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SituationProjetCriteria copy() {
        return new SituationProjetCriteria(this);
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

    public LocalDateFilter getDateStatutValid() {
        return dateStatutValid;
    }

    public LocalDateFilter dateStatutValid() {
        if (dateStatutValid == null) {
            dateStatutValid = new LocalDateFilter();
        }
        return dateStatutValid;
    }

    public void setDateStatutValid(LocalDateFilter dateStatutValid) {
        this.dateStatutValid = dateStatutValid;
    }

    public StringFilter getStatutProjet() {
        return statutProjet;
    }

    public StringFilter statutProjet() {
        if (statutProjet == null) {
            statutProjet = new StringFilter();
        }
        return statutProjet;
    }

    public void setStatutProjet(StringFilter statutProjet) {
        this.statutProjet = statutProjet;
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
        final SituationProjetCriteria that = (SituationProjetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateStatutValid, that.dateStatutValid) &&
            Objects.equals(statutProjet, that.statutProjet) &&
            Objects.equals(projetId, that.projetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateStatutValid, statutProjet, projetId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SituationProjetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateStatutValid != null ? "dateStatutValid=" + dateStatutValid + ", " : "") +
            (statutProjet != null ? "statutProjet=" + statutProjet + ", " : "") +
            (projetId != null ? "projetId=" + projetId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package ma.gov.social.partenariat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ma.gov.social.partenariat.domain.Partenaire} entity. This class is used
 * in {@link ma.gov.social.partenariat.web.rest.PartenaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /partenaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartenaireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomPartenaire;

    private StringFilter tel;

    private StringFilter email;

    private LongFilter conventionId;

    private Boolean distinct;

    public PartenaireCriteria() {}

    public PartenaireCriteria(PartenaireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomPartenaire = other.nomPartenaire == null ? null : other.nomPartenaire.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.conventionId = other.conventionId == null ? null : other.conventionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PartenaireCriteria copy() {
        return new PartenaireCriteria(this);
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

    public StringFilter getNomPartenaire() {
        return nomPartenaire;
    }

    public StringFilter nomPartenaire() {
        if (nomPartenaire == null) {
            nomPartenaire = new StringFilter();
        }
        return nomPartenaire;
    }

    public void setNomPartenaire(StringFilter nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
    }

    public StringFilter getTel() {
        return tel;
    }

    public StringFilter tel() {
        if (tel == null) {
            tel = new StringFilter();
        }
        return tel;
    }

    public void setTel(StringFilter tel) {
        this.tel = tel;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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
        final PartenaireCriteria that = (PartenaireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomPartenaire, that.nomPartenaire) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(email, that.email) &&
            Objects.equals(conventionId, that.conventionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomPartenaire, tel, email, conventionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartenaireCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomPartenaire != null ? "nomPartenaire=" + nomPartenaire + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (conventionId != null ? "conventionId=" + conventionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package ma.gov.social.partenariat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Partenaire.
 */
@Entity
@Table(name = "partenaire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Partenaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_partenaire", length = 255, nullable = false)
    private String nomPartenaire;

    @NotNull
    @Column(name = "tel", nullable = false)
    private String tel;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToMany(mappedBy = "partenaires")
    @JsonIgnoreProperties(value = { "projet", "typeConvention", "partenaires" }, allowSetters = true)
    private Set<Convention> conventions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Partenaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPartenaire() {
        return this.nomPartenaire;
    }

    public Partenaire nomPartenaire(String nomPartenaire) {
        this.setNomPartenaire(nomPartenaire);
        return this;
    }

    public void setNomPartenaire(String nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
    }

    public String getTel() {
        return this.tel;
    }

    public Partenaire tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return this.email;
    }

    public Partenaire email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Convention> getConventions() {
        return this.conventions;
    }

    public void setConventions(Set<Convention> conventions) {
        if (this.conventions != null) {
            this.conventions.forEach(i -> i.removePartenaire(this));
        }
        if (conventions != null) {
            conventions.forEach(i -> i.addPartenaire(this));
        }
        this.conventions = conventions;
    }

    public Partenaire conventions(Set<Convention> conventions) {
        this.setConventions(conventions);
        return this;
    }

    public Partenaire addConvention(Convention convention) {
        this.conventions.add(convention);
        convention.getPartenaires().add(this);
        return this;
    }

    public Partenaire removeConvention(Convention convention) {
        this.conventions.remove(convention);
        convention.getPartenaires().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partenaire)) {
            return false;
        }
        return id != null && id.equals(((Partenaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Partenaire{" +
            "id=" + getId() +
            ", nomPartenaire='" + getNomPartenaire() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}

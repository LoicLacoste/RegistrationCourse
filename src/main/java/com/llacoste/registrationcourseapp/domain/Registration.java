package com.llacoste.registrationcourseapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * A Registration.
 */
@Entity
@Table(name = "registration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_cert_ok")
    private Boolean isCertOk;

    @Column(name = "is_paye")
    private Boolean isPaye;

    @Column(name = "dossard")
    private Long dossard;

    @Column(name = "temps")
    private Duration temps;

    @Column(name = "date")
    private Instant date;

    @OneToMany(mappedBy = "registration")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "registration")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsCertOk() {
        return isCertOk;
    }

    public Registration isCertOk(Boolean isCertOk) {
        this.isCertOk = isCertOk;
        return this;
    }

    public void setIsCertOk(Boolean isCertOk) {
        this.isCertOk = isCertOk;
    }

    public Boolean isIsPaye() {
        return isPaye;
    }

    public Registration isPaye(Boolean isPaye) {
        this.isPaye = isPaye;
        return this;
    }

    public void setIsPaye(Boolean isPaye) {
        this.isPaye = isPaye;
    }

    public Long getDossard() {
        return dossard;
    }

    public Registration dossard(Long dossard) {
        this.dossard = dossard;
        return this;
    }

    public void setDossard(Long dossard) {
        this.dossard = dossard;
    }

    public Duration getTemps() {
        return temps;
    }

    public Registration temps(Duration temps) {
        this.temps = temps;
        return this;
    }

    public void setTemps(Duration temps) {
        this.temps = temps;
    }

    public Instant getDate() {
        return date;
    }

    public Registration date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Registration courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Registration addCourse(Course course) {
        this.courses.add(course);
        course.setRegistration(this);
        return this;
    }

    public Registration removeCourse(Course course) {
        this.courses.remove(course);
        course.setRegistration(null);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Registration users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Registration addUser(User user) {
        this.users.add(user);
        user.setRegistration(this);
        return this;
    }

    public Registration removeUser(User user) {
        this.users.remove(user);
        user.setRegistration(null);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Registration)) {
            return false;
        }
        return id != null && id.equals(((Registration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Registration{" +
            "id=" + getId() +
            ", isCertOk='" + isIsCertOk() + "'" +
            ", isPaye='" + isIsPaye() + "'" +
            ", dossard=" + getDossard() +
            ", temps='" + getTemps() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}

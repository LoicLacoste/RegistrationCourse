package com.llacoste.registrationcourseapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private Long name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "price")
    private Long price;

    @Column(name = "places")
    private Long places;

    @Column(name = "date_course")
    private Instant dateCourse;

    @Lob
    @Column(name = "image_course")
    private byte[] imageCourse;

    @Column(name = "image_course_content_type")
    private String imageCourseContentType;

    @ManyToOne
    @JsonIgnoreProperties("courses")
    private Registration registration;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getName() {
        return name;
    }

    public Course name(Long name) {
        this.name = name;
        return this;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public Course address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPrice() {
        return price;
    }

    public Course price(Long price) {
        this.price = price;
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getPlaces() {
        return places;
    }

    public Course places(Long places) {
        this.places = places;
        return this;
    }

    public void setPlaces(Long places) {
        this.places = places;
    }

    public Instant getDateCourse() {
        return dateCourse;
    }

    public Course dateCourse(Instant dateCourse) {
        this.dateCourse = dateCourse;
        return this;
    }

    public void setDateCourse(Instant dateCourse) {
        this.dateCourse = dateCourse;
    }

    public byte[] getImageCourse() {
        return imageCourse;
    }

    public Course imageCourse(byte[] imageCourse) {
        this.imageCourse = imageCourse;
        return this;
    }

    public void setImageCourse(byte[] imageCourse) {
        this.imageCourse = imageCourse;
    }

    public String getImageCourseContentType() {
        return imageCourseContentType;
    }

    public Course imageCourseContentType(String imageCourseContentType) {
        this.imageCourseContentType = imageCourseContentType;
        return this;
    }

    public void setImageCourseContentType(String imageCourseContentType) {
        this.imageCourseContentType = imageCourseContentType;
    }

    public Registration getRegistration() {
        return registration;
    }

    public Course registration(Registration registration) {
        this.registration = registration;
        return this;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name=" + getName() +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            ", price=" + getPrice() +
            ", places=" + getPlaces() +
            ", dateCourse='" + getDateCourse() + "'" +
            ", imageCourse='" + getImageCourse() + "'" +
            ", imageCourseContentType='" + getImageCourseContentType() + "'" +
            "}";
    }
}

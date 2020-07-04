package com.llacoste.registrationcourseapp.web.rest;

import com.llacoste.registrationcourseapp.RegistrationCourseApp;
import com.llacoste.registrationcourseapp.domain.Course;
import com.llacoste.registrationcourseapp.repository.CourseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = RegistrationCourseApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CourseResourceIT {

    private static final Long DEFAULT_NAME = 1L;
    private static final Long UPDATED_NAME = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final Long DEFAULT_PLACES = 1L;
    private static final Long UPDATED_PLACES = 2L;

    private static final Instant DEFAULT_DATE_COURSE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COURSE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_IMAGE_COURSE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_COURSE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_COURSE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_COURSE_CONTENT_TYPE = "image/png";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .address(DEFAULT_ADDRESS)
            .price(DEFAULT_PRICE)
            .places(DEFAULT_PLACES)
            .dateCourse(DEFAULT_DATE_COURSE)
            .imageCourse(DEFAULT_IMAGE_COURSE)
            .imageCourseContentType(DEFAULT_IMAGE_COURSE_CONTENT_TYPE);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .price(UPDATED_PRICE)
            .places(UPDATED_PLACES)
            .dateCourse(UPDATED_DATE_COURSE)
            .imageCourse(UPDATED_IMAGE_COURSE)
            .imageCourseContentType(UPDATED_IMAGE_COURSE_CONTENT_TYPE);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getPlaces()).isEqualTo(DEFAULT_PLACES);
        assertThat(testCourse.getDateCourse()).isEqualTo(DEFAULT_DATE_COURSE);
        assertThat(testCourse.getImageCourse()).isEqualTo(DEFAULT_IMAGE_COURSE);
        assertThat(testCourse.getImageCourseContentType()).isEqualTo(DEFAULT_IMAGE_COURSE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setName(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].places").value(hasItem(DEFAULT_PLACES.intValue())))
            .andExpect(jsonPath("$.[*].dateCourse").value(hasItem(DEFAULT_DATE_COURSE.toString())))
            .andExpect(jsonPath("$.[*].imageCourseContentType").value(hasItem(DEFAULT_IMAGE_COURSE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageCourse").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_COURSE))));
    }
    
    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.places").value(DEFAULT_PLACES.intValue()))
            .andExpect(jsonPath("$.dateCourse").value(DEFAULT_DATE_COURSE.toString()))
            .andExpect(jsonPath("$.imageCourseContentType").value(DEFAULT_IMAGE_COURSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageCourse").value(Base64Utils.encodeToString(DEFAULT_IMAGE_COURSE)));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .price(UPDATED_PRICE)
            .places(UPDATED_PLACES)
            .dateCourse(UPDATED_DATE_COURSE)
            .imageCourse(UPDATED_IMAGE_COURSE)
            .imageCourseContentType(UPDATED_IMAGE_COURSE_CONTENT_TYPE);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCourse.getPlaces()).isEqualTo(UPDATED_PLACES);
        assertThat(testCourse.getDateCourse()).isEqualTo(UPDATED_DATE_COURSE);
        assertThat(testCourse.getImageCourse()).isEqualTo(UPDATED_IMAGE_COURSE);
        assertThat(testCourse.getImageCourseContentType()).isEqualTo(UPDATED_IMAGE_COURSE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

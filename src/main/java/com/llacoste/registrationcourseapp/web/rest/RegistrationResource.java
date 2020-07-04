package com.llacoste.registrationcourseapp.web.rest;

import com.llacoste.registrationcourseapp.domain.Registration;
import com.llacoste.registrationcourseapp.repository.RegistrationRepository;
import com.llacoste.registrationcourseapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.llacoste.registrationcourseapp.domain.Registration}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RegistrationResource {

    private final Logger log = LoggerFactory.getLogger(RegistrationResource.class);

    private static final String ENTITY_NAME = "registration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistrationRepository registrationRepository;

    public RegistrationResource(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    /**
     * {@code POST  /registrations} : Create a new registration.
     *
     * @param registration the registration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registration, or with status {@code 400 (Bad Request)} if the registration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registrations")
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) throws URISyntaxException {
        log.debug("REST request to save Registration : {}", registration);
        if (registration.getId() != null) {
            throw new BadRequestAlertException("A new registration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Registration result = registrationRepository.save(registration);
        return ResponseEntity.created(new URI("/api/registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registrations} : Updates an existing registration.
     *
     * @param registration the registration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registration,
     * or with status {@code 400 (Bad Request)} if the registration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registrations")
    public ResponseEntity<Registration> updateRegistration(@RequestBody Registration registration) throws URISyntaxException {
        log.debug("REST request to update Registration : {}", registration);
        if (registration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Registration result = registrationRepository.save(registration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /registrations} : get all the registrations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registrations in body.
     */
    @GetMapping("/registrations")
    public List<Registration> getAllRegistrations() {
        log.debug("REST request to get all Registrations");
        return registrationRepository.findAll();
    }

    /**
     * {@code GET  /registrations/:id} : get the "id" registration.
     *
     * @param id the id of the registration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registrations/{id}")
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        log.debug("REST request to get Registration : {}", id);
        Optional<Registration> registration = registrationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(registration);
    }

    /**
     * {@code DELETE  /registrations/:id} : delete the "id" registration.
     *
     * @param id the id of the registration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registrations/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        log.debug("REST request to delete Registration : {}", id);
        registrationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

package com.llacoste.registrationcourseapp.web.rest;

import com.llacoste.registrationcourseapp.domain.ExtraUser;
import com.llacoste.registrationcourseapp.repository.ExtraUserRepository;
import com.llacoste.registrationcourseapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.llacoste.registrationcourseapp.domain.ExtraUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExtraUserResource {

    private final Logger log = LoggerFactory.getLogger(ExtraUserResource.class);

    private static final String ENTITY_NAME = "extraUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraUserRepository extraUserRepository;

    public ExtraUserResource(ExtraUserRepository extraUserRepository) {
        this.extraUserRepository = extraUserRepository;
    }

    /**
     * {@code POST  /extra-users} : Create a new extraUser.
     *
     * @param extraUser the extraUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extraUser, or with status {@code 400 (Bad Request)} if the extraUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extra-users")
    public ResponseEntity<ExtraUser> createExtraUser(@Valid @RequestBody ExtraUser extraUser) throws URISyntaxException {
        log.debug("REST request to save ExtraUser : {}", extraUser);
        if (extraUser.getId() != null) {
            throw new BadRequestAlertException("A new extraUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtraUser result = extraUserRepository.save(extraUser);
        return ResponseEntity.created(new URI("/api/extra-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extra-users} : Updates an existing extraUser.
     *
     * @param extraUser the extraUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraUser,
     * or with status {@code 400 (Bad Request)} if the extraUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extraUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extra-users")
    public ResponseEntity<ExtraUser> updateExtraUser(@Valid @RequestBody ExtraUser extraUser) throws URISyntaxException {
        log.debug("REST request to update ExtraUser : {}", extraUser);
        if (extraUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExtraUser result = extraUserRepository.save(extraUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extraUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /extra-users} : get all the extraUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extraUsers in body.
     */
    @GetMapping("/extra-users")
    public List<ExtraUser> getAllExtraUsers() {
        log.debug("REST request to get all ExtraUsers");
        return extraUserRepository.findAll();
    }

    /**
     * {@code GET  /extra-users/:id} : get the "id" extraUser.
     *
     * @param id the id of the extraUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extraUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extra-users/{id}")
    public ResponseEntity<ExtraUser> getExtraUser(@PathVariable Long id) {
        log.debug("REST request to get ExtraUser : {}", id);
        Optional<ExtraUser> extraUser = extraUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extraUser);
    }

    /**
     * {@code DELETE  /extra-users/:id} : delete the "id" extraUser.
     *
     * @param id the id of the extraUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extra-users/{id}")
    public ResponseEntity<Void> deleteExtraUser(@PathVariable Long id) {
        log.debug("REST request to delete ExtraUser : {}", id);
        extraUserRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

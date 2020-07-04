package com.llacoste.registrationcourseapp.repository;

import com.llacoste.registrationcourseapp.domain.Registration;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Registration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}

package com.llacoste.registrationcourseapp.repository;

import com.llacoste.registrationcourseapp.domain.ExtraUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExtraUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraUserRepository extends JpaRepository<ExtraUser, Long> {
}

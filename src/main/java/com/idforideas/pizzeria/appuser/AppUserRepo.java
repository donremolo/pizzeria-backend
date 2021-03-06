package com.idforideas.pizzeria.appuser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick Galan
 * @version 1.0
 * @since 2/28/2022
 */
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}

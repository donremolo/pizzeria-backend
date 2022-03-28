package com.idforideas.pizzeria.appuser;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Nick Galan
 * @version 1.0
 * @since 2/28/2022
 */
public interface AppUserService {

    AppUser create(AppUser user);

    Optional<AppUser> get(String email);

    Collection<AppUser> getUsers();

    void delete(Long id);
}

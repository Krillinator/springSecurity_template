package com.krillinator.springSecurityIntro.auth;

import java.util.Optional;

/** DAO - Queries */

public interface ApplicationUserDAO {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}

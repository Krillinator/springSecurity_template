package com.krillinator.springSecurityIntro.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.krillinator.springSecurityIntro.Security.ApplicationUserRole.ADMIN;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDAO{

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream() // List from Array
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    // Debug List of Users
    private List<ApplicationUser> getApplicationUsers() {

        List<ApplicationUser> applicationUsers = Lists.newArrayList(
            new ApplicationUser(
                    "Anton",
                    passwordEncoder.encode("123"),
                    ADMIN.getGrantedAuthorities(),
                    true,
                    true,
                    true,
                    true),

            new ApplicationUser(
                    "Benny",
                    passwordEncoder.encode("123"),
                    ADMIN.getGrantedAuthorities(),
                    true,
                    true,
                    true,
                    true)
            );
        return applicationUsers;
    }
}




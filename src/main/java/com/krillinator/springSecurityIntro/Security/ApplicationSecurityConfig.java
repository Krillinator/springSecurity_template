package com.krillinator.springSecurityIntro.Security;

// Mission = override (Spring Security Authentication/Authorization)

import com.krillinator.springSecurityIntro.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.krillinator.springSecurityIntro.Security.ApplicationUserRole.*;

@Configuration                                      // Override
@EnableWebSecurity                                  // Override
@EnableGlobalMethodSecurity(prePostEnabled = true)  // Enables @PreAuthorize Annotation
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().disable()
                .authorizeRequests() // Auth #1
                    .antMatchers("/", "/index", "css/*", "js/*", "/index", "/test").permitAll()    // endpoints argument
                    .antMatchers("/admin/**").hasRole(ADMIN.toString())          // Has Role Admin
                    .antMatchers("/student/**").hasRole(STUDENT.toString())      // Has Role Student
                    .anyRequest().authenticated()                                            // Any Request Auth & Username / Password
                    .and()
                .formLogin().loginPage("/login").permitAll()
                    .defaultSuccessUrl("/account", true) // Change default page after login
                    //.passwordParameter("bank-id") // Override default password
                    //.usernameParameter("email"); // Override default username
                    .and()
                .rememberMe()                                                            // Defaults to 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))     // Overrides default time
                    .key("SomeSecureKey")                                                 // Key for token validity
                    .userDetailsService(applicationUserService)
                    .rememberMeParameter("remember-me")                                    // default name = remember-me
                    .and()
                .logout()
                    .logoutUrl("/logout") // Default logout
                    //.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // Recommended IF CSRF is Disabled
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login"); // Redirect URL on Logout
    }

    // Wiring Provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    // Provider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);

        return provider;
    }
}

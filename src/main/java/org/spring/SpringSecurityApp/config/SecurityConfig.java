package org.spring.SpringSecurityApp.config;

import org.spring.SpringSecurityApp.security.AuthProviderImpl;
import org.spring.SpringSecurityApp.services.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final PersonDetailsService personDetailsService;
  private final AuthProviderImpl authProvider;

  public SecurityConfig(PersonDetailsService personDetailsService, AuthProviderImpl authProvider) {
    this.personDetailsService = personDetailsService;
    this.authProvider = authProvider;
  }

  /*  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }*/

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(
            formLogin ->
                formLogin.permitAll().defaultSuccessUrl("/hello", true).failureUrl("/login?error"))
        .rememberMe(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/resources/**");
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(personDetailsService);
    authenticationManagerBuilder.authenticationProvider(authProvider);
    return authenticationManagerBuilder.build();
  }
}

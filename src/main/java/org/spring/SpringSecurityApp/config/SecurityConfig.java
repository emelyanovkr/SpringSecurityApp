package org.spring.SpringSecurityApp.config;

import org.spring.SpringSecurityApp.services.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final PersonDetailsService personDetailsService;
  private final JWTFilter jwtFilter;

  public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
    this.personDetailsService = personDetailsService;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/auth/*", "/error")
                    .permitAll()
                    .anyRequest()
                    .hasAnyRole("ADMIN", "USER"))
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/auth/login")
                    .permitAll()
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/hello", true)
                    .failureUrl("/auth/login?error"))
        .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login"))
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .rememberMe(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

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
    authenticationManagerBuilder
        .userDetailsService(personDetailsService)
        .passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }
}

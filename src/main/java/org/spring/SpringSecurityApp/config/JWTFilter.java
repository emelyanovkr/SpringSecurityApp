package org.spring.SpringSecurityApp.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.spring.SpringSecurityApp.security.JwtUtil;
import org.spring.SpringSecurityApp.services.PersonDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final PersonDetailsService personDetailsService;

  public JWTFilter(JwtUtil jwtUtil, PersonDetailsService personDetailsService) {
    this.jwtUtil = jwtUtil;
    this.personDetailsService = personDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwtToken = authHeader.substring(7);

      if (jwtToken.isBlank()) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "INVALID JWT TOKEN IN HEADER");
      } else {
        try {
          String username = jwtUtil.validateTokenAndGetUsername(jwtToken);
          UserDetails userDetails = personDetailsService.loadUserByUsername(username);

          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, userDetails.getPassword(), userDetails.getAuthorities());

          if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
          }
        } catch (JWTVerificationException | UsernameNotFoundException e) {
          response.sendError(response.SC_BAD_REQUEST, "Authentication failed");
        }

        filterChain.doFilter(request, response);
      }
    }
  }
}

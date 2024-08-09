package org.spring.SpringSecurityApp.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void doAdmin() {
    System.out.println("Admin do admin");
  }
}

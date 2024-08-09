package org.spring.SpringSecurityApp.controllers;

import org.spring.SpringSecurityApp.security.PersonDetails;
import org.spring.SpringSecurityApp.services.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

  private final AdminService adminService;

  public HelloController(AdminService adminService) {
    this.adminService = adminService;
  }

  @GetMapping("/hello")
  public String sayHello() {
    return "hello";
  }

  @GetMapping("/showUser")
  public String showUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

    System.out.println(personDetails.getPerson());

    return "redirect:/hello";
  }

  @GetMapping("/admin")
  public String admingPage() {
    adminService.doAdmin();
    return "admin";
  }
}

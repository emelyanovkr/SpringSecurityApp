package org.spring.SpringSecurityApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.spring.SpringSecurityApp.dto.AuthenticationDTO;
import org.spring.SpringSecurityApp.dto.PersonDTO;
import org.spring.SpringSecurityApp.models.Person;
import org.spring.SpringSecurityApp.security.JwtUtil;
import org.spring.SpringSecurityApp.services.RegistrationService;
import org.spring.SpringSecurityApp.util.PersonValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final PersonValidator personValidator;
  private final RegistrationService registrationService;
  private final JwtUtil jwtUtil;
  private final ModelMapper modelMapper;
  private final AuthenticationManager authenticationManager;

  public AuthController(
      PersonValidator personValidator,
      RegistrationService registrationService,
      JwtUtil jwtUtil,
      ModelMapper modelMapper,
      AuthenticationManager authenticationManager) {
    this.personValidator = personValidator;
    this.registrationService = registrationService;
    this.jwtUtil = jwtUtil;
    this.modelMapper = modelMapper;
    this.authenticationManager = authenticationManager;
  }

  /*@GetMapping("/login")
  public String loginPage() {
    return "/auth/login";
  }*/

  @GetMapping("/registration")
  public String registrationPage(@ModelAttribute("person") Person person) {
    return "/auth/registration";
  }

  @PostMapping("/registration")
  public Map<String, String> performRegistration(
      @RequestBody PersonDTO personDTO, BindingResult bindingResult) {

    Person person = convertToPerson(personDTO);
    personValidator.validate(person, bindingResult);

    if (bindingResult.hasErrors()) {
      return Map.of("message", "Registration failed");
    }

    registrationService.register(person);

    return Map.of("jwt-token", jwtUtil.generateToken(person.getUserName()));
  }

  @PostMapping("/login")
  public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
    UsernamePasswordAuthenticationToken authInputToken =
        new UsernamePasswordAuthenticationToken(
            authenticationDTO.getUsername(), authenticationDTO.getPassword());

    try {
      authenticationManager.authenticate(authInputToken);
    } catch (BadCredentialsException e) {
      return Map.of("message", e.getMessage());
    }

    String token = jwtUtil.generateToken(authenticationDTO.getUsername());
    return Map.of("jwt-token", token);
  }

  public Person convertToPerson(PersonDTO personDTO) {
    return modelMapper.map(personDTO, Person.class);
  }
}

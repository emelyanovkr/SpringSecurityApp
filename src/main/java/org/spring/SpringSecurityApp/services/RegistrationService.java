package org.spring.SpringSecurityApp.services;

import org.spring.SpringSecurityApp.models.Person;
import org.spring.SpringSecurityApp.repositories.PeopleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

  private final PeopleRepository peopleRepository;
  private final PasswordEncoder passwordEncoder;

  public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
    this.peopleRepository = peopleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void register(Person person) {
    person.setPassword(passwordEncoder.encode(person.getPassword()));
    peopleRepository.save(person);
  }
}

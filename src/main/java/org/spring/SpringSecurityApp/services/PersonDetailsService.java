package org.spring.SpringSecurityApp.services;

import org.spring.SpringSecurityApp.models.Person;
import org.spring.SpringSecurityApp.repositories.PeopleRepository;
import org.spring.SpringSecurityApp.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
  private final PeopleRepository peopleRepository;

  public PersonDetailsService(PeopleRepository peopleRepository) {
    this.peopleRepository = peopleRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Person> person = peopleRepository.findByUserName(username);

    if (person.isEmpty()) {
      throw new UsernameNotFoundException("Username not found");
    }

    return new PersonDetails(person.get());
  }
}

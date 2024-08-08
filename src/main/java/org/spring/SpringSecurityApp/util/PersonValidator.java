package org.spring.SpringSecurityApp.util;

import org.spring.SpringSecurityApp.models.Person;
import org.spring.SpringSecurityApp.services.PersonDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
  private final PersonDetailsService personDetailsService;

  public PersonValidator(PersonDetailsService personDetailsService) {
    this.personDetailsService = personDetailsService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Person.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Person person = (Person) target;
    try {
      personDetailsService.loadUserByUsername(person.getUserName());
    } catch (UsernameNotFoundException e) {
      return;
    }

    errors.rejectValue("userName", null, "Username already exist");
  }
}

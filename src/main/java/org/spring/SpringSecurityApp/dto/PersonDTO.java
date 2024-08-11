package org.spring.SpringSecurityApp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {

  @NotEmpty(message = "Username can't be empty")
  @Size(min = 2, max = 100, message = "Username must be more than 2 and less than 100 symbols")
  private String userName;

  @Min(value = 1900, message = "Birthday can't be more than 1900")
  private int yearOfBirth;

  @NotEmpty(message = "Password must be set for an user")
  private String password;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(int yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

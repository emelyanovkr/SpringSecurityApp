package org.spring.SpringSecurityApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "username")
  @NotEmpty(message = "Username can't be empty")
  @Size(min = 2, max = 100, message = "Username must be more than 2 and less than 100 symbols")
  private String userName;

  @Column(name = "year_of_birth")
  @Min(value = 1900, message = "Birthday can't be more than 1900")
  private int yearOfBirth;

  @Column(name = "password")
  @NotEmpty(message = "Password must be set for an user")
  private String password;

  @Column(name = "role")
  private String role;

  public Person() {}

  public Person(String userName, int yearOfBirth) {
    this.userName = userName;
    this.yearOfBirth = yearOfBirth;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

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

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "Person{" + "userName='" + userName + '\'' + ", yearOfBirth=" + yearOfBirth + '}';
  }
}

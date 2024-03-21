package user;

import assets.Country;

import java.util.Objects;

public class User {
  private Long id;
  private String username;
  private String password;
  private String name;
  private String surname;
  private Country country;
  private String street;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  private User() {}

  public User(
      Long id,
      String username,
      String password,
      String name,
      String surname,
      Country country,
      String street) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.surname = surname;
    this.country = country;
    this.street = street;
  }

  @Override
  public String toString() {
    return String.format(
        "%s %s %s %s %s %s [%s]", id, username, password, name, surname, country, street);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(username, user.username)
        && Objects.equals(password, user.password)
        && Objects.equals(name, user.name)
        && Objects.equals(surname, user.surname)
        && country == user.country
        && Objects.equals(street, user.street);
  }
}

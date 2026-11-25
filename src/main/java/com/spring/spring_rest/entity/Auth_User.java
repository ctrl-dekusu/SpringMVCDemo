package com.spring.spring_rest.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity mapped to the {@code auth_user} database table representing 
 * an authenticated user account.
 */
@Entity 
@Table(name = "auth_user")
public class Auth_User {

  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @ManyToMany(
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL
  )
  @JoinTable(name = "authusers_authroles",
    joinColumns = @JoinColumn(name = "authuser_id", 
      referencedColumnName = "id"
    ),
    inverseJoinColumns = @JoinColumn(name = "authrole_id",
      referencedColumnName = "id"
    )
  )
  private List<Auth_Role> roles = new ArrayList<>();

  /**
   * Default no-argument constructor required by JPA.
   */
  public Auth_User() {}

  /**
   * Constructs a new {@code Auth_User} entity with the specified attributes.
   *
   * @param id the unique identifier of the user
   * @param username the user's login username
   * @param password the user's encoded password
   * @param roles the list of {@link Auth_Role} entities assigned to the user
   */
  public Auth_User(
    Long id, String username, 
    String password, 
    List<Auth_Role> roles
  ) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  /**
   * Returns the user's unique identifier.
   *
   * @return the user ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the user's unique identifier.
   *
   * @param id the user ID to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the user's username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the user's username.
   *
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns the user's password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's password.
   *
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the list of roles assigned to the user.
   *
   * @return the list of {@link Auth_Role} objects
   */
  public List<Auth_Role> getRoles() {
    return roles;
  }

  /**
   * Sets the list of roles assigned to the user.
   *
   * @param roles the list of {@link Auth_Role} objects to set
   */
  public void setRoles(List<Auth_Role> roles) {
    this.roles = roles;
  }

  
}

package com.spring.spring_rest.repository;

import com.spring.spring_rest.entity.Auth_User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository interface for performing CRUD and 
 * lookup operations on {@link Auth_User} entities.
 */
public interface AuthUserRepository extends JpaRepository<Auth_User, Long> {

  /**
   * Finds an {@link Auth_User} entity by its username.
   *
   * @param theUsername the username to search for
   * @return an {@link Optional} containing the matching 
   * {@link Auth_User}, or empty if not found
   */
  Optional<Auth_User> findByUsername(String theUsername);

  /**
   * Checks whether a user with the given username already exists.
   *
   * @param theUsername the username to check for existence
   * @return {@code true} if a user with the specified username exists, 
   * {@code false} otherwise
   */
  Boolean existsByUsername(String theUsername);
}

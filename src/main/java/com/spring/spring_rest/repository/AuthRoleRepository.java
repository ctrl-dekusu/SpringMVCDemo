package com.spring.spring_rest.repository;

import com.spring.spring_rest.entity.Auth_Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository interface for performing CRUD and 
 * lookup operations on {@link Auth_Role} entities.
 */
public interface AuthRoleRepository extends JpaRepository<Auth_Role, Long> {

  /**
   * Finds an {@link Auth_Role} entity by its role name.
   *
   * @param theName the name of the role to search for
   * @return an {@link Optional} containing the matching {@link Auth_Role}, 
   * or empty if not found
   */
  Optional<Auth_Role> findByName(String theName);

  /**
   * Checks whether a role with the given name already exists.
   *
   * @param theUsername the role name to check for existence
   * @return {@code true} if a role with the specified name exists, 
   * {@code false} otherwise
   */
  Boolean existsByName(String theUsername);
}

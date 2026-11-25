package com.spring.spring_rest.service;

import com.spring.spring_rest.entity.Auth_Role;
import com.spring.spring_rest.entity.Auth_User;
import com.spring.spring_rest.repository.AuthUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} that loads user-specific 
 * authentication data from the database.
 */
@Service 
public class CustomUserDetailService implements UserDetailsService {

  private final AuthUserRepository authUserRepo;

  /**
   * Constructs a {@code CustomUserDetailService} with the required 
   * {@link AuthUserRepository}.
   *
   * @param authUserRepo the repository used for accessing 
   * {@link Auth_User} records
   */
  public CustomUserDetailService(AuthUserRepository authUserRepo) {
    this.authUserRepo = authUserRepo;
  }

  /**
   * Loads a user's security details by their username.
   *
   * @param theUsername the username identifying the user whose 
   * data is required
   * @return a fully populated {@link UserDetails} instance
   * @throws UsernameNotFoundException if no user exists with the 
   * given username
   */
  public UserDetails loadUserByUsername(String theUsername) {
    Auth_User user = authUserRepo.findByUsername(theUsername)
              .orElseThrow(() -> new UsernameNotFoundException(
                "Username " + theUsername + " does not exist"
              ));
    return new User(user.getUsername(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
  }

  /**
   * Maps a list of {@link Auth_Role} entities to Spring Security 
   * {@link GrantedAuthority} objects.
   *
   * @param roles the list of {@link Auth_Role} entities assigned to a user
   * @return a collection of {@link GrantedAuthority} instances 
   * corresponding to the roles
   */
  private Collection<GrantedAuthority> mapRolesToAuthorities(
    List<Auth_Role> roles
  ) {
    return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
  }
}

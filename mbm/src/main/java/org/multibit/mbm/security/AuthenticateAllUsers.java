package org.multibit.mbm.security;

import org.multibit.mbm.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Handles the process of authenticating all users
 */
@Component("userService")
public class AuthenticateAllUsers implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(AuthenticateAllUsers.class);

  private final static GrantedAuthority CUSTOMER = new SimpleGrantedAuthority("ROLE_USER");
  private final static GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

  @Autowired
  private CustomerService customerService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("User '{}' logged in", username);
    // Keep track of their activity in the logs
    MDC.put("username", username);

    // In OpenId authentication the username is the URL
    customerService.persistAuthenticatedCustomer(username);

    // TODO Read the database to get GrantedAuthority

    // Provide a default UserDetails (no password)
    return new User(username, "", Collections.singleton(CUSTOMER));
  }
}

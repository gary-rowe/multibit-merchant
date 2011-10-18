package org.multibit.mbm.security;

import org.multibit.mbm.service.CustomerService;
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

@Component("userService")
public class AuthenticateAllUsers implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(AuthenticateAllUsers.class);
  private final static GrantedAuthority CUSTOMER = new SimpleGrantedAuthority("ROLE_USER");

  @Autowired
  private CustomerService customerService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("User '{}' logged in", username);
    MDC.put("user", username);
    customerService.haveBeenAuthenticated(username, "test");
    return new User(username, "", Collections.singleton(CUSTOMER));
  }
}

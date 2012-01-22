package org.multibit.mbm.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * <p>Handles the process of authenticating all {@link User}s</p>
 * <p>Several authentication schemes are supported:</p>
 * <ul>
 *   <li>Basic authentication (username+password in Base64 digest over HTTPS followed by IP-restricted session token)</li>
 *   <li>Digest authentication ()</li>
 *   <li>OpenId authentication (shared token from third-party)</li>
 * </ul>
 * <p>Typically, a User presents credentials which are matched against the database and then get {@link org.multibit.mbm.security.dto.Role}s assigned.</p>
 * <p>This is the Spring "userService" component since it does not get proxied.</p>
 */
@Component("userService")
public class AuthenticateAllUsers implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(AuthenticateAllUsers.class);

  private final static GrantedAuthority CUSTOMER = new SimpleGrantedAuthority("ROLE_USER");
  private final static GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

  @Resource(name="securityService")
  private SecurityService securityService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("User '{}' logged in", username);
    // Keep track of their activity in the logs
    MDC.put("username", username);

    // In OpenId authentication the username is the URL
    securityService.persistAuthenticatedUser(username);

    // TODO Read the database to get GrantedAuthority

    // Provide a default UserDetails (no password)
    return new User(username, "", Collections.singleton(CUSTOMER));
  }
}

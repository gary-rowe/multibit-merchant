package org.multibit.mbm.security.service;

import com.google.common.collect.Lists;
import org.multibit.mbm.security.dao.UserDao;
import org.multibit.mbm.security.dao.UserNotFoundException;
import org.multibit.mbm.security.dto.Authority;
import org.multibit.mbm.security.dto.Role;
import org.multibit.mbm.security.dto.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>Handles the process of authenticating all {@link User}s</p>
 * <p>Several authentication schemes are supported:</p>
 * <ul>
 * <li>Basic authentication (username+password in Base64 digest over HTTPS followed by IP-restricted session token)</li>
 * <li>Digest authentication ()</li>
 * <li>OpenId authentication (shared token from third-party)</li>
 * </ul>
 * <p>Typically, a User presents credentials which are matched against the database and then get {@link org.multibit.mbm.security.dto.Role}s assigned.</p>
 * <p>This is the Spring "userService" component since it does not get proxied.</p>
 */
@Component("userService")
public class AuthenticateAllUsers implements UserDetailsService, AuthenticationProvider {

  private final Logger log = LoggerFactory.getLogger(AuthenticateAllUsers.class);

  private final static GrantedAuthority CUSTOMER = new SimpleGrantedAuthority("ROLE_USER");
  private final static GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

  @Resource(name = "securityService")
  private SecurityService securityService;

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

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

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    Authentication fullyAuthenticated = null;
    List<GrantedAuthority> grantedAuthorities;

    try {

      if (authentication instanceof UsernamePasswordAuthenticationToken) {

        // Extract information from the username/password token
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        org.multibit.mbm.security.dto.User user = userDao.getUserByCredentials(username, password);

        grantedAuthorities = getGrantedAuthorities(user);

        fullyAuthenticated = new UsernamePasswordAuthenticationToken(user.getUUID(), null, grantedAuthorities);

      }

    } catch (UserNotFoundException e) {
      throw new UsernameNotFoundException("Authentication failed", e);
    }

    return fullyAuthenticated;

  }

  @Override
  public boolean supports(Class<?> authentication) {

    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));

  }

  /**
   * Determines the granted authorities for the User
   *
   * @param user The User
   *
   * @return The list of granted authorities based on all the User's Roles
   */
  private List<GrantedAuthority> getGrantedAuthorities(org.multibit.mbm.security.dto.User user) {
    List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
    for (UserRole userRole : user.getUserRoles()) {
      Role role = userRole.getRole();
      for (Authority authority : role.getAuthorities()) {
        grantedAuthorities.add(authority.toGrantedAuthority());
      }
    }
    return grantedAuthorities;
  }
}

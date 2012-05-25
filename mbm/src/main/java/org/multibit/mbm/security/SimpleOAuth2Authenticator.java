package org.multibit.mbm.security;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *         
 */
public class SimpleOAuth2Authenticator implements Authenticator<String, SimpleUser> {

  public SimpleOAuth2Authenticator() {
  }

  @Override
  public Optional<SimpleUser> authenticate(String s) throws AuthenticationException {
    if ("secret".equals(s)) {
      return Optional.of(new SimpleUser("example", "password"));
    }
    return Optional.absent();
  }
}

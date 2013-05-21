package org.multibit.mbm.auth.cookie;

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

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.slf4j.LoggerFactory;

/**
 * <p>Authentication provider to provide the following to Jersey:</p>
 * <ul>
 * <li>Bridge between Dropwizard and Jersey for HMAC authentication</li>
 * <li>Provides additional {@link org.multibit.mbm.auth.Authority} information</li>
 * </ul>
 *
 * @param <T> the principal type.
 *
 * @since 0.0.1
 */
public class CookieClientRestrictedToProvider<T> implements InjectableProvider<RestrictedTo, Parameter> {

  private static final Logger log = LoggerFactory.getLogger(CookieClientRestrictedToProvider.class);

  private final Authenticator<CookieClientCredentials, T> authenticator;
  private final String sessionTokenName;
  private final String rememberMeName;


  /**
   * Creates a new {@link CookieClientRestrictedToProvider} with the given {@link com.yammer.dropwizard.auth.Authenticator} and realm.
   *
   * @param authenticator    the authenticator which will take the {@link CookieClientCredentials} and
   *                         convert them into instances of {@code T}
   * @param sessionTokenName The session token name
   * @param rememberMeName   The "remember me" token name
   */
  public CookieClientRestrictedToProvider(
    Authenticator<CookieClientCredentials, T> authenticator,
    String sessionTokenName,
    String rememberMeName) {
    this.authenticator = authenticator;
    this.sessionTokenName = sessionTokenName;
    this.rememberMeName = rememberMeName;
  }

  @Override
  public ComponentScope getScope() {
    return ComponentScope.PerRequest;
  }

  @Override
  public Injectable<?> getInjectable(ComponentContext ic,
                                     RestrictedTo a,
                                     Parameter c) {
    return new CookieClientRestrictedToInjectable<T>(authenticator, sessionTokenName, rememberMeName, a.value());
  }
}


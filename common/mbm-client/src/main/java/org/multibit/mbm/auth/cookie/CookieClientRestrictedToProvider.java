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
import com.yammer.dropwizard.logging.Log;
import org.multibit.mbm.auth.annotation.RestrictedTo;

/**
 * <p>Authentication provider to provide the following to Jersey:</p>
 * <ul>
 * <li>Bridge between Dropwizard and Jersey for HMAC authentication</li>
 * <li>Provides additional {@link org.multibit.mbm.auth.Authority} information</li>
 * </ul>
 *
 * @param <T>    the principal type.
 * @since 0.0.1
 */
public class CookieClientRestrictedToProvider<T> implements InjectableProvider<RestrictedTo, Parameter> {
  static final Log LOG = Log.forClass(CookieClientRestrictedToProvider.class);

  private final Authenticator<CookieClientCredentials, T> authenticator;
  private final String realm;

  /**
   * Creates a new {@link CookieClientRestrictedToProvider} with the given {@link com.yammer.dropwizard.auth.Authenticator} and realm.
   *
   * @param authenticator the authenticator which will take the {@link CookieClientCredentials} and
   *                      convert them into instances of {@code T}
   * @param realm         the name of the authentication realm
   */
  public CookieClientRestrictedToProvider(Authenticator<CookieClientCredentials, T> authenticator, String realm) {
    this.authenticator = authenticator;
    this.realm = realm;
  }

  @Override
  public ComponentScope getScope() {
    return ComponentScope.PerRequest;
  }

  @Override
  public Injectable<?> getInjectable(ComponentContext ic,
                                     RestrictedTo a,
                                     Parameter c) {
    return new CookieClientRestrictedToInjectable<T>(authenticator, realm, a.value());
  }
}


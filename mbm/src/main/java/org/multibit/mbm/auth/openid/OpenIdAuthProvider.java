package org.multibit.mbm.auth.openid;

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
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.logging.Log;

/**
 * A Jersey provider for Basic HTTP authentication.
 *
 * @param <T>    the principal type.
 */
public class OpenIdAuthProvider<T> implements InjectableProvider<Auth, Parameter> {
  static final Log LOG = Log.forClass(OpenIdAuthProvider.class);

  private final Authenticator<OpenIdCredentials, T> authenticator;
  private final String realm;

  /**
   * Creates a new {@link OpenIdAuthProvider} with the given {@link Authenticator} and realm.
   *
   * @param authenticator    the authenticator which will take the {@link OpenIdCredentials} and
   *                         convert them into instances of {@code T}
   * @param realm            the name of the authentication realm
   */
  public OpenIdAuthProvider(Authenticator<OpenIdCredentials, T> authenticator, String realm) {
    this.authenticator = authenticator;
    this.realm = realm;
  }

  @Override
  public ComponentScope getScope() {
    return ComponentScope.PerRequest;
  }

  @Override
  public Injectable<?> getInjectable(ComponentContext ic,
                                     Auth a,
                                     Parameter c) {
    return new OpenIdAuthInjectable<T>(authenticator, realm, a.required());
  }
}


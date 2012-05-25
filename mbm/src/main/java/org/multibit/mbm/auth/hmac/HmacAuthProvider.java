package org.multibit.mbm.auth.hmac;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * A Jersey provider for Basic HTTP authentication.
 * @param <T>    the principal type.
 * @since 0.0.1
 * TODO Requires implementation
 * http://rc3.org/2011/12/02/using-hmac-to-authenticate-web-service-requests/ 
 */

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.logging.Log;

public class HmacAuthProvider<T> implements InjectableProvider<Auth, Parameter> {
  static final Log LOG = Log.forClass(HmacAuthProvider.class);

  private final Authenticator<HmacCredentials, T> authenticator;
  private final String realm;

  /**
   * Creates a new {@link org.multibit.mbm.auth.hmac.HmacAuthProvider} with the given {@link com.yammer.dropwizard.auth.Authenticator} and realm.
   *
   * @param authenticator    the authenticator which will take the {@link HmacCredentials} and
   *                         convert them into instances of {@code T}
   * @param realm            the name of the authentication realm
   */
  public HmacAuthProvider(Authenticator<HmacCredentials, T> authenticator, String realm) {
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
    return new HmacAuthInjectable<T>(authenticator, realm, a.required());
  }
}


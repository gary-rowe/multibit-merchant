package org.multibit.mbm.interfaces.rest.api.representations.hal.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.interfaces.rest.auth.Authority;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates a minimal representation of a User for the client to use</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class ClientUserRepresentation {

  public Representation get(User user, Optional<User> principal) {

    RepresentationFactory factory = new DefaultRepresentationFactory();
    Representation representation;

    if (user != null) {
      // Working with an authenticated User

      // Determine how the path should be presented
      String path;
      // TODO Consider a general Decorator
      if (principal.isPresent() && principal.get().hasAuthority(Authority.ROLE_ADMIN)) {
        path = "/admin/user/" + user.getId();
      } else if (user.getCustomer() != null) {
        path = "/customer/user";
      } else if (user.getSupplier() != null) {
        path = "/supplier/user";
      } else {
        throw new IllegalStateException("User does not have correct rights to be here ["+user.getId()+"]");
      }

      // The user will refer to their own profile implicitly
      representation = factory.newRepresentation(path)
        // The username and password digest are not required for any further authentication
        // If they are required it will be as part of a user profile update
        // The API and secret key are required for future user requests via HMAC
        .withProperty("api_key", user.getApiKey())
        .withProperty("secret_key", user.getSecretKey())
      // End of build
      ;
    } else {
      // The unauthenticated user will still refer to their own profile implicitly
      representation = factory.newRepresentation("/customer/user")
        // Provide empty credentials indicating a failure
        .withProperty("api_key", "")
        .withProperty("secret_key", "")
      // End of build
      ;
    }

    return representation;
  }

  public Representation get(User user) {

    return get(user, Optional.<User>absent());

  }

}

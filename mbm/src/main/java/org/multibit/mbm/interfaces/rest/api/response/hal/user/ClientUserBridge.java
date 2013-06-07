package org.multibit.mbm.interfaces.rest.api.response.hal.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.response.hal.BaseBridge;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.domain.model.model.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates a minimal representation of a User for the client to use</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class ClientUserBridge extends BaseBridge<User> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.domain.model.model.User} to provide a security principal
   */
  public ClientUserBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(User user) {

    ResourceFactory resourceFactory = getResourceFactory();
    Resource userResource;

    if (user != null) {
      // Working with an authenticated User

      // Determine how the resource path should be presented
      String path;
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
      userResource = resourceFactory.newResource(path)
        // The username and password digest are not required for any further authentication
        // If they are required it will be as part of a user profile update
        // The API and secret key are required for future user requests via HMAC
        .withProperty("api_key", user.getApiKey())
        .withProperty("secret_key", user.getSecretKey())
      // End of build
      ;
    } else {
      // The unauthenticated user will still refer to their own profile implicitly
      userResource = resourceFactory.newResource("/customer/user")
        // Provide empty credentials indicating a failure
        .withProperty("api_key", "")
        .withProperty("secret_key", "")
      // End of build
      ;
    }

    return userResource;

  }

}

package org.multibit.mbm.api.response.hal.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.User}:</p>
 * <ul>
 * <li>Creates representations of a User for the anonymous public</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class PublicUserBridge extends BaseBridge<User> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public PublicUserBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(User user) {

    ResourceFactory resourceFactory = getResourceFactory();
    Resource userResource;

    if (user == null) {
      // Specialist handling for an unauthenticated User
      userResource = resourceFactory.newResource("/user")
        // Provide empty credentials indicating a failure
        .withProperty("api_key", "")
        .withProperty("secret_key", "")
      // End of build
      ;

    } else {
      // The user will refer to their own profile with their API key
      userResource = resourceFactory.newResource("/user/" + user.getApiKey())
        // The username and password digest are not required for any further authentication
        // If they are required it will be as part of a user profile update
        // The API and secret key are required for future user requests via HMAC
        .withProperty("api_key", user.getApiKey())
        .withProperty("secret_key", user.getSecretKey())
      // End of build
      ;

    }

    return userResource;

  }

}

package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.User}:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class DefaultUserBridge extends BaseBridge<User> {

  /**
   * @param uriInfo The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param user    An optional User to provide a security principal
   */
  public DefaultUserBridge(UriInfo uriInfo, Optional<User> user) {
    super(uriInfo, user);
  }

  public Resource toResource(User user) {
    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/user")
      .withLink("search", "?q={query}")
      .withLink("description", "/description")
      .withProperty("id", user.getId())
      .withProperty("name", user.getPublicName())
      // End of build
      ;
  }

}

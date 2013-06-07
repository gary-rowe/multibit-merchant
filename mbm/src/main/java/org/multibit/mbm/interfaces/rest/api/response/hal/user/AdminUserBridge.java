package org.multibit.mbm.interfaces.rest.api.response.hal.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.response.hal.BaseBridge;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.domain.model.model.User} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminUserBridge extends BaseBridge<User> {

  private final CustomerUserBridge customerUserBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.domain.model.model.User} to provide a security principal
   */
  public AdminUserBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    customerUserBridge = new CustomerUserBridge(uriInfo,principal);
  }

  public Resource toResource(User user) {
    ResourceAsserts.assertNotNull(user, "user");
    ResourceAsserts.assertNotNull(user.getId(),"id");

    // Build on the Customer representation
    Resource userResource = customerUserBridge.toResource(user)
      // Must use individual property entries due to collections
      // The admin must have full visibility
      .withProperty("staff_member", user.isStaffMember())
      .withProperty("locked",user.isLocked())
      .withProperty("created_at",user.getCreatedAt())
      .withProperty("password_reset_at",user.getPasswordResetAt())
      // End of build
      ;

    return userResource;

  }

}

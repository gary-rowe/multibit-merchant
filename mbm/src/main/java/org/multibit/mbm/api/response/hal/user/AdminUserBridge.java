package org.multibit.mbm.api.response.hal.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.User}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.db.dto.User} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminUserBridge extends BaseBridge<User> {

  private final CustomerUserBridge customerUserBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminUserBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    customerUserBridge = new CustomerUserBridge(uriInfo,principal);
  }

  public Resource toResource(User user) {

    // Build on the Customer representation
    Resource userResource = customerUserBridge.toResource(user)
      // Must use individual property entries due to collections
      .withProperty("uuid", user.getUUID())
      .withProperty("staffMember", user.isStaffMember())
      .withProperty("locked",user.isLocked())
      .withProperty("createdAt",user.getCreatedAt())
      .withProperty("passwordResetAt",user.getPasswordResetAt())
      // End of build
      ;

    return userResource;

  }

}

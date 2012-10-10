package org.multibit.mbm.api.response.hal.role;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Role}:</p>
 * <ul>
 * <li>Creates representation of multiple Roles for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminRoleCollectionBridge extends BaseBridge<List<Role>> {

  private final AdminRoleBridge adminRoleBridge;

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminRoleCollectionBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
    adminRoleBridge = new AdminRoleBridge(uriInfo,principal);
  }

  public Resource toResource(List<Role> roles) {

    ResourceAsserts.assertNotNull(roles, "roles");

    ResourceFactory resourceFactory = getResourceFactory();

    Resource roleList = resourceFactory.newResource("/role");

    for (Role role : roles) {
      Resource roleResource = adminRoleBridge.toResource(role);

      roleList.withSubresource("/role/"+role.getId(), roleResource);
    }

    return roleList;

  }

}

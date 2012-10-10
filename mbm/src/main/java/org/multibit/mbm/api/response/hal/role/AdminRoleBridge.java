package org.multibit.mbm.api.response.hal.role;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.User}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.db.dto.User} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminRoleBridge extends BaseBridge<Role> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminRoleBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Role role) {

    ResourceAsserts.assertNotNull(role, "role");
    ResourceAsserts.assertNotNull(role.getId(),"id");

    // Build the representation
    Resource roleResource = getResourceFactory().newResource("/role/" + role.getId())
      // Must use individual property entries due to collections
      .withProperty("name", role.getName())
      .withProperty("description", role.getDescription())
      .withProperty("internal",role.isInternal())
      // End of build
      ;

    // Build a sub-resource representing all the authorities bound to this Role
    Resource authoritiesResource = getResourceFactory().newResource("authorities");
    for (Authority authority : role.getAuthorities()) {
      authoritiesResource.withProperty(authority.name(),Boolean.TRUE);
    }
    roleResource.withSubresource("authorities", authoritiesResource);

    return roleResource;

  }

}

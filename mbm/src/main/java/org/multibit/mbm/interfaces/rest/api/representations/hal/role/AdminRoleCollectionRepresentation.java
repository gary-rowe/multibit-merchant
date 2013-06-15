package org.multibit.mbm.interfaces.rest.api.representations.hal.role;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.interfaces.rest.api.hal.Representations;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Role}:</p>
 * <ul>
 * <li>Creates representation of multiple Roles for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminRoleCollectionRepresentation {

  public Representation get(PaginatedList<Role> roles) {

    Preconditions.checkNotNull(roles, "roles");

    URI self = UriBuilder.fromPath("/admin/role").build();
    Representation roleList = Representations.newPaginatedList(self, roles);

    for (Role role : roles.list()) {
      Representation roleRepresentation = new AdminRoleRepresentation().get(role);

      roleList.withRepresentation("/role/" + role.getId(), roleRepresentation);
    }

    return roleList;

  }

}

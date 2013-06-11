package org.multibit.mbm.interfaces.rest.api.representations.hal.role;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.Role;
import org.multibit.mbm.domain.model.model.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Role}:</p>
 * <ul>
 * <li>Creates representation of multiple Roles for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminRoleCollectionRepresentation {

  public Representation get(List<Role> roles) {

    Preconditions.checkNotNull(roles, "roles");

    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation roleList = factory.newRepresentation("/role");

    for (Role role : roles) {
      Representation roleRepresentation = new AdminRoleRepresentation().get(role);

      roleList.withRepresentation("/role/" + role.getId(), roleRepresentation);
    }

    return roleList;

  }

}

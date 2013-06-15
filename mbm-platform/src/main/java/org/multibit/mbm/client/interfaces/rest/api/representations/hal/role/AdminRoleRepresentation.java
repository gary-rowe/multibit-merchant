package org.multibit.mbm.client.interfaces.rest.api.representations.hal.role;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.client.domain.model.model.Role;
import org.multibit.mbm.client.interfaces.rest.auth.Authority;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.client.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.client.domain.model.model.User} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminRoleRepresentation {

  public Representation get(Role role) {

    Preconditions.checkNotNull(role, "role");
    Preconditions.checkNotNull(role.getId(), "id");

    // Build the representation
    Representation roleRepresentation = new DefaultRepresentationFactory().newRepresentation("/role/" + role.getId())
      // Must use individual property entries due to collections
      .withProperty("name", role.getName())
      .withProperty("description", role.getDescription())
      .withProperty("internal",role.isInternal())
      // End of build
      ;

    // Build a sub-Representationrepresenting all the authorities bound to this Role
    Representation authoritiesRepresentation = new DefaultRepresentationFactory().newRepresentation("authorities");
    for (Authority authority : role.getAuthorities()) {
      authoritiesRepresentation.withProperty(authority.name(),Boolean.TRUE);
    }
    roleRepresentation.withRepresentation("authorities", authoritiesRepresentation);

    return roleRepresentation;

  }

}

package org.multibit.mbm.client.interfaces.rest.api.representations.hal.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.client.common.pagination.PaginatedList;
import org.multibit.mbm.client.domain.model.model.User;
import org.multibit.mbm.client.interfaces.rest.api.hal.Representations;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.client.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates representation of multiple Users for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminUserCollectionRepresentation {

  private final CustomerUserRepresentation customerUserRepresentation = new CustomerUserRepresentation();

  public Representation get(PaginatedList<User> users, Optional<User> principal) {

    Preconditions.checkNotNull(users, "users");

    URI self = UriBuilder.fromPath("/admin/user").build();
    Representation userList = Representations.newPaginatedList(self, users);

    for (User user : users.list()) {
      Representation userRepresentation = customerUserRepresentation.get(user, principal);

      // TODO Fill this in for all admin fields
      //userRepresentation.withProperty("id", user.getId())
      // End of build
      ;

      userList.withRepresentation("/user/" + user.getId(), userRepresentation);
    }

    return userList;

  }

  public Representation get(PaginatedList<User> users) {
    return get(users, Optional.<User>absent());

  }

}

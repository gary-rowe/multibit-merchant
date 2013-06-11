package org.multibit.mbm.interfaces.rest.api.representations.hal.user;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.multibit.mbm.domain.model.model.User;

import java.util.List;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates representation of multiple Users for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminUserCollectionRepresentation {

  private final CustomerUserRepresentation customerUserRepresentation = new CustomerUserRepresentation();

  public Representation get(List<User> users) {
    Preconditions.checkNotNull(users, "users");

    RepresentationFactory factory = new DefaultRepresentationFactory();

    Representation userList = factory.newRepresentation();

    for (User user : users) {
      Representation userRepresentation = customerUserRepresentation.get(user);

      // TODO Fill this in for all admin fields
      //userRepresentation.withProperty("id", user.getId())
        // End of build
        ;

      userList.withRepresentation("/user/" + user.getId(), userRepresentation);
    }

    return userList;

  }

}

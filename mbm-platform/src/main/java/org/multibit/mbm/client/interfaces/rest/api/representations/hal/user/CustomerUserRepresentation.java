package org.multibit.mbm.client.interfaces.rest.api.representations.hal.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.client.domain.model.model.ContactMethod;
import org.multibit.mbm.client.domain.model.model.ContactMethodDetail;
import org.multibit.mbm.client.domain.model.model.User;

import java.util.Map;
import java.util.Set;

/**
 * <p>Representation to provide the following to {@link User}:</p>
 * <ul>
 * <li>Creates representations of a User for a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CustomerUserRepresentation {

  private final ClientUserRepresentation clientUserRepresentation = new ClientUserRepresentation();

  public Representation get(User user, Optional<User> principal) {

    // Build on the minimal representation
    Representation representation = clientUserRepresentation.get(user, principal);

    // Apply restrictions against the more detailed representation
    Preconditions.checkNotNull(user, "user");
    Preconditions.checkNotNull(user.getId(), "id");

    // Add properties
    representation.withProperty("username", user.getUsername());

    // Convert the ContactMethodDetails map into primary and secondary property entries
    for (Map.Entry<ContactMethod, ContactMethodDetail> entry : user.getContactMethodMap().entrySet()) {
      String propertyName = entry.getKey().getPropertyNameSingular();
      ContactMethodDetail contactMethodDetail = entry.getValue();
      String primaryDetail = contactMethodDetail.getPrimaryDetail();

      representation.withProperty(propertyName, primaryDetail);

      // Determine if secondary details should be included
      if (entry.getKey().isSecondaryDetailSupported()) {
        Set<String> secondaryDetails = contactMethodDetail.getSecondaryDetails();
        // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
        int index = 1;
        for (String secondaryDetail : secondaryDetails) {
          representation.withProperty(propertyName + index, secondaryDetail);
          index++;
        }
      }
    }

    return representation;

  }

  public Representation get(User user) {

    return get(user, Optional.<User>absent());
  }

}

package org.multibit.mbm.interfaces.rest.api.representations.hal.user;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.model.model.ContactMethod;
import org.multibit.mbm.domain.model.model.ContactMethodDetail;
import org.multibit.mbm.domain.model.model.User;

import java.util.Map;
import java.util.Set;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.User}:</p>
 * <ul>
 * <li>Creates representations of a User for a Supplier</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierUserRepresentation {

  public Representation get(User user) {

    // Build on the minimal representation
    Representation userRepresentation = new ClientUserRepresentation().get(user);

    // Apply restrictions against the more detailed representation
    Preconditions.checkNotNull(user, "user");
    Preconditions.checkNotNull(user.getId(), "id");

    // Add properties
    userRepresentation.withProperty("username", user.getUsername());

    // Convert the ContactMethodDetails map into primary and secondary property entries
    for (Map.Entry<ContactMethod, ContactMethodDetail> entry : user.getContactMethodMap().entrySet()) {
      String propertyName = entry.getKey().getPropertyNameSingular();
      ContactMethodDetail contactMethodDetail = entry.getValue();
      String primaryDetail = contactMethodDetail.getPrimaryDetail();

      userRepresentation.withProperty(propertyName, primaryDetail);

      // Determine if secondary details should be included
      if (entry.getKey().isSecondaryDetailSupported()) {
        Set<String> secondaryDetails = contactMethodDetail.getSecondaryDetails();
        // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
        int index = 1;
        for (String secondaryDetail : secondaryDetails) {
          userRepresentation.withProperty(propertyName + index, secondaryDetail);
          index++;
        }
      }
    }

    return userRepresentation;

  }

}

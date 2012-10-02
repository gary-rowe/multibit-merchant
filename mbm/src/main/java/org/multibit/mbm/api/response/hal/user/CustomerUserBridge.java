package org.multibit.mbm.api.response.hal.user;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.ContactMethod;
import org.multibit.mbm.db.dto.ContactMethodDetail;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.Set;

/**
 * <p>Bridge to provide the following to {@link User}:</p>
 * <ul>
 * <li>Creates representations of a User for a Customer</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CustomerUserBridge extends BaseBridge<User> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link User} to provide a security principal
   */
  public CustomerUserBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(User user) {
    ResourceFactory resourceFactory = getResourceFactory();

    Resource userResource = resourceFactory.newResource("/user/" + user.getId())
      .withProperty("id",user.getId())
      .withProperty("username", user.getUsername())
      // Do not return password - it is useless
      .withProperty("secretKey", user.getSecretKey())
      // End of build
      ;

    // Convert the ContactMethodDetails map into primary and secondary property entries
    for (Map.Entry<ContactMethod, ContactMethodDetail> entry : user.getContactMethodMap().entrySet()) {
      String propertyName = entry.getKey().getPropertyName();
      ContactMethodDetail contactMethodDetail = entry.getValue();
      String primaryDetail = contactMethodDetail.getPrimaryDetail();

      userResource.withProperty(propertyName, primaryDetail);

      // Determine if secondary details should be included
      if (entry.getKey().isSecondaryDetailSupported()) {
        Set<String> secondaryDetails = contactMethodDetail.getSecondaryDetails();
        // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
        int index = 1;
        for (String secondaryDetail : secondaryDetails) {
          userResource.withProperty(propertyName + index, secondaryDetail);
          index++;
        }
      }
    }

    return userResource;

  }

}

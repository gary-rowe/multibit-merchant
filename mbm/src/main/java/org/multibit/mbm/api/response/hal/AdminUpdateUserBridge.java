package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.db.dto.ContactMethod;
import org.multibit.mbm.db.dto.ContactMethodDetail;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.Set;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.User}:</p>
 * <ul>
 * <li>Creates a representation of a single {@link org.multibit.mbm.db.dto.User} update for an administrator</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class AdminUpdateUserBridge extends BaseBridge<User> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public AdminUpdateUserBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(User user) {
    ResourceFactory resourceFactory = getResourceFactory();

    // TODO Fill this in for all fields
    String rel = "/user/" + user.getId();
    Resource userResource = resourceFactory
      .newResource(rel)
      // Must use individual property entries due to collections
      .withProperty("id", user.getId())
      .withProperty("uuid", user.getUUID())
      .withProperty("openId", user.getOpenId())
      .withProperty("username", user.getUsername())
      .withProperty("password",user.getPassword())
      .withProperty("publicName",user.getPublicName())
      .withProperty("createdAt",user.getCreatedAt())
      .withProperty("passwordResetAt",user.getPasswordResetAt())
      .withProperty("secretKey",user.getSecretKey())
      // End of build
      ;

    // Convert the ContactMethodDetails map into primary and secondary property entries
    for (Map.Entry<ContactMethod, ContactMethodDetail> entry : user.getContactMethodMap().entrySet()) {
      String propertyName = entry.getKey().getPropertyName();
      ContactMethodDetail contactMethodDetail = entry.getValue();
      String primaryDetail = contactMethodDetail.getPrimaryDetail();

      userResource.withProperty(propertyName,primaryDetail);

      // Determine if secondary details should be included
      if (entry.getKey().isSecondaryDetailSupported()) {
        Set<String> secondaryDetails = contactMethodDetail.getSecondaryDetails();
        // TODO Consider if a 1-based field index is the best representation here: array? sub-resource?
        int index=1;
        for (String secondaryDetail: secondaryDetails) {
          userResource.withProperty(propertyName+index,secondaryDetail);
          index++;
        }
      }
    }

    return userResource;

  }

}

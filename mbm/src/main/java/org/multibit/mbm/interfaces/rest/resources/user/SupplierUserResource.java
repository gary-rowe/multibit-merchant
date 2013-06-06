package org.multibit.mbm.interfaces.rest.resources.user;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;
import org.multibit.mbm.interfaces.rest.api.response.hal.user.ClientUserBridge;
import org.multibit.mbm.interfaces.rest.api.response.hal.user.SupplierUserBridge;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.interfaces.rest.auth.annotation.RestrictedTo;
import org.multibit.mbm.domain.model.model.*;
import org.multibit.mbm.domain.repositories.UserDao;
import org.multibit.mbm.interfaces.rest.resources.BaseResource;
import org.multibit.mbm.infrastructure.utils.DateUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage operations by a Supplier</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/supplier/user")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class SupplierUserResource extends BaseResource {

  @Resource(name = "hibernateUserDao")
  private UserDao userDao;

  /**
   * @param supplierUser The authenticated Supplier
   *
   * @return A HAL representation of the result
   */
  @GET
  @Timed
  public Response retrieveOwnUser(
    @RestrictedTo({Authority.ROLE_SUPPLIER})
    User supplierUser) {

    SupplierUserBridge bridge = new SupplierUserBridge(uriInfo, Optional.of(supplierUser));

    return ok(bridge, supplierUser);

  }

  /**
   * @param supplierUser The Supplier User
   *
   * @return A HAL representation of the result
   */
  @DELETE
  @Timed
  public Response deregisterUser(
    @RestrictedTo({Authority.ROLE_SUPPLIER})
    User supplierUser) {

    Preconditions.checkNotNull(supplierUser);

    // Remove all identifying information from the User
    // but leave the entity available for audit purposes
    // We leave the secret key in case the user has been
    // accidentally deleted and the user wants to be
    // reinstated
    supplierUser.setApiKey("");
    supplierUser.setContactMethodMap(Maps.<ContactMethod, ContactMethodDetail>newHashMap());
    supplierUser.setUsername("");
    supplierUser.setPasswordDigest("");
    supplierUser.setPasswordResetAt(DateUtils.nowUtc());
    supplierUser.setLocked(true);
    supplierUser.setDeleted(true);
    supplierUser.setReasonForDelete("Supplier deregistered");
    supplierUser.setUserFieldMap(Maps.<UserField, UserFieldDetail>newHashMap());

    // Persist the User with cascade for the Supplier
    User persistentUser = userDao.saveOrUpdate(supplierUser);

    // Provide a minimal representation to the client
    // so that they can see their secret key as a last resort
    // manual recovery option
    ClientUserBridge bridge = new ClientUserBridge(uriInfo, Optional.of(supplierUser));
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentUser.getApiKey()).build();

    return created(bridge, persistentUser, location);

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
}
package org.multibit.mbm.api.response.hal.customer;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.response.hal.BaseBridge;
import org.multibit.mbm.db.dto.Customer;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Customer}:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class CustomerBridge extends BaseBridge<Customer> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.db.dto.User} to provide a security principal
   */
  public CustomerBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Customer customer) {

    if (customer.getId() == null) {
      throw new IllegalArgumentException("Cannot respond with a transient Customer. Id is null.");
    }

    String basePath = "/customer/" + customer.getId();

    // Create top-level resource
    return getResourceFactory()
      .newResource(basePath)
      .withLink("/user/" + customer.getUser().getId(), "user")
      .withLink("/cart/" + customer.getCart().getId(), "cart");
  }

}

package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
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
// TODO Replace Customer with CustomerResponse
public class DefaultCustomerBridge extends BaseBridge<Customer> {

  /**
   * @param uriInfo The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal    An optional {@link User} to provide a security principal
   */
  public DefaultCustomerBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Customer customer) {
    ResourceFactory resourceFactory = getResourceFactory();

    return resourceFactory.newResource("/customer")
      .withLink("search", "?q={query}")
      .withLink("description", "/description")
      .withProperty("id", customer.getId())
      // End of build
      ;
  }

}

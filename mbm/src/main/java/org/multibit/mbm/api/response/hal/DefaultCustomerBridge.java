package org.multibit.mbm.api.response.hal;

import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.db.dto.Customer;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.db.dto.Customer}:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class DefaultCustomerBridge extends BaseBridge<Customer> {

  private final String href;

  public DefaultCustomerBridge(String href) {
    this.href = href;
  }

  public Resource toResource(Customer customer) {
    ResourceFactory resourceFactory = getResourceFactory(href);

    return resourceFactory.newResource("/customer")
      .withLink("search", "?q={query}")
      .withLink("description", "/description")
      .withProperty("id", customer.getId())
      // End of build
      ;
  }

}

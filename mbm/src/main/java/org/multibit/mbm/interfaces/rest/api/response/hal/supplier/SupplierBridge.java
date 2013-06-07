package org.multibit.mbm.interfaces.rest.api.response.hal.supplier;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.interfaces.rest.api.response.hal.BaseBridge;
import org.multibit.mbm.domain.model.model.Supplier;
import org.multibit.mbm.domain.model.model.User;
import org.multibit.mbm.interfaces.rest.resources.ResourceAsserts;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Bridge to provide the following to {@link org.multibit.mbm.domain.model.model.Supplier}:</p>
 * <ul>
 * <li>Creates {@link com.theoryinpractise.halbuilder.spi.Resource} representations</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierBridge extends BaseBridge<Supplier> {

  /**
   * @param uriInfo   The {@link javax.ws.rs.core.UriInfo} containing the originating request information
   * @param principal An optional {@link org.multibit.mbm.domain.model.model.User} to provide a security principal
   */
  public SupplierBridge(UriInfo uriInfo, Optional<User> principal) {
    super(uriInfo, principal);
  }

  public Resource toResource(Supplier supplier) {
    ResourceAsserts.assertNotNull(supplier, "supplier");
    ResourceAsserts.assertNotNull(supplier.getId(),"id");

    String basePath = "/suppliers/" + supplier.getId();

    // Create top-level resource
    return getResourceFactory()
      .newResource(basePath)
      .withLink("/users/" + supplier.getUser().getId(), "user");
  }

}

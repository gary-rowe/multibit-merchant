package org.multibit.mbm.interfaces.rest.api.representations.hal.supplier;

import com.google.common.base.Preconditions;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.model.model.Supplier;

/**
 * <p>Representation to provide the following to {@link org.multibit.mbm.domain.model.model.Supplier}:</p>
 * <ul>
 * <li>Creates {@link Representation} representations</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class SupplierRepresentation {

  public Representation get(Supplier supplier) {
    Preconditions.checkNotNull(supplier, "supplier");
    Preconditions.checkNotNull(supplier.getId(), "id");

    String basePath = "/suppliers/" + supplier.getId();

    // Create top-level resource
    return new DefaultRepresentationFactory()
      .newRepresentation(basePath)
      .withLink("user","/users/" + supplier.getUser().getId());
  }

}

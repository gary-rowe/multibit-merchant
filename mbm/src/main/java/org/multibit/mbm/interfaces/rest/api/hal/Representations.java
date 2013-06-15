package org.multibit.mbm.interfaces.rest.api.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.common.pagination.PaginatedList;
import org.multibit.mbm.interfaces.rest.common.RepresentationBuilder;

import java.net.URI;

/**
 * <p>Abstract base class to provide the following to subclasses:</p>
 * <ul>
 * <li>Provision of common support methods</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class Representations {

  /**
   * <p>Provides an initial "paginated list" representation</p>
   *
   * @param self          The URI for this resource (standard pagination parameters will be added)
   * @param paginatedList The paginated list to represent
   *
   * @return The {@link Representation} with pagination links in place
   */
  public static <T> Representation newPaginatedList(URI self, PaginatedList<T> paginatedList) {

    return RepresentationBuilder
      .newInstance(self, Optional.absent())
      .withPagination(paginatedList.pagination())
      .build();
  }

  /**
   * @return A {@link Representation} from the default factory
   */
  private static Representation newRepresentation(URI self) {
    return new DefaultRepresentationFactory().newRepresentation(self);
  }

}

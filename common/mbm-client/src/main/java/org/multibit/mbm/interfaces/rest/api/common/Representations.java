package org.multibit.mbm.interfaces.rest.api.common;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.common.Identifiable;
import org.multibit.mbm.domain.common.pagination.PaginatedList;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;

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
   * <p>Provides a "paginated list" representation with each item in the list being included in the side-loaded "_embedded" section.</p>
   * <p>In this form the list items should only include a summary of the information from the underlying domain object.
   * This summary should be relevant within the context of the request (e.g. an admin context would have more detail than a
   * public user one).</p>
   *
   * @param self          The URI to this resource
   * @param paginatedList The paginated list to represent
   * @param uriTemplate   The URI template describing how each item is to be identified (e.g. "/example/{id}")
   *
   * @return The {@link Representation}
   */
  public static <T extends Identifiable> Representation asPaginatedList(URI self, PaginatedList<T> paginatedList, String uriTemplate) {

    Map<String, List<Representation>> embedded = createEmbedded(paginatedList, uriTemplate);

    Representation representation = RepresentationBuilder
      .newInstance(self, Optional.absent())
      .withPagination(paginatedList.pagination())
      .withEmbedded(embedded)
      .build();

    return representation;
  }

  /**
   * <p>Provides a "paginated list" representation with each item in the list being included in the side-loaded "_embedded" section.</p>
   * <p>In this form the list items should only include a summary of the information from the underlying domain object.
   * This summary should be relevant within the context of the request (e.g. an admin context would have more detail than a
   * public user one).</p>
   *
   * @param representation          The representation of the {@link org.multibit.mbm.domain.common.pagination.PaginatedList}
   *
   * @return The {@link Representation}
   */
  public static <T extends Identifiable> PaginatedList<T> fromPaginatedList(Representation representation) {

    return null;
  }

  /**
   * <p>Provides the representation of a DTO suitable for a "detail" view such as one dedicated to editing
   * the entity.</p>
   * <p>In this form the representation shows all of the information from the underlying object that is
   * relevant within the context of the request (e.g. an admin context would have more detail than a public user one)</p>
   *
   *
   * @param self The URI identifying this DTO
   *
   * @param dto The dto to represent
   * @return The {@link Representation}
   */
  public static Representation asDetail(URI self, Identifiable dto) {

    return newRepresentation(self).withBean(dto);
  }



  /**
   * <p>Creates the embedded DTO map</p>
   *
   * @param paginatedList A paginated list of {@link Identifiable} DTOs
   * @param uriTemplate   The URI template to use to provide embedded "self" links for each DTO
   *
   * @return A map suitable for use with {@link RepresentationBuilder#withEmbedded(java.util.Map)}
   */
  private static <T extends Identifiable> Map<String, List<Representation>> createEmbedded(PaginatedList<T> paginatedList, String uriTemplate) {
    Map<String, List<Representation>> embedded = Maps.newHashMap();
    for (T dto : paginatedList.list()) {

      // Require the Identifiable interface to ensure we can build the "self" URI
      URI self = buildSelfUri(dto, uriTemplate);

      Representation representation = newRepresentation(self);
      representation.withBean(dto);

    }
    return embedded;
  }

  /**
   * @param dto        The {@link Identifiable} DTO
   * @param uriTemplate The URI template providing the placement of the ID
   *
   * @return A URI suitable for use as a "self" link
   */
  private static URI buildSelfUri(Identifiable dto, String uriTemplate) {
    return UriBuilder.fromPath(uriTemplate).build(dto.getId());
  }

  /**
   * @return A default {@link Representation}
   */
  private static Representation newRepresentation(URI self) {
    return new DefaultRepresentationFactory().newRepresentation(self);
  }

}

package org.multibit.mbm.interfaces.rest.common;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.domain.common.Identifiable;
import org.multibit.mbm.domain.common.pagination.PaginatedList;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Collection;
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
   * @param name          The name to give the embedded collection
   * @param paginatedList The paginated list to represent
   * @param uriTemplate   The URI template describing how each item is to be identified (e.g. "/example/{id}")
   *
   * @return The {@link Representation}
   */
  public static <T extends Identifiable> Representation asPaginatedList(URI self, String name, PaginatedList<T> paginatedList, String uriTemplate) {

    List<Representation> embedded = asEmbedded(paginatedList.list(), uriTemplate);

    Map<String, List<Representation>> embeddedMap = Maps.newHashMap();
    embeddedMap.put(name, embedded);

    return RepresentationBuilder
      .newInstance(self, Optional.absent())
      .withPagination(paginatedList.pagination())
      .withEmbedded(embeddedMap)
      .build();
  }

  /**
   * <p>Provides a "paginated list" representation with each item in the list being included in the side-loaded "_embedded" section.</p>
   * <p>In this form the list items should only include a summary of the information from the underlying domain object.
   * This summary should be relevant within the context of the request (e.g. an admin context would have more detail than a
   * public user one).</p>
   *
   * @param representation The representation of the {@link org.multibit.mbm.domain.common.pagination.PaginatedList}
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
   * @param self  The URI identifying this DTO
   * @param dto   The dto to represent
   * @param links The additional links (can include URI templates if required)
   *
   * @return The {@link Representation}
   */
  public static Representation asDetail(URI self, Identifiable dto, Map<String, String> links) {

    Representation representation = newRepresentation(self);

    for (Map.Entry<String, String> entry : links.entrySet()) {
      representation.withLink(entry.getKey(), entry.getValue());
    }

    return representation.withBean(dto);
  }

  /**
   * <p>Provides the representation of a DTO suitable for a "detail" view such as one dedicated to editing
   * the entity.</p>
   * <p>In this form the representation shows all of the information from the underlying object that is
   * relevant within the context of the request (e.g. an admin context would have more detail than a public user one)</p>
   *
   * @param self        The URI identifying this DTO
   * @param dto         The dto to represent
   * @param links       The additional links (can include URI templates if required)
   * @param embeddedMap The map providing side-loaded representations (reduces request counts for complex representations)
   *
   * @return The {@link Representation}
   */
  public static Representation asDetailWithEmbedded(URI self, Identifiable dto, Map<String, String> links, Map<String, List<Representation>> embeddedMap) {

    // Create the initial detail view
    Representation representation = asDetail(self, dto, links);

    // Add side-loading
    for (Map.Entry<String, List<Representation>> entry : embeddedMap.entrySet()) {

      String name = entry.getKey();

      for (Representation item : entry.getValue()) {

        representation.withRepresentation(name, item);
      }
    }

    return representation;

  }

  /**
   * <p>Creates the embedded DTO list</p>
   *
   * @param collection  A collection of {@link Identifiable} DTOs
   * @param uriTemplate The URI template to use to provide embedded "self" links for each DTO
   *
   * @return A list suitable for use with {@link Representations#asDetailWithEmbedded(java.net.URI, org.multibit.mbm.domain.common.Identifiable, java.util.Map, java.util.Map)}
   */
  public static <T extends Identifiable> List<Representation> asEmbedded(Collection<T> collection, String uriTemplate) {

    List<Representation> embedded = Lists.newArrayList();

    for (T dto : collection) {
      embedded.add(asEmbedded(dto, uriTemplate));
    }

    return embedded;
  }

  /**
   * <p>Creates the embedded DTO list</p>
   *
   * @param dto  An {@link Identifiable} DTO
   * @param uriTemplate The URI template to use to provide embedded "self" links for each DTO
   *
   * @return A list suitable for use with {@link Representations#asDetailWithEmbedded(java.net.URI, org.multibit.mbm.domain.common.Identifiable, java.util.Map, java.util.Map)}
   */
  public static <T extends Identifiable> Representation asEmbedded(T dto, String uriTemplate) {

      // Require the Identifiable interface to ensure we can build the "self" URI
      URI self = buildSelfUri(dto, uriTemplate);

      Representation representation = newRepresentation(self);
      representation.withBean(dto);

      return representation;

  }

  /**
   * @param dto         The {@link Identifiable} DTO
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

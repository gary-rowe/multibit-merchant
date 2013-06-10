package org.multibit.mbm.interfaces.rest.resources;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.api.Representation;
import org.multibit.mbm.interfaces.rest.api.hal.HalMediaType;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import java.net.URI;

/**
 * <p>Abstract base class to provide the following to resources:</p>
 * <ul>
 * <li>Provision of common methods dealing with REST representations within HTTP</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseResource {

  @Context
  protected UriInfo uriInfo;

  @Context
  protected HttpHeaders httpHeaders;

  /**
   * @param representation The representation for the entity
   *
   * @return A configured HTTP 200 OK response
   */
  protected Response ok(Representation representation) {
    MediaType acceptedMediaType = getAcceptedMediaType(httpHeaders);

    String body = representation.toString(acceptedMediaType.toString());

    return Response.ok().type(acceptedMediaType).entity(body).build();
  }

  /**
   * @param representation The representation for the entity
   *
   * @return A configured HTTP 201 CREATED response
   */
  protected Response created(Representation representation, URI location) {
    MediaType acceptedMediaType = getAcceptedMediaType(httpHeaders);

    String body = representation.toString(acceptedMediaType.toString());

    return Response.created(location).type(acceptedMediaType).entity(body).build();
  }

  /**
   * @return An exception covering a HTTP BAD_REQUEST
   */
  protected WebApplicationException badRequest() {
    return new WebApplicationException(Response.Status.BAD_REQUEST);
  }

  /**
   * @return An exception covering a HTTP NOT_FOUND
   */
  protected WebApplicationException notFound() {
    return new WebApplicationException(Response.Status.NOT_FOUND);
  }

  /**
   * Determines which of the acceptable media types will be the chosen one
   * based on the q-factor
   *
   * @param httpHeaders The HTTP headers of the request
   *
   * @return The accepted media type to be used
   */
  private MediaType getAcceptedMediaType(HttpHeaders httpHeaders) {
    Optional<MediaType> accepted = Optional.absent();
    for (MediaType mediaType : httpHeaders.getAcceptableMediaTypes()) {
      if (mediaType.isCompatible(HalMediaType.APPLICATION_HAL_JSON_TYPE)) {
        accepted = Optional.of(HalMediaType.APPLICATION_HAL_JSON_TYPE);
        break;
      }
      if (mediaType.isCompatible(HalMediaType.APPLICATION_HAL_XML_TYPE)) {
        accepted = Optional.of(HalMediaType.APPLICATION_HAL_XML_TYPE);
        break;
      }
    }
    if (!accepted.isPresent()) {
      throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
    }
    return accepted.get();
  }

  protected URI self() {
    return uriInfo.getRequestUri();
  }
}

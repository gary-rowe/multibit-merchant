package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.hal.MinifiedJsonRenderer;
import org.multibit.mbm.api.hal.MinifiedXmlRenderer;
import org.multibit.mbm.core.model.User;

import javax.ws.rs.core.UriInfo;

/**
 * <p>Abstract base class to provide the following to subclasses:</p>
 * <ul>
 * <li>Provision of common support methods</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseBridge<T> {

  protected final UriInfo uriInfo;
  protected final Optional<User> principal;

  /**
   * @param uriInfo The {@link UriInfo} containing the originating request information
   * @param principal An optional {@link User} to provide a security principal
   */
  public BaseBridge(UriInfo uriInfo, Optional<User> principal) {
    this.uriInfo = uriInfo;
    this.principal = principal;
  }
  /**
   * @return A {@link ResourceFactory} configured for production use
   */
  protected ResourceFactory getResourceFactory() {
    ResourceFactory resourceFactory = new ResourceFactory(uriInfo.getBaseUri().toString());
    // Override the default configuration
    resourceFactory.withRenderer(HalMediaType.APPLICATION_HAL_JSON,
      MinifiedJsonRenderer.class);
    resourceFactory.withRenderer(HalMediaType.APPLICATION_HAL_XML,
      MinifiedXmlRenderer.class);
    return resourceFactory;
  }

  /**
   * Bridges from entity to resource, providing all necessary context extracted from the entity
   *
   * @param entity The entity to represent as a {@link Resource}
   * @return The {@link Resource}
   */
  public abstract Resource toResource(T entity);

}

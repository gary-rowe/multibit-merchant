package org.multibit.mbm.client.interfaces.rest.links;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * <p>Utility to provide the following to link utilities:</p>
 * <ul>
 * <li>Convenience methods supporting common operations</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class Links {

  public static URI createSelfUri(String identifiableTemplate, String id) {
    return UriBuilder.fromPath(identifiableTemplate).build(id);
  }

  public static URI createSelfUri(String inferredTemplate) {
    return UriBuilder.fromPath(inferredTemplate).build();
  }

}

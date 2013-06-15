package org.multibit.mbm.client.interfaces.rest.links.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.multibit.mbm.client.domain.model.model.Customer;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

/**
 * <p>Utility to provide the following to resources:</p>
 * <ul>
 * <li>Use case interface to provide links between entities</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CustomerLinks {

  public static URI self(Customer customer) {
    Preconditions.checkNotNull(customer, "'customer' cannot be null");

    Map<String,String> links = Maps.newHashMap();

    return UriBuilder.fromPath("/customer").build();

  }
}

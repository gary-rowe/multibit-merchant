package org.multibit.merchant.client.store;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>DropWizard Configuration to provide the following to application:</p>
 * <ul>
 * <li>Initialisation code</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class StoreConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  private String authenticationCachePolicy="maximumSize=10000, expireAfterAccess=10m";

  @NotEmpty
  @JsonProperty
  private String assetCachePolicy="maximumSize=10000, expireAfterAccess=5s";

  public String getAuthenticationCachePolicy() {
    return authenticationCachePolicy;
  }

  public String getAssetCachePolicy() {
    return assetCachePolicy;
  }
}

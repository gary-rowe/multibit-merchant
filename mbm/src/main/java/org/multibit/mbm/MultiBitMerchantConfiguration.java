package org.multibit.mbm;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MultiBitMerchantConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  private String authenticationCachePolicy="maximumSize=10000, expireAfterAccess=10m";

  public String getAuthenticationCachePolicy() {
    return authenticationCachePolicy;
  }

}

package org.multibit.mbm;

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
public class MultiBitMerchantConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  private String template;

  @NotEmpty
  @JsonProperty
  private String defaultName = "Stranger";

  @NotEmpty
  @JsonProperty
  private String authenticationCachePolicy;

  public String getTemplate() {
    return template;
  }

  public String getDefaultName() {
    return defaultName;
  }

  public String getAuthenticationCachePolicy() {
    return authenticationCachePolicy;
  }
}

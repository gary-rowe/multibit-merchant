package org.multibit.mbm.client.interfaces.rest.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>Base request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state common to all interactions with the User entity</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class WebFormAuthenticationDto {

  /**
   * A username
   */
  @Valid
  @NotNull
  @JsonProperty
  private String username;

  /**
   * The password after being run through a one pass digest algorithm, such as SHA-256
   * This provides a small level of protection against casual browsing of accidental server log leakage
   * during development. It is not intended for storage.
   */
  @Valid
  @NotNull
  @JsonProperty
  private String passwordDigest;

  /**
   * Default constructor for Jackson
   */
  public WebFormAuthenticationDto() {
  }

  /**
   *
   * @param username The username from the web form
   * @param passwordDigest The password digest
   */
  public WebFormAuthenticationDto(String username, String passwordDigest) {
    this.username = username;
    this.passwordDigest = passwordDigest;
  }

  /**
   * @return The password digest (see notes)
   */
  public String getPasswordDigest() {
    return passwordDigest;
  }

  /**
   * @return The user name
   */
  public String getUsername() {
    return username;
  }

}

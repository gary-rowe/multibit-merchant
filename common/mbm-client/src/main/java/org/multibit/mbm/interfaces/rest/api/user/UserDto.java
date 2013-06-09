package org.multibit.mbm.interfaces.rest.api.user;

import com.google.common.collect.Sets;
import org.multibit.mbm.interfaces.rest.auth.Authority;
import org.multibit.mbm.infrastructure.utils.ObjectUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * <p>DTO to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of state for a single User as viewed by the Customer</li>
 * </ul>
 * <p>This is used when the client needs to authenticate the user, and when proxying
 * requests upstream to MBM.</p>
 *
 * @since 0.0.1
 *         
 */
public class UserDto {

  /**
   * Numerical ID to allow faster indexing (for internal use)
   */
  protected Long id = null;

  /**
   * <p>UUID to allow public User reference without
   * revealing a sequential ID that could be guessed.
   * Typically used as an API key</p>
   */
  protected String apiKey = null;

  /**
   * <p>Used as a shared secret to authenticate this user to the upstream server. Typically
   * part of an HMAC authentication scheme.</p>
   */
  protected String secretKey = null;

  /**
   * <p>A user password (not plaintext and optional for anonymity reasons)</p>
   */
  protected String passwordDigest = null;

  /**
   * <p>A username (optional for anonymity reasons)</p>
   */
  protected String username = null;

  /**
   * A shared secret between this client the user's browser that is revoked when the session ends
   */
  private UUID sessionToken;

  private Set<Authority> cachedAuthorities=Sets.newLinkedHashSet();

  /**
   * @return The internal unique ID
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return The public API key used when identifying the user during HMAC authentication
   */
  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   * @return The private shared secret for upstream communications
   */
  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  /**
   * @return The user name to authenticate with the client
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return The digested password to provide authentication between the user and the client
   */
  public String getPasswordDigest() {
    return passwordDigest;
  }

  /**
   * <h3>Note that it is expected that Jasypt or similar is used prior to storage</h3>
   * @param passwordDigest The password digest
   */
  public void setPasswordDigest(String passwordDigest) {
    this.passwordDigest = passwordDigest;
  }

  /**
   * @return The session key
   */
  public UUID getSessionToken() {
    return sessionToken;
  }

  public void setSessionToken(UUID sessionToken) {
    this.sessionToken = sessionToken;
  }

  /**
   * @return The set of authorities for this user
   */
  public Set<Authority> getCachedAuthorities() {
    return cachedAuthorities;
  }

  public void setCachedAuthorities(Authority[] cachedAuthorities) {
    this.cachedAuthorities = Sets.newLinkedHashSet(Arrays.asList(cachedAuthorities));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UserDto other = (UserDto) obj;

    return ObjectUtils.isEqual(
      apiKey, other.apiKey,
      secretKey, other.secretKey
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(apiKey);
  }

  /**
   * @param authorities The required authorities
   * @return True if the user has all the required authorities
   *
   */
  public boolean hasAllAuthorities(Authority[] authorities) {
    Set<Authority> requiredAuthorities = Sets.newHashSet(authorities);
    return getCachedAuthorities().containsAll(requiredAuthorities);
  }

}

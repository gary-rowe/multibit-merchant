package org.multibit.mbm.auth.hmac;


import com.google.common.base.Charsets;
import com.google.common.base.Objects;

import java.security.MessageDigest;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Value object to provide the following to {@link HmacAuthenticator}:</p>
 * <ul>
 * <li>Storage of the necessary credentials for HMAC authentication</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class HmacCredentials {
  private final String apiKey;
  private final String digest;
  private final String contents;
  private final String algorithm;

  /**
   * Creates a new {@link org.multibit.mbm.auth.hmac.HmacCredentials} with the given username and password.
   *
   * @param algorithm   The algorithm used for computing the digest (e.g. "HmacSHA1", "HmacSHA256", "HmacSHA512")
   * @param apiKey   The API key used for looking up the shared secret key associated with the user
   * @param digest   The digest of (contents + shared secret key)
   * @param contents The contents that were signed
   */
  public HmacCredentials(String algorithm, String apiKey, String digest, String contents) {
    this.algorithm = checkNotNull(algorithm);
    this.apiKey = checkNotNull(apiKey);
    this.digest = checkNotNull(digest);
    this.contents = checkNotNull(contents);
  }

  /**
   * @return The name of the algorithm used to compute the digest
   */
  public String getAlgorithm() {
    return algorithm;
  }

  /**
   * @return The public API key (allows look up of shared secret key)
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * @return The digest of the request content and shared secret key
   */
  public String getDigest() {
    return digest;
  }

  /**
   * @return The request content to which the digest applies (either entity or URI)
   */
  public String getContents() {
    return contents;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final HmacCredentials that = (HmacCredentials) obj;
    // N.B.: Do a constant-time comparison here to prevent timing attacks.
    final byte[] thisBytes = digest.getBytes(Charsets.UTF_8);
    final byte[] thatBytes = that.digest.getBytes(Charsets.UTF_8);
    return apiKey.equals(that.apiKey) && MessageDigest.isEqual(thisBytes, thatBytes);
  }

  @Override
  public int hashCode() {
    return (31 * apiKey.hashCode()) + digest.hashCode();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("apiKey", apiKey)
      .add("digest", digest)
      .add("contents", contents)
      .toString();
  }
}

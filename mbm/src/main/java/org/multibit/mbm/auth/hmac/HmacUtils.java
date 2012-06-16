package org.multibit.mbm.auth.hmac;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Utility class to provide the following to HMAC operations:</p>
 * <ul>
 * <li>HMAC signature computation</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class HmacUtils {

  // Public to allow tests to share the designated accepted structure
  public static final String PREFIX = "HMAC";
  public static final String X_HMAC_NONCE = "X-HMAC-Nonce";
  static final String HEADER_VALUE = PREFIX + " realm=\"%s\"";

  /**
   * Compute the HMAC signature for the given data and shared secret
   *
   * @param algorithm       The algorithm to use (e.g. "HmacSHA512")
   * @param data            The data to sign
   * @param sharedSecret The shared secret key to use for signing
   *
   * @return A base 64 encoded signature encoded as UTF-8
   *
   */
  public static byte[] computeSignature(String algorithm, byte[] data, byte[] sharedSecret) throws NoSuchAlgorithmException, InvalidKeyException {

    SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(sharedSecret), algorithm);
    Mac mac = Mac.getInstance(algorithm);
    mac.init(secretKey);
    mac.update(data);
    return Base64.encodeBase64(mac.doFinal());
  }

}

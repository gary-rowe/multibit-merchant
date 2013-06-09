package org.multibit.mbm.interfaces.rest.auth.hmac;

import com.google.common.net.HttpHeaders;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Utility class to provide the following to HMAC operations:</p>
 * <ul>
 * <li>HMAC signature computation</li>
 * </ul>
 * <h3>Canonical request algorithm</h3>
 * <ol>
 * <li>Read the HTTP "Authorization" header</li>
 * <li>Read the HTTP "Content-Type" header (optional)</li>
 * <li>Read the HTTP "Content-MD5" header (optional)</li>
 * <li>Read the HTTP "X-HMAC-Nonce" header (optional) - </li>
 * <li>Read the HTTP "X-HMAC-Date" header (optional) - HTTP-Date format RFC1123 5.2.14 UTC</li>
 * </ol>
 * <p>Create the canonical representation of the request using the following algorithm:</p>
 * <ul>
 * <li>Start with the empty string ("").</li>
 * <li>Add the HTTP-Verb for the request ("GET", "POST", ...) in capital letters, followed by a single newline (U+000A).</li>
 * <li>Add the date for the request using the form "date:#date-of-request" followed by a single newline. The date for the signature must be formatted exactly as in the request.</li>
 * <li>Add the nonce for the request in the form "nonce:#nonce-in-request" followed by a single newline. If no nonce is passed use the empty string as nonce value.</li>
 * <li><strong>Note: removed headers from HMAC due to hosting services providing a multitude of headers outside of the scope of the user and largely impossible to predict.</strong></li>
 * <li>Append the original absolute request URI to the canonical representation (no parameter re-ordering and include any request fragments)</li>
 * </ul>
 * <p>There is no support for query-based authentication because this breaks the purpose of a URI as a resource
 * identifier, not as authenticator. It would lead to authenticated URIs being shared as permalinks which would
 * later fail through a TTL threshold being exceeded.</p>
 * <p>Examples</p>
 * <h3>Example with X-HMAC-Nonce</h3>
 * <pre>
 * GET /example/resource.html?sort=header%20footer&order=ASC HTTP/1.1
 * Host: www.example.org
 * Date: Mon, 20 Jun 2011 12:06:11 GMT
 * User-Agent: curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3
 * X-MAC-Nonce: Thohn2Mohd2zugoo
 * </pre>
 * <p>Applying the above gives a canonical representation of</p>
 * <pre>
 * GET\n
 * date:Mon, 20 Jun 2011 12:06:11 GMT\n
 * nonce:Thohn2Mohd2zugo\n
 * http://example.org/example/resource.html?order=ASC&sort=header footer
 * </pre>
 * <p>This would be passed into the HMAC signature </p>
 * <h3>Example with X-HMAC-Date</h3>
 * <p>Given the following request:</p>
 * <pre>
 * GET /example/resource.html?sort=header%20footer&order=ASC HTTP/1.1
 * Host: www.example.org
 * Date: Mon, 20 Jun 2011 12:06:11 GMT
 * User-Agent: curl/7.20.0 (x86_64-pc-linux-gnu) libcurl/7.20.0 OpenSSL/1.0.0a zlib/1.2.3
 * X-MAC-Nonce: Thohn2Mohd2zugoo
 * X-MAC-Date: Mon, 20 Jun 2011 14:06:57 GMT
 * </pre>
 * <p>The canonical representation is:</p>
 * <pre> GET\n
 * date:Mon, 20 Jun 2011 14:06:57 GMT\n
 * nonce:Thohn2Mohd2zugo\n
 * http://example.org/example/resource.html?order=ASC&sort=header footer
 * </pre>
 * <p>Based on <a href=""http://rubydoc.info/gems/warden-hmac-authentication/0.6.1/file/README.md></a>the Warden HMAC Ruby gem</a>.</p>
 *
 * @since 0.0.1
 *         
 */
public class HmacUtils {

  // Public to allow tests to share the designated accepted structure
  public static final String PREFIX = "HMAC";
  public static final String X_HMAC_NONCE = "X-HMAC-Nonce";
  public static final String X_HMAC_DATE = "X-HMAC-Date";
  static final String HEADER_VALUE = PREFIX + " realm=\"%s\"";

  /**
   * Compute the HMAC signature for the given data and shared secret
   *
   * @param algorithm    The algorithm to use (e.g. "HmacSHA512")
   * @param data         The data to sign
   * @param sharedSecret The shared secret key to use for signing
   *
   * @return A base 64 encoded signature encoded as UTF-8
   */
  public static byte[] computeSignature(String algorithm, byte[] data, byte[] sharedSecret) {

    try {
      SecretKey secretKey = new SecretKeySpec(Base64.decode(sharedSecret), algorithm);
      Mac mac = Mac.getInstance(algorithm);
      mac.init(secretKey);
      mac.update(data);
      return Base64.encode(mac.doFinal());
    } catch (NoSuchAlgorithmException e) {
      throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    } catch (InvalidKeyException e) {
      throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * @param clientRequest Providing the HTTP information necessary from the client side
   *
   * @return A curl command that emulates the client request
   */
  public static String createCurlCommand(ClientRequest clientRequest, Providers providers, String canonicalRepresentation, String sharedSecret, String apiKey) {

    // Extract the original headers
    MultivaluedMap<String, Object> originalHeaders = clientRequest.getHeaders();

    // Compute the representation signature
    String signature = new String(HmacUtils.computeSignature("HmacSHA1", canonicalRepresentation.getBytes(), sharedSecret.getBytes()));
    String authorization = "HMAC " + apiKey + " " + signature;

    String httpMethod = clientRequest.getMethod().toUpperCase();
    StringBuilder curlCommand = new StringBuilder("curl --verbose ");
    curlCommand.append("--output \"response.json\" ");

    // Add in the Authorization header
    curlCommand
      .append("--header Authorization:\"")
      .append(authorization)
      .append("\" ");

    // Add in the Content-Type header
    curlCommand
      .append("--header \"Content-Type:application/json\" ");

    // Add in HMAC date
    if (originalHeaders.containsKey(X_HMAC_DATE)) {
      // Use the TTL date
      curlCommand
        .append("--header \"")
        .append(X_HMAC_DATE)
        .append(":")
        .append(originalHeaders.getFirst(X_HMAC_DATE))
        .append("\" ");
    }

    // Add in HTTP date
    if (originalHeaders.containsKey(HttpHeaders.DATE)) {
      // Use the HTTP date
      curlCommand
        .append("--header \"")
        .append(HttpHeaders.DATE)
        .append(":")
        .append(originalHeaders.getFirst(HttpHeaders.DATE))
        .append("\" ");
    }

    // Add the nonce for the request in the form
    // "nonce:#nonce-in-request" followed by a single newline.
    // If no nonce is passed use the empty string as nonce value.
    if (originalHeaders.containsKey(X_HMAC_NONCE)) {
      curlCommand
        .append("--header \"")
        .append(X_HMAC_NONCE)
        .append(":")
        .append(originalHeaders.getFirst(X_HMAC_NONCE))
        .append("\" ");
    } else {
      curlCommand
        .append("--header \"")
        .append(X_HMAC_NONCE)
        .append(":\" ");
    }


    // Check for payload
    if (isEntityRequired(clientRequest.getEntity(), httpMethod)) {
      // Configure the request method
      curlCommand.append("--request ");
      curlCommand.append(httpMethod);
      curlCommand.append(" ");
      // Provide the data
      curlCommand.append("--data '");
      // Get the message body writer that will be used later
      Object entity = clientRequest.getEntity();
      final MessageBodyWriter messageBodyWriter = providers
        .getMessageBodyWriter(entity.getClass(), entity.getClass(),
          new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      if (messageBodyWriter == null) {
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
      }

      try {
        // TODO Need to infer media type
        messageBodyWriter.writeTo(entity, entity.getClass(),
          entity.getClass(), new Annotation[0],
          MediaType.APPLICATION_JSON_TYPE, clientRequest.getHeaders(), baos);
      } catch (IOException e) {
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
      }
      curlCommand.append(baos);
      curlCommand.append("' ");

    }

    curlCommand.append(clientRequest.getURI());

    return curlCommand.toString();

  }


  /**
   * @param clientRequest Providing the HTTP information necessary from the client side
   * @param providers     The Providers for marshalling/unmarshalling the request body
   *
   * @return The canonical representation of the request for the client to use
   */
  public static String createCanonicalRepresentation(ClientRequest clientRequest, Providers providers) {

    // Extract the original headers
    MultivaluedMap<String, Object> originalHeaders = clientRequest.getHeaders();

    // Extract the original URI
    URI uri = clientRequest.getURI();

    String httpMethod = clientRequest.getMethod().toUpperCase();

    // Start with the empty string ("")
    final StringBuilder canonicalRepresentation = new StringBuilder("");

    // Add the HTTP-Verb for the request ("GET", "POST", ...) in capital letters, followed by a single newline (U+000A).
    canonicalRepresentation
      .append(httpMethod.toUpperCase())
      .append("\n");

    // Add the date for the request using the form "date:#date-of-request" followed by a single newline. The date for the signature must be formatted exactly as in the request.
    if (originalHeaders.containsKey(X_HMAC_DATE)) {
      // Use the TTL date
      canonicalRepresentation
        .append("date:")
        .append(originalHeaders.getFirst(X_HMAC_DATE))
        .append("\n");
    } else if (originalHeaders.containsKey(HttpHeaders.DATE)) {
      // Use the HTTP date
      canonicalRepresentation
        .append("date:")
        .append(originalHeaders.getFirst(HttpHeaders.DATE))
        .append("\n");
    }

    // Add the nonce for the request in the form
    // "nonce:#nonce-in-request" followed by a single newline.
    // If no nonce is passed use the empty string as nonce value.
    if (originalHeaders.containsKey(X_HMAC_NONCE)) {
      canonicalRepresentation
        .append("nonce:")
        .append(originalHeaders.getFirst(X_HMAC_NONCE))
        .append("\n");
    } else {
      canonicalRepresentation
        .append("nonce:")
        .append("\n");
    }

    // Append the original URI (URL-decoded)
    canonicalRepresentation.append(uri.toString());

    // Check for payload
    if (isEntityRequired(clientRequest.getEntity(), httpMethod)) {
      // Include the entity as a simple string
      canonicalRepresentation.append("\n");
      // Get the message body writer that will be used later
      Object entity = clientRequest.getEntity();
      final MessageBodyWriter messageBodyWriter = providers
        .getMessageBodyWriter(entity.getClass(), entity.getClass(),
          new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      if (messageBodyWriter == null) {
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
      }

      try {
        // TODO Need to infer media type
        messageBodyWriter.writeTo(entity, entity.getClass(),
          entity.getClass(), new Annotation[0],
          MediaType.APPLICATION_JSON_TYPE, originalHeaders, baos);
      } catch (IOException e) {
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
      }
      canonicalRepresentation.append(baos);

    }

    return canonicalRepresentation.toString();
  }

  /**
   * @param containerRequest Providing the required HTTP request information on the server side
   *
   * @return The canonical representation of the request
   */
  public static String createCanonicalRepresentation(ContainerRequest containerRequest) {

    // Provide a map of all header names converted to lowercase
    MultivaluedMap<String, String> originalHeaders = containerRequest.getRequestHeaders();

    // Keep track of the method in a fixed format
    String httpMethod = containerRequest.getMethod().toUpperCase();

    // Start with the empty string ("")
    final StringBuilder canonicalRepresentation = new StringBuilder("");

    // Add the HTTP-Verb for the request ("GET", "POST", ...) in capital letters, followed by a single newline (U+000A).
    canonicalRepresentation
      .append(httpMethod)
      .append("\n");

    // Add the date for the request using the form "date:#date-of-request" followed by a single newline. The date for the signature must be formatted exactly as in the request.
    if (originalHeaders.containsKey(HmacUtils.X_HMAC_DATE)) {
      // Use the TTL date
      canonicalRepresentation
        .append("date:")
        .append(originalHeaders.getFirst(HmacUtils.X_HMAC_DATE))
        .append("\n");

    } else if (originalHeaders.containsKey(HttpHeaders.DATE)) {
      // Use the HTTP date
      canonicalRepresentation
        .append("date:")
        .append(originalHeaders.getFirst(HttpHeaders.DATE))
        .append("\n");
    }

    // Add the nonce for the request in the form
    // "nonce:#nonce-in-request" followed by a single newline.
    // If no nonce is passed use the empty string as nonce value.
    if (originalHeaders.containsKey(HmacUtils.X_HMAC_NONCE)) {
      canonicalRepresentation
        .append("nonce:")
        .append(originalHeaders.getFirst(HmacUtils.X_HMAC_NONCE))
        .append("\n");
    } else {
      canonicalRepresentation
        .append("nonce:")
        .append("\n");
    }

    // Append the original absolute URI
    canonicalRepresentation.append(containerRequest.getRequestUri().toString());

    // Check for payload
    if (isEntityRequired(containerRequest.getEntityInputStream(), httpMethod)) {
      // Include the entity as a simple string
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      InputStream in = containerRequest.getEntityInputStream();
      try {
        if (in.available() > 0) {
          // Simplifies server side logic for isEntityRequired
          canonicalRepresentation.append("\n");
          ReaderWriter.writeTo(in, out);

          canonicalRepresentation.append(out);

          // Reset the input stream to the same contents to avoid problems with chained reading
          byte[] requestEntity = out.toByteArray();
          containerRequest.setEntityInputStream(new ByteArrayInputStream(requestEntity));
        }
      } catch (IOException ex) {
        throw new ContainerException(ex);
      }

    }

    return canonicalRepresentation.toString();
  }

  private static boolean isEntityRequired(Object entity, String httpMethod) {
    return (
      "POST".equalsIgnoreCase(httpMethod) ||
        "PUT".equalsIgnoreCase(httpMethod)) &&
      entity != null;
  }

}

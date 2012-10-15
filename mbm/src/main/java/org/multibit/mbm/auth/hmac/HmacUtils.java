package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Sets;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

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
      return Base64.encodeBytesToBytes(mac.doFinal());
    } catch (NoSuchAlgorithmException e) {
      throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    } catch (InvalidKeyException e) {
      throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    } catch (IOException e) {
      throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   *
   * @param clientRequest Providing the HTTP information necessary from the client side
   *
   * @return A curl command that emulates the client request
   */
  public static String createCurlCommand(ClientRequest clientRequest, Providers providers, String canonicalRepresentation, String sharedSecret, String apiKey) {

    // Compute the representation signature
    String signature = new String(HmacUtils.computeSignature("HmacSHA1", canonicalRepresentation.getBytes(), sharedSecret.getBytes()));
    String authorization = "HMAC " + apiKey + " " + signature;

    String httpMethod = clientRequest.getMethod().toUpperCase();
    StringBuilder curlCommand = new StringBuilder("curl --verbose ");
    curlCommand.append("--output \"response.txt\" ");
    curlCommand.append("--header \"Accept:application/hal+json\" ");
    curlCommand.append("--header \"Content-Type:application/json\" ");
    curlCommand.append("--header \"Authorization:");
    curlCommand.append(authorization);
    curlCommand.append("\" ");

    // Check for payload
    if ("POST".equalsIgnoreCase(httpMethod) ||
      "PUT".equalsIgnoreCase(httpMethod)) {
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

    curlCommand.append("http://localhost:8080/mbm");
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

    // Create a lexicographically sorted set of the header names for lookup later
    SortedSet<String> headerNames = Sets.newTreeSet(originalHeaders.keySet());
    // Remove some headers that should not be included or will have special treatment
    headerNames.remove(HttpHeaders.DATE);
    headerNames.remove(X_HMAC_DATE);
    headerNames.remove(X_HMAC_NONCE);
    // TODO Check if the following should be removed (would the client know them in advance?)
    headerNames.remove(HttpHeaders.USER_AGENT);
    headerNames.remove(HttpHeaders.HOST);
    headerNames.remove(HttpHeaders.ACCEPT);
    headerNames.remove(HttpHeaders.CONTENT_TYPE);

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
    } else {
      // Use the HTTP date
      canonicalRepresentation
        .append("date:")
        .append(originalHeaders.getFirst(HttpHeaders.DATE))
        .append("\n");
    }

    // Add the nonce for the request in the form "nonce:#nonce-in-request" followed by a single newline. If no nonce is passed use the empty string as nonce value.
    if (originalHeaders.containsKey(X_HMAC_NONCE)) {
      canonicalRepresentation
        .append("nonce:")
        .append(originalHeaders.getFirst(X_HMAC_NONCE))
        .append("\n");
    }

    // Sort the remaining headers lexicographically by header name.
    // Trim header values by removing any whitespace before the first non-whitespace character and after the last non-whitespace character.
    // Combine lowercase header names and header values using a single colon (“:”) as separator. Do not include whitespace characters around the separator.
    // Combine all headers using a single newline (U+000A) character and append them to the canonical representation, followed by a single newline (U+000A) character.
    for (String headerName : headerNames) {
      canonicalRepresentation
        .append(headerName.toLowerCase())
        .append(":");
      // TODO Consider effect of different separators on this list
      for (Object value : originalHeaders.get(headerName)) {
        canonicalRepresentation
          .append(value.toString());
      }
      canonicalRepresentation
        .append("\n");
    }

    // Append the url-decoded path and query to the canonical representation
    canonicalRepresentation
      .append(uri.getPath());
    if (uri.getQuery() != null) {
      canonicalRepresentation
        .append("?")
        .append(uri.getQuery());
    }

    // Check for payload
    if ("POST".equalsIgnoreCase(httpMethod) ||
      "PUT".equalsIgnoreCase(httpMethod)) {
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

    // Create a lexicographically sorted set of the header names for lookup later
    Set<String> headerNames = Sets.newTreeSet(originalHeaders.keySet());
    // Remove some headers that should not be included or will have special treatment
    headerNames.remove(HttpHeaders.DATE);
    headerNames.remove(HmacUtils.X_HMAC_DATE);
    headerNames.remove(HmacUtils.X_HMAC_NONCE);
    headerNames.remove(HttpHeaders.AUTHORIZATION);
    // TODO Check if the following should be removed (would the client know them in advance?)
    headerNames.remove(HttpHeaders.USER_AGENT);
    headerNames.remove(HttpHeaders.HOST);
    headerNames.remove(HttpHeaders.ACCEPT);
    headerNames.remove(HttpHeaders.CONTENT_TYPE);

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

    } else {
      // Use the HTTP date
      canonicalRepresentation
        .append("date:")
        .append(originalHeaders.getFirst(HttpHeaders.DATE))
        .append("\n");
    }

    // Add the nonce for the request in the form "nonce:#nonce-in-request" followed by a single newline. If no nonce is passed use the empty string as nonce value.
    if (originalHeaders.containsKey(HmacUtils.X_HMAC_NONCE)) {
      canonicalRepresentation
        .append("nonce:")
        .append(originalHeaders.getFirst(HmacUtils.X_HMAC_NONCE))
        .append("\n");
    }

    // Sort the remaining headers lexicographically by header name.
    // Trim header values by removing any whitespace before the first non-whitespace character and after the last non-whitespace character.
    // Combine lowercase header names and header values using a single colon (“:”) as separator. Do not include whitespace characters around the separator.
    // Combine all headers using a single newline (U+000A) character and append them to the canonical representation, followed by a single newline (U+000A) character.
    for (String headerName : headerNames) {
      canonicalRepresentation
        .append(headerName.toLowerCase())
        .append(":");
      // TODO Consider effect of different separators on this list
      for (String value : originalHeaders.get(headerName)) {
        canonicalRepresentation
          .append(value);
      }
      canonicalRepresentation
        .append("\n");
    }

    // Append the path to the canonical representation
    canonicalRepresentation
      .append("/")
      .append(containerRequest.getPath());

    // Append the url-decoded query parameters to the canonical representation
    MultivaluedMap<String, String> decodedQueryParameters = containerRequest.getQueryParameters();
    if (!decodedQueryParameters.isEmpty()) {
      canonicalRepresentation.append("?");
      boolean first = true;
      for (Map.Entry<String, List<String>> queryParameter : decodedQueryParameters.entrySet()) {
        if (first) {
          first = false;
        } else {
          canonicalRepresentation.append("&");
        }
        canonicalRepresentation
          .append(queryParameter.getKey())
          .append("=");
        // TODO Consider effect of different separators on this list
        for (String value : queryParameter.getValue()) {
          canonicalRepresentation
            .append(value);
        }
      }
    }

    // Check for payload
    if ("POST".equalsIgnoreCase(httpMethod) ||
      "PUT".equalsIgnoreCase(httpMethod)) {
      // Include the entity as a simple string
      canonicalRepresentation.append("\n");
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      InputStream in = containerRequest.getEntityInputStream();
      try {
        if (in.available() > 0) {
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


}

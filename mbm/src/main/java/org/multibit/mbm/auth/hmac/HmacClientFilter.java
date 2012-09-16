package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.multibit.mbm.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.SortedSet;

/**
 * <p>Client filter to provide the following to requests:</p>
 * <ul>
 * <li>Addition of Authorization information to client requests</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class HmacClientFilter extends ClientFilter {

  private Logger log = LoggerFactory.getLogger(HmacClientFilter.class);

  private final String apiKey;
  private final String sharedSecret;

  public HmacClientFilter(String apiKey, String sharedSecret) {
    this.apiKey = apiKey;
    this.sharedSecret = sharedSecret;
  }

  public ClientResponse handle(ClientRequest cr) {
    // Modify the request
    ClientRequest mcr = modifyRequest(cr);

    // Call the next client handler in the filter chain
    return getNext().handle(mcr);

  }

  /**
   * Handles the process of modifying the outbound request with suitable HMAC headers
   *
   * @param clientRequest The original client request
   * @return The modified client request
   */
  private ClientRequest modifyRequest(ClientRequest clientRequest) {

    // Provide a short TTL
    String httpNow = DateUtils.formatHttpDateHeader(DateUtils.nowUtc().plusSeconds(5));
    clientRequest.getHeaders().put(HmacUtils.X_HMAC_DATE, Lists.<Object>newArrayList(httpNow));

    String canonicalRepresentation = createCanonicalRepresentation(clientRequest);
    log.debug("Canonical represenation: '{}'", canonicalRepresentation);

    String signature;
    try {
      signature = new String(HmacUtils.computeSignature("HmacSHA1", canonicalRepresentation.getBytes(), sharedSecret.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(e.getMessage());
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(e.getMessage());
    }

    String authorization = "HMAC " + apiKey + " " + signature;

    clientRequest.getHeaders().put(HttpHeaders.AUTHORIZATION, Lists.<Object>newArrayList(authorization));

    return clientRequest;
  }

  /**
   * @param clientRequest Providing the HTTP information necessary
   * @return The canonical representation of the request for the client to use
   */
  /* package */ String createCanonicalRepresentation(ClientRequest clientRequest) {

    // Extract the original headers
    MultivaluedMap<String, Object> originalHeaders = clientRequest.getHeaders();

    // Extract the original URI
    URI uri = clientRequest.getURI();

    // Create a lexicographically sorted set of the header names for lookup later
    SortedSet<String> headerNames = Sets.newTreeSet(originalHeaders.keySet());
    // Remove some headers that should not be included or will have special treatment
    headerNames.remove(HttpHeaders.DATE);
    headerNames.remove(HmacUtils.X_HMAC_DATE);
    headerNames.remove(HmacUtils.X_HMAC_NONCE);
    // TODO Check if the following should be removed (would the client know them in advance?)
    headerNames.remove(HttpHeaders.USER_AGENT);
    headerNames.remove(HttpHeaders.HOST);

    String httpMethod = clientRequest.getMethod().toUpperCase();

    // Start with the empty string ("")
    final StringBuilder canonicalRepresentation = new StringBuilder("");

    // Add the HTTP-Verb for the request ("GET", "POST", ...) in capital letters, followed by a single newline (U+000A).
    canonicalRepresentation
      .append(httpMethod.toUpperCase())
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
      canonicalRepresentation.append(clientRequest.getEntity());
    }

    return canonicalRepresentation.toString();
  }


}
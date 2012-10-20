package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Sets;
import com.google.common.net.HttpHeaders;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import org.multibit.mbm.utils.Base64;

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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

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
 * <li>Convert all remaining header names to lowercase.</li>
 * <li>Sort the remaining headers lexicographically by header name.</li>
 * <li>Trim header values by removing any whitespace before the first non-whitespace character and after the last non-whitespace character.</li>
 * <li>Combine lowercase header names and header values using a single colon (“:”) as separator. Do not include whitespace characters around the separator.</li>
 * <li>Combine all headers using a single newline (U+000A) character and append them to the canonical representation, followed by a single newline (U+000A) character.</li>
 * <li>Append the path (including context) to the canonical representation</li>
 * <li>Append the url-decoded query parameters to the canonical representation</li>
 * <li>URL-decode query parameters if required</li>
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
 * /example/resource.html?order=ASC&sort=header footer
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
 * /example/resource.html?order=ASC&sort=header footer
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
   * <p>Exclude the following HTTP request headers since they may be added
   * after the client has released control of the request</p>
   * <p>Using the Guava list ensures that we cover the following RFCs</p>
   * <ul>
   * <li><a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a>
   * <li><a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
   * <li><a href="http://www.ietf.org/rfc/rfc2616.txt">RFC 2616</a>
   * <li><a href="http://www.ietf.org/rfc/rfc2965.txt">RFC 2965</a>
   * <li><a href="http://www.ietf.org/rfc/rfc5988.txt">RFC 5988</a>
   * </ul>
   */
  private static Set<String> excludedHttpHeaderNames = Sets.newHashSet(
    X_HMAC_DATE,
    X_HMAC_NONCE,
    HttpHeaders.CACHE_CONTROL,
    HttpHeaders.CONTENT_ENCODING,
    HttpHeaders.CONTENT_LENGTH,
    HttpHeaders.CONTENT_LENGTH,
    HttpHeaders.CONTENT_TYPE,
    HttpHeaders.DATE,
    HttpHeaders.PRAGMA,
    HttpHeaders.VIA,
    HttpHeaders.WARNING,
    HttpHeaders.ACCEPT,
    HttpHeaders.ACCEPT_CHARSET,
    HttpHeaders.ACCEPT_ENCODING,
    HttpHeaders.ACCEPT_LANGUAGE,
    HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
    HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
    HttpHeaders.AUTHORIZATION,
    HttpHeaders.CONNECTION,
    HttpHeaders.COOKIE,
    HttpHeaders.EXPECT,
    HttpHeaders.FROM,
    HttpHeaders.HOST,
    HttpHeaders.IF_MATCH,
    HttpHeaders.IF_MODIFIED_SINCE,
    HttpHeaders.IF_NONE_MATCH,
    HttpHeaders.IF_RANGE,
    HttpHeaders.IF_UNMODIFIED_SINCE,
    HttpHeaders.LAST_EVENT_ID,
    HttpHeaders.MAX_FORWARDS,
    HttpHeaders.ORIGIN,
    HttpHeaders.PROXY_AUTHORIZATION,
    HttpHeaders.RANGE,
    HttpHeaders.REFERER,
    HttpHeaders.TE,
    HttpHeaders.TRANSFER_ENCODING,
    HttpHeaders.UPGRADE,
    HttpHeaders.USER_AGENT,
    HttpHeaders.ACCEPT_RANGES,
    HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
    HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
    HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
    HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
    HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
    HttpHeaders.ACCESS_CONTROL_MAX_AGE,
    HttpHeaders.AGE,
    HttpHeaders.ALLOW,
    HttpHeaders.CONTENT_DISPOSITION,
    HttpHeaders.CONTENT_ENCODING,
    HttpHeaders.CONTENT_LANGUAGE,
    HttpHeaders.CONTENT_LOCATION,
    HttpHeaders.CONTENT_MD5,
    HttpHeaders.CONTENT_RANGE,
    HttpHeaders.ETAG,
    HttpHeaders.EXPIRES,
    HttpHeaders.LAST_MODIFIED,
    HttpHeaders.LINK,
    HttpHeaders.LOCATION,
    HttpHeaders.P3P,
    HttpHeaders.PROXY_AUTHENTICATE,
    HttpHeaders.REFRESH,
    HttpHeaders.RETRY_AFTER,
    HttpHeaders.SERVER,
    HttpHeaders.SET_COOKIE,
    HttpHeaders.SET_COOKIE2,
    HttpHeaders.TRAILER,
    HttpHeaders.TRANSFER_ENCODING,
    HttpHeaders.VARY,
    HttpHeaders.WWW_AUTHENTICATE,
    HttpHeaders.DNT,
    HttpHeaders.X_CONTENT_TYPE_OPTIONS,
    HttpHeaders.X_DO_NOT_TRACK,
    HttpHeaders.X_FORWARDED_FOR,
    HttpHeaders.X_FORWARDED_PROTO,
    HttpHeaders.X_FRAME_OPTIONS,
    HttpHeaders.X_POWERED_BY,
    HttpHeaders.X_REQUESTED_WITH,
    HttpHeaders.X_USER_IP,
    HttpHeaders.X_XSS_PROTECTION
    // End of collection
  );

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
   * @param clientRequest Providing the HTTP information necessary from the client side
   *
   * @return A curl command that emulates the client request
   */
  public static String createCurlCommand(ClientRequest clientRequest, Providers providers, String canonicalRepresentation, String sharedSecret, String apiKey) {

    // Extract the original headers
    MultivaluedMap<String, Object> originalHeaders = clientRequest.getHeaders();

    // Create a lexicographically sorted set of the header names for lookup later
    Set<String> headerNames = filterAndSortHeaderNames(originalHeaders.keySet());

    // Compute the representation signature
    String signature = new String(HmacUtils.computeSignature("HmacSHA1", canonicalRepresentation.getBytes(), sharedSecret.getBytes()));
    String authorization = "HMAC " + apiKey + " " + signature;

    String httpMethod = clientRequest.getMethod().toUpperCase();
    StringBuilder curlCommand = new StringBuilder("curl --verbose ");
    curlCommand.append("--output \"response.txt\" ");

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

    // Sort the remaining headers lexicographically by header name.
    // Trim header values by removing any whitespace before the first non-whitespace character and after the last non-whitespace character.
    // Combine lowercase header names and header values using a single colon (“:”) as separator. Do not include whitespace characters around the separator.
    // Combine all headers using a single newline (U+000A) character and append them to the canonical representation, followed by a single newline (U+000A) character.
    for (String headerName : headerNames) {
      curlCommand
        .append("--header \"")
        .append(headerName.toLowerCase())
        .append(":");
      // TODO Consider effect of different separators on this list
      for (Object value : originalHeaders.get(headerName)) {
        curlCommand
          .append(value.toString());
      }
      curlCommand
        .append("\" ");
    }

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

    SortedSet<String> headerNames = filterAndSortHeaderNames(originalHeaders.keySet());

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

    // Append the url-decoded path (including context) and query to the canonical representation
    canonicalRepresentation.append(uri.getPath());
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
    Set<String> headerNames = filterAndSortHeaderNames(originalHeaders.keySet());

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

    // Append the path (including context) to the canonical representation
    canonicalRepresentation.append(containerRequest.getAbsolutePath().getPath());

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

  /**
   * @param originalHeaderNames The original HTTP header names
   *
   * @return A filtered collection of headers that should be included in the canonical representation
   */
  private static SortedSet<String> filterAndSortHeaderNames(Set<String> originalHeaderNames) {
    // Create a lexicographically sorted set of the header names for lookup later
    SortedSet<String> headerNames = Sets.newTreeSet(originalHeaderNames);

    // Remove some headers that should not be included or will have special treatment
    headerNames.removeAll(excludedHttpHeaderNames);

    return headerNames;
  }


}

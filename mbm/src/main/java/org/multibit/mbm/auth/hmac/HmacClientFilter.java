package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.multibit.mbm.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
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
  private final Providers providers;

  public HmacClientFilter(String apiKey, String sharedSecret, Providers providers) {
    this.apiKey = apiKey;
    this.sharedSecret = sharedSecret;
    this.providers = providers;
  }

  public ClientResponse handle(ClientRequest cr) {
    ClientRequest mcr = modifyRequest(cr);

    // Call the next client handler in the filter chain
    ClientResponse resp = getNext().handle(mcr);

    // Modify the response
    return modifyResponse(resp);
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

    String canonicalRepresentation = HmacUtils.createCanonicalRepresentation(clientRequest,providers);
    log.debug("Client side canonical representation: '{}'", canonicalRepresentation);

    // Build the authorization header
    String signature = new String(HmacUtils.computeSignature("HmacSHA1", canonicalRepresentation.getBytes(), sharedSecret.getBytes()));
    String authorization = "HMAC " + apiKey + " " + signature;
    clientRequest.getHeaders().put(HttpHeaders.AUTHORIZATION, Lists.<Object>newArrayList(authorization));

    return clientRequest;
  }

  private ClientResponse modifyResponse(ClientResponse resp) {
    // Placeholder to complete the implementation
    return resp;
  }



}
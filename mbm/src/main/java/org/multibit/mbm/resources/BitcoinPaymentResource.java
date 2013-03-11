package org.multibit.mbm.resources;

import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.core.bitcoin.service.BitcoinService;
import org.multibit.mbm.core.bitcoin.service.SwatchBuilder;
import org.multibit.mbm.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * <p>Resource to provide the following to {@link org.multibit.mbm.core.model.Customer}:</p>
 * <ul>
 * <li>Provision of REST endpoints</li>
 * </ul>
 *
 * @since 0.0.1
 * TODO Consider a base class with configured UriInfo and HttpHeaders (need to verify Prototype pattern)
 */
@Component
@Path("/bitcoin")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class BitcoinPaymentResource extends BaseResource {

  private static final Logger log = LoggerFactory.getLogger(BitcoinPaymentResource.class);

  //@Resource(name="defaultBitcoinService")
  BitcoinService bitcoinService;

  /**
   * Provides a Bitcoin swatch with the given parameters
   *
   * @param address  The Bitcoin address
   * @param amount   The amount
   * @param label    The associated label
   *
   * @return A String containing the message
   *
   * @throws java.io.IOException If something goes wrong
   *
   * TODO Refactor this into a POST with payload
   */
  @GET
  @Timed
  @Path("/swatch")
  //@Produces(ImageMediaType.IMAGE_PNG)
  @Produces(MediaType.TEXT_PLAIN)
  public Response swatch(
    @RestrictedTo({Authority.ROLE_CUSTOMER}) User user,
    @QueryParam("address") String address,
    @QueryParam("amount") String amount,
    @QueryParam("label") String label) throws IOException {

    // Generate the swatch

    String rawSwatch = SwatchBuilder
      .newInstance()
      .withAddress(address)
      .withAmount(amount)
      .withLabel(label)
      .build();

    // Configure the response

    // TODO Build a suitable image
//    response.setHeader("Content-Type", "image/png");
//    ImageIO.write(rawSwatch, "png", response.getOutputStream());

    return Response.ok(rawSwatch).type(MediaType.TEXT_PLAIN).build();
  }

  /**
   * Creates a new Bitcoin address for monitoring
   *
   * @param user The authenticated user
   *
   * @return A String containing the message
   */
  @POST
  @Path("/address")
  public String newAddress(
    @RestrictedTo({Authority.ROLE_CUSTOMER}) User user) {

    String newBitcoinAddress = bitcoinService.getNextAddress(user.getId());
    log.debug("New bitcoin address requested '{}'", newBitcoinAddress);
    return newBitcoinAddress;
  }
}
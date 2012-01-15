package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.bitcoin.service.BitcoinService;
import org.multibit.mbm.customer.service.CustomerService;
import org.multibit.mbm.customer.dto.Customer;
import org.multibit.mbm.qrcode.SwatchGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;

/**
 * <p>Controller to handle the submission and receipt of Bitcoin messages</p>
 * <ul>
 * <li>Registering a Bitcoin address to monitor</li>
 * </ul>
 */
@Controller
@RequestMapping(value = "/v1")
public class BitcoinController {

  // Define the resource paths for this controller so they can be shared by the functional tests

  private static final Logger log = LoggerFactory.getLogger(BitcoinController.class);

  @Resource(name="defaultBitcoinService")
  BitcoinService bitcoinService;

  @Resource()
  CustomerService customerService;

  // TODO Reinstate this annotation
//  @Resource
  SwatchGenerator swatchGenerator=new SwatchGenerator();

  /**
   * Provides a Bitcoin swatch with the given parameters
   *
   * @param address  The Bitcoin address
   * @param amount   The amount
   * @param label    The associated label
   * @param response The response
   *
   * @return A String containing the message
   *
   * @throws java.io.IOException If something goes wrong
   */
  @RequestMapping(value = "/swatch", method = RequestMethod.GET)
  @ResponseBody
  public void swatch(
    @RequestParam("address") String address,
    @RequestParam("amount") String amount,
    @RequestParam("label") String label,
    HttpServletResponse response) throws IOException {

    // Generate the swatch
    BufferedImage rawSwatch = swatchGenerator.generateSwatch(address, amount, label);

    // Configure the response
    response.setHeader("Content-Type", "image/png");
    ImageIO.write(rawSwatch, "png", response.getOutputStream());
  }

  /**
   * Creates a new Bitcoin address for monitoring
   *
   * @param principal The security principal
   *
   * @return A String containing the message
   */
  @RequestMapping(value = "/new-address", method = RequestMethod.POST)
  @ResponseBody
  public String newAddress(Principal principal) {
    Customer customer = customerService.getCustomerByPrincipal(principal);
    if (customer == null) {
      // TODO Should this be an authorisation failure?
      return null;
    }
    String newBitcoinAddress = bitcoinService.getNextAddress(customer.getId());
    log.debug("New bitcoin address requested '{}'", newBitcoinAddress);
    return newBitcoinAddress;
  }

}

package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.qrcode.SwatchGenerator;
import org.multibit.mbm.web.view.PNGImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;

/**
 * <p>Controller to handle the submission and receipt of Bitcoin messages</p>
 * <ul>
 * <li>Registering a Bitcoin address to monitor</li>
 * </ul>
 */
@Controller
@RequestMapping(value = "/v1/bitcoin")
public class BitcoinController {

  private static final Logger log = LoggerFactory.getLogger(BitcoinController.class);

  @Resource
  SwatchGenerator swatchGenerator;

  /**
   * Provides a Bitcoin swatch with the given parameters
   *
   *
   * @param address The Bitcoin address
   * @param amount The amount
   * @param label The associated label
   * @return A String containing the message
   */
  @RequestMapping(value = "/swatch", method = RequestMethod.GET)
  @ResponseBody
  public PNGImageView swatch(
    @RequestParam("address") String address,
    @RequestParam("amount") String amount,
    @RequestParam("label") String label) {
    BufferedImage rawSwatch = swatchGenerator.generateSwatch(address, amount, label);
    return new PNGImageView(rawSwatch);
  }

}

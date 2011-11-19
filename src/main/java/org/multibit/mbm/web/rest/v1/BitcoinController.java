package org.multibit.mbm.web.rest.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>Controller to handle the submission and receipt of Bitcoin messages</p>
 * <ul>
 *   <li>Registering a Bitcoin address to monitor</li>
 * </ul>
 */
@Controller
@RequestMapping(value="/v1/bitcoin")
public class BitcoinController {

  private static final Logger log = LoggerFactory.getLogger(BitcoinController.class);

  /**
   * Echo service
   * @param bitcoinAddress The Bitcoin address to monitor
   * @return A String containing the message
   */
  @RequestMapping(value="/monitor/{bitcoinAddress}",method = RequestMethod.POST)
  @ResponseBody
  public String alert(@PathVariable("bitcoinAddress") String bitcoinAddress) {
    log.debug("Adding monitor for '{}'",bitcoinAddress);
    return bitcoinAddress;
  }

}

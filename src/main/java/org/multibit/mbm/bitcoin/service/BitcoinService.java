package org.multibit.mbm.bitcoin.service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.bitcoin.core.Address;

/**
 * <p>Service to provide the following to Controllers</p>
 * <ul>
 * <li>Next available address from the address bucket.</li>
 * <li>Registration of addresses for notification of receipt of relevant Transactions</li>
 * </p>
 *
 * @since 1.0.0
 */
public interface BitcoinService {

  public static final long DEFAULT_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(3);

  /**
   * <p>Get the next bitcoin address from the address bucket and register the id for notifications. 
   * The address is guaranteed to be a never-previously used Bitcoin address</p>
   *
   * @param id The id that is to be notified if the address receives any bitcoin
   * @return nextAddress The next Bitcoin address to use, or null if no more addresses are available
   * 
   */
  public String getNextAddress(Long id);

  /**
   * <p>Create a swatch containing the specified address, label and amount</p>
   *
   * <p>Time to live for the registration is set to BitcoinService.DEFAULT_TIME_TO_LIVE</p>
   *
   * @param address         The address to show on the swatch and to register the AddressListener against
   * @param label           Text to appear in the label of the swatch
   * @param amount          Text to appear in the amount field of the swatch, string e.g. "0.12"
   * @return swatch         BufferedImage containing the rendition of the generated swatch
   */
  public BufferedImage createSwatch(String address, String label, String amount);
    
  /**
   * <p>Set the List of addresses that are available for use by the application</p>
   * @param addressBucket The list of available addresses.
   */
  public void setAddressBucket(List<Address> addressBucket);

}

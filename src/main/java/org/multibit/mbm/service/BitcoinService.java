package org.multibit.mbm.service;

import com.google.bitcoin.core.Address;

/**
 * <p>An interface encapsulating the iteraction with the Bitcoin network for MultiBitMerchant</p>
 *
 * @author jim
 */
public interface BitcoinService {

  // TODO Consider using TimeUnit in java.util.concurrent
  public static final long DEFAULT_TIME_TO_LIVE = 1000 * 60 * 60 * 24;    // 24 hours, in milliseconds

  /**
   * <p>Get the next bitcoin address from the address bucket. This is guaranteed to be a never-previously used Bitcoin address</p>
   *
   * @return nextAddress The next Bitcoin address to use, or null if no more addresses are available
   *
   * @since 1.0.0
   */
  public Address getNextAddress();

  /**
   * <p>Register a Bitcoin address so that the AddressListener receives notifications
   * of any Transactions involving this address</p>
   *
   * <p>Time to live for the registration is set to BitcoinService.DEFAULT_TIME_TO_LIVE</p>
   *
   * @param address         The address to register the AddressListener against
   * @param addressListener The addressListener that will receive notifications
   */
  public void registerAddress(Address address, AddressListener addressListener);
}

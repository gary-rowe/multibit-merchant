package org.multibit.mbm.service;

import java.util.concurrent.TimeUnit;

import com.google.bitcoin.core.Address;

/**
 * <p>Service to provide the following to Controllers</p>
 * <ul>
 * <li>Next available address from the address bucket.</li>
 * <li>Registration of addresses for notification of relevant Transactions</li>
 * </p>
 *
 * @since 1.0.0
 */
public interface BitcoinService {

  public static final long DEFAULT_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(1);

  /**
   * <p>Get the next bitcoin address from the address bucket. This is guaranteed to be a never-previously used Bitcoin address</p>
   *
   * @return nextAddress The next Bitcoin address to use, or null if no more addresses are available
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

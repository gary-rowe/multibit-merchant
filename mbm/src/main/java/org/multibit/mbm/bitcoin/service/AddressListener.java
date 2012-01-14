package org.multibit.mbm.bitcoin.service;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Transaction;

/**
 * <p>Interface to provide callback from BitcoinService.  Callbacks are:</p>
 * <ul>
 * <li>A transaction has been received for the address with zero confirmations.</li>
 * <li>A transaction has been received for an address in the block chain</li>
 * </p>
 *
 * @since 1.0.0
 */
public interface AddressListener {
  /**
   * <p>This is called on a Peer thread when a block is received that sends some
   * coins to you. Note that this will also be called when downloading the block
   * chain as the wallet balance catches up so if you don't want that register
   * the event listener after the chain is downloaded. It's safe to use methods
   * of wallet during the execution of this callback.</p>
   * 
   * @param address
   *          The address that received the coins
   * @param transaction
   *          The transaction which sent us the coins.
   */
  void onCoinsReceived(Address address, Transaction transaction);

  /**
   * This is called on a Peer thread when a transaction is received that sends
   * some coins to you. It's safe to use methods of wallet during the execution
   * of this callback.
   * 
   * @param address
   *          The address that received the coins/
   * @param tx
   *          The transaction which sent us the coins.
   */
  public void onPendingCoinsReceived(Address address, Transaction tx);
}

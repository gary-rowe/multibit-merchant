package org.multibit.mbm.service;

import com.google.bitcoin.core.Address;

/**
 * an interface encapsulating the iteraction with the Bitcoin network for MultiBitMerchant
 * @author jim
 *
 */
public interface BitcoinService {

    public static final long DEFAULT_TIME_TO_LIVE = 1000 * 60 * 60 * 24;    // 24 hours, in milliseconds

    /**
     * get the next bitcoin address from the address bucket
     * 
     * this is guaranteed to be a never-previously used Bitcoin address
     * 
     * @return nextAddress The next Bitcoin address to use.
     *                     Null if no more addresses are available
     */
    public Address getNextAddress();
    
    /**
     * register a Bitcoin address so that the AddressListener receives notifications
     * of any Transactions involving this address
     * 
     * time to live for the registration is set to BitcoinService.DEFAULT_TIME_TO_LIVE
     * @param address The address to register the AddressListener against
     * @param addressListener The addressListener that will receive notifications
     */
    public void registerAddress(Address address, AddressListener addressListener);
}

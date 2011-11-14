package org.multibit.mbm.service;

import com.google.bitcoin.core.Address;

public class DefaultBitcoinService implements BitcoinService {

  @Override
  public Address getNextAddress() {
    return null;
  }

  @Override
  public void registerAddress(Address address, AddressListener addressListener) {
  }
}

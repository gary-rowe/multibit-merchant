package org.multibit.mbm.service;

import java.io.IOException;

import org.multibit.mbm.domain.AlertMessage;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Transaction;

public class DefaultAddressListener implements AddressListener {

  private Address address;
  private Long id;
  
  public DefaultAddressListener(Address address, Long id) {
    this.address = address;
    this.id = id;
  }
  
  @Override
  public void onCoinsReceived(Address address, Transaction transaction) {
    if (this.address.equals(address)) {
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setText("Payment received for " + address.toString());
      try {
        BroadcastService.INSTANCE.broadcast(id, alertMessage);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {
      // wrongly wired up
    }
  }

  @Override
  public void onPendingCoinsReceived(Address address, Transaction tx) {
    if (this.address.equals(address)) {
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setText("Payment received for " + address.toString());
      try {
        BroadcastService.INSTANCE.broadcast(id, alertMessage);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {
      // wrongly wired up
    }
  }

  public long getId() {
    return id;
  }
}

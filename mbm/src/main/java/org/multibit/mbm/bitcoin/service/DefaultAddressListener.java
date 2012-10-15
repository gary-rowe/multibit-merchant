package org.multibit.mbm.bitcoin.service;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Transaction;
import com.yammer.dropwizard.logging.Log;

public class DefaultAddressListener implements AddressListener {

  private static final Log LOG = Log.forClass(DefaultAddressListener.class);

  private Address address;
  private Long id;

  public DefaultAddressListener(Address address, Long id) {
    this.address = address;
    this.id = id;
  }

  @Override
  public void onCoinsReceived(Address address, Transaction transaction) {
// TODO Re-implement this
//    if (this.address.equals(address)) {
//      AlertMessage alertMessage = new AlertMessage();
//      alertMessage.setText("Payment received for " + address.toString());
//      try {
//        BroadcastService.INSTANCE.broadcast(id, alertMessage);
//      } catch (IOException e) {
//        LOG.error(e.getClass().getName() + " " + e.getMessage());
//      }
//    } else {
//      // wrongly wired up
//      LOG.warn("Address listener for address " + address.toString() + " is wrongly wired up");
//    }
  }

  @Override
  public void onPendingCoinsReceived(Address address, Transaction tx) {
// TODO Re-implement this
//    if (this.address.equals(address)) {
//      AlertMessage alertMessage = new AlertMessage();
//      alertMessage.setText("Payment received for " + address.toString());
//      try {
//        BroadcastService.INSTANCE.broadcast(id, alertMessage);
//      } catch (IOException e) {
//        LOG.error(e.getClass().getName() + " " + e.getMessage());
//      }
//    } else {
//      // wrongly wired up
//      LOG.warn("Address listener for address " + address.toString() + " is wrongly wired up");
//    }
  }

  public long getId() {
    return id;
  }
}

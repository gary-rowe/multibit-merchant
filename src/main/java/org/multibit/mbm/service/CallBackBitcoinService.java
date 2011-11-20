package org.multibit.mbm.service;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionOutput;
import com.google.common.collect.Maps;
import org.multibit.mbm.qrcode.SwatchGenerator;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Service to provide integration testing of BitcoinService to Controllers. This
 * implementation is for integration testing and does not actually talk to the
 * Bitcoin network.
 * </p>
 * <ul>
 * <li>Next available address from the address bucket.</li>
 * <li>Registration of addresses for notification of relevant Transactions</li>
 * </p>
 * 
 * @since 1.0.0
 */
@Component
public class CallBackBitcoinService implements BitcoinService {

  private static final int NO_ADDRESS_GOT_YET = -1;
  
  /**
   * the List of Bitcoin addresses available for use
   */
  private List<Address> addressBucket;

  /**
   * the index of the last address that was given out
   */
  private int lastAddressIndex=NO_ADDRESS_GOT_YET;

  /**
   * Map of addresses to address listeners
   */
  private Map<Address, AddressListener> addressToAddressListenerMap= Maps.newHashMap();;

  // TODO Reinstate this annotation
//  @Resource
  private SwatchGenerator swatchGenerator;

  public CallBackBitcoinService() {
    swatchGenerator=new SwatchGenerator();

  }

  public String getNextAddress(Long id) {
    // if no addresses available return null;
    if (addressBucket == null || lastAddressIndex >= addressBucket.size() - 1) {
      return null;
    } else {
      lastAddressIndex++;
      return addressBucket.get(lastAddressIndex).toString();
    }
  }

  // Set up methods
  public List<Address> getAddressBucket() {
    return addressBucket;
  }

  @Override
  public BufferedImage createSwatch(String address, String label, String amount) {
    return swatchGenerator.generateSwatch(address, amount, label);
  }

  @Override
  public void setAddressBucket(List<Address> addressBucket) {
    this.addressBucket = addressBucket;
  }

  /**
   * notify any address listeners of any of the addresses which receive bitcoin
   * in the transaction
   * 
   * @param transaction
   * @param isPending
   *          true = transaction is a pending transaction i.e zero confirms
   *          false = transaction is a confirmed transaction
   */
  public void notifyAddressListenersOfTransaction(Transaction transaction,
      boolean isPending) {
    if (transaction != null) {
      List<TransactionOutput> transactionOutputs = transaction.getOutputs();
      if (transactionOutputs != null) {
        for (TransactionOutput transactionOutput : transactionOutputs) {
          try {
            Address loopAddress = transactionOutput.getScriptPubKey()
                .getToAddress();

            AddressListener addressListener = addressToAddressListenerMap
                .get(loopAddress);
            if (addressListener != null) {
              // notify listener
              if (isPending) {
                addressListener
                    .onPendingCoinsReceived(loopAddress, transaction);
              } else {
                addressListener.onCoinsReceived(loopAddress, transaction);
              }
            }
          } catch (ScriptException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public void setSwatchGenerator(SwatchGenerator swatchGenerator) {
    this.swatchGenerator = swatchGenerator;
  }


}

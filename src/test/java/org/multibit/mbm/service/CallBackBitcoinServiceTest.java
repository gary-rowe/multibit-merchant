package org.multibit.mbm.service;

import com.google.bitcoin.core.*;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CallBackBitcoinServiceTest{

  /**
   * Verifies the integration test bitcoin service
   */
  @Test
  public void testGetNextAddress() {
    // create a bucket of bitcoin addresses;
    NetworkParameters prodNet = NetworkParameters.prodNet();
    
    List<Address> addressBucket = new LinkedList<Address>();
    
    ECKey key1 = new ECKey();
    Address address1 = key1.toAddress(prodNet); 
    addressBucket.add(address1);
    
    ECKey key2 = new ECKey();
    Address address2 = key2.toAddress(prodNet); 
    addressBucket.add(address2);
    
    ECKey key3 = new ECKey();
    Address address3 = key3.toAddress(prodNet); 
    addressBucket.add(address3);
    
    BitcoinService bitcoinService = CallBackBitcoinService.INSTANCE;
   
    assertNotNull(bitcoinService);
    
    // should return null if no address bucket set

    assertNull(bitcoinService.getNextAddress(1L));
    
    bitcoinService.setAddressBucket(addressBucket);
    
    assertEquals(address1.toString(), bitcoinService.getNextAddress(1L));
    assertEquals(address2.toString(), bitcoinService.getNextAddress(1L));
    assertEquals(address3.toString(), bitcoinService.getNextAddress(1L));
    
    // no more addresses available
    assertNull(bitcoinService.getNextAddress(1L));
  }
  
  @Test
  public void testReceivePendingTransaction() {
    // create a bucket of bitcoin addresses;
    NetworkParameters prodNet = NetworkParameters.prodNet();
    
    List<Address> addressBucket = new LinkedList<Address>();
    
    ECKey key1 = new ECKey();
    Address address1 = key1.toAddress(prodNet); 
    addressBucket.add(address1);
        
    ECKey key2 = new ECKey();
    Address address2 = key2.toAddress(prodNet); 
    addressBucket.add(address2);
       
    CallBackBitcoinService bitcoinService = CallBackBitcoinService.INSTANCE;
   
    assertNotNull(bitcoinService);
    
//    WasICalledAddressListener addressListener1 = new WasICalledAddressListener();
//    WasICalledAddressListener addressListener2 = new WasICalledAddressListener();

    BufferedImage swatch1 = bitcoinService.createSwatch(address1.toString(), "label1", "1.0");
    BufferedImage swatch2 = bitcoinService.createSwatch(address2.toString(), "label2", "2.0");
    assertNotNull(swatch1);
    assertNotNull(swatch2);
    
    Transaction transaction1 = TestUtils.createFakeTx(prodNet, BigInteger.ONE, address1);   // 1 BTC received to address1

    bitcoinService.notifyAddressListenersOfTransaction(transaction1, true);
    
//    assertTrue(addressListener1.onPendingCoinsReceivedCalled);
//    assertFalse(addressListener1.onCoinsReceivedCalled);
//       
//    assertFalse(addressListener2.onPendingCoinsReceivedCalled);
//    assertFalse(addressListener2.onCoinsReceivedCalled);
  }
  
  @Test
  public void testReceiveConfirmedTransaction() {
    // create a bucket of bitcoin addresses;
    NetworkParameters prodNet = NetworkParameters.prodNet();
    
    List<Address> addressBucket = new LinkedList<Address>();
    
    ECKey key1 = new ECKey();
    Address address1 = key1.toAddress(prodNet); 
    addressBucket.add(address1);
        
    ECKey key2 = new ECKey();
    Address address2 = key2.toAddress(prodNet); 
    addressBucket.add(address2);
       
    CallBackBitcoinService bitcoinService = CallBackBitcoinService.INSTANCE;
   
    assertNotNull(bitcoinService);
    
//    WasICalledAddressListener addressListener1 = new WasICalledAddressListener();
//    WasICalledAddressListener addressListener2 = new WasICalledAddressListener();

    BufferedImage swatch1 = bitcoinService.createSwatch(address1.toString(), "label1", "1.0");
    BufferedImage swatch2 = bitcoinService.createSwatch(address2.toString(), "label2", "2.0");
    assertNotNull(swatch1);
    assertNotNull(swatch2);
   
    Transaction transaction2 = TestUtils.createFakeTx(prodNet, BigInteger.ONE, address2);   // 1 BTC received to address2

    bitcoinService.notifyAddressListenersOfTransaction(transaction2, false);
    
//    assertFalse(addressListener1.onPendingCoinsReceivedCalled);
//    assertFalse(addressListener1.onCoinsReceivedCalled);
//       
//    assertFalse(addressListener2.onPendingCoinsReceivedCalled);
//    assertTrue(addressListener2.onCoinsReceivedCalled);
  }
  
  class WasICalledAddressListener implements AddressListener {
    public boolean onCoinsReceivedCalled = false;
    public boolean onPendingCoinsReceivedCalled = false;
       
    @Override
    public void onCoinsReceived(Address address, Transaction transaction) {
      onCoinsReceivedCalled = true;
    }

    @Override
    public void onPendingCoinsReceived(Address address, Transaction tx) {
      onPendingCoinsReceivedCalled = true;
    }
  }
}

package org.multibit.mbm.service;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.TestUtils;
import com.google.bitcoin.core.Transaction;

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
    
    BitcoinService bitcoinService = CallBackBitcoinService.getBitcoinService();
   
    assertNotNull(bitcoinService);
    
    // should return null if no address bucket set
    assertNull(bitcoinService.getNextAddress());
    
    bitcoinService.setAddressBucket(addressBucket);
    
    assertEquals(address1, bitcoinService.getNextAddress());
    assertEquals(address2, bitcoinService.getNextAddress());
    assertEquals(address3, bitcoinService.getNextAddress());
    
    // no more addresses available
    assertNull(bitcoinService.getNextAddress());
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
       
    CallBackBitcoinService bitcoinService = (CallBackBitcoinService)CallBackBitcoinService.getBitcoinService();
   
    assertNotNull(bitcoinService);
    
    WasICalledAddressListener addressListener1 = new WasICalledAddressListener();
    WasICalledAddressListener addressListener2 = new WasICalledAddressListener();

    BufferedImage swatch1 = bitcoinService.createSwatchAndRegisterAddress(address1, "label1", "1.0", addressListener1);
    BufferedImage swatch2 = bitcoinService.createSwatchAndRegisterAddress(address2, "label2", "2.0", addressListener2);
    assertNotNull(swatch1);
    assertNotNull(swatch2);
    
    Transaction transaction1 = TestUtils.createFakeTx(prodNet, BigInteger.ONE, address1);   // 1 BTC received to address1

    bitcoinService.notifyAddressListenersOfTransaction(transaction1, true);
    
    assertTrue(addressListener1.onPendingCoinsReceivedCalled);
    assertFalse(addressListener1.onCoinsReceivedCalled);
       
    assertFalse(addressListener2.onPendingCoinsReceivedCalled);
    assertFalse(addressListener2.onCoinsReceivedCalled);
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
       
    CallBackBitcoinService bitcoinService = (CallBackBitcoinService)CallBackBitcoinService.getBitcoinService();
   
    assertNotNull(bitcoinService);
    
    WasICalledAddressListener addressListener1 = new WasICalledAddressListener();
    WasICalledAddressListener addressListener2 = new WasICalledAddressListener();

    BufferedImage swatch1 = bitcoinService.createSwatchAndRegisterAddress(address1, "label1", "1.0", addressListener1);
    BufferedImage swatch2 = bitcoinService.createSwatchAndRegisterAddress(address2, "label2", "2.0", addressListener2);
    assertNotNull(swatch1);
    assertNotNull(swatch2);
   
    Transaction transaction2 = TestUtils.createFakeTx(prodNet, BigInteger.ONE, address2);   // 1 BTC received to address2

    bitcoinService.notifyAddressListenersOfTransaction(transaction2, false);
    
    assertFalse(addressListener1.onPendingCoinsReceivedCalled);
    assertFalse(addressListener1.onCoinsReceivedCalled);
       
    assertFalse(addressListener2.onPendingCoinsReceivedCalled);
    assertTrue(addressListener2.onCoinsReceivedCalled);
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

package org.multibit.mbm.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.multibit.mbm.qrcode.SwatchGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Block;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.PeerAddress;
import com.google.bitcoin.core.PeerEventListener;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.PendingTransactionListener;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionOutput;
import com.google.bitcoin.discovery.DnsDiscovery;
import com.google.bitcoin.discovery.IrcDiscovery;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.BoundedOverheadBlockStore;

public class DefaultBitcoinService implements BitcoinService, PeerEventListener, PendingTransactionListener {

  public Logger log = LoggerFactory.getLogger(DefaultBitcoinService.class.getName());

  public static final String MULTIBIT_PREFIX = "multibit";
  public static final String TEST_NET_PREFIX = "testnet";
  public static final String SEPARATOR = "-";

  public static final String BLOCKCHAIN_SUFFIX = ".blockchain";

  public static final String IRC_CHANNEL_TEST = "#bitcoinTEST";;

  private static final int NO_ADDRESS_GOT_YET = -1;

  private final NetworkParameters networkParameters;

  private PeerGroup peerGroup;

  private BlockChain blockChain;

  /**
   * the List of Bitcoin addresses available for use
   */
  private List<Address> addressBucket;

  /**
   * the index of the last address that was given out
   */
  private int lastAddressIndex;

  /**
   * Map of addresses to address listeners
   */
  private Map<Address, AddressListener> addressToAddressListenerMap;

  /**
   * the instance of the BitcoinService
   */
  private static BitcoinService instance;
  
  private SwatchGenerator swatchGenerator;

  /**
   * start up the DefaultBitcoinServer listening to a single address if you send
   * bitcoin to the address printed out you get a message
   * 
   * @param args
   */
  public static void main(String[] args) {
    // create a two addresses for the address bucket
    ECKey key1 = new ECKey();
    ECKey key2 = new ECKey();

    Address address1 = key1.toAddress(NetworkParameters.prodNet());
    Address address2 = key2.toAddress(NetworkParameters.prodNet());

    List<Address> addressBucket = new LinkedList<Address>();
    addressBucket.add(address1);
    addressBucket.add(address2);
    
    BitcoinService bitcoinService = DefaultBitcoinService.getBitcoinService();
    AddressListener addressListener = new AddressListener() {
      @Override
      public void onCoinsReceived(Address address, Transaction transaction) {
        System.out.println("DefaultBitcoinService#main the address " + address.toString() + " saw the confirmed transaction "
            + transaction);
      }

      @Override
      public void onPendingCoinsReceived(Address address, Transaction transaction) {
        System.out.println("DefaultBitcoinService#main the address " + address.toString() + " saw the pending transaction "
            + transaction);
      }
    };

    BufferedImage swatch1 = bitcoinService.createSwatchAndRegisterAddress(address1, "myLabel1", "12.34", addressListener);
    ImageIcon icon1 = new ImageIcon(swatch1);
    JOptionPane.showMessageDialog(null, "", "Example Swatch 1", JOptionPane.INFORMATION_MESSAGE, icon1);

    BufferedImage swatch2 = bitcoinService.createSwatchAndRegisterAddress(address2, "myLabel2", "62.34", addressListener);
    ImageIcon icon2 = new ImageIcon(swatch2);
    JOptionPane.showMessageDialog(null, "", "Example Swatch 2", JOptionPane.INFORMATION_MESSAGE, icon2);

    System.out.println("DefaultBitcoinService#main - listening to addresses " + address1 + " and " + address2);
    System.out.println("DefaultBitcoinService#main - send either some bitcoin and you should see the listeners print out here.");

  }

  private DefaultBitcoinService() {
    addressToAddressListenerMap = new HashMap<Address, AddressListener>();
    lastAddressIndex = NO_ADDRESS_GOT_YET;

    // TODO replace settings from config file

    // BEGIN config options

    // default to prodNet
    boolean useTestNet = false;

    // default to not connecting to a single node
    String singleNodeConnection = "";

    // END config options

    networkParameters = useTestNet ? NetworkParameters.testNet() : NetworkParameters.prodNet();

    BlockStore blockStore = null;
    try {
      // Load the block chain
      String filePrefix = getFilePrefix(useTestNet);
      String blockchainFilename = filePrefix + BLOCKCHAIN_SUFFIX;

      log.debug("Reading block store '" + blockchainFilename + "' from disk");

      blockStore = new BoundedOverheadBlockStore(networkParameters, new File(blockchainFilename));

      log.debug("Connecting ...");
      blockChain = new BlockChain(networkParameters, blockStore);

      peerGroup = new PeerGroup(blockStore, networkParameters, blockChain);

      if (singleNodeConnection != null && !singleNodeConnection.equals("")) {
        try {
          peerGroup.addAddress(new PeerAddress(InetAddress.getByName(singleNodeConnection)));
          peerGroup.setMaxConnections(1);
        } catch (UnknownHostException e) {
          log.error(e.getMessage(), e);
        }
      } else {
        // use DNS for production, IRC for test
        if (useTestNet) {
          peerGroup.addPeerDiscovery(new IrcDiscovery(IRC_CHANNEL_TEST));
        } else {
          peerGroup.addPeerDiscovery(new DnsDiscovery(networkParameters));
        }
      }
      // add this class as a PeerEventListener
      peerGroup.addEventListener(this);

      // add this class as a PendingTransaction listener
      peerGroup.addPendingTransactionListener(this);

      peerGroup.start();
    } catch (BlockStoreException e) {
      e.printStackTrace();
    }
    
    // create a SwatchGenerator for later use
    swatchGenerator = new SwatchGenerator();
    
    // warm it up
    swatchGenerator.generateSwatch((new ECKey()).toAddress(networkParameters).toString(), "0.0", "warmup");
  }

  /**
   * get the single instance of the Bitcoin service
   * 
   * @return bitcoinService
   */
  public static BitcoinService getBitcoinService() {
    if (instance == null) {
      instance = new DefaultBitcoinService();
    }
    return instance;
  }

  @Override
  public Address getNextAddress() {
    // if no addresses available return null;
    if (addressBucket == null || lastAddressIndex >= addressBucket.size() - 1) {
      return null;
    } else {
      lastAddressIndex++;
      return addressBucket.get(lastAddressIndex);
    }
  }

  @Override
  public BufferedImage createSwatchAndRegisterAddress(Address address, String label, String amount, AddressListener addressListener) {
    // TODO add time-to-live and a timer to remove stale addressListeners
    if (address != null && addressListener != null) {
      addressToAddressListenerMap.put(address, addressListener);
    }
    
    BufferedImage swatch = swatchGenerator.generateSwatch(address.toString(), amount, label);
    return swatch;
  }

  @Override
  public void setAddressBucket(List<Address> addressBucket) {
    this.addressBucket = addressBucket;
  }

  // PeerEventListener methods

  @Override
  public void onBlocksDownloaded(Peer peer, Block block, int blocksLeft) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onChainDownloadStarted(Peer peer, int blocksLeft) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPeerConnected(Peer peer, int peerCount) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPeerDisconnected(Peer peer, int peerCount) {
    // TODO Auto-generated method stub

  }

  // PendingTransactionListener methods

  @Override
  public void processPendingTransaction(Transaction transaction) {
    if (transaction != null) {
      List<TransactionOutput> transactionOutputs = transaction.getOutputs();
      if (transactionOutputs != null) {
        for (TransactionOutput transactionOutput : transactionOutputs) {
          try {
            Address loopAddress = transactionOutput.getScriptPubKey().getToAddress();

            AddressListener addressListener = addressToAddressListenerMap.get(loopAddress);
            if (addressListener != null) {
              // notify listener
              addressListener.onPendingCoinsReceived(loopAddress, transaction);
            }
          } catch (ScriptException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // Utility methods

  private String getFilePrefix(boolean useTestNet) {
    return useTestNet ? MULTIBIT_PREFIX + SEPARATOR + TEST_NET_PREFIX : MULTIBIT_PREFIX;
  }

}

package org.multibit.mbm.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.multibit.mbm.qrcode.SwatchGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
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

@Component
public class DefaultBitcoinService implements BitcoinService, PeerEventListener, PendingTransactionListener {

  public Logger log = LoggerFactory.getLogger(DefaultBitcoinService.class.getName());

  public static final String MULTIBIT_PREFIX = "multibit";
  public static final String TEST_NET_PREFIX = "testnet";
  public static final String SEPARATOR = "-";

  public static final String BLOCKCHAIN_SUFFIX = ".blockchain";

  public static final String IRC_CHANNEL_TEST = "#bitcoinTEST";;

  private static final int NO_ADDRESS_GOT_YET = -1;

  public static final String DEFAULT_ADDRESS_BUCKET_FILENAME = "bulkAddresses.csv";

  private NetworkParameters networkParameters;

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

   // TODO Reinstate this annotation
//  @Resource
  private SwatchGenerator swatchGenerator;

  public DefaultBitcoinService() {
    // TODO replace settings from config file
    // BEGIN config options

    // default to prodNet
    boolean useTestNet = false;

    // default to not connecting to a single node
    String singleNodeConnection = "";

    // END config options

    networkParameters = useTestNet ? NetworkParameters.testNet() : NetworkParameters.prodNet();

    lastAddressIndex = NO_ADDRESS_GOT_YET;

    // load the address bucket
    addressBucket = loadAddressBucket();
    log.info("Address bucket loaded with " + addressBucket.size() + " addresses");

    addressToAddressListenerMap = new HashMap<Address, AddressListener>();

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

    // warm it up - initialises some of the QR code so that it runs faster
    swatchGenerator.generateSwatch((new ECKey()).toAddress(networkParameters).toString(), "0.0", "warmup");
  }

  @Override
  public String getNextAddress(Long id) {
    // if no addresses available return null;
    if (addressBucket == null || lastAddressIndex >= addressBucket.size() - 1) {
      return null;
    } else {
      lastAddressIndex++;

      Address nextAddress = addressBucket.get(lastAddressIndex);

      // register the address and the address listener with id
      // TODO add time-to-live and a timer to remove stale addressListeners
      if (nextAddress != null) {
        AddressListener addressListener = new DefaultAddressListener(nextAddress, id);
        addressToAddressListenerMap.put(nextAddress, addressListener);
      }

      return nextAddress.toString();
    }
  }

  @Override
  public BufferedImage createSwatch(String address, String label, String amount) {
    return swatchGenerator.generateSwatch(address.toString(), amount, label);
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
            // some of the transaction have non-standard scripts that are not
            // addresses
            // we ignore these
          }
        }
      }
    }
  }

  // Utility methods

  private String getFilePrefix(boolean useTestNet) {
    return useTestNet ? MULTIBIT_PREFIX + SEPARATOR + TEST_NET_PREFIX : MULTIBIT_PREFIX;
  }

  /**
   * load the address bucket stored in the default address bucket file
   * 
   * @return
   */
  List<Address> loadAddressBucket() {
    List<Address> newAddressBucket = new LinkedList<Address>();

    // Read in the address bucket
    if ((new File(DEFAULT_ADDRESS_BUCKET_FILENAME)).exists()) {
      FileInputStream fileInputStream = null;
      try {
        fileInputStream = new FileInputStream(DEFAULT_ADDRESS_BUCKET_FILENAME);
        // Get the object of DataInputStream
        InputStream inputStream = new DataInputStream(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null) {
          // see if the input line is an address
          try {
            Address loopAddress = new Address(networkParameters, inputLine);
            newAddressBucket.add(loopAddress);
          } catch (AddressFormatException afe) {
            // not an address
          }
        }
      } catch (IOException ioe) {
        log.error(ioe.getClass().getName() + " " + ioe.getMessage());
      } finally {

        // Close the input stream
        if (fileInputStream != null) {
          try {
            fileInputStream.close();
          } catch (IOException e) {
            log.error(e.getClass().getName() + " " + e.getMessage());
          }
        }
      }
    }
    return newAddressBucket;
  }

  /**
   * start a DefaultBitcoinService
   * 
   * @param args
   */
  public static void main(String[] args) {
    BitcoinService bitcoinService = new DefaultBitcoinService();
  }
}

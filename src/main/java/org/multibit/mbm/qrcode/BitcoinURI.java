package org.multibit.mbm.qrcode;

import java.math.BigInteger;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Utils;

/**
 * <p>Utility class for use by QR code creation</p>
 * <ul>
 * <li>Create bitcoin uri from component elements</li>
 * <li>Parse bitcoin uri to components</li>
 * </p>
 *
 * @since 1.0.0
 */
public class BitcoinURI {

  private static final Logger log = LoggerFactory.getLogger(BitcoinURI.class);

  private Address address;
  private BigInteger amount;
  private String label;

  private boolean parsedOk;

  private static final Pattern AMOUNT_PATTERN = Pattern.compile("([\\d.]+)(?:X(\\d+))?");

  private static final String BITCOIN_PREFIX = "bitcoin:";

  private static StringBuffer stringBuffer;
  
  private NetworkParameters networkParameters;

  public BitcoinURI(NetworkParameters networkParameters, String uriString) {
    // the uri strings accepted are:
    // <an address>
    // bitcoin:<an address>
    // bitcoin:<an address>?amount=<an amount>
    // bitcoin:<an address>?amount=<an amount>&label=<a label>
    // bitcoin:<an address>?label=<a label>&amount=<an amount>

    this.networkParameters = networkParameters;
    
    // initialise the result variables
    address = null;
    amount = null;
    label = "";

    parsedOk = false;

    if (uriString == null || uriString.equals("")) {
      return;
    }

    // see if the string is an address (this format is used by Instawallet)
    try {
      address = new Address(networkParameters, uriString);
      parsedOk = true; // we are done
    } catch (final AddressFormatException x) {
      // do nothing
    }

    if (!parsedOk) {
      // see if the string is a bitcon uri
      if (uriString.startsWith(BITCOIN_PREFIX)) {
        // remove the bitcoin prefix
        uriString = uriString.substring(BITCOIN_PREFIX.length());

        // see if what remains is an address
        try {
          address = new Address(networkParameters, uriString);
          parsedOk = true; // we are done
          log.debug("BitcoinURI - Ping 1");
        } catch (final AddressFormatException x) {
          // do nothing
        }

        if (!parsedOk) {
          // split by question mark
          StringTokenizer addressTokenizer = new StringTokenizer(uriString, "?");
          if (addressTokenizer.countTokens() == 2) {
            String addressString = addressTokenizer.nextToken();
            String queryString = addressTokenizer.nextToken();
            try {
              address = new Address(networkParameters, addressString);
              parsedOk = true;
            } catch (final AddressFormatException x) {
              // do nothing
            }

            StringTokenizer queryTokenizer = new StringTokenizer(queryString, "&");

            while (queryTokenizer.hasMoreTokens()) {
              // get the next nameValue pair - these are of format
              // "<name>=<value>:
              String nameValuePair = queryTokenizer.nextToken();

              // parse nameValuePair
              StringTokenizer nameValueTokenizer = new StringTokenizer(nameValuePair, "=");
              String name = null;
              String value = null;

              if (nameValueTokenizer.hasMoreTokens()) {
                name = nameValueTokenizer.nextToken();
              }
              if (nameValueTokenizer.hasMoreTokens()) {
                value = nameValueTokenizer.nextToken();
              }
              if ("amount".equalsIgnoreCase(name)) {
                // amount to parse
                if (value != null) {
                  Matcher matcher = AMOUNT_PATTERN.matcher(value);
                  if (matcher.matches()) {
                    amount = Utils.toNanoCoins(matcher.group(1));
                    if (matcher.group(2) != null) {
                      amount = amount.multiply(BigInteger.valueOf(10).pow(Integer.parseInt(matcher.group(2)) - 8));
                    }
                  }
                }
              } else if ("label".equalsIgnoreCase(name)) {
                // label
                label = value;
              }
              // we ignore any other name value pairs
            }
          } else {
            // we cannot parse this
          }
        }
      } else {
        // we cannot parse this
      }
    }
  }

  public static String convertToBitcoinURI(String address, String amount, String label) {
    if (address == null) {
      return "";
    }

    stringBuffer = new StringBuffer("bitcoin:");

    stringBuffer.append(address);

    boolean haveOutputQuestionMark = false;
    if (amount != null && amount.length() > 0) {
      stringBuffer.append("?amount=").append(amount);
      haveOutputQuestionMark = true;
    }

    if (label != null && label.length() > 0) {
      if (haveOutputQuestionMark) {
        stringBuffer.append("&label=").append(label);
      } else {
        stringBuffer.append("?label=").append(label);
      }
    }
    return stringBuffer.toString();
  }

  public Address getAddress() {
    return address;
  }

  public BigInteger getAmount() {
    return amount;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return "BitcoinURI[" + address + "," + amount + "," + label + "]";
  }

  public boolean isParsedOk() {
    return parsedOk;
  }
}

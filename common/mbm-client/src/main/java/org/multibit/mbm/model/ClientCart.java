package org.multibit.mbm.model;

/**
 * <p>Value object to provide the following to resources:</p>
 * <ul>
 * <li>Storage of state for Cart representations by the client</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class ClientCart {

  private String localSymbol;
  private String localTotal;
  private String btcTotal;
  private String itemCount;

  public String getLocalSymbol() {
    return localSymbol;
  }

  public void setLocalSymbol(String localSymbol) {
    this.localSymbol = localSymbol;
  }

  public String getLocalTotal() {
    return localTotal;
  }

  public void setLocalTotal(String localTotal) {
    this.localTotal = localTotal;
  }

  public String getBtcTotal() {
    return btcTotal;
  }

  public void setBtcTotal(String btcTotal) {
    this.btcTotal = btcTotal;
  }

  public String getItemCount() {
    return itemCount;
  }

  public void setItemCount(String itemCount) {
    this.itemCount = itemCount;
  }
}

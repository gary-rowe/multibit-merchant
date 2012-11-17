package org.multibit.mbm.core.bitcoin.service;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *         
 */
public class SwatchBuilder {

  private String address;
  private String amount;
  private String label;

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static SwatchBuilder newInstance() {
    return new SwatchBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public String build() {
    validateState();

    isBuilt = true;

    // TODO Implement the Swatch image building code
    return String.format("%s %s %s",address,amount,label);
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
    if (address==null) {
      throw new IllegalStateException("Address field is mandatory");
    }
  }

  /**
   * @param address The Bitcoin address (e.g. "1abcdefgh...")
   *
   * @return The builder
   */
  public SwatchBuilder withAddress(String address) {
    this.address = address;
    return this;
  }

  /**
   * @param amount The amount (e.g. "12.3456789")
   *
   * @return The builder
   */
  public SwatchBuilder withAmount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * @param label The label (e.g. "Order #4567")
   *
   * @return The builder
   */
  public SwatchBuilder withLabel(String label) {
    this.label = label;
    return this;
  }

}

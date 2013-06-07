package org.multibit.mbm.domain.model.model;

/**
 * <p>Builder to provide the following to {@link Supplier}:</p>
 * <ul>
 * <li>Provide a fluent interface to facilitate building the entity</li>
 * </ul>
 * <h3>Note</h3>
 * <p>There is no User setting, since the User is the owner of the relationship
 * and thus handles the addition of the transient Supplier.</p>
 *
 * @since 0.0.1
 *         
 */
public class SupplierBuilder {

  private boolean isBuilt = false;

  /**
   * @return A new instance of the builder
   */
  public static SupplierBuilder newInstance() {
    return new SupplierBuilder();
  }

  /**
   * Handles the building process. No further configuration is possible after this.
   */
  public Supplier build() {
    validateState();

    // Supplier is a DTO and so requires a default constructor
    Supplier supplier = new Supplier();

    isBuilt = true;

    return supplier;
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

}

package org.multibit.mbm.domain.common;

/**
 * <p>Signature interface to provide the following to application:</p>
 * <ul>
 * <li>Indication of the unique object identifier method</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public interface Identifiable {

  /**
   * @return The unique identifier for this object
   */
  Long getId();
}

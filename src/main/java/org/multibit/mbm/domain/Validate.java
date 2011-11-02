package org.multibit.mbm.domain;

/**
 * Provides simple validation tools to domain objects
 */
public final class Validate {

  private Validate() {
  }

  public static void isNotNull(Object obj, String fieldName) {
    if (obj == null) {
      throw new IllegalArgumentException(fieldName + " is null!");
    }
  }

  public static void isNull(Object obj, String fieldName) {
    if (obj != null) {
      throw new IllegalArgumentException(fieldName + " is not null!");
    }
  }

}

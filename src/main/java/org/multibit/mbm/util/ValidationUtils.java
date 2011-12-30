package org.multibit.mbm.util;

/**
 * Provides simple validation tools to domain objects
 */
public final class ValidationUtils {

  private ValidationUtils() {
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

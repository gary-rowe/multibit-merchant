package org.multibit.mbm.util;

/**
 * Provides standard implementations of common method overrides
 */
public final class ObjectUtils {

  static final int HASH_START_VALUE = 11;
  static final int HASH_MULTIPLIER = 29;

  private ObjectUtils() {
  }

  public static int getHashCode(Object... objects) {
    int hash = HASH_START_VALUE;
    for (Object o : objects) {
      hash = HASH_MULTIPLIER * hash + o.hashCode();
    }

    return hash;
  }

  public static boolean isEqual(Object... pairwise) {
    if (pairwise.length % 2 != 0) {
      throw new IllegalArgumentException("Arguments are not paired");
    }

    for (int i = 0; i < pairwise.length; i = i + 2) {
      if (!pairwise[i].equals(pairwise[i + 1])) {
        return false;
      }
    }
    return true;
  }
}

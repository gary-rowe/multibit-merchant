package org.multibit.mbm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides standard implementations of common method overrides
 */
public final class ObjectUtils {
  
  private static final Logger log = LoggerFactory.getLogger(ObjectUtils.class); 

  private static final int HASH_START_VALUE = 11;
  private static final int HASH_MULTIPLIER = 29;

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
      if (pairwise[i]==null && pairwise[i+1] != null) {
        return false;
      } else if (pairwise[i]==null && pairwise[i+1]==null) {
        continue;
      } else if (!pairwise[i].equals(pairwise[i + 1])) {
        return false;
      }
    }

    log.debug("Objects are equal");
    
    return true;
  }
}

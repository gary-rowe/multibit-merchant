package org.multibit.mbm.util;

import java.util.Random;

/**
 * Provides a default implementation
 */
public final class DefaultIdGenerator implements IdGenerator {

  @Override
  public Long random() {
    Random random = new Random();
    return random.nextLong();
  }
}

package org.multibit.mbm.client.infrastructure.persistence.hibernate.utils;

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

package org.multibit.mbm.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Provides a default implementation
 */
@Component
public final class DefaultIdGenerator implements IdGenerator {

  @Override
  public UUID random() {
    return UUID.randomUUID();
  }
}

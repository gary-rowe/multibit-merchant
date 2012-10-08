package org.multibit.merchant.client.store.health;

/**
 * <p>HealthCheck to provide the following to application:</p>
 * <ul>
 * <li>Provision of checks against a given Configuration property </li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */

import com.yammer.metrics.core.HealthCheck;

public class TemplatePropertyHealthCheck extends HealthCheck {
  private final String template;

  public TemplatePropertyHealthCheck(String template) {
    super("template");
    this.template = template;
  }

  @Override
  protected Result check() throws Exception {
    final String saying = String.format(template, "TEST");
    if (!saying.contains("TEST")) {
      return Result.unhealthy("template doesn't include a name");
    }
    return Result.healthy();
  }
}


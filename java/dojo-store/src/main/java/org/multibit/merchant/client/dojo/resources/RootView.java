package org.multibit.merchant.client.dojo.resources;

import com.yammer.dropwizard.views.View;

/**
 * <p>View to provide the following to application:</p>
 * <ul>
 * <li>Initial starting point for website</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class RootView extends View {

  private final String message;

  protected RootView(String message) {
    super("/assets/ftl/home.ftl");
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

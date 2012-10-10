package org.multibit.merchant.client.store.resources;

import com.yammer.dropwizard.views.View;

/**
 * <p>View to provide the following to application:</p>
 * <ul>
 * <li>Representation of the home page</li>
 * </ul>
 *
 * @since 0.0.1
 *
 * TODO Add support for backing bean with i18n etc
 *         
 */
public class PublicFreemarkerView extends View {

  protected PublicFreemarkerView(String templateName) {
    super("/views/ftl/"+templateName);
  }
}

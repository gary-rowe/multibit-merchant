package org.multibit.mbm.client.handlers;

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.joda.time.DateTime;
import org.multibit.mbm.infrastructure.utils.DateUtils;

import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Abstract base class to provide the following to representation handlers:</p>
 * <ul>
 * <li>Access to common methods</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public abstract class BaseHandler {

  protected final Locale locale;

  /**
   * @param locale The locale providing i18n information
   */
  public BaseHandler(Locale locale) {
    this.locale = locale;
  }

  /**
   * @return A {@link RepresentationFactory} configured for production use
   */
  protected static RepresentationFactory getRepresentationFactory() {
    return new DefaultRepresentationFactory();
  }

  /**
   * @param hal The HAL representation
   *
   * @return The unmarshalled domain object
   */
  protected static ReadableRepresentation unmarshalHal(String hal) {
    RepresentationFactory rf = getRepresentationFactory();
    Reader reader = new StringReader(hal);
    return rf.readRepresentation(reader);
  }

  /**
   * Attempts to retrieve a mandatory property as an Integer
   *
   * @param key        The property key
   * @param properties The properties
   *
   * @return The Integer, never null
   */
  protected static Integer getMandatoryPropertyAsInteger(String key, Map<String, Object> properties) {

    return Integer.valueOf((String) getMandatoryPropertyAsObject(key, properties));

  }

  /**
   * Attempts to retrieve a mandatory property as a String
   *
   *
   *
   * @param key        The property key
   * @param properties The properties
   *
   * @return The String, never null
   */
  protected static String getMandatoryPropertyAsString(String key, Map<String, Object> properties) {

    return (String) getMandatoryPropertyAsObject(key, properties);

  }

  /**
   * Attempts to retrieve a mandatory property as a DateTime
   *
   * @param key        The property key
   * @param properties The properties
   *
   * @return The DateTime, never null
   */
  protected static DateTime getMandatoryPropertyAsDateTime(String key, Map<String, Object> properties) {

    return DateUtils.parseISO8601((String) getMandatoryPropertyAsObject(key, properties));

  }

  /**
   * Attempts to retrieve a mandatory property
   *
   * @param key        The property key
   * @param properties The properties
   *
   * @return The Object (never null)
   *
   * @throws IllegalStateException If the mandatory property is not present
   */
  protected static Object getMandatoryPropertyAsObject(String key, Map<String, Object> properties) {

    if (!properties.containsKey(key)) {
      throw new IllegalStateException("Missing mandatory property key: " + key);
    }
    Object property = properties.get(key);
    if (property == null) {
      throw new IllegalStateException("Missing mandatory property entry: " + key);
    }

    // Must be OK to be here
    return property;

  }

  /**
   * Attempts to retrieve an optional property
   *
   * @param key        The property key
   * @param properties The properties
   *
   * @return An optional that may be absent
   */
  protected static Optional<Object> getOptionalPropertyAsObject(String key, Map<String, Object> properties) {

    if (!properties.containsKey(key)) {
      return Optional.absent();
    }
    Object optional = properties.get(key);
    if (optional == null) {
      return Optional.absent();
    }

    // Must be OK to be here
    return Optional.of(optional);

  }

}

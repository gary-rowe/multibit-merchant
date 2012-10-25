package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.logging.Log;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * <p>Specialised validation for use in Resources</p>
 * TODO Consider refactoring to follow the Guava Preconditions library
 */
public final class ResourceAsserts {

  private static final Log LOG = Log.forClass(ResourceAsserts.class);

  /**
   * Fails if the object is null
   *
   * @param obj       Object to test
   * @param fieldName The field name for logging
   */
  public static void assertNotNull(Object obj, String fieldName) {
    if (obj == null) {
      LOG.warn("Field '{}' should not be null", fieldName);
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Fails if the object is not positive (0 or greater)
   *
   * @param obj       Object to test
   * @param fieldName The field name for logging
   */
  public static void assertPositive(Number obj, String fieldName) {
    assertNotNull(obj, fieldName);
    if (obj.intValue() < 0) {
      LOG.warn("Field '{}' should be positive", fieldName);
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Fails if the object is absent
   *
   * @param obj       Object to test
   * @param fieldName The field name for logging
   */
  public static void assertPresent(Optional obj, String fieldName) {
    assertNotNull(obj, fieldName);
    if (!obj.isPresent()) {
      LOG.warn("Field '{}' should be present", fieldName);
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }

  /**
   * Fails if the object is present
   *
   * @param obj       Object to test
   * @param fieldName The field name for logging
   */
  public static void assertNotConflicted(Optional obj, String fieldName) {
    assertNotNull(obj, fieldName);
    if (obj.isPresent()) {
      LOG.warn("Field '{}' should be absent to avoid conflict", fieldName);
      throw new WebApplicationException(Response.Status.CONFLICT);
    }
  }

  /**
   * Fails if the object is absent
   *
   * @param state     Conditional that must be true
   * @param condition The condition message
   */
  public static void assertTrue(boolean state, String condition) {
    if (!state) {
      LOG.warn("Condition '{}' should be true", condition);
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }

}

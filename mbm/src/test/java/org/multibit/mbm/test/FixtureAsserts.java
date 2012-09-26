package org.multibit.mbm.test;

import com.theoryinpractise.halbuilder.spi.Resource;
import com.yammer.dropwizard.testing.FixtureHelpers;
import junit.framework.Assert;
import org.bouncycastle.util.Arrays;
import org.multibit.mbm.api.hal.HalMediaType;

import java.io.IOException;

import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static junit.framework.Assert.assertTrue;

/**
 * <p>Fixture assertions to provide the following to application:</p>
 * <ul>
 * <li>Support methods to simplify common test patterns</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class FixtureAsserts {

  /**
   * Renders a HAL resource as JSON and compares it to a normalised fixture (
   *
   * @param reason           The reason (e.g. "a Customer can be marshalled to JSON")
   * @param representation   The simple string representation
   * @param fixtureClasspath The classpath reference to the resource (e.g. "fixtures/example.json")
   * @throws IOException If something goes wrong
   */
  public static void assertStringMatchesJsonFixture(String reason, String representation, String fixtureClasspath) throws IOException {
    Assert.assertEquals(reason,
      jsonFixture(fixtureClasspath),
      representation
    );
  }

  /**
   * Renders a HAL resource as JSON and compares it to a normalised fixture (
   *
   * @param reason           The reason (e.g. "a Customer can be marshalled to JSON")
   * @param representation   The HAL representation
   * @param fixtureClasspath The classpath reference to the resource (e.g. "fixtures/example.json")
   * @throws IOException If something goes wrong
   */
  public static void assertRepresentationMatchesJsonFixture(String reason, Resource representation, String fixtureClasspath) throws IOException {
    Assert.assertEquals(reason,
      jsonFixture(fixtureClasspath),
      representation.renderContent(HalMediaType.APPLICATION_HAL_JSON)
    );
  }

  /**
   * Renders a HAL representation as JSON and compares it to the expected fixture
   *
   * @param reason           The reason (e.g. "a Customer can be marshalled to JSON")
   * @param representation   The HAL representation
   * @param fixtureClasspath The classpath reference to the resource (e.g. "fixtures/example.xml")
   * @throws IOException If something goes wrong
   */
  public static void assertRepresentationMatchesXmlFixture(String reason, Resource representation, String fixtureClasspath) throws IOException {
    Assert.assertEquals(reason,
      FixtureHelpers.fixture(fixtureClasspath),
      representation.renderContent(HalMediaType.APPLICATION_HAL_XML)
    );
  }

  /**
   * Compares the given byte[] with that read from the expected fixture
   *
   * @param reason           The reason (e.g. "a correct swatch has been generated")
   * @param representation   The byte[] to test
   * @param fixtureClasspath The classpath reference to the resource (e.g. "fixtures/example.png")
   * @throws IOException If something goes wrong
   */
  public static void assertRepresentationMatchesBinaryFixture(String reason, byte[] representation, String fixtureClasspath) throws IOException {
    assertTrue(reason, Arrays.areEqual(representation, BinaryFixtureHelpers.fixture(fixtureClasspath)));
  }

}
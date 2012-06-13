package org.multibit.mbm.test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.multibit.mbm.api.hal.HalMediaType;

import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * <p>Test utilities to provide the following to application:</p>
 * <ul>
 * <li>Support methods to simplify common test patterns</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class TestUtils {

  /**
   * Reads an input stream entirely into a String then closes the input stream
   *
   * @param inputStream The input stream
   *
   * @return The String containing the entire contents
   *
   * @throws IOException If something goes wrong
   */
  public static String readFullyAndClose(final InputStream inputStream) throws IOException {

    String result = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
    inputStream.close();
    return result;

  }

  /**
   * @return A StreamResult that returns the result as a String
   */
  public static StreamResult newStringResult() {

    StreamResult sr = new StreamResult() {
      public String toString() {
        return getWriter().toString();
      }
    };

    sr.setWriter(new StringWriter());

    return sr;
  }

  /**
   *
   *
   * @param reason The reason (e.g. "a Customer can be marshalled to JSON")
   * @param resource The HAL resource
   * @throws IOException If something goes wrong
   */
  public static void assertResourceMatchesJsonFixture(String reason, Resource resource) throws IOException {
    assertThat(reason,
      resource.renderContent(HalMediaType.APPLICATION_HAL_JSON),
      is(equalTo(jsonFixture("fixtures/hal/expected-customer-simple.json"))));
  }
}
package org.multibit.mbm.test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *        TODO Add documentation and consider introducing mbm-core library
 */
public class TestUtils {

  /**
   * Reads an input stream entirely into a string
   *
   * @param inputStream The input stream
   *
   * @return The String containing the entire contents
   *
   * @throws IOException If something goes wrong
   */
  public static String readStringFromStream(final InputStream inputStream) throws IOException {

    return CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));

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
}
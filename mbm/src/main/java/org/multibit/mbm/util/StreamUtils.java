package org.multibit.mbm.util;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.*;
import java.nio.charset.Charset;

/**
 * <p>
 * Utility to provide the following to application:</p>
 * <ul>
 * <li>Methods to operate on streams</li>
 * </ul>
 */

public class StreamUtils {

  /**
   * Fully copy an input stream to a String
   *
   * @param is The {@link InputStream}
   * @return A String encoded in UTF8
   * @throws IOException
   */
  public static String toString(final InputStream is) throws IOException {
    return toString(is, Charsets.UTF_8);
  }

  /**
   * Full copy an input stream to a String
   *
   * @param is The {@link InputStream}
   * @param cs The {@link Charset}
   * @return A String encoded in the charset
   * @throws IOException
   */
  public static String toString(final InputStream is, final Charset cs)
    throws IOException {
    Closeable closeMe = is;
    try {
      final InputStreamReader isr = new InputStreamReader(is, cs);
      closeMe = isr;
      return CharStreams.toString(isr);
    } finally {
      Closeables.closeQuietly(closeMe);
    }
  }
}

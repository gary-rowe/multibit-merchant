package org.multibit.mbm.web.view;

import com.google.common.io.Closeables;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 *  <p>Spring View to provide the following to Controllers:<br>
 *  <ul>
 *  <li>Provision of specific view properties</li>
 *  </ul>
 *  </p>
 *  
 */
public class PNGImageView extends AbstractView {
  private static final String CONTENT_TYPE = "Content-Type";
  private final BufferedImage image;

  public PNGImageView(final BufferedImage image) {
    this.image = image;
  }

  @Override
  protected void renderMergedOutputModel(final Map<String, Object> model,
                                         final HttpServletRequest request,
                                         final HttpServletResponse response) {
    response.setHeader(CONTENT_TYPE, MediaType.IMAGE_PNG.toString());
    PrintWriter writer = null;
    try {
      OutputStream os = response.getOutputStream();
      ImageIO.write(image, "png", os);
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    } finally {
      Closeables.closeQuietly(writer);
    }
  }

}

package org.multibit.mbm.api.hal;

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
 *         
 */

import com.google.common.base.Optional;
import com.theoryinpractise.halbuilder.spi.Link;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import com.theoryinpractise.halbuilder.spi.Renderer;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static com.theoryinpractise.halbuilder.impl.api.Support.*;

/**
 * <p>XML renderer to provide the following to resources:</p>
 * <ul>
 * <li>Minified XML representation</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class MinifiedXmlRenderer implements Renderer<String> {

  public Optional<String> render(ReadableResource resource, Writer writer) {
    final Element element = renderElement(resource, false);
    final XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
    try {
      outputter.output(element, writer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return Optional.absent();
  }

  private Element renderElement(ReadableResource resource, boolean embedded) {

    final Link resourceLink = resource.getResourceLink();
    final String href = resourceLink.getHref();

    // Create the root element
    final Element resourceElement = new Element("resource");
    resourceElement.setAttribute("href", href);
    if (!resourceLink.getRel().equals("self")) {
      resourceElement.setAttribute("rel", resourceLink.getRel());
    }

    // Only add namespaces to non-embedded resources
    if (!embedded) {
      for (Map.Entry<String, String> entry : resource.getNamespaces().entrySet()) {
        resourceElement.addNamespaceDeclaration(
          Namespace.getNamespace(entry.getKey(), entry.getValue()));
      }
    }

    //add a comment
//        resourceElement.addContent(new Comment("Description of a resource"));

    // add links
    List<Link> links = resource.getLinks();
    for (Link link : links) {
      Element linkElement = new Element(LINK);
      if (!link.getRel().contains(SELF)) {
        linkElement.setAttribute(REL, link.getRel());
        linkElement.setAttribute(HREF, link.getHref());
        if (link.getName().isPresent()) {
          linkElement.setAttribute(NAME, link.getName().get());
        }
        if (link.getTitle().isPresent()) {
          linkElement.setAttribute(TITLE, link.getTitle().get());
        }
        if (link.getHreflang().isPresent()) {
          linkElement.setAttribute(HREFLANG, link.getHreflang().get());
        }
        resourceElement.addContent(linkElement);
      }
    }

    // add properties
    for (Map.Entry<String, Object> entry : resource.getProperties().entrySet()) {
      Element propertyElement = new Element(entry.getKey());
      propertyElement.setContent(new Text(entry.getValue().toString()));
      resourceElement.addContent(propertyElement);
    }

    // add subresources
    for (Resource halResource : resource.getResources()) {
      Element subResourceElement = renderElement(halResource, true);
      resourceElement.addContent(subResourceElement);
    }

    return resourceElement;
  }

}


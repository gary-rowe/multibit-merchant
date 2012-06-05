package org.multibit.mbm.api.hal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import java.net.URI;
import java.util.List;
import java.util.TreeMap;

public class HalResponseBuilder {

  private TreeMap<String, String> namespaces = Maps.newTreeMap(Ordering.usingToString());
  private List<Link> links = Lists.newArrayList();
  private String baseHref;

  private boolean isBuilt = false;

  private HalResponseBuilder() {
  }

  ;

  /**
   * @return A new instance of the builder
   */
  public static HalResponseBuilder getInstance() {
    return new HalResponseBuilder();
  }

  private void validateState() {
    if (isBuilt) {
      throw new IllegalStateException("The entity has been built");
    }
  }

  public HalResponse build() {
    validateState();

    HalResponse halResponse = new HalResponse();
    halResponse.setBaseHref(baseHref);
    halResponse.setLinks(links);

    isBuilt = true;

    return halResponse;
  }

  public HalResponseBuilder(URI baseUri) {
    this(baseUri.toASCIIString());
  }

  public HalResponseBuilder(String baseHref) {
    this.baseHref = baseHref;
  }

  public String getBaseHref() {
    return baseHref;
  }

  public HalResponseBuilder withBaseHref(URI baseUri) {
    this.baseHref = baseUri.toASCIIString();
    return this;
  }

  public HalResponseBuilder withBaseHref(String baseHref) {
    this.baseHref = baseHref;
    return this;
  }

  /**
   * @param namespace
   * @param url
   *
   * @return
   */
  public HalResponseBuilder withNamespace(String namespace, String url) {
    if (namespaces.containsKey(namespace)) {
      throw new ResponseBuilderException(String.format("Duplicate namespace '%s' provided for response", namespace));
    }
    namespaces.put(namespace, url);
    return this;
  }

  /**
   * The minimum specification for a {@link Link}
   *
   * @param rel  The link relation
   * @param href The link URL
   *
   * @return The builder
   */
  public HalResponseBuilder withLink(String rel, String href) {
    links.add(new Link(rel, href));
    return this;
  }

  /**
   * The full specification for a {@link Link}
   *
   * @param rel      The link relation
   * @param href     The link URL (a target IRI compliant with <a href="http://tools.ietf.org/html/rfc5988">RFC 5988</a>)
   * @param name     The name of the link (if it has the same rel value)
   * @param title    The title of the link (a human readable identifier)
   * @param hreflang The language of the expected resource
   *
   * @return The builder
   */
  public HalResponseBuilder withLink(String rel, String href, String name, String title, String hreflang) {
    links.add(new Link(rel, href, name, title, hreflang));
    return this;
  }

}

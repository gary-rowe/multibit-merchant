package org.multibit.mbm.api.hal;

import com.google.common.collect.Lists;

import java.net.URI;
import java.util.List;

public class HalResponseBuilder {

  private List<Link> links = Lists.newArrayList();
  private List<HalResponse> halResponses = Lists.newArrayList();
  private List<Object> entities = Lists.newArrayList();
  private String baseHref;

  private boolean isBuilt = false;

  /**
   * Force use of static instance method
   */
  private HalResponseBuilder() {
  }

  /**
   * @return A new instance of the builder
   */
  public static HalResponseBuilder newInstance() {
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
    halResponse.setHalResponses(halResponses);
    halResponse.setEntities(entities);

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
   * Add a minimally specified link (can be called multiple times)
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
   * Add a fully specified link (can be called multiple times)
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

  /**
   * Add a nested resource (can be called multiple times)
   *
   * @param halResponse The HalResponse to add as a nested resource
   *
   * @return The builder
   */
  public HalResponseBuilder withHalResponse(HalResponse halResponse) {
    halResponses.add(halResponse);
    return this;
  }

  /**
   * Add a nested entity (can be called multiple times)
   *
   * @param entity The arbitrary entity (must be annotated for JAXB)
   *
   * @return The builder
   */
  public HalResponseBuilder withEntity(Object entity) {
    entities.add(entity);
    return this;
  }
}

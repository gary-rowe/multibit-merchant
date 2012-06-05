package org.multibit.mbm.api.hal;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.regex.Pattern;

/**
 * A Link to an external resource.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {
  /**
   * Pattern that will hit an RFC 6570 URI template.
   */
  private static final Pattern URI_TEMPLATE_PATTERN = Pattern.compile("\\{.+\\}");

  @XmlElement(name = "rel", defaultValue = "")
  @JsonProperty
  private String rel;

  @XmlElement(name = "href", defaultValue = "")
  @JsonProperty
  private String href;

  @XmlElement(name = "name", defaultValue = "")
  @JsonProperty
  private String name;

  @XmlElement(name = "title", defaultValue = "")
  @JsonProperty
  private String title;

  @XmlElement(name = "hreflang", defaultValue = "")
  @JsonProperty
  private String hreflang;

  @XmlTransient
  @JsonIgnore
  private boolean hasTemplate = false;

  /**
   * Default constructor for JAXB
   */
  public Link() {
  }

  /**
   * Minimal compliant constructor
   *
   * @param rel  The relation (e.g. "self", "customer")
   * @param href The href (absolute, relative or template)
   */
  public Link(String rel, String href) {
    this.rel = rel;
    this.href = href;
  }

  public Link(String rel, String href, String name, String title, String hreflang) {
    this(rel, href);
    this.name = name;
    this.title = title;
    this.hreflang = hreflang;
  }

  public String getHref() {
    return href;
  }

  public String getRel() {
    return rel;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getHreflang() {
    return hreflang;
  }

  public void setHreflang(String hreflang) {
    this.hreflang = hreflang;
  }

  public boolean isHasTemplate() {
    return hasTemplate;
  }

  public void setHasTemplate(boolean hasTemplate) {
    this.hasTemplate = hasTemplate;
  }

  public boolean hasTemplate() {
    return hasTemplate;
  }

  /**
   * Determine whether the argument href contains at least one URI template,
   * as defined in RFC 6570.
   *
   * @param href Href to check.
   *
   * @return True if the href contains a template, false if not (or if the
   *         argument is null).
   */
  private boolean hasTemplate(String href) {
    return href != null && URI_TEMPLATE_PATTERN.matcher(href).find();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(href, rel, name, title, hreflang);
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Link)) {
      return false;
    }
    Link that = (Link) obj;
    boolean e = this.href.equals(that.href);
    e &= this.rel.equals(that.rel);
    e &= this.name.equals(that.name);
    e &= this.title.equals(that.title);
    e &= this.hreflang.equals(that.hreflang);
    return e;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("rel", rel)
      .add("name", name)
      .add("title", title)
      .add("hreflang", hreflang)
      .toString();
  }
}

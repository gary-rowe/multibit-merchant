package org.multibit.mbm.api.hal;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.w3c.dom.Node;

import javax.xml.bind.annotation.*;
import java.util.List;

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
 *        TODO Fill in documentation         
 */
@XmlRootElement(name = "resource")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Link.class)
public class HalResponse {

  @XmlTransient
  @JsonIgnore
  private String baseHref;

  @XmlElement(name = "link")
  @JsonProperty
  private List<Link> links;

  private HalResponse response = null;

  @XmlAnyElement
  private List<Node> entity = null;

  public void setBaseHref(String baseHref) {
    this.baseHref = baseHref;
  }

  public String getBaseHref() {
    return baseHref;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public List<Link> getLinks() {
    return links;
  }

  public HalResponse getResponse() {
    return response;
  }

  public void setResponse(HalResponse response) {
    this.response = response;
  }

  public List<Node> getEntity() {
    return entity;
  }

  public void setEntity(List<Node> entity) {
    this.entity = entity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HalResponse that = (HalResponse) o;

    if (baseHref != null ? !baseHref.equals(that.baseHref) : that.baseHref != null) return false;
    if (entity != null ? !entity.equals(that.entity) : that.entity != null) return false;
    if (links != null ? !links.equals(that.links) : that.links != null) return false;
    if (response != null ? !response.equals(that.response) : that.response != null) return false;

    return true;
  }

  @Override
  public int hashCode() {

    int result = baseHref != null ? baseHref.hashCode() : 0;
    result = 31 * result + (links != null ? links.hashCode() : 0);
    result = 31 * result + (response != null ? response.hashCode() : 0);
    result = 31 * result + (entity != null ? entity.hashCode() : 0);
    return result;
  }
}

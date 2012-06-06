package org.multibit.mbm.api.hal;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * <p>HAL response to provide the following to application:</p>
 * <ul>
 * <li>Decorator for other response objects to provide linking information</li>
 * </ul>
 *
 * @since 0.0.1
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

  @XmlElement(name = "resource")
  @JsonProperty
  private List<HalResponse> halResponses = Lists.newArrayList();

  @XmlAnyElement
  @XmlMixed
  private List<Object> entities = null;

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

  public List<HalResponse> getHalResponses() {
    return halResponses;
  }

  public void setHalResponses(List<HalResponse> halResponses) {
    this.halResponses = halResponses;
  }

  public List<Object> getEntities() {
    return entities;
  }

  public void setEntities(List<Object> entities) {
    this.entities = entities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HalResponse that = (HalResponse) o;

    if (baseHref != null ? !baseHref.equals(that.baseHref) : that.baseHref != null) return false;
    if (entities != null ? !entities.equals(that.entities) : that.entities != null) return false;
    if (links != null ? !links.equals(that.links) : that.links != null) return false;
    if (halResponses != null ? !halResponses.equals(that.halResponses) : that.halResponses != null) return false;

    return true;
  }

  @Override
  public int hashCode() {

    int result = baseHref != null ? baseHref.hashCode() : 0;
    result = 31 * result + (links != null ? links.hashCode() : 0);
    result = 31 * result + (halResponses != null ? halResponses.hashCode() : 0);
    result = 31 * result + (entities != null ? entities.hashCode() : 0);
    return result;
  }
}

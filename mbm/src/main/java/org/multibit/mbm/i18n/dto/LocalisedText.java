package org.multibit.mbm.i18n.dto;

import org.multibit.mbm.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  <p>Localised text field to provide the following to application:</p>
 *  <ul>
 *  <li>Provision of language co-ordinates and text content for a text field</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
@Embeddable
public class LocalisedText implements Serializable {

  @Column(name = "locale_key", nullable = false)
  private String localeKey="en";

  @Column(name = "content", nullable = false)
  private String content="Empty";

  /*
   * Default constructor required for Hibernate
   */
  public LocalisedText() {
  }

  /**
   * @return The text content in the given language
   */
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return The locale co-ordinates (language_country_variant) encoded into a simple key (e.g. th_TH_TH)
   * @see <a href="http://docs.oracle.com/javase/tutorial/i18n/locale/create.html">The Java SE i18n tutorial</a>
   */
  public String getLocaleKey() {
    return localeKey;
  }

  public void setLocaleKey(String localeKey) {
    this.localeKey = localeKey;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final LocalisedText other = (LocalisedText) obj;

    return ObjectUtils.isEqual(
      content, other.content,
      localeKey, other.localeKey
    );
  }

  @Override
  public int hashCode() {
    return ObjectUtils.getHashCode(content, localeKey);
  }

  @Override
  public String toString() {
    return String.format("LocalisedText[content='%s', localeKey='%s']]", content, localeKey);
  }
}

package org.multibit.mbm.web.atmosphere;

import org.joda.time.DateTime;

/**
 * <p>Value object to provide the following to Alert handlers:<br>
 * <ul>
 * <li>Storage of state</li>
 * </ul>
 * </p>
 */
public class AlertMessage {

  private String topic = "AlertMessage";

  private Long id;
  private DateTime createdAt;
  private String text;
  private String imageUrl;

  /**
   * Default constructor.
   */
  public AlertMessage() {
    super();
  }

  /**
   * Constructor to initialize all fields available.
   * @param id The Alert Id
   * @param createdAt The instant this Alert was created
   * @param text The text to show
   * @param imageUrl An image URL if appropriate
   */
  public AlertMessage(Long id, DateTime createdAt, String text, String imageUrl) {
    super();
    this.id = id;
    this.createdAt = createdAt;
    this.text = text;
    this.imageUrl = imageUrl;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTopic() {
    return topic;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((createdAt == null) ? 0 : createdAt.hashCode());
    result = prime * result
      + ((imageUrl == null) ? 0 : imageUrl.hashCode());
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AlertMessage other = (AlertMessage) obj;
    if (createdAt == null) {
      if (other.createdAt != null)
        return false;
    } else if (!createdAt.equals(other.createdAt))
      return false;
    if (imageUrl == null) {
      if (other.imageUrl != null)
        return false;
    } else if (!imageUrl.equals(other.imageUrl))
      return false;
    if (text == null) {
      if (other.text != null)
        return false;
    } else if (!text.equals(other.text))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "AlertMessage [createdAt=" + createdAt + ", text=" + text
      + ", imageUrl="
      + imageUrl + "]";
  }

}

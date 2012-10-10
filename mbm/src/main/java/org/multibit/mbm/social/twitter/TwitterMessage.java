package org.multibit.mbm.social.twitter;

import org.joda.time.DateTime;

/**
 * <p>Value object to provide the following to Twitter handlers:<br>
 * <ul>
 * <li>Storage of state</li>
 * </ul>
 * </p>
 */
public class TwitterMessage {

  private String topic = "TwitterMessage";

  private Long id;
  private DateTime createdAt;
  private String text;
  private String fromUser;
  private String profileImageUrl;

  /**
   * Default constructor.
   */
  public TwitterMessage() {
  }

  /**
   * Constructor to initialize all fields available.
   */
  public TwitterMessage(Long id, DateTime createdAt, String text, String fromUser,
                        String profileImageUrl) {
    super();
    this.id = id;
    this.createdAt = createdAt;
    this.text = text;
    this.fromUser = fromUser;
    this.profileImageUrl = profileImageUrl;
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

  public String getFromUser() {
    return fromUser;
  }

  public void setFromUser(String fromUser) {
    this.fromUser = fromUser;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
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
      + ((fromUser == null) ? 0 : fromUser.hashCode());
    result = prime * result
      + ((profileImageUrl == null) ? 0 : profileImageUrl.hashCode());
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
    TwitterMessage other = (TwitterMessage) obj;
    if (createdAt == null) {
      if (other.createdAt != null)
        return false;
    } else if (!createdAt.equals(other.createdAt))
      return false;
    if (fromUser == null) {
      if (other.fromUser != null)
        return false;
    } else if (!fromUser.equals(other.fromUser))
      return false;
    if (profileImageUrl == null) {
      if (other.profileImageUrl != null)
        return false;
    } else if (!profileImageUrl.equals(other.profileImageUrl))
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
    return "Tweet [createdAt=" + createdAt + ", text=" + text
      + ", fromUser=" + fromUser + ", profileImageUrl="
      + profileImageUrl + "]";
  }

}

package org.multibit.mbm.core;

import org.codehaus.jackson.annotate.JsonProperty;

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
public class Saying {

  @JsonProperty
  private long id;

  @JsonProperty
  private String content;

  public Saying() {
  }

  public Saying(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }
}
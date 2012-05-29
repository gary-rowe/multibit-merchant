package org.multibit.mbm.api.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>Base response to provide the following to Resources:</p>
 *<ul>
 *<li>Response entity common to all </li>
 *</ul>
 *
 * @since 0.0.1
 *         
 */
public class BaseResponse {
  @JsonProperty
  private String sessionId;

  @JsonProperty
  private int errorCode;

  @JsonProperty
  private String errorDescription;

  /**
   * @return
   */
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }
}

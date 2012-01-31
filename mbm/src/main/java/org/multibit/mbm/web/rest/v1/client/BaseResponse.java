package org.multibit.mbm.web.rest.v1.client;

/**
 *  <p>Base response to provide the following to controllers:</p>
 *  <ul>
 *  <li>Response entity common to all </li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class BaseResponse {
  public String sessionId;
  public int errorCode;
  public String errorDescription;

  /**
   *
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

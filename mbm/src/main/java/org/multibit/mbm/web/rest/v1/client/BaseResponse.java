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

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}

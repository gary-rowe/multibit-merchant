package org.multibit.mbm.web.rest.v1.client;

/**
 * <p>Base class to provide the following to client side objects:</p>
 * <ul>
 * <li>Common state shared across all requests and responses</li>
 * </ul>
 * <p>Client side state is generally transferred using Summary objects which are snapshots of the underlying
 * persistent entities that they derive from. </p>
 *
 * @since 1.0.0
 *         
 */
public abstract class BaseRequest {
  /**
   * The sessionId provides an anonymous session for RESTful API calls requiring state without authentication
   */
  private String sessionId=null;

  // TODO Consider adding support for application secrets

  // TODO Consider error reporting through JSON

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}

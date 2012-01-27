package org.multibit.mbm.web.rest.v1.controller;

import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.security.service.SecurityService;
import org.multibit.mbm.util.CookieUtils;
import org.multibit.mbm.web.rest.v1.UUIDNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 *  <p>Base class to provide the following to Controllers:</p>
 *  <ul>
 *  <li>Provision of standard exception handling</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
@Controller
public abstract class BaseController {

  /**
   * Provides logging for this class
   */
  private static final Logger log = LoggerFactory.getLogger(BaseController.class);

  @Resource(name = "securityService")
  private SecurityService securityService = null;

  /**
   * <p>Provides a general purpose exception handler</p>
   *
   * @param exception The exception
   *
   * @return A token and a cookie
   */
  @ExceptionHandler(UUIDNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleUUIDNotFoundException(UUIDNotFoundException exception) {
    return "The API token was not found in the request. Use /api/v1/token to obtain one.";
  }

  /**
   * <p>Provides a general purpose exception handler</p>
   *
   * @param exception The exception
   *
   * @return A token and a cookie
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleRuntimeException(RuntimeException exception) {
    return "Plik! The server has encountered an internal error.";
  }

  /**
   * Adds a Location header to the response (used in successful POST requests)
   *
   * @param request         The original request
   * @param response        The original response
   * @param subResourcePath The path to the sub-resource (e.g. "/cart/1" to get "http://localhost:8080/mbm/api/v1/cart/1")
   */
  protected void addLocationHeader(String subResourcePath, HttpServletRequest request, HttpServletResponse response) {
    String baseURI = buildBaseURI(request);
    response.setHeader("Location", baseURI + subResourcePath);
  }

  /**
   * @param request The original request
   *
   * @return The base URI to the resource (e.g. "http://localhost:8080/mbm/api/v1")
   */
  protected String buildBaseURI(HttpServletRequest request) {
    String baseURI;
    if (request.getRemotePort() != 80) {
      // Include the port number
      baseURI = request.getScheme()
        + "://"
        + request.getRemoteHost()
        + ":" + request.getServerPort()
        + request.getContextPath()
        + "/api/v1";
    } else {
      // Omit the port number (80)
      baseURI = request.getScheme()
        + "://"
        + request.getRemoteHost()
        + request.getContextPath()
        + "/api/v1";
    }
    return baseURI;
  }

  /**
   * Attempts to get the User based on the information provided in the request
   *
   * @param principal The user principal (anonymous or authenticated)
   * @param request   The original request
   * @param sessionId The sessionId from the BaseRequest
   *
   * @return The associated User (never null)
   */
  protected User getUserFromRequest(Principal principal, HttpServletRequest request, String sessionId) {
    // Ensure that a limit on the creation of Customers from a given IP address is enforced
    User user;
    if (principal == null || principal instanceof AnonymousAuthenticationToken) {
      log.debug("Looking for UUID in cookie");
      // Require a UUID to proceed
      if (sessionId == null) {
        // Attempt to locate it on a Cookie
        sessionId = CookieUtils.getCookieValue(request, TokenController.TOKEN_COOKIE_NAME);
        if (sessionId == null) {
          // No token anywhere
          throw new UUIDNotFoundException();
        }
      }
      log.debug("Persisting anonymous User with UUID '{}'", sessionId);
      user = securityService.persistAnonymousUser(sessionId);
    } else {
      log.debug("Persisting authenticated User");
      user = securityService.getUserByPrincipal(principal);
    }
    return user;
  }
}

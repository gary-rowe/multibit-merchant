package org.multibit.mbm.web.rest.v1.controller;

import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.security.service.SecurityService;
import org.multibit.mbm.util.CookieUtils;
import org.multibit.mbm.web.rest.v1.UUIDNotFoundException;
import org.multibit.mbm.web.rest.v1.client.BaseResponse;
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
  public BaseResponse handleUUIDNotFoundException(UUIDNotFoundException exception) {
    log.debug("HTTP {} returned", HttpStatus.BAD_REQUEST.name(), exception);
    BaseResponse baseResponse = new BaseResponse();
    baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
    baseResponse.setErrorDescription("The sessionId was not found in a request that requires it. See API documentation for details.");
    return baseResponse;
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
  public BaseResponse handleRuntimeException(RuntimeException exception) {
    log.error("HTTP {} returned", HttpStatus.INTERNAL_SERVER_ERROR.name(), exception);
    BaseResponse baseResponse = new BaseResponse();
    baseResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    baseResponse.setErrorDescription("An internal error has occurred. We apologise for the inconvenience and will take care of it.");
    return baseResponse;
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

    // TODO Restrict the creation of new Users to a specific registration process

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
      user=null;
    } else {
      log.debug("Retrieving authenticated User");
      user = securityService.getUserByPrincipal(principal);
    }
    return user;
  }
}

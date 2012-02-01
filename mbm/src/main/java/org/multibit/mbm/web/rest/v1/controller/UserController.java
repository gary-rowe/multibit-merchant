package org.multibit.mbm.web.rest.v1.controller;

import org.multibit.mbm.security.dto.User;
import org.multibit.mbm.security.service.SecurityService;
import org.multibit.mbm.web.rest.v1.client.security.CreateUserRequest;
import org.multibit.mbm.web.rest.v1.client.security.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Handles the RESTful APIs for the Customer package
 */
@Controller
@RequestMapping(value = "/v1")
public class UserController extends BaseController {

  @Resource(name="securityService")
  private SecurityService securityService=null;

  /**
   * Creates a new User
   * @param createUserRequest The initial User contents
   * @param principal The security principal (injected)
   * @param request The originating request (injected)
   * @param response The response (injected)
   * @return The response
   */
  @RequestMapping(
    headers = {"content-type=application/json"},
    method = RequestMethod.POST,
    value = "/user"
  )
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse createUser(
    @RequestBody CreateUserRequest createUserRequest,
    Principal principal,
    HttpServletRequest request,
    HttpServletResponse response ) {

    // Check who is making this request (could be an admin or an anonymous customer)    
    String sessionId = createUserRequest.getSessionId();
    User user = getUserFromRequest(principal, request, sessionId);

    if (user==null) {
      // Fresh user from an anonymous user - treat as a Customer
      user = securityService.createUserAsCustomer(createUserRequest);

      // Ensure the new User can find themselves
      addLocationHeader("/user", request, response);
    } else {
      // An authenticated User is creating this User
      // TODO Add support for admins creaing users
      // Ensure the new User can be found directly
      addLocationHeader("/user/"+user.getId(),request,response);
    }

    // Provide the User summary for lazy clients
    return new UserResponse(user);

  }


}


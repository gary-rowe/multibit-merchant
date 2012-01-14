package org.multibit.mbm.web.rest.v1;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
   * <p>Provides a general purpose exception handler</p>
   * @param exception The exception
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
   * @param exception The exception
   * @return A token and a cookie
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleRuntimeException(RuntimeException exception) {
    return "Plik! The server has encountered an internal error.";
  }
}

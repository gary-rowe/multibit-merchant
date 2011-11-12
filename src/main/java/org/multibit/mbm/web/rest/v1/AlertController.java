package org.multibit.mbm.web.rest.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/v1")
public class AlertController {

  /**
   * Echo service
   * @param message The message to echo
   * @return A String containing the message
   */
  @RequestMapping(value="/alert/echo/{message}",method = RequestMethod.GET)
  @ResponseBody
  public String alert(@PathVariable("message") String message) {
    return message;
  }

}

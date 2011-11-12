package org.multibit.mbm.web.rest.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AlertController {

  @RequestMapping(value="/alert",method = RequestMethod.GET)
  @ResponseBody
  public String alert() {
    return "Alert message";
  }

}

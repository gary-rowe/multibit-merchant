package org.multibit.mbm.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Controller for RESTful API demonstrator</p>
 *
 * @since 1.0.0
 */
@Controller
public class ApiDemoController extends BaseMVCController {

  @RequestMapping("/api-demo.html")
  public ModelAndView apiDemo() {
    return new ModelAndView("public/api-demo");
  }

}

package org.multibit.mbm.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CatalogController {

  @RequestMapping("/authenticate.html")
  public ModelAndView authenticate() {
    return new ModelAndView("public/authenticate");
  }

  @RequestMapping("/index.html")
  public ModelAndView index() {

    // TODO Add a model providing products
    return new ModelAndView("public/index");
  }
}

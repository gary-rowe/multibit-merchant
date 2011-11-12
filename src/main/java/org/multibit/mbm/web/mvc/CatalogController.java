package org.multibit.mbm.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Controller for public catalog </p>
 *
 * @since 1.0.0
 */
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

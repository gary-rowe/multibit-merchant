package org.multibit.mbm.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Controller for public site pages</p>
 *
 * @since 1.0.0
 */
@Controller
public class SiteController {

  @RequestMapping("/authenticate.html")
  public ModelAndView authenticate() {
    return new ModelAndView("public/authenticate");
  }

  @RequestMapping("/index.html")
  public ModelAndView index() {

    // TODO Add a model providing products
    return new ModelAndView("public/index");
  }

  @RequestMapping("/contact.html")
  public ModelAndView contact() {

    // TODO Add a model providing products
    return new ModelAndView("public/contact");
  }

  @RequestMapping("/delivery.html")
  public ModelAndView delivery() {

    // TODO Add a model providing products
    return new ModelAndView("public/delivery");
  }

  @RequestMapping("/feedback.html")
  public ModelAndView feedback() {

    // TODO Add a model providing products
    return new ModelAndView("public/feedback");
  }

  @RequestMapping("/help.html")
  public ModelAndView help() {

    // TODO Add a model providing products
    return new ModelAndView("public/help");
  }

}

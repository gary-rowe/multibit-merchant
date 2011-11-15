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

  /*
        HttpServletRequest req = r.getRequest();
        HttpServletResponse res = r.getResponse();
        String method = req.getMethod();

        // Suspend the response.
        if ("GET".equalsIgnoreCase(method)) {
            // Log all events on the console, including WebSocket events.
            r.addEventListener(new WebSocketEventListenerAdapter());

            res.setContentType("text/html;charset=ISO-8859-1");

            Broadcaster b = lookupBroadcaster(req.getPathInfo());
            r.setBroadcaster(b);

            if (req.getHeader(HeaderConfig.X_ATMOSPHERE_TRANSPORT).equalsIgnoreCase(HeaderConfig.LONG_POLLING_TRANSPORT)) {
                req.setAttribute(ApplicationConfig.RESUME_ON_BROADCAST, Boolean.TRUE);
                r.suspend(-1, false);
            } else {
                r.suspend(-1);
            }
        } else if ("POST".equalsIgnoreCase(method)) {
            Broadcaster b = lookupBroadcaster(req.getPathInfo());

            String message = req.getReader().readLine();

            if (message != null && message.indexOf("message") != -1) {
                b.broadcast(message.substring("message=".length()));
            }
        }

   */



}

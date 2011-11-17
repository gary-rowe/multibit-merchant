package org.multibit.mbm.web.atmosphere;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.multibit.mbm.domain.AlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * <p>Reverse Ajax Controller providing the following for clients</p>
 * <ul>
 * <li>Subscription to an alert mechanism tailored to the individual client</li>
 * </ul>
 * <p>Clients will receive an individual alert message of some kind through
 * a broadcast event.</p>
 */
@Controller
@RequestMapping(value = "/v1")
public class AlertMessageController {

  private static final Logger log = LoggerFactory.getLogger(AlertMessageController.class);

  /**
   * Send out a regular alert
   * @param event The Atmosphere event
   */
  @RequestMapping(value = "/pubsub/alert", method = RequestMethod.GET)
  @ResponseBody
  public void alert(final AtmosphereResource<HttpServletRequest, HttpServletResponse> event) {

    final ObjectMapper mapper = new ObjectMapper();

    event.suspend();

    final Broadcaster bc = event.getBroadcaster();

    // Set up a scheduled broadcast for this client
    bc.scheduleFixedBroadcast(new Callable<String>() {

      @Override
      public String call() throws Exception {

        AlertMessage alertMessage = new AlertMessage(1L,new DateTime(), "Your order for \"Cryptonomicon by Neal Stephenson\" has been confirmed",null);

        // Create a JSON output
        String result = mapper.writeValueAsString(alertMessage);
        log.debug("JSON: {}", mapper.writeValueAsString(alertMessage));
        return result;
      }

    }, 20, TimeUnit.SECONDS);

  }

}


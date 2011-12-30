package org.multibit.mbm.web.rest.v1;

import org.multibit.mbm.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1/time")
public class TimeController {

  /**
   * Provides the current server time in UTC
   *
   * @return A default ISO 8601 time string
   */
  @RequestMapping(value = "/now", method = RequestMethod.GET)
  @ResponseBody
  public String time() {
    return DateUtils.nowUtc().toString();
  }

  /**
   * Provides the current server time in UTC
   *
   * @return A {@link DateTimeSummary} containing the date split into fields
   */
  @RequestMapping(value = "/time/extended", method = RequestMethod.GET)
  @ResponseBody
  public DateTimeSummary extendedTime() {
    return new DateTimeSummary(DateUtils.nowUtc());
  }

}


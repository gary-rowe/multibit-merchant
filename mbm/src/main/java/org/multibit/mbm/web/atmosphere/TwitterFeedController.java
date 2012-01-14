package org.multibit.mbm.web.atmosphere;

import com.google.common.collect.Lists;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.multibit.mbm.social.twitter.TwitterMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/v1")
public class TwitterFeedController {

  private static final Logger log = LoggerFactory.getLogger(TwitterFeedController.class);

  /**
   * @param event The Atmosphere event
   */
  @RequestMapping(value = "/subscribe/twitter", method = RequestMethod.GET)
  @ResponseBody
  public void twitter(final AtmosphereResource<HttpServletRequest, HttpServletResponse> event) {

    final ObjectMapper mapper = new ObjectMapper();

    event.suspend();

    final Broadcaster bc = event.getBroadcaster();

    // Set up a scheduled broadcast for this client
    bc.scheduleFixedBroadcast(new Callable<String>() {

      private long sinceId = 0;

      @Override
      public String call() throws Exception {

        final TwitterTemplate twitterTemplate = new TwitterTemplate();
        final SearchResults results = twitterTemplate.searchOperations().search("world", 1, 5, sinceId, 0);

        log.info("sinceId: " + sinceId + "; maxId: " + results.getMaxId());

        sinceId = results.getMaxId();

        List<TwitterMessage> twitterMessages = Lists.newArrayList();

        for (Tweet tweet : results.getTweets()) {
          twitterMessages.add(new TwitterMessage(tweet.getId(),
            new DateTime(tweet.getCreatedAt()),
            tweet.getText(),
            tweet.getFromUser(),
            tweet.getProfileImageUrl()));
        }

        // Create a JSON output
        String result = mapper.writeValueAsString(twitterMessages);
        log.debug("JSON: {}",mapper.writeValueAsString(twitterMessages));
        return result;
      }

    }, 10, TimeUnit.SECONDS);


  }

  /**
   */
  @RequestMapping(value = "/broadcast/twitter", method = RequestMethod.GET)
  @ResponseBody
  public void twitter(final Tweet tweet) {


  }


}


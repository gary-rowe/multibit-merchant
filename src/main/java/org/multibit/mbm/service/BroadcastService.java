package org.multibit.mbm.service;

import com.google.common.collect.Maps;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.multibit.mbm.web.atmosphere.AlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 *  <p>Service to provide the following to application:<br>
 *  <ul>
 *  <li>Provision of event broadcasting</li>
 *  </ul>
 *  </p>
 */
public enum BroadcastService {

  INSTANCE;

  private static final Logger log = LoggerFactory.getLogger(BroadcastService.class);

  // TODO ensure that this is threadsafe (require multiple HttpClients working on REST API)
  private Map<Long, Broadcaster> broadcasterMap= Maps.newConcurrentMap();

  public void broadcast(Long customerId, AlertMessage alertMessage) throws IOException {
    Broadcaster bc = broadcasterMap.get(customerId);
    if (bc == null) {
      log.warn("No Broadcaster for Customer id={}", customerId);
      return;
    }

    final ObjectMapper mapper = new ObjectMapper();

    // Broadcast takes place within a Future which we can monitor if necessary
    bc.broadcast(mapper.writeValueAsString(alertMessage));

    log.debug("Message broadcasted");

  }

  // TODO Refactor to allow topics and a general purpose event bus
  public void addBroadcaster(Long customerId, Broadcaster broadcaster) {
    broadcasterMap.put(customerId, broadcaster);
  }
}

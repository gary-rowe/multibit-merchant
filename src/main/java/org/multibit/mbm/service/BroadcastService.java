package org.multibit.mbm.service;

import com.google.common.collect.Maps;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.multibit.mbm.domain.AlertMessage;
import org.multibit.mbm.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *  <p>[Pattern] to provide the following to [related classes]:<br>
 *  <ul>
 *  <li></li>
 *  </ul>
 *  Example:<br>
 *  <pre>
 *  </pre>
 *  </p>
 *  
 */
public enum BroadcastService {

  INSTANCE;

  private static final Logger log = LoggerFactory.getLogger(BroadcastService.class);

  // TODO Make this threadsafe
  private Map<Customer, Broadcaster> broadcasterMap= Maps.newHashMap();

  public void broadcast(Customer customer, AlertMessage alertMessage) throws IOException {
    Broadcaster bc = broadcasterMap.get(customer);
    if (bc == null) {
      log.warn("No Broadcaster for Customer id={}", customer.getId());
      return;
    }

    final ObjectMapper mapper = new ObjectMapper();

    // Broadcast takes place within a Future which we can monitor
    Future<String> f = bc.broadcast(mapper.writeValueAsString(alertMessage));

    // TODO Remove this blocking operation
    try {
      f.get();
    } catch (InterruptedException ex) {
      log.error("", ex);
    } catch (ExecutionException ex) {
      log.error("", ex);
    }


  }

  // TODO Refactor to allow topics
  public void addBroadcaster(Customer customer, Broadcaster broadcaster) {
    broadcasterMap.put(customer, broadcaster);
  }
}

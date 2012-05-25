package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.core.Saying;
import org.multibit.mbm.security.SimpleUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
  private final String template;
  private final String defaultName;
  private final AtomicLong counter;

  public HelloWorldResource(String template, String defaultName) {
    this.template = template;
    this.defaultName = defaultName;
    this.counter = new AtomicLong();
  }

  @GET
  @Timed
  @Path("/hello-world")
  public Saying sayHello(@QueryParam("name") Optional<String> name) {
    return new Saying(counter.incrementAndGet(),
      String.format(template, name.or(defaultName)));
  }

  @GET
  @Timed
  @Path("/secret")
  public Saying saySecuredHello(@Auth SimpleUser user) {
    return new Saying(counter.incrementAndGet(),
      "You cracked the code!");
  }

}
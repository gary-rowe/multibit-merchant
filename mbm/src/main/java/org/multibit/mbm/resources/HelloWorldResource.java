package org.multibit.mbm.resources;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.core.Saying;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
  public Saying saySecuredHello(
    @RestrictedTo({Authority.ROLE_CUSTOMER})
    User user) {
    return new Saying(counter.incrementAndGet(),
      "You cracked the code!");
  }

}
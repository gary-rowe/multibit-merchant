package org.multibit.mbm.resources;

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Ignore;
import org.junit.Test;
import org.multibit.mbm.core.Saying;

import static org.junit.Assert.assertEquals;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *         
 */
public class HelloWorldResourceTest extends ResourceTest {

  @Override
  protected void setUpResources() {
    addResource(new HelloWorldResource("Hello, %s!","Stranger"));

  }

  @Test
  public void simpleResourceTest() throws Exception {

    Saying expectedSaying = new Saying(1,"Hello, Stranger!");

    Saying actualSaying = client()
      .resource("/hello-world")
      .get(Saying.class);

    assertEquals("GET hello-world returns a default",expectedSaying.getContent(),actualSaying.getContent());

  }


  // TODO Fix the authentication processing in requests
  // Study the MtGox approach with HMAC signing and shared secret
  // Possible write your own version (maybe offer a pull request)
  @Ignore
  public void oauthResourceTest() throws Exception {

    Saying actual = client()
      .resource("/secret")
      .header("Authorization","Bearer secret")
      .get(Saying.class);

    assertEquals("GET secret returns unauthorized","You cracked the code!", actual.getContent());

  }


}

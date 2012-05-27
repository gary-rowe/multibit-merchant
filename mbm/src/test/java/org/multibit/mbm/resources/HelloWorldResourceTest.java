package org.multibit.mbm.resources;

import com.xeiam.xchange.utils.CryptoUtils;
import com.xeiam.xchange.utils.HttpTemplate;
import org.junit.Test;
import org.multibit.mbm.auth.hmac.HmacAuthProvider;
import org.multibit.mbm.auth.hmac.HmacAuthenticator;
import org.multibit.mbm.core.Saying;
import org.multibit.mbm.persistence.dao.UserDao;
import org.multibit.mbm.persistence.dto.User;
import org.multibit.mbm.persistence.dto.UserBuilder;
import org.multibit.mbm.test.BaseResourceTest;

import javax.ws.rs.core.HttpHeaders;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
public class HelloWorldResourceTest extends BaseResourceTest {

  private final String apiKey = "abc123";
  private final String secretKey = "def456";


  @Override
  protected void setUpResources() {
    addResource(new HelloWorldResource("Hello, %s!","Stranger"));

    User user = UserBuilder
      .getInstance()
      .setUUID("abc123")
      .setSecretKey("def456")
      .build();

    //
    UserDao userDao = mock(UserDao.class);
    when(userDao.getUserByUUID("abc123")).thenReturn(user);

    HmacAuthenticator authenticator = new HmacAuthenticator();
    authenticator.setUserDao(userDao);

    addProvider(new HmacAuthProvider<User>(authenticator, "REST"));
  }

  @Test
  public void simpleResourceTest() throws Exception {

    Saying expectedSaying = new Saying(1,"Hello, Stranger!");

    Saying actualSaying = client()
      .resource("/hello-world")
      .get(Saying.class);

    assertEquals("GET hello-world returns a default",expectedSaying.getContent(),actualSaying.getContent());

  }


  @Test
  public void hmacResourceTest() throws Exception {

    // TODO Make this a standard test utility in the base class
    String contents = "/secret";
    String authorization = String.format("HmacSHA1 %s %s",
      URLEncoder.encode(apiKey, HttpTemplate.CHARSET_UTF_8),
      CryptoUtils.computeSignature("HmacSHA1", contents, secretKey));

    Saying actual = client()
      .resource("/secret")
      .header(HttpHeaders.AUTHORIZATION, authorization)
      .get(Saying.class);

    assertEquals("GET secret returns unauthorized","You cracked the code!", actual.getContent());

  }


}

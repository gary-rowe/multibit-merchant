package org.multibit.mbm.resources;

import org.junit.Test;
import org.multibit.mbm.test.BaseJerseyHmacResourceTest;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BitcoinPaymentResourceTest extends BaseJerseyHmacResourceTest {

  @Override
  protected void setUpResources() {

    // Configure the test object
    BitcoinPaymentResource testObject = new BitcoinPaymentResource();

    addSingleton(testObject);

    setUpAliceHmacAuthenticator();
  }

  // TODO Improve this test when swatch is re-implemented
  @Test
  public void testCreateSwatch() throws Exception {

    String actualResponse = configureAsClient("/bitcoin/swatch")
      .queryParam("amount", "12.34")
      .queryParam("address", "1abcdefgh")
      .queryParam("label", "Hello")
      .accept(MediaType.TEXT_PLAIN)
      .get(String.class);

    String expectedResponse= "1abcdefgh 12.34 Hello";

    assertThat(actualResponse,is(equalTo(expectedResponse)));
  }

}

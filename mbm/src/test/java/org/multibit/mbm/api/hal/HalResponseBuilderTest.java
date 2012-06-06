package org.multibit.mbm.api.hal;

import org.junit.Test;
import org.multibit.mbm.api.response.UserResponse;
import org.multibit.mbm.test.TestUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import static org.junit.Assert.assertEquals;

public class HalResponseBuilderTest {

  @Test
  public void testSimple() {

    HalResponse testObject = HalResponseBuilder
      .newInstance()
      .withBaseHref("http://localhost:8080")
      .withLink("customer", "/customer/124", "customer124", "Your account", "en")
      .build();

    assertEquals("http://localhost:8080", testObject.getBaseHref());
    assertEquals("customer", testObject.getLinks().get(0).getRel());
    assertEquals("/customer/124", testObject.getLinks().get(0).getHref());
    assertEquals("customer124", testObject.getLinks().get(0).getName());
    assertEquals("Your account", testObject.getLinks().get(0).getTitle());
    assertEquals("en", testObject.getLinks().get(0).getHreflang());

  }

  @Test
  public void testXmlMarshalling_Simple() throws Exception {

    HalResponse testObject = HalResponseBuilder
      .newInstance()
      .withBaseHref("http://localhost:8080")
      .withLink("customer", "/customer/124", "customer124", "Your account", "en")
      .build();

    JAXBContext context = JAXBContext.newInstance(HalResponse.class);
    Marshaller marshaller = context.createMarshaller();

    Result actual = TestUtils.newStringResult();
    marshaller.marshal(testObject, actual);

    String expected = TestUtils.readStringFromStream(HalResponseBuilderTest.class.getResourceAsStream("/fixtures/hal/expected-hal-marshalling-simple.xml"));

    assertEquals(expected, actual.toString());
  }

  @Test
  public void testXmlMarshalling_Nested() throws Exception {

    UserResponse userResponse = new UserResponse();
    userResponse.setUsername("user124");

    HalResponse testObject = HalResponseBuilder
      .newInstance()
      .withBaseHref("http://localhost:8080")
      .withLink("customer", "/customer/124", "customer124", "Your account", "en")
      .withEntity(userResponse)
      .build();

    JAXBContext context = JAXBContext.newInstance(HalResponse.class, UserResponse.class);
    Marshaller marshaller = context.createMarshaller();

    Result actual = TestUtils.newStringResult();
    marshaller.marshal(testObject, actual);

    String expected = TestUtils.readStringFromStream(HalResponseBuilderTest.class.getResourceAsStream("/fixtures/hal/expected-hal-marshalling-nested.xml"));

    assertEquals(expected, actual.toString());
  }

  @Test
  public void testXmlMarshalling_MultipleNested() throws Exception {

    UserResponse userResponse1 = new UserResponse();
    userResponse1.setUsername("user124");

    UserResponse userResponse2 = new UserResponse();
    userResponse2.setUsername("user125");

    HalResponse nestedResponse1 = HalResponseBuilder
      .newInstance()
      .withBaseHref("http://localhost:8080")
      .withLink("customer", "/customer/124", "customer124", "Your account", "en")
      .withEntity(userResponse1)
      .build();

    HalResponse nestedResponse2 = HalResponseBuilder
      .newInstance()
      .withBaseHref("http://localhost:8080")
      .withLink("customer", "/customer/125", "customer125", "Your account", "en")
      .withEntity(userResponse2)
      .build();

    HalResponse testObject = HalResponseBuilder
      .newInstance()
      .withBaseHref("http://localhost:8080")
      .withLink("customer", "/customer/124", "customer124", "Your account", "en")
      .withHalResponse(nestedResponse1)
      .withHalResponse(nestedResponse2)
      .withEntity(userResponse1)
      .withEntity(userResponse2)
      .build();

    JAXBContext context = JAXBContext.newInstance(HalResponse.class, UserResponse.class);
    Marshaller marshaller = context.createMarshaller();

    Result actual = TestUtils.newStringResult();
    marshaller.marshal(testObject, actual);

    String expected = TestUtils.readStringFromStream(HalResponseBuilderTest.class.getResourceAsStream("/fixtures/hal/expected-hal-marshalling-multiple-nested.xml"));

    assertEquals(expected, actual.toString());
  }

}

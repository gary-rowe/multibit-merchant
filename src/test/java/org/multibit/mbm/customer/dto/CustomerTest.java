package org.multibit.mbm.customer.dto;

import org.junit.Test;
import org.multibit.mbm.customer.builder.CustomerBuilder;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

  @Test
  public void testPrimaryEmailAddress() {

    Customer testObject = CustomerBuilder.getInstance()
      .addContactMethod(ContactMethod.EMAIL,"test1@example.org")
      .build();

    assertEquals("test1@example.org", testObject.getContactMethodDetail(ContactMethod.EMAIL).getPrimaryDetail());

  }
}

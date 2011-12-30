package org.multibit.mbm.customer.dto;

import org.junit.Test;
import org.multibit.mbm.customer.dto.ContactMethod;
import org.multibit.mbm.customer.dto.ContactMethodDetail;
import org.multibit.mbm.customer.dto.Customer;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

  @Test
  public void testPrimaryEmailAddress() {
    Customer testObject = new Customer();

    ContactMethodDetail contactMethodDetail = new ContactMethodDetail();
    contactMethodDetail.setPrimaryDetail("test1@example.org");

    testObject.setContactMethodDetail(ContactMethod.EMAIL,contactMethodDetail);
    
    assertEquals("test1@example.org", testObject.getContactMethodDetail(ContactMethod.EMAIL).getPrimaryDetail());

  }
}

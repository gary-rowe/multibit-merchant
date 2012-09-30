package org.multibit.mbm.api.response.hal;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.theoryinpractise.halbuilder.spi.Resource;
import org.junit.Before;
import org.junit.Test;
import org.multibit.mbm.db.DatabaseLoader;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.db.dto.UserBuilder;
import org.multibit.mbm.test.BaseResourceTest;
import org.multibit.mbm.test.FixtureAsserts;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class AdminUserBridgeTest extends BaseResourceTest {

  @Before
  public void setUpDefaultRequestInfo() throws Exception {
    setUpUriInfo(Optional.<String>absent());
    setUpHttpHeaders(Optional.<List<MediaType>>absent());
  }

  @Test
  public void representUserListAsJson() throws Exception {

    Role customerRole = DatabaseLoader.buildRoleCustomer();
    User aliceUser = DatabaseLoader.buildCustomerAlice(customerRole);
    User bobUser = DatabaseLoader.buildCustomerAlice(customerRole);

    AdminUserBridge testObject = new AdminUserBridge(uriInfo,principal);

    Resource resource = testObject.toResource(Lists.newArrayList(aliceUser,bobUser));

    FixtureAsserts.assertRepresentationMatchesJsonFixture("a User list can be marshalled to JSON", resource, "fixtures/hal/user/expected-users-by-admin-page-1.json");

  }

  @Test
  public void representUserListAsXml() throws IOException {

    User user = UserBuilder
      .newInstance()
      .build();

    AdminUserBridge testObject = new AdminUserBridge(uriInfo,principal);

    Resource resource = testObject.toResource(Lists.newArrayList(user));

    FixtureAsserts.assertRepresentationMatchesXmlFixture("a User list can be marshalled to XML", resource, "fixtures/hal/user/expected-user-by-admin.xml");

  }

}

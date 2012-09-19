package org.multibit.mbm.auth.hmac;

import com.google.common.collect.Lists;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.core.header.InBoundHeaders;
import com.yammer.dropwizard.testing.FixtureHelpers;
import org.junit.Test;
import org.multibit.mbm.db.dto.Authority;
import org.multibit.mbm.db.dto.User;

import javax.ws.rs.core.HttpHeaders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HmacRestrictedToInjectableTest {


}

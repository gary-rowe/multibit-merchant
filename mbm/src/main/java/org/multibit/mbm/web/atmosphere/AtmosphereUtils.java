package org.multibit.mbm.web.atmosphere;

import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  <p>[Pattern] to provide the following to [related classes]:<br>
 *  <ul>
 *  <li></li>
 *  </ul>
 *  Example:<br>
 *  <pre>
 *  </pre>
 *  </p>
 *  
 */
public final class AtmosphereUtils {

  private static final String ATMOSPHERE_RESOURCE="org.atmosphere.cpr.AtmosphereResource";
  public static AtmosphereResource<HttpServletRequest, HttpServletResponse> getAtmosphereResource(HttpServletRequest request) {

    AtmosphereResource<HttpServletRequest, HttpServletResponse> resource = (AtmosphereResource<HttpServletRequest, HttpServletResponse>) request.getAttribute(ATMOSPHERE_RESOURCE);
    Assert.notNull(resource, "AtmosphereResource could not be located for the request. Check that AtmosphereServlet is configured correctly in web.xml");
    return resource;
  }

}

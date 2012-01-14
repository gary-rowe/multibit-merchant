package org.multibit.mbm.web.atmosphere;

import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

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
public class AtmosphereResourceArgumentResolver implements WebArgumentResolver {

  @Override
  public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

    if (AtmosphereResource.class.isAssignableFrom(methodParameter.getParameterType())) {
      return AtmosphereUtils.getAtmosphereResource(webRequest.getNativeRequest(HttpServletRequest.class));
    } else {
      return WebArgumentResolver.UNRESOLVED;
    }

  }

}

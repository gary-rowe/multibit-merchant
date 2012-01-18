package org.multibit.mbm.web.rest.v1.usecase.authentication;

import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Basic authentication</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class BasicAuthenticationLogin extends BaseUseCase {

  @Override
  protected void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}

package org.multibit.mbm.web.rest.v1.usecase.authentication;

import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Anonymous authentication</li>
 *  </ul>
 * <p>This is required when a user attempts to use a publicly writable part of the API</p>
 *
 * @since 1.0.0
 *         
 */
public class AnonymousLogin extends BaseUseCase {
  @Override
  protected void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}

package org.multibit.mbm.web.rest.v1.usecase.authentication;

import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;

import java.util.Map;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Token request to allow subsequent anonymous calls to proceed</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class TokenRequest extends BaseUseCase {
  @Override
  public void perform(Map<UseCaseParameter, Object> useCaseParameterMap) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}

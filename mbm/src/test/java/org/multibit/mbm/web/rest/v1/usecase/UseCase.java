package org.multibit.mbm.web.rest.v1.usecase;

import java.util.Map;

/**
 *  <p>Interface to provide the following to {@link org.multibit.mbm.web.rest.v1.FunctionalTestExecutor}:</p>
 *  <ul>
 *  <li>Provision of standard execution methods</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public interface UseCase {
  void perform(Map<UseCaseParameter, Object> useCaseParameterMap);
}

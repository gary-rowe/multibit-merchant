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
  /**
   * Execute the given use case
   * @param useCaseParameterMap The shared parameter map
   */
  void execute(Map<UseCaseParameter, Object> useCaseParameterMap);
}

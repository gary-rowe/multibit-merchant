package org.multibit.mbm.web.rest.v1.usecase.catalog;

import org.multibit.mbm.web.rest.v1.search.SearchResults;
import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Catalog search to provide the latest promotions</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class ListItems extends BaseUseCase {
  @Override
  protected void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate) {

    SearchResults searchSummary = restTemplate.getForObject(
      buildResourceUri("/items"),
      SearchResults.class);

    assertThat("Unexpected data for /items", searchSummary.getResults().size(), equalTo(5));

  }

}

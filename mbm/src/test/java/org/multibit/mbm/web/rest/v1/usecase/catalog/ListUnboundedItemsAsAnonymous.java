package org.multibit.mbm.web.rest.v1.usecase.catalog;

import org.multibit.mbm.web.rest.v1.client.catalog.ItemSearchResponse;
import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Anonymous unbounded catalog search to provide the latest promotions</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class ListUnboundedItemsAsAnonymous extends BaseUseCase {
  @Override
  protected void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate) {

    // Perform a default search of all promotional items
    ItemSearchResponse itemSearchResponse = restTemplate.getForObject(
      buildResourceUri("/items"),
      ItemSearchResponse.class);

    assertThat("Unexpected data for /items", itemSearchResponse.getItemSummaries().size(), equalTo(5));

  }

}

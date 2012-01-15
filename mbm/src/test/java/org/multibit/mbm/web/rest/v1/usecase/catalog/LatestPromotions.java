package org.multibit.mbm.web.rest.v1.usecase.catalog;

import org.multibit.mbm.web.rest.v1.search.SearchResults;
import org.multibit.mbm.web.rest.v1.usecase.BaseUseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *  <p>UseCase to provide the following to {@link org.multibit.mbm.web.rest.v1.usecase.UseCase}:</p>
 *  <ul>
 *  <li>Catalog search to provide the latest promotions</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class LatestPromotions extends BaseUseCase {
  @Override
  public void perform(Map<UseCaseParameter, Object> useCaseParameterMap) {
    RestTemplate restTemplate = getConfiguredRestTemplate(useCaseParameterMap);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");

    SearchResults searchSummary = restTemplate.getForObject(
      buildResourceUri("/catalog/item/search"),
      SearchResults.class);

    Assert.notNull(searchSummary,"Unexpected null");
    
  }

}

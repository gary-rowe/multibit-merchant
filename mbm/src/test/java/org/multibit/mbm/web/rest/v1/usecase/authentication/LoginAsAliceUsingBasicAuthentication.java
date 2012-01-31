package org.multibit.mbm.web.rest.v1.usecase.authentication;

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
 *  <li>Preemptive Basic authentication</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class LoginAsAliceUsingBasicAuthentication extends BaseUseCase {

  @Override
  protected void doConfiguration(Map<UseCaseParameter, Object> useCaseParameterMap) {
    // Configure the RestTemplate to use Basic authentication
    useCaseParameterMap.put(UseCaseParameter.HTTP_AUTHENTICATE_BASIC,new String[] {"alice","alice"});

  }

  @Override
  protected void doExecute(Map<UseCaseParameter, Object> useCaseParameterMap, RestTemplate restTemplate) {




    // Perform a default search of all promotional items
    ItemSearchResponse itemSearchResponse = restTemplate.getForObject(
      buildResourceUri("/items"),
      ItemSearchResponse.class);

    assertThat("Unexpected data for /items", itemSearchResponse.getItemSummaries().size(), equalTo(5));
    

  }
}

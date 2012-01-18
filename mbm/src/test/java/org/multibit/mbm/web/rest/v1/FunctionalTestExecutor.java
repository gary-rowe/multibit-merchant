package org.multibit.mbm.web.rest.v1;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Ignore;
import org.multibit.mbm.web.rest.v1.usecase.authentication.AnonymousLogin;
import org.multibit.mbm.web.rest.v1.usecase.authentication.BasicAuthenticationLogin;
import org.multibit.mbm.web.rest.v1.usecase.UseCase;
import org.multibit.mbm.web.rest.v1.usecase.UseCaseParameter;
import org.multibit.mbm.web.rest.v1.usecase.catalog.ListItems;

import java.util.List;
import java.util.Map;

/**
 * <p>Functional test to provide the following to application:</p>
 * <ul>
 * <li>Test bed to exercise a variety of use cases against the RESTful API</li>
 * </ul>
 * <p></p>
 *
 * @since 1.0.0
 *         
 */
@Ignore
public class FunctionalTestExecutor {

  private List<UseCase> useCases = Lists.newArrayList();

  /**
   * Entry point to the functional test scenarios
   *
   * @param args Not used
   */
  public static void main(String[] args) {
    FunctionalTestExecutor executor = new FunctionalTestExecutor();

    // Add the use cases
    executor.addUseCase(new ListItems());
    executor.addUseCase(new AnonymousLogin());
    executor.addUseCase(new BasicAuthenticationLogin());

    executor.execute();
  }

  /**
   * Executes the list of use cases in the order presented, sharing state between them
   */
  private void execute() {

    // Create a general purposes map to be passed around between use cases
    Map<UseCaseParameter, Object> useCaseParameterMap = Maps.newHashMap();
    useCaseParameterMap.put(UseCaseParameter.HTTP_ACCEPT_HEADER,"application/json");

    // Perform each use case - sharing state as required
    for (UseCase useCase : useCases) {
      System.out.print("Executing "+useCase.getClass().getSimpleName()+"...");
      useCase.execute(useCaseParameterMap);
      System.out.println("OK");
    }

    // No exceptions means it passed
    System.out.println("Functional tests PASSED");
    
  }

  /**
   * @param useCase The use case to add
   */
  private void addUseCase(UseCase useCase) {
    useCases.add(useCase);
  }


}

package org.multibit.mbm.web.rest.v1.usecase;

/**
 *  <p>Enum to provide the following to {@link UseCase}:</p>
 *  <ul>
 *  <li>Provision of standard accessor keys to the parameter map</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public enum UseCaseParameter {
  // HTTP specific parameters
  HTTP_ACCEPT_HEADER,
  // MBM specific parameters
  MBM_TOKEN,
  MBM_USER_NAME,
  MBM_PASSWORD,
  MBM_ITEM_ID,
  MBM_CUSTOMER_ID,
  ; // End of enum
}

package org.multibit.mbm.api.request.user;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to create an initial bare bones User by a client</li>
 * </ul>
 * <p>Note that subsequent updates to the User can set more detail into the User as required</p>
 * <p>When a client creates a customer it can be as a result of the anonymous public performing a session
 * persistent operation (such as adding an item to their shopping cart) or as a result of the
 * sign up process.</p>
 *
 * @since 0.0.1
 *         
 */
public class ClientCreateCustomerRequest extends WebFormAuthenticationRequest {

  // TODO Add in registration fields


}
